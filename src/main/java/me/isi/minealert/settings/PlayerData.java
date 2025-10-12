package me.isi.minealert.settings;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.UUID;

@Getter
@Setter
public class PlayerData {

	private final UUID playerUUID;
	private final String playerName;

	private int breakCount;
	private long lastBreakTime;
	private Material lastBlockType;
	private Location lastBlockLocation;

	public PlayerData(UUID playerUUID, String playerName) {
		this.playerUUID = playerUUID;
		this.playerName = playerName;
		this.breakCount = 0;
		this.lastBreakTime = 0;
		this.lastBlockType = null;
		this.lastBlockLocation = null;
	}
}
