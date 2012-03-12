package com.james.skiller.model;

public class LevelRow {
	private final int id;
	private final String name;
	private final double score;

	public LevelRow(int id, String name, double score) {
		this.id = id;
		this.name = name;
		this.score = score;
	}

	public String getName() {
		return name;
	}

	public double getScore() {
		return score;
	}

	public int getId() {
		return id;
	}
}
