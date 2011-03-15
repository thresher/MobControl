package com.WinSock.MobControl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.World;
import org.bukkit.entity.CreatureType;
import org.bukkit.util.config.Configuration;

import com.WinSock.MobControl.Spawner.CreatureInfo;
import com.WinSock.MobControl.Spawner.CreatureNature;
import com.WinSock.MobControl.Spawner.SpawnTime;

public class Defaults {

	private Map<CreatureType, CreatureInfo> defaults = new HashMap<CreatureType, CreatureInfo>();
	private Configuration config;

	public Defaults(Configuration config) {

		this.config = config;

		// Default Creatures
		Set<Material> spawnBAgro = new HashSet<Material>(Arrays.asList(Material
				.values()));
		spawnBAgro.remove(Material.AIR);
		spawnBAgro.remove(Material.COBBLESTONE_STAIRS);
		spawnBAgro.remove(Material.WOOD_STAIRS);
		spawnBAgro.remove(Material.FENCE);
		spawnBAgro.remove(Material.STEP);
		spawnBAgro.remove(Material.GLASS);

		// Passive
		CreatureInfo pig = new CreatureInfo(CreatureType.PIG, Material.GRASS,
				9, 15, 0.5F, 0, 0, 2, false, CreatureNature.PASSIVE,
				CreatureNature.PASSIVE, 5, 0, SpawnTime.BOTH, true,
				Environment.NORMAL);
		defaults.put(pig.getCreature(), pig);

		CreatureInfo chicken = new CreatureInfo(CreatureType.CHICKEN,
				Material.GRASS, 9, 15, 0.5F, 0, 0, 2, false,
				CreatureNature.PASSIVE, CreatureNature.PASSIVE, 2, 0,
				SpawnTime.BOTH, true, Environment.NORMAL);
		defaults.put(chicken.getCreature(), chicken);

		CreatureInfo cow = new CreatureInfo(CreatureType.COW, Material.GRASS,
				9, 15, 0.5F, 0, 0, 2, false, CreatureNature.PASSIVE,
				CreatureNature.PASSIVE, 5, 0, SpawnTime.BOTH, true,
				Environment.NORMAL);
		defaults.put(cow.getCreature(), cow);

		CreatureInfo squid = new CreatureInfo(CreatureType.SQUID,
				Material.GRASS, 9, 15, 0.5F, 0, 0, 2, false,
				CreatureNature.PASSIVE, CreatureNature.PASSIVE, 5, 0,
				SpawnTime.BOTH, true, Environment.NORMAL);
		defaults.put(squid.getCreature(), squid);

		CreatureInfo sheep = new CreatureInfo(CreatureType.SHEEP,
				Material.GRASS, 9, 15, 0.5F, 0, 0, 2, false,
				CreatureNature.PASSIVE, CreatureNature.PASSIVE, 5, 0,
				SpawnTime.BOTH, true, Environment.NORMAL);
		defaults.put(sheep.getCreature(), sheep);

		// Neutral
		CreatureInfo pigZombie = new CreatureInfo(CreatureType.PIG_ZOMBIE,
				Material.NETHERRACK, 0, 15, 0.5F, 0, 0, 2, false,
				CreatureNature.NEUTRAL, CreatureNature.NEUTRAL, 10, 3,
				SpawnTime.BOTH, true, Environment.NETHER);
		defaults.put(pigZombie.getCreature(), pigZombie);

		// Aggressive
		CreatureInfo spider = new CreatureInfo(CreatureType.SPIDER, spawnBAgro,
				0, 7, 0.5F, 0, 0, 1, false, CreatureNature.AGGRESSIVE,
				CreatureNature.NEUTRAL, 10, 1, SpawnTime.BOTH, true,
				Environment.NORMAL);
		defaults.put(spider.getCreature(), spider);

		CreatureInfo zombie = new CreatureInfo(CreatureType.ZOMBIE, spawnBAgro,
				0, 7, 0.5F, 0, 0, 2, true, CreatureNature.AGGRESSIVE,
				CreatureNature.AGGRESSIVE, 10, 3, SpawnTime.BOTH, true,
				Environment.NORMAL);
		defaults.put(zombie.getCreature(), zombie);

		CreatureInfo skeleton = new CreatureInfo(CreatureType.SKELETON,
				spawnBAgro, 0, 7, 0.5F, 0, 0, 2, true,
				CreatureNature.AGGRESSIVE, CreatureNature.AGGRESSIVE, 10, 1,
				SpawnTime.BOTH, true, Environment.NORMAL);
		defaults.put(skeleton.getCreature(), skeleton);

		CreatureInfo creeper = new CreatureInfo(CreatureType.CREEPER,
				spawnBAgro, 0, 7, 0.5F, 0, 0, 2, true,
				CreatureNature.AGGRESSIVE, CreatureNature.AGGRESSIVE, 10, 8,
				SpawnTime.BOTH, true, Environment.NORMAL);
		defaults.put(creeper.getCreature(), creeper);

		CreatureInfo slime = new CreatureInfo(CreatureType.SLIME, spawnBAgro,
				0, 7, 0.5F, 0, 0, 2, true, CreatureNature.AGGRESSIVE,
				CreatureNature.AGGRESSIVE, 0, 0, SpawnTime.BOTH, true,
				Environment.NORMAL);
		defaults.put(slime.getCreature(), slime);

		CreatureInfo ghast = new CreatureInfo(CreatureType.GHAST, spawnBAgro,
				0, 7, 0.5F, 0, 0, 2, true, CreatureNature.AGGRESSIVE,
				CreatureNature.AGGRESSIVE, 5, 3, SpawnTime.BOTH, true,
				Environment.NETHER);
		defaults.put(ghast.getCreature(), ghast);
	}

	public void SetConfig(World world) {
		for (CreatureInfo i : defaults.values()) {
			CreatureInfo temp = i;
			temp.setWorld(world);
			temp.saveConfig(config);
		}
		config.save();
	}

	public CreatureInfo getDefaultFromType(CreatureType type) {
		for (CreatureInfo i : defaults.values()) {
			if (i.getCreature() == type) {
				return i;
			}
		}
		return null;
	}

	public Configuration getConfig() {
		return this.config;
	}
}
