package com.WinSock.MobControl;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Creature;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Flying;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.Giant;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Pig;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Spider;
import org.bukkit.entity.Squid;
import org.bukkit.entity.WaterMob;
import org.bukkit.entity.Zombie;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;

import com.WinSock.MobControl.Listeners.MobControlEntityListener;
import com.WinSock.MobControl.Listeners.MobControlWorldListener;
//import com.WinSock.MobControl.Spawner.SpawnerCreature;
import com.WinSock.MobControl.Spawner.CreatureInfo;
import com.WinSock.MobControl.Spawner.CreatureNature;
import com.WinSock.MobControl.Spawner.Creatures;
import com.WinSock.MobControl.Spawner.CreaturesHandler;
import com.WinSock.MobControl.Spawner.SpawnTime;

public class MobControlPlugin extends JavaPlugin {

	private MobControlEntityListener entityListener = new MobControlEntityListener(
			this);
	public MobControlWorldListener worldListener;

	public Logger log = null;
	private PluginDescriptionFile pdfFile;

	private Defaults defaults;
	public CreaturesHandler creaturesHandler = new CreaturesHandler(this);

	public Map<Creature, List<LivingEntity>> attacked = new HashMap<Creature, List<LivingEntity>>();
	public boolean running = false;

	public void onDisable() {
		this.running = false;
		log.info("[" + pdfFile.getName() + "] Version " + pdfFile.getVersion()
				+ " is disabled!");
	}

	public boolean shouldTarget(Entity e, CreatureNature creatureNature,
			boolean attacked) {
		if (creatureNature != null) {
			if (creatureNature == CreatureNature.PASSIVE) {
				return false;
			} else if (creatureNature == CreatureNature.NEUTRAL) {
				if (attacked) {
					return true;
				} else {
					return false;
				}
			} else if (creatureNature == CreatureNature.AGGRESSIVE) {
				return true;
			}
		}
		if (e instanceof Monster) {
			return true;
		} else {
			return false;
		}
	}

	public boolean shouldBurn(Location loc) {
		if (isDay(loc.getWorld())) {
			if (loc.getWorld()
					.getBlockAt(loc.getBlockX(), loc.getBlockY() + 1,
							loc.getBlockZ()).getLightLevel() > 7) {
				if (canSeeSky(loc)) {
					return true;
				}
			}

		}
		return false;
	}

	public boolean canSeeSky(Location loc) {
		for (int i = 128; i >= 0; i++) {
			if (okBlock(loc.getWorld().getBlockAt(loc.getBlockX(), i,
					loc.getBlockZ()))) {
				if (loc.getBlockY() == i) {
					return true;
				}
			} else {
				break;
			}
		}
		return false;
	}

	public boolean isDay(World world) {
		return world.getTime() < 12000 || world.getTime() == 24000;
	}

	public boolean okBlock(Block block) {
		if (block.getType() != Material.AIR
				|| block.getType() != Material.LEAVES) {
			return false;
		} else {
			return true;
		}
	}

	public boolean spawnChance(int precent) {
		Random random = new Random();
		if (precent >= 100) {
			return true;
		} else if (precent <= 0) {
			return false;
		} else if (random.nextInt(100) <= precent) {
			return true;
		} else {
			return false;
		}
	}

	public int getNumberOfCreatures(Location loc, CreatureNature nature) {
		int creatures = 0;
		for (Entity e : loc.getWorld().getEntities()) {
			if (e instanceof Creature) {
				if (isDay(loc.getWorld())) {
					switch (nature) {
					case PASSIVE:
						if (creaturesHandler.get(loc.getWorld())
								.get(getCreatureType(e)).getNatureDay() == CreatureNature.PASSIVE) {
							creatures++;
						}

					case AGGRESSIVE:
						if (creaturesHandler.get(loc.getWorld())
								.get(getCreatureType(e)).getNatureDay() == CreatureNature.AGGRESSIVE) {
							creatures++;
						}
						break;

					case NEUTRAL:
						if (creaturesHandler.get(loc.getWorld())
								.get(getCreatureType(e)).getNatureDay() == CreatureNature.NEUTRAL) {
							creatures++;
						}
						break;
					}
				} else {
					switch (nature) {
					case PASSIVE:
						if (creaturesHandler.get(loc.getWorld())
								.get(getCreatureType(e)).getNatureNight() == CreatureNature.PASSIVE) {
							creatures++;
						}
						break;

					case AGGRESSIVE:
						if (creaturesHandler.get(loc.getWorld())
								.get(getCreatureType(e)).getNatureNight() == CreatureNature.AGGRESSIVE) {
							creatures++;
						}
						break;

					case NEUTRAL:
						if (creaturesHandler.get(loc.getWorld())
								.get(getCreatureType(e)).getNatureNight() == CreatureNature.NEUTRAL) {
							creatures++;
						}
						break;
					}
				}
			}
		}
		return creatures;
	}

