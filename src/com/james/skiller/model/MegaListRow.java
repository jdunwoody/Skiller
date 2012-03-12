package com.james.skiller.model;

import java.util.List;

import com.james.skiller.Row;

public abstract class MegaListRow implements Row{
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