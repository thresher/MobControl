package com.WinSock.MobControl.Spawner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.entity.CreatureType;
import org.bukkit.util.config.Configuration;

public class CreatureInfo {

	private CreatureType creature;
	private Set<Material> spawnBlocks = new HashSet<Material>();
	private int minLight, maxLight;
	private double spawnRate;
	private int minSpawnHeight, maxSpawnHeight;
	private int spawnRoom;
	private boolean burn;
	private CreatureNature natureDay;
	private CreatureNature natureNight;
	private int health;
	private int attackDamage;
	private SpawnTime spawnTime;
	private boolean enabled;
	private Environment environment;
	private World world;

	public CreatureInfo(CreatureType creature, Set<Material> spawnBlocks,
			int minLight, int maxLight, double spawnRate, int minSpawnHeight,
			int maxSpawnHeight, int spawnRoom, boolean burn,
			CreatureNature natureDay, CreatureNature natureNight, int health,
			int attackDamage, SpawnTime spawnTime, boolean enabled,
			Environment environment, World world) {
		this.creature = creature;
		this.spawnBlocks = spawnBlocks;
		this.minLight = minLight;
		this.maxLight = maxLight;
		this.spawnRate = spawnRate;
		this.minSpawnHeight = minSpawnHeight;
		this.maxSpawnHeight = maxSpawnHeight;
		this.spawnRoom = spawnRoom;
		this.burn = burn;
		this.natureDay = natureDay;
		this.natureNight = natureNight;
		this.health = health;
		this.attackDamage = attackDamage;
		this.spawnTime = spawnTime;
		this.enabled = enabled;
		this.environment = environment;
		this.world = world;
	}

	public CreatureInfo(CreatureType creature, Material[] spawnBlocks,
			int minLight, int maxLight, double spawnRate, int minSpawnHeight,
			int maxSpawnHeight, int spawnRoom, boolean burn,
			CreatureNature natureDay, CreatureNature natureNight, int health,
			int attackDamage, SpawnTime spawnTime, boolean enabled,
			Environment environment, World world) {
		this.creature = creature;
		this.spawnBlocks = new HashSet<Material>();
		this.spawnBlocks.addAll(Arrays.asList(spawnBlocks));
		this.minLight = minLight;
		this.maxLight = maxLight;
		this.spawnRate = spawnRate;
		this.minSpawnHeight = minSpawnHeight;
		this.maxSpawnHeight = maxSpawnHeight;
		this.spawnRoom = spawnRoom;
		this.burn = burn;
		this.natureDay = natureDay;
		this.natureNight = natureNight;
		this.health = health;
		this.attackDamage = attackDamage;
		this.spawnTime = spawnTime;
		this.enabled = enabled;
		this.environment = environment;
		this.world = world;
	}

	public CreatureInfo(CreatureType creature, Material spawnBlock,
			int minLight, int maxLight, double spawnRate, int minSpawnHeight,
			int maxSpawnHeight, int spawnRoom, boolean burn,
			CreatureNature natureDay, CreatureNature natureNight, int health,
			int attackDamage, SpawnTime spawnTime, boolean enabled,
			Environment environment, World world) {
		this.creature = creature;
		this.spawnBlocks = new HashSet<Material>();
		this.spawnBlocks.add(spawnBlock);
		this.minLight = minLight;
		this.maxLight = maxLight;
		this.spawnRate = spawnRate;
		this.minSpawnHeight = minSpawnHeight;
		this.maxSpawnHeight = maxSpawnHeight;
		this.spawnRoom = spawnRoom;
		this.burn = burn;
		this.natureDay = natureDay;
		this.natureNight = natureNight;
		this.health = health;
		this.attackDamage = attackDamage;
		this.spawnTime = spawnTime;
		this.enabled = enabled;
		this.environment = environment;
		this.world = world;
	}

