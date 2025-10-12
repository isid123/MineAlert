package me.isi.minealert.managers;

import lombok.Getter;
import me.isi.minealert.database.Database;
import me.isi.minealert.settings.DatabaseSettings;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.plugin.SimplePlugin;

import java.io.File;

public final class DatabaseManager {

	@Getter
	private final static DatabaseManager instance = new DatabaseManager();

	public void connect(SimplePlugin plugin) {
		if (!DatabaseSettings.ENABLED) {
			Common.warning("Database is disabled in database.yml. No data will be saved.");
			return;
		}

		Common.log("Connecting to the database...");

		String type = DatabaseSettings.TYPE;

		try {
			if ("sqlite".equals(type)) {
				connectToSQLite(plugin);
			} else {
				connectToMySQL();
			}
			Common.log("Database connection established successfully.");
		} catch (Exception e) {
			Common.error(e, "Failed to establish a database connection. Please check your configuration and credentials.");
		}
	}

	private void connectToSQLite(SimplePlugin plugin) {
		File dataFolder = plugin.getDataFolder();

		if (!dataFolder.exists()) {
			dataFolder.mkdirs();
		}

		File databaseFile = new File(dataFolder, "data.sqlite");
		Database.getInstance().connect("jdbc:sqlite:" + databaseFile.getAbsolutePath());
	}

	private void connectToMySQL() {
		StringBuilder jdbcUrlBuilder = new StringBuilder("jdbc:");
		jdbcUrlBuilder.append(DatabaseSettings.TYPE).append("://");
		jdbcUrlBuilder.append(DatabaseSettings.HOST).append(":");
		jdbcUrlBuilder.append(DatabaseSettings.PORT).append("/");
		jdbcUrlBuilder.append(DatabaseSettings.DATABASE);

		jdbcUrlBuilder.append("?useSSL=false&autoReconnect=true");

		if (DatabaseSettings.ALLOW_PUBLIC_KEY_RETRIEVAL) {
			jdbcUrlBuilder.append("&allowPublicKeyRetrieval=true");
		}

		Database.getInstance().connect(
				jdbcUrlBuilder.toString(),
				DatabaseSettings.USER,
				DatabaseSettings.PASSWORD
		);
	}

	public void disconnect() {
		if (Database.getInstance().isLoaded()) {
			Database.getInstance().close();
		}
	}
}