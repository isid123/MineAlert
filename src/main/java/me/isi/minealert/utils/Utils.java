package me.isi.minealert.utils;

public class Utils {

	public static String formatPlaytime(long ticks) {
		long seconds = ticks / 20;
		long minutes = seconds / 60;
		long hours = minutes / 60;
		long days = hours / 24;

		long remainingHours = hours % 24;
		long remainingMinutes = minutes % 60;

		return days + "d " + remainingHours + "h " + remainingMinutes + "m";
	}
}
