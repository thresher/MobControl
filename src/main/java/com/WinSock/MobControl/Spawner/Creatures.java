package com.WinSock.MobControl.Spawner;

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.entity.CreatureType;
import org.bukkit.util.config.Configuration;

import com.WinSock.MobControl.Exception.EnvironmentNotFoundException;
import com.WinSock.MobControl.Exception.MobTypeNotFoundException;
import com.WinSock.MobControl.Exception.WorldNotFoundException;
import com.WinSock.MobControl.Spawner.CreatureNature;
import com.WinSock.MobControl.Spawner.SpawnTime;

public class Creatures extends AbstractMap<CreatureType, CreatureInfo> {

	private ArrayList<Map.Entry<CreatureType, CreatureInfo>> data = new ArrayList<Map.Entry<CreatureType, CreatureInfo>>();
	private Set<Map.Entry<CreatureType, CreatureInfo>> entries = null;

	// Global Default Settings
	private int maxPassive = 500;
	private int maxHostile = 200;
	private int maxNeutral = 15;
	private int distanceFromPlayer = 24;
	private int spawnDelay = 1;

	private World world;
	private Configuration config;

	public Creatures(World world, Configuration config) {
		this.world = world;
		this.config = config;
	}

	static class Entry implements Map.Entry<CreatureType, CreatureInfo> {
		protected Object key, value;

		public Entry(Object key, Object value) {
			this.key = key;
			this.value = value;
		}

		public CreatureType getKey() {
			return (CreatureType) key;
		}

		public CreatureInfo getValue() {
			return (CreatureInfo) value;
		}

		public CreatureInfo setValue(CreatureInfo newValue) {
			CreatureInfo oldValue = (CreatureInfo) value;
			value = newValue;
			return oldValue;
		}

		public boolean equals(Object o) {
			if (!(o instanceof Map.Entry)) {
				return false;
			}
			@SuppressWarnings("unchecked")
			Map.Entry<CreatureType, CreatureInfo> e = (Map.Entry<CreatureType, CreatureInfo>) o;
			return (key == null ? e.getKey() == null : key.equals(e.getKey()))
					&& (value == null ? e.getValue() == null : value.equals(e
							.getValue()));
		}

		public int hashCode() {
			int keyHash = (key == null ? 0 : key.hashCode());
			int valueHash = (value == null ? 0 : value.hashCode());
			return keyHash ^ valueHash;
		}

		public String toString() {
			return key + "=" + value;
		}
	}

	public void setMaxHostile(int maxHostile) {
		this.maxHostile = maxHostile;
	}

	public int getMaxHostile() {
		return maxHostile;
	}

	public void setMaxNeutral(int maxNeutral) {
		this.maxNeutral = maxNeutral;
	}

	public int getMaxNeutral() {
		return maxNeutral;
	}

	public void setDistanceFromPlayer(int distanceFromPlayer) {
		this.distanceFromPlayer = distanceFromPlayer;
	}

	public int getDistanceFromPlayer() {
		return distanceFromPlayer;
	}

	public int getMaxPassive() {
		return maxPassive;
	}

	public void setMaxPassive(int maxPassive) {
		this.maxPassive = maxPassive;
	}

	public CreatureInfo getCreatureFromType(CreatureType type)
			throws MobTypeNotFoundException {
		for (CreatureInfo i : this.values()) {
			if (i.getCreature() == type) {
				return i;
			}
		}

		throw new MobTypeNotFoundException();
	}

	public Set<CreatureInfo> getEnabled() {
		Set<CreatureInfo> returnData = new HashSet<CreatureInfo>();
		for (CreatureInfo i : this.values()) {
			if (i.isEnabled()) {
				returnData.add(i);
			}
		}

		return returnData;
	}

	public Set<CreatureInfo> getCreatures(SpawnTime t, CreatureNature n) {
		Set<CreatureInfo> returnData = new HashSet<CreatureInfo>();
		for (CreatureInfo i : this.values()) {
			if (i.isEnabled()) {
				if (t == SpawnTime.DAY) {
					if (i.getNatureDay() == n) {
						returnData.add(i);
					}
				} else if (t == SpawnTime.NIGHT) {
					if (i.getNatureNight() == n) {
						returnData.add(i);
					}
				}
			}
		}

		return returnData;
	}

	public void setSpawnDelay(int spawnDelay) {
		this.spawnDelay = spawnDelay;
	}

	public int getSpawnDelay() {
		return spawnDelay;
	}

	public void saveSettings() {
		config.setProperty("MobControl." + world.getName() + ".MaxPassive",
				maxPassive);
		config.setProperty("MobControl." + world.getName() + ".MaxHostile",
				maxHostile);
		config.setProperty("MobControl." + world.getName() + ".MaxNeutral",
				maxNeutral);
		config.setProperty("MobControl." + world.getName()
				+ ".SpawnDistanceFromPlayer", distanceFromPlayer);
		config.setProperty("MobControl." + world.getName() + ".SpawnDelay",
				spawnDelay);
		config.save();

		for (CreatureInfo i : this.values()) {
			i.saveConfig(config);
		}
	}

