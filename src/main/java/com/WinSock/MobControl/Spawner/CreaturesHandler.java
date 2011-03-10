package com.WinSock.MobControl.Spawner;

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.bukkit.World;
import org.bukkit.util.config.Configuration;

import com.WinSock.MobControl.MobControlPlugin;

public class CreaturesHandler extends AbstractMap<World, Creatures> {

	private final MobControlPlugin plugin;

	private ArrayList<Map.Entry<World, Creatures>> data = new ArrayList<Map.Entry<World, Creatures>>();
	private Set<Map.Entry<World, Creatures>> entries = null;

	public CreaturesHandler(final MobControlPlugin plugin) {
		this.plugin = plugin;
	}

	static class Entry implements Map.Entry<World, Creatures> {
		protected Object key, value;

		public Entry(Object key, Object value) {
			this.key = key;
			this.value = value;
		}

		public World getKey() {
			return (World) key;
		}

		public Creatures getValue() {
			return (Creatures) value;
		}

		public Creatures setValue(Creatures newValue) {
			Creatures oldValue = (Creatures) value;
			value = newValue;
			return oldValue;
		}

		public boolean equals(Object o) {
			if (!(o instanceof Map.Entry)) {
				return false;
			}
			@SuppressWarnings("unchecked")
			Map.Entry<World, Creatures> e = (Map.Entry<World, Creatures>) o;
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

	public void loadSettings(Configuration config) {
		config.load();
		for (World w : plugin.getServer().getWorlds()) {
			Creatures c = new Creatures(w, config);
			c.loadSettings();
			this.put(w, c);
		}
	}

	@Override
	public Creatures put(World key, Creatures value) {
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
		Creatures oldValue = null;
		if (i < data.size()) {
			oldValue = entry.getValue();
			entry.setValue(value);
		} else {
			data.add(new Entry(key, value));
		}
		return oldValue;
	}

	public void saveSettings() {
		for (Creatures c : this.values()) {
			c.saveSettings();
		}
	}

	@Override
	public Set<java.util.Map.Entry<World, Creatures>> entrySet() {
		if (entries == null) {
			entries = new AbstractSet<Map.Entry<World, Creatures>>() {
				public void clear() {
					data.clear();
				}

				public Iterator<Map.Entry<World, Creatures>> iterator() {
					return data.iterator();
				}

				public int size() {
					return data.size();
				}
			};
		}
		return entries;
	}

}
