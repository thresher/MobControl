package com.WinSock.MobControl.Spawner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Creature;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.WinSock.MobControl.MobControlPlugin;
import com.WinSock.MobControl.Spawner.CreatureNature;
import com.WinSock.MobControl.Spawner.SpawnTime;

public class SpawnerCreature implements Runnable {
	private final MobControlPlugin plugin;
	private final Random rand = new Random();
	private Creatures creatures;

	public SpawnerCreature(final MobControlPlugin plugin, Creatures creatures) {
		this.plugin = plugin;
		this.creatures = creatures;
	}

	private Set<World> getActiveWorlds() {
		Set<World> returnData = new HashSet<World>();
		for (Player p : plugin.getServer().getOnlinePlayers()) {
			returnData.add(p.getWorld());
		}
		return returnData;
	}

	private Set<Chunk> getChunks(World world) {
		Set<Chunk> returnData = new HashSet<Chunk>();
		for (Player p : plugin.getServer().getOnlinePlayers()) {
			if (p.getWorld() == world) {
				Chunk pChunk = p.getWorld().getChunkAt(p.getLocation());
				Chunk startChunk = world.getChunkAt(pChunk.getX() - 4,
						pChunk.getZ() - 4);

				for (int x = 0; x <= 8; x++) {
					for (int z = 0; z <= 8; z++) {
						returnData.add(world.getChunkAt(startChunk.getX() + x,
								startChunk.getZ() + z));
					}
				}
			}
		}
		return returnData;
	}

	private CreatureInfo getRandPassiveCreature(World world) {
		if (plugin.isDay(world)) {
			Set<CreatureInfo> enabledCreatures = creatures.getCreatures(
					SpawnTime.DAY, CreatureNature.PASSIVE);
			if (enabledCreatures.size() != 0) {
				return enabledCreatures.iterator().next();
			} else {
				return null;
			}
		} else {
			Set<CreatureInfo> enabledCreatures = creatures.getCreatures(
					SpawnTime.NIGHT, CreatureNature.PASSIVE);
			if (enabledCreatures.size() != 0) {
				return enabledCreatures.iterator().next();
			} else {
				return null;
			}
		}
	}

	private void spawnMobs(List<Block> blocks, CreatureInfo cInfo, World world) {
		Set<Block> blocksToSpawn = new HashSet<Block>();

		for (int i = 0; i < 500; i++) {
			blocksToSpawn.add(blocks.get(rand.nextInt(blocks.size())));
		}

		Iterator<Block> it = blocksToSpawn.iterator();
		checkloop: while (it.hasNext()) {
			Block block = it.next();

			if (world.getEnvironment() != cInfo.getEnvironment()) {
				continue checkloop;
			}

			for (LivingEntity e : world.getLivingEntities()) {
				if (e instanceof Creature) {
					if (e.getLocation().getBlock() == block) {
						continue checkloop;
					}
				} else if (e instanceof HumanEntity) {
					int deltax = Math.abs(e.getLocation().getBlockX()
							- block.getX());
					int deltay = Math.abs(e.getLocation().getBlockY()
							- block.getY());
					int deltaz = Math.abs(e.getLocation().getBlockZ()
							- block.getZ());
					double distance = Math.sqrt((deltax * deltax)
							+ (deltay * deltay) + (deltaz * deltaz));

					if (distance < creatures.getDistanceFromPlayer()) {
						continue checkloop;
					}
				}
			}
			for (int i = 0; i < cInfo.getSpawnRoom(); i++) {
				Location blockLoc = new Location(world, block.getX(),
						block.getY() + i, block.getZ());
				if (world.getBlockAt(blockLoc).getType() != Material.AIR) {
					continue checkloop;
				}
			}
			Location blockLoc = new Location(world, block.getX(),
					block.getY() - 1, block.getZ());
			if (!cInfo.getSpawnBlocks().contains(
					world.getBlockAt(blockLoc).getType())) {
				continue checkloop;
			}

			if (plugin.isDay(world)) {
				switch (cInfo.getNatureDay()) {
				case AGGRESSIVE:
					int maxLight = rand.nextInt(cInfo.getMaxLight());
					if (block.getLightLevel() < cInfo.getMinLight()) {
						continue checkloop;
					} else if (block.getLightLevel() > maxLight) {
						continue checkloop;
					}
					break;
				default:
					if (block.getLightLevel() < cInfo.getMinLight()) {
						continue checkloop;
					} else if (block.getLightLevel() > cInfo.getMaxLight()) {
						continue checkloop;
					}
					break;
				}
			} else {
				switch (cInfo.getNatureNight()) {
				case AGGRESSIVE:
					int maxLight = rand.nextInt(cInfo.getMaxLight());
					if (block.getLightLevel() < cInfo.getMinLight()) {
						continue checkloop;
					} else if (block.getLightLevel() > maxLight) {
						continue checkloop;
					}
					break;
				default:
					if (block.getLightLevel() < cInfo.getMinLight()) {
						continue checkloop;
					} else if (block.getLightLevel() > cInfo.getMaxLight()) {
						continue checkloop;
					}
					break;
				}
			}
			if (rand.nextInt(100) <= cInfo.getSpawnRate()) {
				world.spawnCreature(block.getLocation(), cInfo.getCreature());
			}
		}
	}

	private CreatureInfo getRandHostileCreature(World world) {
		if (plugin.isDay(world)) {
			Set<CreatureInfo> enabledCreatures = creatures.getCreatures(
					SpawnTime.DAY, CreatureNature.AGGRESSIVE);
			enabledCreatures.addAll(creatures.getCreatures(SpawnTime.DAY,
					CreatureNature.NEUTRAL));
			if (enabledCreatures.size() != 0) {
				return enabledCreatures.iterator().next();
			} else {
				return null;
			}
		} else {
			Set<CreatureInfo> enabledCreatures = creatures.getCreatures(
					SpawnTime.NIGHT, CreatureNature.AGGRESSIVE);
			enabledCreatures.addAll(creatures.getCreatures(SpawnTime.NIGHT,
					CreatureNature.NEUTRAL));
			if (enabledCreatures.size() != 0) {
				return enabledCreatures.iterator().next();
			} else {
				return null;
			}
		}
	}

	public void run() {
		Set<World> worlds = getActiveWorlds();
		for (World w : worlds) {
			Set<Chunk> chunkSet = getChunks(w);
			CreatureInfo spawnPassive = getRandPassiveCreature(w);
			CreatureInfo spawnAggressive = getRandHostileCreature(w);

			if (spawnPassive != null || spawnAggressive != null) {
				List<Block> blocks = new ArrayList<Block>();

				for (Chunk c : chunkSet) {
					for (int y = 0; y < 128; y++) {
						for (int x = 0; x < 16; x++) {
							for (int z = 0; z < 16; z++) {
								blocks.add(c.getBlock(x, y, z));
							}
						}
					}
				}

				if (spawnPassive != null) {
					spawnMobs(blocks, spawnPassive, w);
				}
				if (spawnAggressive != null) {
					spawnMobs(blocks, spawnAggressive, w);
				}
			}
		}
	}
}