	public CreatureInfo(CreatureType creature, Set<Material> spawnBlocks,
			int minLight, int maxLight, double spawnRate, int minSpawnHeight,
			int maxSpawnHeight, int spawnRoom, boolean burn,
			CreatureNature natureDay, CreatureNature natureNight, int health,
			int attackDamage, SpawnTime spawnTime, boolean enabled,
			Environment environment) {
		this.creature = creature;
		this.spawnBlocks = spawnBlocks;
		this.minLight = minLight;
		this.maxLight = maxLight;
		this.spawnRate = spawnRate;
		this.minSpawnHeight = minSpawnHeight;
		this.maxSpawnHeight = maxSpawnHeight;
		this.spawnRoom = spawnRoom;
		this.burn = burn;
		this.natureDay = natureDay;
		this.natureNight = natureNight;
		this.health = health;
		this.attackDamage = attackDamage;
		this.spawnTime = spawnTime;
		this.enabled = enabled;
		this.environment = environment;
	}

	public CreatureInfo(CreatureType creature, Material[] spawnBlocks,
			int minLight, int maxLight, double spawnRate, int minSpawnHeight,
			int maxSpawnHeight, int spawnRoom, boolean burn,
			CreatureNature natureDay, CreatureNature natureNight, int health,
			int attackDamage, SpawnTime spawnTime, boolean enabled,
			Environment environment) {
		this.creature = creature;
		this.spawnBlocks = new HashSet<Material>();
		this.spawnBlocks.addAll(Arrays.asList(spawnBlocks));
		this.minLight = minLight;
		this.maxLight = maxLight;
		this.spawnRate = spawnRate;
		this.minSpawnHeight = minSpawnHeight;
		this.maxSpawnHeight = maxSpawnHeight;
		this.spawnRoom = spawnRoom;
		this.burn = burn;
		this.natureDay = natureDay;
		this.natureNight = natureNight;
		this.health = health;
		this.attackDamage = attackDamage;
		this.spawnTime = spawnTime;
		this.enabled = enabled;
		this.environment = environment;
	}

	public CreatureInfo(CreatureType creature, Material spawnBlock,
			int minLight, int maxLight, double spawnRate, int minSpawnHeight,
			int maxSpawnHeight, int spawnRoom, boolean burn,
			CreatureNature natureDay, CreatureNature natureNight, int health,
			int attackDamage, SpawnTime spawnTime, boolean enabled,
			Environment environment) {
		this.creature = creature;
		this.spawnBlocks = new HashSet<Material>();
		this.spawnBlocks.add(spawnBlock);
		this.minLight = minLight;
		this.maxLight = maxLight;
		this.spawnRate = spawnRate;
		this.minSpawnHeight = minSpawnHeight;
		this.maxSpawnHeight = maxSpawnHeight;
		this.spawnRoom = spawnRoom;
		this.burn = burn;
		this.natureDay = natureDay;
		this.natureNight = natureNight;
		this.health = health;
		this.attackDamage = attackDamage;
		this.spawnTime = spawnTime;
		this.enabled = enabled;
		this.environment = environment;
	}

	public void setCreature(CreatureType creature) {
		this.creature = creature;
	}

	public CreatureType getCreature() {
		return creature;
	}

	public void setSpawnBlocks(Set<Material> spawnBlocks) {
		this.spawnBlocks = spawnBlocks;
	}

	public void addSpawnBlocks(Material[] spawnBlocks) {
		this.spawnBlocks.addAll(Arrays.asList(spawnBlocks));
	}

	public void addSpawnBlock(Material spawnBlock) {
		this.spawnBlocks.add(spawnBlock);
	}

	public Set<Material> getSpawnBlocks() {
		return spawnBlocks;
	}

	public void setMaxLight(int maxLight) {
		this.maxLight = maxLight;
	}

	public int getMaxLight() {
		return maxLight;
	}

	public void setMinLight(int minLight) {
		this.minLight = minLight;
	}

	public int getMinLight() {
		return minLight;
	}

	public void setSpawnRate(float spawnRate) {
		this.spawnRate = spawnRate;
	}

	public double getSpawnRate() {
		return spawnRate;
	}

