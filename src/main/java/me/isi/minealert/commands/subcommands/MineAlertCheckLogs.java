package me.isi.minealert.commands.subcommands;

import me.isi.minealert.menu.CheckAlertMenu;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.mineacademy.fo.command.SimpleSubCommand;
import org.mineacademy.fo.settings.SimpleLocalization;

public class MineAlertCheckLogs extends SimpleSubCommand {


	public MineAlertCheckLogs(){
		super("checklogs");
		setDescription("Open a GUI with player's info's");
		setMinArguments(1);
		setUsage("<player>");
	}

	@Override
	protected void onCommand() {
		String playerName = args[0];
		OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(playerName);

		if(!targetPlayer.hasPlayedBefore() && !targetPlayer.isOnline()){

			tellWarn(SimpleLocalization.Player.NOT_PLAYED_BEFORE.
					replace("{player}", playerName));

			return;
		}

		tellNoPrefix("&aOpening logs for &7" + targetPlayer.getName());

		new CheckAlertMenu(targetPlayer).displayTo(getPlayer());
	}
}
