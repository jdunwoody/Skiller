package com.james.skiller.model;

import java.util.ArrayList;
import java.util.List;

public class MegaListSkillTreeRow extends MegaListRow {
	private final double score;
	private List<MegaListTaskRow> tasks;

	public MegaListSkillTreeRow(int id, String name, double score) {
		super(id, name);
		this.score = score;
		this.tasks = new ArrayList<MegaListTaskRow>();
	}

	public double score() {
		return score;
	}

	public void add(MegaListTaskRow task) {
		tasks.add(task);
	}

	@Override
	public List<? extends MegaListRow> getChildren() {
		return tasks;
	}
}