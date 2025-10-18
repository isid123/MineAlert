package me.isi.minealert.commands.subcommands;

import me.isi.minealert.database.Database;
import me.isi.minealert.models.LogEntry;
import me.isi.minealert.settings.Settings;
import org.bukkit.OfflinePlayer;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.command.SimpleSubCommand;
import org.mineacademy.fo.settings.SimpleLocalization;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MineAlertRecentLogs extends SimpleSubCommand {


	public MineAlertRecentLogs() {
		super("recentlogs");
		setDescription("Check player latest logs");
		setMinArguments(1);
		setUsage("<player>");
	}


	@Override
	protected void onCommand() {
		String playerName = args[0];
		OfflinePlayer targetPlayer = findPlayer(playerName);

		if (targetPlayer == null) {
			tellError(SimpleLocalization.Player.NOT_ONLINE);
			return;
		}

		tellNoPrefix(Settings.recentLogs.FETCHING_MESSAGE.replace("%player%", playerName));

		Database.getInstance().getPlayerLogs(targetPlayer.getUniqueId(), this::displayLogs);
	}

	private void displayLogs(List<LogEntry> logs) {
		OfflinePlayer targetPlayer = findPlayer(args[0]);

		if (logs.isEmpty()) {
			tellNoPrefix(Settings.recentLogs.NO_LOGS_MESSAGE.replace("%player%", targetPlayer.getName()));
			return;
		}

		displayHeader(targetPlayer.getName());

		int logsToShow = Math.min(Settings.recentLogs.LOGS_TO_SHOW, logs.size());
		for (int i = 0; i < logsToShow; i++) {
			tellNoPrefix(formatLogEntry(logs.get(i)));
		}

		displayFooter();
	}

	private void displayHeader(String playerName) {
		tellNoPrefix(Settings.recentLogs.HEADER.replace("%player%",playerName));
	}

	private void displayFooter() {
		tellNoPrefix(Settings.recentLogs.FOOTER);
	}

	private String formatLogEntry(LogEntry log) {
		String format = Settings.recentLogs.ENTRY_FORMAT;
		String formattedDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date(log.getTimestamp()));
		String blockName = log.getBlockType().name().replace("_", " ");

		return Common.colorize(format
				.replace("%block_type%", blockName)
				.replace("%date%", formattedDate)
				.replace("%x%", String.valueOf(log.getLocation().getBlockX()))
				.replace("%y%", String.valueOf(log.getLocation().getBlockY()))
				.replace("%z%", String.valueOf(log.getLocation().getBlockZ()))
				.replace("%world%", log.getLocation().getWorld().getName())
		);
	}

}
