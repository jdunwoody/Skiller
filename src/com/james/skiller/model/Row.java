package com.james.skiller.model;

public class Row {
	// private final String skillTree;
	// private final String level;
	// private final String task;
	// private final String url;
	// private final int taskId;

	private final String text;
	private boolean status;
	private final int id;

	public Row(String text, boolean status, int id) {
		this.text = text;
		this.status = status;
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public String toString() {
		return text + "[" + status + "]";
	}

	public int getId() {
		return id;
	}

	public boolean getStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}
}