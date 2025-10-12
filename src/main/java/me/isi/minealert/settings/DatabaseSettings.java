package me.isi.minealert.settings;

import lombok.SneakyThrows;
import org.mineacademy.fo.settings.YamlStaticConfig;

import java.lang.reflect.Field;

public class DatabaseSettings extends YamlStaticConfig {

	public static Boolean ENABLED;
	public static String TYPE;
	public static String HOST;
	public static Integer PORT;
	public static String DATABASE;
	public static String USER;
	public static String PASSWORD;

	public static Boolean ALLOW_PUBLIC_KEY_RETRIEVAL;

	@Override
	protected void onLoad() throws Exception {
		this.loadConfiguration("database.yml");
	}

	private static void init() {
		ENABLED = getBoolean("enabled");
		TYPE = getString("type").toLowerCase();
		HOST = getString("host");
		PORT = getInteger("port");
		DATABASE = getString("database");
		USER = getString("user");
		PASSWORD = getString("password");
		ALLOW_PUBLIC_KEY_RETRIEVAL = getBoolean("Options.Allow_Public_Key_Retrieval");
	}

	@SneakyThrows
	private static void printField(Class<?> clazz) {
		for (Field field : clazz.getDeclaredFields())
			System.out.println(clazz.getSimpleName() + "." + field.getName() + "--->" + field.get(null));

		for (Class<?> superClass : clazz.getDeclaredClasses())
			printField(superClass);
	}
}
