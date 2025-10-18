package me.isi.minealert.commands;

import me.isi.minealert.commands.subcommands.MineAlertCheckLogs;
import me.isi.minealert.commands.subcommands.MineAlertRecentLogs;
import me.isi.minealert.commands.subcommands.MineAlertReloadSettings;
import org.mineacademy.fo.annotation.AutoRegister;
import org.mineacademy.fo.command.SimpleCommandGroup;

@AutoRegister
public final class MineAlertCommand extends SimpleCommandGroup {

	public MineAlertCommand() {
		super("minealert|ma");
	}
	@Override
	protected void registerSubcommands() {
		registerSubcommand(new MineAlertReloadSettings(this));
		registerSubcommand(new MineAlertRecentLogs());
		registerSubcommand(new MineAlertCheckLogs());
	}

	@Override
	protected String getCredits() {
		return "&7Visit github.com/isid123/MineAlert";
	}

}
