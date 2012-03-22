package com.james.skiller.model;

import java.util.List;

public abstract class MegaListRow implements Row {
	private final int id;
	private final String name;

	public MegaListRow(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public abstract List<? extends MegaListRow> getChildren();

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}
}