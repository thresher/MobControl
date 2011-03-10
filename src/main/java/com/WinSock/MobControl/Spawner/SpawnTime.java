package com.WinSock.MobControl.Spawner;

public enum SpawnTime {
	DAY("Day"), NIGHT("Night"), BOTH("Both");

	private String text;

	SpawnTime(String text) {
		this.text = text;
	}

	public String getText() {
		return this.text;
	}

	public static SpawnTime fromString(String text) {
		if (text != null) {
			for (SpawnTime t : SpawnTime.values()) {
				if (text.equalsIgnoreCase(t.text)) {
					return t;
				}
			}
			throw new IllegalArgumentException("No constant with text " + text
					+ " found");
		}
		throw new IllegalArgumentException("No constant with text NULL");
	}
}