package com.james.skiller.model;

import java.util.ArrayList;
import java.util.List;

public class MegaListSkillTreeRow extends MegaListRow {
	private final double score;
	private final List<MegaListTaskRow> tasks;
	private boolean visible;

	public MegaListSkillTreeRow(int id, String name, double score) {
		super(id, name);
		this.score = score;
		this.visible = true;
		this.tasks = new ArrayList<MegaListTaskRow>();
	}

	public void add(MegaListTaskRow task) {
		tasks.add(task);
	}

	@Override
	public List<? extends MegaListRow> getChildren() {
		return tasks;
	}

	public boolean getStatus() {
		return false;
	}

	public double score() {
		return score;
	}

	public void toggle_hidden() {
		this.visible = !visible;
	}
}