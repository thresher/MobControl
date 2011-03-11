package com.WinSock.MobControl.Listeners;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Creature;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.LivingEntity;
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

public class MobControlEntityListener extends EntityListener {
	private final MobControlPlugin plugin;

	private List<Creature> attacked = new ArrayList<Creature>();

	public MobControlEntityListener(final MobControlPlugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public void onEntityDeath(EntityDeathEvent event) {
		if (event.getEntity() instanceof Creature) {
			Creature c = (Creature) event.getEntity();
			attacked.remove(c);
		}
	}

	@Override
	public void onEntityCombust(EntityCombustEvent event) {
		CreatureType cType = plugin.getCreatureType(event.getEntity());
		if (cType != null) {
			CreatureInfo cInfo = plugin.creaturesHandler.get(
					event.getEntity().getWorld()).get(cType);
			if (!cInfo.isBurn()) {
				event.setCancelled(true);
			}
		}
	}

	@Override
	public void onEntityDamage(EntityDamageEvent event) {
		if (event instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent dmgByEntity = (EntityDamageByEntityEvent) event;
			CreatureType cType = plugin.getCreatureType(event.getEntity());
			if (cType != null) {
				CreatureInfo cInfo = plugin.creaturesHandler.get(
						event.getEntity().getWorld()).get(cType);
				if (plugin.isDay(event.getEntity().getWorld())) {
					if (plugin.shouldTarget(event.getEntity(),
							cInfo.getNatureDay(), true)) {
						Creature c = (Creature) dmgByEntity.getEntity();
						if (dmgByEntity.getDamager() instanceof LivingEntity) {
							c.setTarget((LivingEntity) dmgByEntity.getDamager());
							if (!attacked.contains(c)) {
								attacked.add(c);
							}
						}
					}
				} else {
					if (plugin.shouldTarget(event.getEntity(),
							cInfo.getNatureNight(), true)) {
						Creature c = (Creature) event.getEntity();
						if (dmgByEntity.getDamager() instanceof LivingEntity) {
							c.setTarget((LivingEntity) dmgByEntity.getDamager());
							if (!attacked.contains(c)) {
								attacked.add(c);
							}
						}
					}
				}
			}
		}
	}

	@Override
	public void onEntityTarget(EntityTargetEvent event) {
		CreatureType cType = plugin.getCreatureType(event.getEntity());
		if (cType != null) {
			CreatureInfo cInfo = plugin.creaturesHandler.get(
					event.getEntity().getWorld()).get(cType);
			if (event.getReason() == TargetReason.FORGOT_TARGET
					|| event.getReason() == TargetReason.TARGET_DIED) {
				if (event.getEntity() instanceof Creature) {
					Creature c = (Creature) event.getEntity();
					attacked.remove(c);
				}
			} else if (event.getReason() == TargetReason.TARGET_ATTACKED_ENTITY) {
				if (event.getEntity() instanceof Creature) {
					Creature c = (Creature) event.getEntity();
					if (!attacked.contains(c)) {
						if (plugin.isDay(event.getEntity().getWorld())) {
							if (plugin.shouldTarget(event.getEntity(),
									cInfo.getNatureDay(), true)) {
								attacked.add(c);
							}
							else
							{
								event.setCancelled(true);
							}
						}
						else
						{
							if (plugin.shouldTarget(event.getEntity(),
									cInfo.getNatureNight(), true)) {
								attacked.add(c);
							}
							else
							{
								event.setCancelled(true);
							}
						}
					}
				}
			} else if (!(event.getReason() == TargetReason.CUSTOM)) {
				if (cType != null) {
					if (plugin.isDay(event.getEntity().getWorld())) {
						Creature c = (Creature) event.getEntity();
						if (!plugin.shouldTarget(event.getEntity(),
								cInfo.getNatureDay(), attacked.contains(c))) {
							event.setCancelled(true);
						}
					} else {
						Creature c = (Creature) event.getEntity();
						if (!plugin.shouldTarget(event.getEntity(),
								cInfo.getNatureNight(), attacked.contains(c))) {
							event.setCancelled(true);
						}
					}
				}
			}
		}
	}

	@Override
	public void onEntityExplode(EntityExplodeEvent event) {
		CreatureType cType = plugin.getCreatureType(event.getEntity());
		if (cType != null) {
			CreatureInfo cInfo = plugin.creaturesHandler.get(
					event.getEntity().getWorld()).get(cType);
			if (cType == CreatureType.CREEPER) {
				if (plugin.isDay(event.getEntity().getWorld())) {
					Creature c = (Creature) event.getEntity();
					if (!plugin.shouldTarget(event.getEntity(),
							cInfo.getNatureDay(), attacked.contains(c))) {
						event.setCancelled(true);
					}
				} else {
					Creature c = (Creature) event.getEntity();
					if (!plugin.shouldTarget(event.getEntity(),
							cInfo.getNatureNight(), attacked.contains(c))) {
						event.setCancelled(true);
					}
				}
			}
		}
	}

	@Override
	public void onCreatureSpawn(CreatureSpawnEvent event) {
		CreatureType cType = plugin.getCreatureType(event.getEntity());
		if (cType != null) {
			CreatureInfo cInfo = plugin.creaturesHandler.get(
					event.getEntity().getWorld()).get(cType);
			if (!plugin.canSpawn(event.getLocation(), cInfo, 100)) {
				event.setCancelled(true);
			}
		}
	}
}
