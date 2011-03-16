package com.WinSock.MobControl.Listeners;

import org.bukkit.event.world.WorldEvent;
import org.bukkit.event.world.WorldListener;

import com.WinSock.MobControl.Defaults;
import com.WinSock.MobControl.MobControlPlugin;
import com.WinSock.MobControl.Spawner.Creatures;

public class MobControlWorldListener extends WorldListener {

	private final MobControlPlugin plugin;
	private Defaults defaults;

	public MobControlWorldListener(final MobControlPlugin plugin,
			Defaults defaults) {
		this.defaults = defaults;
		this.plugin = plugin;
	}

	@Override
	public void onWorldLoaded(WorldEvent event) {
		if (plugin.running) {
			if (!plugin.creaturesHandler.containsKey(event.getWorld())) {
				defaults.SetConfig(event.getWorld());
				Creatures creautres = new Creatures(event.getWorld(),
						defaults.getConfig());
				plugin.creaturesHandler.put(event.getWorld(), creautres);
			}
		}
	}
}