	public void loadSettings() throws WorldNotFoundException {
		config.load();

		maxPassive = config.getInt("MobControl." + world.getName()
				+ ".MaxPassive", 500);
		maxHostile = config.getInt("MobControl." + world.getName()
				+ ".MaxHostile", 200);
		maxNeutral = config.getInt("MobControl." + world.getName()
				+ ".MaxNeutral", 15);
		distanceFromPlayer = config.getInt("MobControl." + world.getName()
				+ ".SpawnDistanceFromPlayer", 24);
		spawnDelay = config.getInt("MobControl." + world.getName()
				+ ".SpawnDelay", 1);

		List<String> nodes = config.getKeys("MobControl." + world.getName()
				+ ".Mobs");

		if (nodes != null) {
			for (String key : nodes) {
				Set<Material> spawnBlocks = new HashSet<Material>();
				List<Integer> blockIds = config.getIntList("MobControl."
						+ world.getName() + ".Mobs." + key + ".SpawnMaterial",
						null);
				for (int i : blockIds) {
					spawnBlocks.add(Material.getMaterial(i));
				}

				int minLight = config.getInt("MobControl." + world.getName()
						+ ".Mobs." + key + ".SpawnLightMin", 0);
				int maxLight = config.getInt("MobControl." + world.getName()
						+ ".Mobs." + key + ".SpawnLightMax", 0);

				CreatureNature natureDay = CreatureNature.fromString(config
						.getString("MobControl." + world.getName() + ".Mobs."
								+ key + ".Day.Nature", "Passive"));
				CreatureNature natureNight = CreatureNature.fromString(config
						.getString("MobControl." + world.getName() + ".Mobs."
								+ key + ".Night.Nature", "Passive"));

				int minSpawnHeight = config.getInt(
						"MobControl." + world.getName() + ".Mobs." + key
								+ ".SpawnHeightMin", 0);
				int maxSpawnHeight = config.getInt(
						"MobControl." + world.getName() + ".Mobs." + key
								+ ".SpawnHeightMax", 0);

				int spawnRoom = config.getInt("MobControl." + world.getName()
						+ ".Mobs." + key + ".SpawnRoom", 0);

				double spawnRate = config.getDouble(
						"MobControl." + world.getName() + ".Mobs." + key
								+ ".SpawnRate", 0.50D);

				boolean dayBurn = config.getBoolean(
						"MobControl." + world.getName() + ".Mobs." + key
								+ ".Day.Burn", false);

				boolean enabled = config.getBoolean(
						"MobControl." + world.getName() + ".Mobs." + key
								+ ".Enabled", true);

				int health = config.getInt("MobControl." + world.getName()
						+ ".Mobs." + key + ".Health", 0);

				int attackDamage = config.getInt(
						"MobControl." + world.getName() + ".Mobs." + key
								+ ".AttackDamage", 0);

				SpawnTime spawnTime = SpawnTime.fromString(config.getString(
						"MobControl." + world.getName() + ".Mobs." + key
								+ ".SpawnTime", "Both"));

				Environment environment;
				try {
					environment = findEnvironment(config.getString(
							"MobControl." + world.getName() + ".Mobs." + key
									+ ".Environment", "NORMAL"));
					CreatureInfo creature = new CreatureInfo(findType(key),
							spawnBlocks, minLight, maxLight, spawnRate,
							minSpawnHeight, maxSpawnHeight, spawnRoom, dayBurn,
							natureDay, natureNight, health, attackDamage,
							spawnTime, enabled, environment, world);
					this.put(findType(key), creature);
				} catch (EnvironmentNotFoundException e) {
					System.out
							.println("Invalid environment node in: MobControl."
									+ world.getName() + ".Mobs." + key);
				} catch (MobTypeNotFoundException e) {
					System.out.println("Invalid node name: " + key
							+ ", in: MobControl." + world.getName() + ".Mobs");
					e.printStackTrace();
				}
			}
		} else {
			throw new WorldNotFoundException();
		}
	}

	@Override
	public Set<java.util.Map.Entry<CreatureType, CreatureInfo>> entrySet() {
		if (entries == null) {
			entries = new AbstractSet<Map.Entry<CreatureType, CreatureInfo>>() {
				public void clear() {
					data.clear();
				}

				public Iterator<Map.Entry<CreatureType, CreatureInfo>> iterator() {
					return data.iterator();
				}

				public int size() {
					return data.size();
				}
			};
		}
		return entries;
	}

	@Override
	public CreatureInfo put(CreatureType key, CreatureInfo value) {
		Entry entry = null;
		int i = 0;

		if (key == null) {
			for (i = 0; i < data.size(); i++) {
				entry = (Entry) (data.get(i));
				if (entry.getKey() == null) {
					break;
				}
			}
		} else {
			for (i = 0; i < data.size(); i++) {
				entry = (Entry) (data.get(i));
				if (key.equals(entry.getKey())) {
					break;
				}
			}
		}
		CreatureInfo oldValue = null;
		if (i < data.size()) {
			oldValue = entry.getValue();
			entry.setValue(value);
		} else {
			data.add(new Entry(key, value));
		}
		return oldValue;
	}

	public CreatureType findType(String mob) throws MobTypeNotFoundException {
		for (CreatureType mobtype : CreatureType.values()) {
			if (mobtype.name().equalsIgnoreCase(mob))
				return mobtype;
			else if (mobtype.name().replaceAll("_", "").equalsIgnoreCase(mob))
				return mobtype;
		}
		throw new MobTypeNotFoundException();
	}

	public Environment findEnvironment(String ev)
			throws EnvironmentNotFoundException {
		for (Environment e : Environment.values()) {
			if (e.name().equalsIgnoreCase(ev))
				return e;
			else if (e.name().replaceAll("_", "").equalsIgnoreCase(ev))
				return e;
		}
		throw new EnvironmentNotFoundException();
	}
}
