package com.WinSock.MobControl.Spawner;

public enum CreatureNature {
	PASSIVE("Passive"), NEUTRAL("Neutral"), AGGRESSIVE("Aggressive");

	private String text;

	CreatureNature(String text) {
		this.text = text;
	}

	public String getText() {
		return this.text;
	}

	public static CreatureNature fromString(String text) {
		if (text != null) {
			for (CreatureNature c : CreatureNature.values()) {
				if (text.equalsIgnoreCase(c.text)) {
					return c;
				}
			}
			throw new IllegalArgumentException("No constant with text " + text
					+ " found");
		}
		throw new IllegalArgumentException("No constant with text NULL");
	}
}
