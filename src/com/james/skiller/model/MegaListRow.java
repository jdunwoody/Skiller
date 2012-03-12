package com.james.skiller.model;

import java.util.List;

public abstract class MegaListRow {
	private final int id;
	private final String name;

	public MegaListRow(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public int getId() {
		return id;
	}

	public abstract List<? extends MegaListRow> getChildren();
}