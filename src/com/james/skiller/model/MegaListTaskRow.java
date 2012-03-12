package com.james.skiller.model;

import java.util.Collections;
import java.util.List;

public class MegaListTaskRow extends MegaListRow {
	private final boolean status;

	public MegaListTaskRow(int id, String name, boolean status) {
		super(id, name);
		this.status = status;
	}

	public boolean status() {
		return status;
	}

	@Override
	public List<MegaListRow> getChildren() {
		return Collections.emptyList();
	}
}