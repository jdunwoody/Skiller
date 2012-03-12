package com.james.skiller.model;

public class SkillRow {
	private final int id;
	private final String name;
	private final double score;

	public SkillRow(int id, String name, double score) {
		this.id = id;
		this.name = name;
		this.score = score;
	}

	public String getName() {
		return name;
	}

	public double score() {
		return score;
	}

	public int getId() {
		return id;
	}

}