	public void setMinSpawnHeight(int minSpawnHeight) {
		this.minSpawnHeight = minSpawnHeight;
	}

	public int getMinSpawnHeight() {
		return minSpawnHeight;
	}

	public void setMaxSpawnHeight(int maxSpawnHeight) {
		this.maxSpawnHeight = maxSpawnHeight;
	}

	public int getMaxSpawnHeight() {
		return maxSpawnHeight;
	}

	public void setSpawnRoom(int spawnRoom) {
		this.spawnRoom = spawnRoom;
	}

	public int getSpawnRoom() {
		return spawnRoom;
	}

	public void setBurn(boolean burn) {
		this.burn = burn;
	}

	public boolean isBurn() {
		return burn;
	}

	public void setNatureDay(CreatureNature natureDay) {
		this.natureDay = natureDay;
	}

	public CreatureNature getNatureDay() {
		return natureDay;
	}

	public void setNatureNight(CreatureNature natureNight) {
		this.natureNight = natureNight;
	}

	public CreatureNature getNatureNight() {
		return natureNight;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public int getHealth() {
		return health;
	}

	public void setAttackDamage(int attackDamage) {
		this.attackDamage = attackDamage;
	}

	public int getAttackDamage() {
		return attackDamage;
	}

	public void setSpawnTime(SpawnTime spawnTime) {
		this.spawnTime = spawnTime;
	}

	public SpawnTime getSpawnTime() {
		return spawnTime;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	public Environment getEnvironment() {
		return environment;
	}

	public void setWorld(World world) {
		this.world = world;
	}

	public World getWorld() {
		return world;
	}

	public void saveConfig(Configuration config) {
		List<Integer> blocks = new ArrayList<Integer>();
		for (Material m : spawnBlocks) {
			blocks.add(m.getId());
		}

		config.setProperty("MobControl." + world.getName() + ".Mobs."
				+ getCreature().getName() + ".SpawnMaterial", blocks);
		config.setProperty("MobControl." + world.getName() + ".Mobs."
				+ getCreature().getName() + ".Day.Nature", natureDay.getText());
		config.setProperty("MobControl." + world.getName() + ".Mobs."
				+ getCreature().getName() + ".Night.Nature",
				natureNight.getText());
		config.setProperty("MobControl." + world.getName() + ".Mobs."
				+ getCreature().getName() + ".SpawnLightMin", minLight);
		config.setProperty("MobControl." + world.getName() + ".Mobs."
				+ getCreature().getName() + ".SpawnLightMax", maxLight);
		config.setProperty("MobControl." + world.getName() + ".Mobs."
				+ getCreature().getName() + ".SpawnRate", spawnRate);
		config.setProperty("MobControl." + world.getName() + ".Mobs."
				+ getCreature().getName() + ".SpawnHeightMin", minSpawnHeight);
		config.setProperty("MobControl." + world.getName() + ".Mobs."
				+ getCreature().getName() + ".SpawnHeightMax", maxSpawnHeight);
		config.setProperty("MobControl." + world.getName() + ".Mobs."
				+ getCreature().getName() + ".SpawnRoom", spawnRoom);
		config.setProperty("MobControl." + world.getName() + ".Mobs."
				+ getCreature().getName() + ".Day.Burn", burn);
		config.setProperty("MobControl." + world.getName() + ".Mobs."
				+ getCreature().getName() + ".Health", health);
		config.setProperty("MobControl." + world.getName() + ".Mobs."
				+ getCreature().getName() + ".AttackDamage", attackDamage);
		config.setProperty("MobControl." + world.getName() + ".Mobs."
				+ getCreature().getName() + ".SpawnTime", spawnTime.getText());
		config.setProperty("MobControl." + world.getName() + ".Mobs."
				+ getCreature().getName() + ".Enabled", enabled);
		config.setProperty("MobControl." + world.getName() + ".Mobs."
				+ getCreature().getName() + ".Environment", environment.name());

		config.save();
	}
}
