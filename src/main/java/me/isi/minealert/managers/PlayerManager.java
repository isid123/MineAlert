package me.isi.minealert.managers;

import lombok.Getter;
import me.isi.minealert.settings.PlayerData;

import java.util.HashMap;
import java.util.UUID;

public class PlayerManager {

	@Getter
	private final static PlayerManager instance = new PlayerManager();

	private final HashMap<UUID, PlayerData> players = new HashMap<>();

	// if player do not exist I'll create it
	public PlayerData getOrCreate(UUID uuid, String playerName) {
		return players.computeIfAbsent(uuid, id -> new PlayerData(uuid, playerName));
	}

	public void reset(UUID uuid) {
		players.remove(uuid);
	}

	public void clear() {
		players.clear();
	}
}
