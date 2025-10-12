package me.isi.minealert.database;

import lombok.Getter;
import me.isi.minealert.models.LogEntry;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.collection.SerializedMap;
import org.mineacademy.fo.database.SimpleDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class Database extends SimpleDatabase {

	@Getter
	private final static Database instance = new Database();

	private Database() {
		this.addVariable("table", "minealert");
	}

	@Override
	protected void onConnected() {
		createTable(
				TableCreator.of("{table}")
						.addAutoIncrement("id", "INT")
						.addNotNull("player_uuid", "VARCHAR(36)")
						.addNotNull("player_name", "VARCHAR(16)")
						.addNotNull("timestamp", "BIGINT")
						.addNotNull("block_type", "VARCHAR(50)")
						.addNotNull("world", "VARCHAR(50)")
						.addNotNull("x", "INT")
						.addNotNull("y", "INT")
						.addNotNull("z", "INT")
						.setPrimaryColumn("id")
		);
	}

	public void saveNewLog(Player player, Location blockLocation, Material blockType) {
		Common.runAsync(() -> {
			SerializedMap logData = new SerializedMap();

			logData.put("player_uuid", player.getUniqueId().toString());
			logData.put("player_name", player.getName());
			logData.put("timestamp", System.currentTimeMillis());
			logData.put("block_type", blockType.name());
			logData.put("world", blockLocation.getWorld().getName());
			logData.put("x", blockLocation.getBlockX());
			logData.put("y", blockLocation.getBlockY());
			logData.put("z", blockLocation.getBlockZ());

			// add a new line everytime
			insert("{table}", logData);
		});
	}

	public void getPlayerLogs(UUID uuid, Consumer<List<LogEntry>> callback) {
		Common.runAsync(() -> {
			final List<LogEntry> logs = new ArrayList<>();

			select("{table}", "player_uuid = '" + uuid.toString() + "' ORDER BY timestamp DESC", resultSet -> {

				// for every line found create logEntry obj
				String worldName = resultSet.getString("world");
				int x = resultSet.getInt("x");
				int y = resultSet.getInt("y");
				int z = resultSet.getInt("z");

				Location location = new Location(org.bukkit.Bukkit.getWorld(worldName), x, y, z);

				LogEntry entry = new LogEntry(
						resultSet.getLong("timestamp"),
						Material.valueOf(resultSet.getString("block_type")),
						location
				);

				logs.add(entry);
			});

			// Once query is done, pass it to the main thread
			Common.runLater(() -> {
				callback.accept(logs);
			});
		});
	}
}

