package com.WinSock.MobControl.Listeners;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.entity.Creature;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTargetEvent.TargetReason;

import com.WinSock.MobControl.MobControlPlugin;
import com.WinSock.MobControl.Spawner.CreatureInfo;
import com.WinSock.MobControl.Spawner.CreatureNature;

public class MobControlEntityListener extends EntityListener {
	private final MobControlPlugin plugin;

	public MobControlEntityListener(final MobControlPlugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public void onEntityDeath(EntityDeathEvent event) {
		if (plugin.running) {
			if (event.getEntity() instanceof Creature) {
				Creature c = (Creature) event.getEntity();
				if (plugin.attacked.containsKey(c)) {
					plugin.attacked.remove(c);
				}
			}
		}
	}

	@Override
	public void onEntityCombust(EntityCombustEvent event) {
		if (plugin.running) {
			CreatureType cType = plugin.getCreatureType(event.getEntity());
			if (cType != null) {
				CreatureInfo cInfo = plugin.creaturesHandler.get(
						event.getEntity().getWorld()).get(cType);
				if (cInfo != null && !cInfo.isBurn()) {
					event.setCancelled(true);
				}
			}
		}
	}

	@Override
	public void onEntityDamage(EntityDamageEvent event) {
		if (plugin.running) {
			if (event instanceof EntityDamageByEntityEvent) {
				EntityDamageByEntityEvent dmgByEntity = (EntityDamageByEntityEvent) event;
				CreatureType cType = plugin.getCreatureType(dmgByEntity
						.getEntity());
				if (cType != null) {
					CreatureInfo cInfo = plugin.creaturesHandler.get(
							event.getEntity().getWorld()).get(cType);
					if (cInfo != null) {
						if (plugin.isDay(event.getEntity().getWorld())) {
							if (plugin.shouldTarget(dmgByEntity.getEntity(),
									cInfo.getNatureDay(), true)) {
								if (dmgByEntity.getEntity() instanceof Creature) {
									Creature c = (Creature) event.getEntity();
									c.setTarget((LivingEntity) dmgByEntity
											.getEntity());
									if (plugin.attacked.containsKey(c)) {
										List<LivingEntity> entites = new ArrayList<LivingEntity>(
												plugin.attacked.get(c));
										if (!entites
												.contains((LivingEntity) dmgByEntity
														.getDamager())) {
											entites.add((LivingEntity) dmgByEntity
													.getDamager());
										}
										plugin.attacked.put(c, entites);
									} else {
										plugin.attacked.put(c, Arrays
												.asList((LivingEntity) event
														.getEntity()));
									}
									if (dmgByEntity.getDamager() instanceof LivingEntity) {
										LivingEntity e = (LivingEntity) event
												.getEntity();
										if (!(dmgByEntity.getDamager() instanceof Player)) {
											CreatureType type = plugin
													.getCreatureType(dmgByEntity
															.getDamager());
											if (type != null) {
												if (type != CreatureType.SKELETON
														|| type != CreatureType.CREEPER
														|| type != CreatureType.GHAST) {
													event.setCancelled(true);
												} else {
													e.damage(cInfo
															.getAttackDamage());
												}
											}
										}
									}
								}
							}
						} else {
							if (plugin.shouldTarget(dmgByEntity.getEntity(),
									cInfo.getNatureDay(), true)) {
								if (dmgByEntity.getEntity() instanceof Creature) {
									Creature c = (Creature) event.getEntity();
									c.setTarget((LivingEntity) dmgByEntity
											.getEntity());
									if (plugin.attacked.containsKey(c)) {
										List<LivingEntity> entites = new ArrayList<LivingEntity>(
												plugin.attacked.get(c));
										entites.add((LivingEntity) dmgByEntity
												.getDamager());
										plugin.attacked.put(c, entites);
									} else {
										plugin.attacked
												.put(c,
														Arrays.asList((LivingEntity) dmgByEntity
																.getDamager()));
									}
									if (dmgByEntity.getDamager() instanceof LivingEntity) {
										LivingEntity e = (LivingEntity) event
												.getEntity();
										if (!(dmgByEntity.getDamager() instanceof Player)) {
											CreatureType type = plugin
													.getCreatureType(dmgByEntity
															.getDamager());
											if (type != null) {
												if (type != CreatureType.SKELETON
														|| type != CreatureType.CREEPER
														|| type != CreatureType.GHAST) {
													event.setCancelled(true);
												} else {
													e.damage(cInfo
															.getAttackDamage());
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}

	@Override
	public void onEntityTarget(EntityTargetEvent event) {
		if (plugin.running) {
			CreatureType cType = plugin.getCreatureType(event.getEntity());
			if (cType != null) {
				CreatureInfo cInfo = plugin.creaturesHandler.get(
						event.getEntity().getWorld()).get(cType);
				if (cInfo != null) {
					if (event.getReason() == TargetReason.FORGOT_TARGET
							|| event.getReason() == TargetReason.TARGET_DIED) {
						if (event.getEntity() instanceof Creature) {
							Creature c = (Creature) event.getEntity();
							if (plugin.attacked.size() > 0) {
								List<LivingEntity> entites = new ArrayList<LivingEntity>(
										plugin.attacked.get(c));
								entites.remove((LivingEntity) event.getTarget());
								plugin.attacked.put(c, entites);
							}
						}
					} else if (event.getReason() == TargetReason.TARGET_ATTACKED_ENTITY) {
						if (event.getEntity() instanceof Creature) {
							Creature c = (Creature) event.getEntity();
							if (plugin.isDay(event.getEntity().getWorld())) {
								if (plugin.shouldTarget(event.getEntity(),
										cInfo.getNatureDay(), true)) {
									if (plugin.attacked.containsKey(c)) {
										List<LivingEntity> entites = new ArrayList<LivingEntity>(
												plugin.attacked.get(c));
										entites.add((LivingEntity) event
												.getEntity());
										plugin.attacked.put(c, entites);
									} else {
										plugin.attacked.put(c, Arrays
												.asList((LivingEntity) event
														.getEntity()));
									}
								} else {
									event.setCancelled(true);
								}
							} else {
								if (plugin.shouldTarget(event.getEntity(),
										cInfo.getNatureNight(), true)) {
									if (plugin.attacked.containsKey(c)) {
										List<LivingEntity> entites = new ArrayList<LivingEntity>(
												plugin.attacked.get(c));
										entites.add((LivingEntity) event
												.getEntity());
										plugin.attacked.put(c, entites);
									} else {
										plugin.attacked.put(c, Arrays
												.asList((LivingEntity) event
														.getEntity()));
									}
								} else {
									event.setCancelled(true);
								}
							}
						}
					} else if (event.getReason() == TargetReason.CUSTOM) {
						if (event.getEntity() instanceof Creature) {
							Creature c = (Creature) event.getEntity();
							if (plugin.isDay(event.getEntity().getWorld())) {
								if (plugin.shouldTarget(event.getEntity(),
										cInfo.getNatureDay(), false)) {
									if (plugin.attacked.containsKey(c)) {
										List<LivingEntity> entites = new ArrayList<LivingEntity>(
												plugin.attacked.get(c));
										entites.add((LivingEntity) event
												.getEntity());
										plugin.attacked.put(c, entites);
									} else {
										plugin.attacked.put(c, Arrays
												.asList((LivingEntity) event
														.getEntity()));
									}
								} else {
									event.setCancelled(true);
								}
							} else {
								if (plugin.shouldTarget(event.getEntity(),
										cInfo.getNatureNight(), false)) {
									if (plugin.attacked.containsKey(c)) {
										List<LivingEntity> entites = new ArrayList<LivingEntity>(
												plugin.attacked.get(c));
										entites.add((LivingEntity) event
												.getEntity());
										plugin.attacked.put(c, entites);
									} else {
										plugin.attacked.put(c, Arrays
												.asList((LivingEntity) event
														.getEntity()));
									}
								} else {
									event.setCancelled(true);
								}
							}
						}
					} else if (event.getReason() != TargetReason.CUSTOM) {
						if (cType != null
								&& event.getTarget() instanceof LivingEntity) {
							if (plugin.isDay(event.getEntity().getWorld())) {
								Creature c = (Creature) event.getEntity();
								if (!plugin.shouldTarget(
										event.getEntity(),
										cInfo.getNatureDay(),
										plugin.attacked.get(c).contains(
												(LivingEntity) event
														.getTarget()))) {
									event.setCancelled(true);
								}
							} else {
								Creature c = (Creature) event.getEntity();
								if (!plugin.shouldTarget(
										event.getEntity(),
										cInfo.getNatureNight(),
										plugin.attacked.get(c).contains(
												(LivingEntity) event
														.getTarget()))) {
									event.setCancelled(true);
								}
							}
						}
					}
				}
			}
		}
	}

	@Override
	public void onEntityExplode(EntityExplodeEvent event) {
		if (plugin.running) {
			CreatureType cType = plugin.getCreatureType(event.getEntity());
			if (cType != null) {
				CreatureInfo cInfo = plugin.creaturesHandler.get(
						event.getEntity().getWorld()).get(cType);
				if (cInfo != null && cType == CreatureType.CREEPER) {
					if (plugin.isDay(event.getEntity().getWorld())) {
						if (cInfo.getNatureDay() == CreatureNature.PASSIVE) {
							event.setCancelled(true);
						}
					} else {
						if (cInfo.getNatureNight() == CreatureNature.PASSIVE) {
							event.setCancelled(true);
						}
					}
				}
			}
		}
	}

	@Override
	public void onCreatureSpawn(CreatureSpawnEvent event) {
		if (plugin.running) {
			CreatureType cType = plugin.getCreatureType(event.getEntity());
			if (cType != null) {
				CreatureInfo cInfo = plugin.creaturesHandler.get(
						event.getEntity().getWorld()).get(cType);
				if (cInfo != null) {
					if (!plugin.canSpawn(event.getLocation(), cInfo, 100)) {
						event.setCancelled(true);
					}
				}
			}
		}
	}
}
