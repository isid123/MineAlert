package me.isi.minealert.listeners;

import me.isi.minealert.managers.PlayerManager;
import me.isi.minealert.services.BlockTrackingService;
import me.isi.minealert.settings.Settings;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public final class BlockBreakListener implements Listener {

	private final BlockTrackingService service;
	private final PlayerManager playerManager;

	public BlockBreakListener(BlockTrackingService service, PlayerManager playerManager) {
		this.service = service;
		this.playerManager = playerManager;
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {

		if (event.getPlayer().hasPermission(Settings.BYPASS_PERMISSION)) {
			return;
		}

		Material blockType = event.getBlock().getType();
		Location location = event.getBlock().getLocation();
		if (Settings.BLOCKS.contains(blockType)) {
			service.HandleBreakBlock(event.getPlayer(), blockType, location);
		}
	}
	@EventHandler
	public void onPlayerQuitEvent(PlayerQuitEvent event){
		playerManager.reset(event.getPlayer().getUniqueId());
	}
}
