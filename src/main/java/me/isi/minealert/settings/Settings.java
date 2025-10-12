package me.isi.minealert.settings;

import lombok.SneakyThrows;
import org.bukkit.Material;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.settings.YamlStaticConfig;

import java.lang.reflect.Field;
import java.util.Set;


public final class Settings extends YamlStaticConfig {

	public static Set<Material> BLOCKS;
	public static Integer MAX_BLOCKS;
	public static Long TIME_WINDOW_MS;
	public static String ALERT_MSG;
	public static String ALERT_PERMISSION;
	public static String BYPASS_PERMISSION;


	public final static class recentLogs {

		public static Integer LOGS_TO_SHOW;
		public static String HEADER;
		public static String ENTRY_FORMAT;
		public static String FOOTER;
		public static String NO_LOGS_MESSAGE;
		public static String FETCHING_MESSAGE;
		public static String PLAYER_NOT_FOUND_MESSAGE;


		private static void init() {
			setPathPrefix("check-logs-command");

			LOGS_TO_SHOW = getInteger("logs-to-show");
			HEADER = Common.colorize(getString("header"));
			ENTRY_FORMAT = Common.colorize(getString("entry-format"));
			FOOTER = Common.colorize(getString("footer"));
			NO_LOGS_MESSAGE = Common.colorize(getString("no-logs-message"));
			FETCHING_MESSAGE = Common.colorize(getString("fetching-message"));
			PLAYER_NOT_FOUND_MESSAGE = Common.colorize(getString("player-not-found-message"));
		}

	}


	@Override
	protected void onLoad() {
		this.loadConfiguration("settings.yml");
	}

	private static void init() {
		BLOCKS = getSet("blocks", Material.class);
		MAX_BLOCKS = getInteger("max-blocks");
		TIME_WINDOW_MS = (long) getInteger("time-window-ms");
		ALERT_MSG = Common.colorize(getString("alert-msg"));
		ALERT_PERMISSION = getString("alert-permission");
		BYPASS_PERMISSION = getString("bypass-permission");
	}

	public static void reload() {
		load(Settings.class);
	}

	@SneakyThrows
	private static void printField(Class<?> clazz) {
		for (Field field : clazz.getDeclaredFields())
			System.out.println(clazz.getSimpleName() + "." + field.getName() + "--->" + field.get(null));

		for (Class<?> superClass : clazz.getDeclaredClasses())
			printField(superClass);
	}
}
