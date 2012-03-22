package com.james.skiller.model;

import java.util.Collections;
import java.util.List;

public class MegaListTaskRow extends MegaListRow {
	private boolean status;

	public MegaListTaskRow(int id, String name, boolean status) {
		super(id, name);
		this.status = status;
	}

	@Override
	public List<MegaListRow> getChildren() {
		return Collections.emptyList();
	}

	public boolean getStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}
}