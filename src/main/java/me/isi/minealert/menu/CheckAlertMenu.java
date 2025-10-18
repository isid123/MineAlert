package me.isi.minealert.menu;

import me.isi.minealert.database.Database;
import me.isi.minealert.settings.Settings;
import me.isi.minealert.utils.Utils;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.mineacademy.fo.collection.SerializedMap;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;

import java.util.ArrayList;
import java.util.List;

public class CheckAlertMenu extends Menu {

	private final OfflinePlayer target;

	public CheckAlertMenu(OfflinePlayer target) {
		this.target = target;
		setTitle(Settings.CheckLogsMenu.TITLE.replace("%player%", target.getName()));
		loadStatistics();
	}

	private void loadStatistics() {
		Database.getInstance().getPlayerMiningStats(target.getUniqueId(), stats -> {
			ItemStack currentItem = getItemAt(15);
			if (currentItem == null)
				return;
			String favoriteWorld = stats.getString("favorite_world", "N/A");
			long totalBlocks = stats.getLong("total_blocks", 0L);
			List<SerializedMap> topBlocks = stats.getMapList("top_blocks");
			List<String> newLore = new ArrayList<>();
			for (String line : Settings.CheckLogsMenu.StatisticsItem.LORE_HEADER) {
				newLore.add(line.replace("%favorite_world%", favoriteWorld));
			}
			if (topBlocks.isEmpty()) {
				newLore.add(Settings.CheckLogsMenu.StatisticsItem.NO_DATA_MESSAGE);
			} else {
				int rank = 1;
				for (SerializedMap blockData : topBlocks) {
					Material material = blockData.get("material", Material.class);
					int count = blockData.getInteger("count");
					double percentage = (totalBlocks > 0) ? ((double) count / totalBlocks * 100.0) : 0.0;
					newLore.add(Settings.CheckLogsMenu.StatisticsItem.TOP_BLOCK_ENTRY_FORMAT
							.replace("%rank%", String.valueOf(rank++))
							.replace("%block_name%", material.toString().replace("_", " ").toLowerCase())
							.replace("%count%", String.valueOf(count))
							.replace("%percentage%", String.format("%.2f%%", percentage)));
				}
			}
			setItem(15, ItemCreator.of(currentItem).lore(newLore).make());
		});
	}

	@Override
	public ItemStack getItemAt(int slot) {
		switch (slot) {
			case 11:
				ItemStack playerHead = CompMaterial.PLAYER_HEAD.toItem();
				SkullMeta skullMeta = (SkullMeta) playerHead.getItemMeta();
				if (skullMeta != null) {
					skullMeta.setOwningPlayer(target);
					playerHead.setItemMeta(skullMeta);
				}
				String formattedPlaytime = Utils.formatPlaytime(target.getStatistic(Statistic.PLAY_ONE_MINUTE));
				boolean isOnline = target.isOnline();
				String onlineStatus = isOnline ? "&aOnline" : "&cOffline";
				List<String> lore = new ArrayList<>();
				for (String line : Settings.CheckLogsMenu.PlayerInfoItem.LORE) {
					lore.add(line
							.replace("%player%", target.getName())
							.replace("%playtime%", formattedPlaytime)
					);
				}
				return ItemCreator.of(playerHead)
						.name(Settings.CheckLogsMenu.PlayerInfoItem.NAME
								.replace("%player%", target.getName())
								.replace("%online_status%", onlineStatus))
						.lore(lore)
						.make();
			case 15:
				return ItemCreator
						.of(CompMaterial.DIAMOND_PICKAXE)
						.hideTags(false)
						.name(Settings.CheckLogsMenu.StatisticsItem.NAME)
						.make();
		}
		return NO_ITEM;
	}

	@Override
	protected void onMenuClick(Player player, int slot, ItemStack clicked) {
		if (slot == 11 && target.isOnline() && target.getPlayer() != null && target != player) {
			player.closeInventory();
			player.teleport(target.getPlayer().getLocation());
		}
	}
}