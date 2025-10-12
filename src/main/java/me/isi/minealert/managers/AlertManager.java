package me.isi.minealert.managers;

import lombok.Getter;
import me.isi.minealert.settings.Settings;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class AlertManager {

	@Getter
	private final static AlertManager instance = new AlertManager();


	public void sendAlert(Player player, Material block, Location location) {

		String message = Settings.ALERT_MSG
				.replace("%player%", player.getName())
				.replace("%block%", block.name())
				.replace("%world%", location.getWorld().getName());


		String perm = Settings.ALERT_PERMISSION;

		for (Player p : Bukkit.getOnlinePlayers()) {
			if (p.hasPermission(perm)) {
				p.sendMessage(message);
			}
		}
	}
}
