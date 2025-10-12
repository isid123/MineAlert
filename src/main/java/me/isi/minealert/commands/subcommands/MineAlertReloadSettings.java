package me.isi.minealert.commands.subcommands;

import me.isi.minealert.settings.Settings;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.command.SimpleSubCommand;

public class MineAlertReloadSettings extends SimpleSubCommand {

	public MineAlertReloadSettings(SimpleCommandGroup parent) {
		super(parent, "reload|rl");
		setDescription("Reload the settings.yml");
	}

	@Override
	protected void onCommand() {
		Settings.reload();
		tellSuccess("Settings reloaded!");
	}
}
