package com.james.skiller.model;

public class SomeRow {
	private final String text;
	private boolean status;
	private final int id;

	public SomeRow(int id, String text, String status) {
		this.text = text;
		this.setStatus(status);
		this.id = id;
	}

	public SomeRow(int id, String text, boolean status) {
		this.text = text;
		this.setStatus(status);
		this.id = id;
	}

	public String getText() {
		return text;
	}

	@Override
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

	public void setStatus(String status) {
		this.status = Boolean.getBoolean(status);
	}
}