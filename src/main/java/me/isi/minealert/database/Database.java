package me.isi.minealert.database;

import lombok.Getter;
import me.isi.minealert.models.LogEntry;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.collection.SerializedMap;
import org.mineacademy.fo.database.SimpleDatabase;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class Database extends SimpleDatabase {

	@Getter
	private final static Database instance = new Database();

	private Database() {
		addVariable("logs_table", "minealert_logs");
		addVariable("stats_table", "minealert_stats");
	}

	@Override
	protected void onConnected() {
		createTable(
				TableCreator.of("{logs_table}")
						.addAutoIncrement("id", "INT")
						.addNotNull("player_uuid", "VARCHAR(36)")
						.addNotNull("timestamp", "BIGINT")
						.addNotNull("block_type", "VARCHAR(50)")
						.addNotNull("world", "VARCHAR(50)")
						.addNotNull("x", "INT")
						.addNotNull("y", "INT")
						.addNotNull("z", "INT")
						.setPrimaryColumn("id")
		);
		createTable(
				TableCreator.of("{stats_table}")
						.addAutoIncrement("id", "INT")
						.addNotNull("player_uuid", "VARCHAR(36)")
						.addNotNull("world_name", "VARCHAR(50)")
						.addNotNull("block_type", "VARCHAR(50)")
						.addNotNull("break_count", "BIGINT")
						.setPrimaryColumn("id")
		);
		update("CREATE UNIQUE INDEX IF NOT EXISTS idx_player_stats ON {stats_table} (player_uuid, world_name, block_type);");
	}

	public void saveNewLog(Player player, Location blockLocation, Material blockType) {
		Common.runAsync(() -> {
			SerializedMap logData = new SerializedMap();
			logData.put("player_uuid", player.getUniqueId().toString());
			logData.put("timestamp", System.currentTimeMillis());
			logData.put("block_type", blockType.name());
			logData.put("world", blockLocation.getWorld().getName());
			logData.put("x", blockLocation.getBlockX());
			logData.put("y", blockLocation.getBlockY());
			logData.put("z", blockLocation.getBlockZ());
			insert("{logs_table}", logData);
		});
	}

	public void getPlayerLogs(UUID uuid, Consumer<List<LogEntry>> callback) {
		Common.runAsync(() -> {
			final List<LogEntry> logs = new ArrayList<>();
			select("{logs_table}", "player_uuid = '" + uuid.toString() + "' ORDER BY timestamp DESC", resultSet -> {
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
			Common.runLater(() -> callback.accept(logs));
		});
	}

	public void incrementBlockBreak(Player player, Material blockType) {
		Common.runAsync(() -> {
			final String playerUUID = player.getUniqueId().toString();
			final String worldName = player.getWorld().getName();
			final String blockTypeName = blockType.name();
			String updateQuery = "INSERT INTO {stats_table} (player_uuid, world_name, block_type, break_count) VALUES ('" + playerUUID + "', '" + worldName + "', '" + blockTypeName + "', 1)";
			if (isSQLite())
				updateQuery += " ON CONFLICT(player_uuid, world_name, block_type) DO UPDATE SET break_count = break_count + 1;";
			else
				updateQuery += " ON DUPLICATE KEY UPDATE break_count = break_count + 1;";
			update(updateQuery);
		});
	}

	public void getPlayerMiningStats(UUID uuid, Consumer<SerializedMap> callback) {
		Common.runAsync(() -> {
			final SerializedMap stats = new SerializedMap();
			final String uuidString = uuid.toString();
			long totalBlocks = 0;

			try (ResultSet rs = query(replaceVariables("SELECT SUM(break_count) FROM {stats_table} WHERE player_uuid = '" + uuidString + "'"))) {
				if (rs != null && rs.next())
					totalBlocks = rs.getLong(1);
			} catch (Exception e) {
				Common.error(e, "Failed to get total blocks for " + uuid);
			}

			if (totalBlocks == 0) {
				Common.runLater(() -> callback.accept(stats));
				return;
			}

			final List<Map<String, Object>> topBlocks = new ArrayList<>();
			final String topBlocksQuery = "SELECT block_type, SUM(break_count) as total_breaks FROM {stats_table} WHERE player_uuid = '" + uuidString + "' GROUP BY block_type ORDER BY total_breaks DESC LIMIT 3";
			try (ResultSet rs = query(replaceVariables(topBlocksQuery))) {
				while (rs != null && rs.next()) {
					String blockType = rs.getString("block_type");
					int count = rs.getInt("total_breaks");
					SerializedMap blockData = new SerializedMap();
					blockData.put("material", Material.valueOf(blockType));
					blockData.put("count", count);
					topBlocks.add(blockData.asMap());
				}
			} catch (Exception e) {
				Common.error(e, "Failed to get top blocks for " + uuid);
			}

			final String favoriteWorldQuery = "SELECT world_name, SUM(break_count) as total_breaks FROM {stats_table} WHERE player_uuid = '" + uuidString + "' GROUP BY world_name ORDER BY total_breaks DESC LIMIT 1";
			try (ResultSet rs = query(replaceVariables(favoriteWorldQuery))) {
				if (rs != null && rs.next())
					stats.put("favorite_world", rs.getString("world_name"));
			} catch (Exception e) {
				Common.error(e, "Failed to get favorite world for " + uuid);
			}

			stats.put("total_blocks", totalBlocks);
			stats.put("top_blocks", topBlocks);
			Common.runLater(() -> callback.accept(stats));
		});
	}
}