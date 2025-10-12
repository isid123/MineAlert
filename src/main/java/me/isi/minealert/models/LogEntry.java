package me.isi.minealert.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;

@Getter
@AllArgsConstructor
public class LogEntry {
	private final long timestamp;
	private final Material blockType;
	private final Location location;
}
