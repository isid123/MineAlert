package me.isi.minealert.settings;

import lombok.SneakyThrows;
import org.bukkit.Material;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.settings.YamlStaticConfig;

import java.lang.reflect.Field;
import java.util.List;
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


		private static void init() {
			setPathPrefix("recent-logs-command");

			LOGS_TO_SHOW = getInteger("logs-to-show");
			HEADER = Common.colorize(getString("header"));
			ENTRY_FORMAT = Common.colorize(getString("entry-format"));
			FOOTER = Common.colorize(getString("footer"));
			NO_LOGS_MESSAGE = Common.colorize(getString("no-logs-message"));
			FETCHING_MESSAGE = Common.colorize(getString("fetching-message"));
		}

		public final static class messages {


			private static void init() {
			}
		}

	}

	public static final class CheckLogsMenu {
		public static String TITLE;

		public static final class PlayerInfoItem {
			public static String NAME;
			public static List<String> LORE;
		}

		public static final class StatisticsItem {
			public static String NAME;
			public static List<String> LORE_HEADER;
			public static String TOP_BLOCK_ENTRY_FORMAT;
			public static String NO_DATA_MESSAGE;
		}

		private static void init() {
			setPathPrefix("check-logs-menu");
			TITLE = getString("title");

			setPathPrefix("check-logs-menu.player-info-item");
			PlayerInfoItem.NAME = getString("name");
			PlayerInfoItem.LORE = getStringList("lore");

			setPathPrefix("check-logs-menu.statistics-item");
			StatisticsItem.NAME = getString("name");
			StatisticsItem.LORE_HEADER = getStringList("lore-header");
			StatisticsItem.TOP_BLOCK_ENTRY_FORMAT = getString("top-block-entry-format");
			StatisticsItem.NO_DATA_MESSAGE = getString("no-data-message");
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