	public boolean canSpawn(Location spawnLoc, CreatureInfo cInfo, int precent) {
		if (cInfo.isEnabled()) {
			if (isDay(cInfo.getWorld())) {
				switch (cInfo.getNatureDay()) {
				case PASSIVE:
					if (creaturesHandler.get(cInfo.getWorld()).getMaxPassive() < getNumberOfCreatures(
							spawnLoc, CreatureNature.PASSIVE)) {
						return false;
					}
					break;
				case AGGRESSIVE:
					if (creaturesHandler.get(cInfo.getWorld()).getMaxHostile() < getNumberOfCreatures(
							spawnLoc, CreatureNature.AGGRESSIVE)) {
						return false;
					}
					break;
				case NEUTRAL:
					if (creaturesHandler.get(cInfo.getWorld()).getMaxNeutral() < getNumberOfCreatures(
							spawnLoc, CreatureNature.NEUTRAL)) {
						return false;
					}
					break;
				}
				if (cInfo.getSpawnTime() != SpawnTime.DAY
						&& cInfo.getSpawnTime() != SpawnTime.BOTH) {
					return false;
				}
			} else {
				switch (cInfo.getNatureNight()) {
				case PASSIVE:
					if (creaturesHandler.get(cInfo.getWorld()).getMaxPassive() < getNumberOfCreatures(
							spawnLoc, CreatureNature.PASSIVE)) {
						return false;
					}
					break;
				case AGGRESSIVE:
					if (creaturesHandler.get(cInfo.getWorld()).getMaxHostile() < getNumberOfCreatures(
							spawnLoc, CreatureNature.AGGRESSIVE)) {
						return false;
					}
					break;
				case NEUTRAL:
					if (creaturesHandler.get(cInfo.getWorld()).getMaxNeutral() < getNumberOfCreatures(
							spawnLoc, CreatureNature.NEUTRAL)) {
						return false;
					}
					break;
				}
				if (cInfo.getSpawnTime() != SpawnTime.NIGHT
						&& cInfo.getSpawnTime() != SpawnTime.BOTH) {
					return false;
				}
			}
			if (cInfo.getMaxSpawnHeight() == 0) {
				return true;
			} else if (spawnLoc.getBlockY() > cInfo.getMaxSpawnHeight()) {
				return false;
			}
			if (cInfo.getMinSpawnHeight() == 0) {
				return true;
			} else if (spawnLoc.getBlockY() < cInfo.getMinSpawnHeight()) {
				return false;
			}
			if (spawnLoc.getBlock().getLightLevel() > cInfo.getMaxLight()) {
				return false;
			}
			if (spawnLoc.getBlock().getLightLevel() < cInfo.getMinLight()) {
				return false;
			}
			if (!cInfo.getSpawnBlocks().contains(spawnLoc.getBlock().getType())) {
				return false;
			}
			if (spawnChance(precent)) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public CreatureType getCreatureType(Entity entity) {
		if (entity instanceof LivingEntity) {
			if (entity instanceof Creature) {
				// Animals
				if (entity instanceof Animals) {
					if (entity instanceof Chicken) {
						return CreatureType.CHICKEN;
					} else if (entity instanceof Cow) {
						return CreatureType.COW;
					} else if (entity instanceof Pig) {
						return CreatureType.PIG;
					} else if (entity instanceof Sheep) {
						return CreatureType.SHEEP;
					}
				}
				// Monsters
				else if (entity instanceof Monster) {
					if (entity instanceof Zombie) {
						if (entity instanceof PigZombie) {
							return CreatureType.PIG_ZOMBIE;
						} else {
							return CreatureType.ZOMBIE;
						}
					} else if (entity instanceof Creeper) {
						return CreatureType.CREEPER;
					} else if (entity instanceof Giant) {
					} else if (entity instanceof Skeleton) {
						return CreatureType.SKELETON;
					} else if (entity instanceof Spider) {
						return CreatureType.SPIDER;
					} else if (entity instanceof Slime) {
						return CreatureType.SLIME;
					}
				}
				// Water Animals
				else if (entity instanceof WaterMob) {
					if (entity instanceof Squid) {
						return CreatureType.SQUID;
					}
				}
			}
			// Flying
			else if (entity instanceof Flying) {
				if (entity instanceof Ghast) {
					return CreatureType.GHAST;
				}
			}
		}
		return null;
	}

	public void onEnable() {
		pdfFile = this.getDescription();
		log = Logger.getLogger("Minecraft");

		// Load config file
		loadSettings(this.getConfiguration());
		worldListener = new MobControlWorldListener(this, defaults);

		// Register our events
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvent(Type.CREATURE_SPAWN, entityListener, Priority.Highest,
				this);
		pm.registerEvent(Type.ENTITY_COMBUST, entityListener, Priority.Normal,
				this);
		pm.registerEvent(Type.ENTITY_TARGET, entityListener, Priority.High,
				this);
		pm.registerEvent(Type.ENTITY_DEATH, entityListener, Priority.Monitor,
				this);
		pm.registerEvent(Type.ENTITY_DAMAGED, entityListener, Priority.High,
				this);
		pm.registerEvent(Type.ENTITY_EXPLODE, entityListener, Priority.High,
				this);
		pm.registerEvent(Type.WORLD_LOADED, worldListener, Priority.Monitor,
				this);

		// Scheduler
		// Remove this until fully working
		// this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new
		// SpawnerCreature(this, creatures), 0L,
		// (long)creatures.getSpawnDelay());
		getServer().getScheduler().scheduleSyncRepeatingTask(this,
				new Runnable() {

					public void run() {
						if (running)
							if (getServer().getOnlinePlayers().length > 0) {
								for (World w : getServer().getWorlds()) {
									for (Entity e : w.getEntities()) {
										if (e instanceof Creature) {
											Creature c = (Creature) e;
											CreatureInfo cInfo = creaturesHandler
													.get(w).get(
															getCreatureType(e));
											if (getCreatureType(e) != null) {
												if (!cInfo.isEnabled()) {
													c.setHealth(0);
												} else {
													if (cInfo.isBurn()) {
														if (shouldBurn(c
																.getLocation())) {
															c.setFireTicks(20);
														}
													} else {
														c.setFireTicks(0);
													}
												}
											}
										}
									}
								}
							}
					}
				}, 0L, 1L);
		getServer().getScheduler().scheduleSyncRepeatingTask(this,
				new Runnable() {

					public void run() {
						if (running)
							for (Player p : getServer().getOnlinePlayers()) {
								for (Entity e : p.getWorld().getEntities()) {
									double deltax = Math.abs(e.getLocation()
											.getX() - p.getLocation().getX());
									double deltay = Math.abs(e.getLocation()
											.getY() - p.getLocation().getY());
									double deltaz = Math.abs(e.getLocation()
											.getZ() - p.getLocation().getZ());
									double distance = Math
											.sqrt((deltax * deltax)
													+ (deltay * deltay)
													+ (deltaz * deltaz));
									CreatureType cType = getCreatureType(e);
									if (cType != null) {
										if (cType != null) {
											CreatureInfo cInfo = creaturesHandler
													.get(p.getWorld()).get(
															cType);
											if (distance < 1.3) {
												if (cInfo.getCreature() != CreatureType.SKELETON
														|| cInfo.getCreature() != CreatureType.CREEPER
														|| cInfo.getCreature() != CreatureType.GHAST) {
													p.damage(cInfo
															.getAttackDamage());
												}
											} else if (distance < 24) {
												if (isDay(e.getWorld())) {
													if (cInfo.getNatureDay() == CreatureNature.AGGRESSIVE) {
														((Creature) e)
																.setTarget((LivingEntity) p);
													}
												} else {
													if (cInfo.getNatureNight() == CreatureNature.AGGRESSIVE) {
														((Creature) e)
																.setTarget((LivingEntity) p);
													}
												}
											}
										}
									}
								}
							}
					}
				}, 0L, 10L);

		this.running = true;
		
		log.info("[" + pdfFile.getName() + "] Version " + pdfFile.getVersion()
				+ " is enabled!");
	}

	public void loadSettings(Configuration config) {
		defaults = new Defaults(config);
		File yml = new File(getDataFolder() + "/config.yml");
		if (!this.getDataFolder().exists()) {
			this.getDataFolder().mkdirs();
		}
		if (!yml.exists()) {
			try {
				yml.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			creaturesHandler.loadSettings(config);
		}

		for (World w : this.getServer().getWorlds()) {
			if (!creaturesHandler.containsKey(w)) {
				defaults.SetConfig(w);
				Creatures creautres = new Creatures(w, config);
				creaturesHandler.put(w, creautres);

				log.info("[" + pdfFile.getName()
						+ "] Created default settings for world: "
						+ w.getName());
			}
		}

		log.info("[" + pdfFile.getName() + "] Loaded settings!");
	}
}
