package com.james.skiller.model;

public class Row {
	private final String text;
	private String status;
	private final int id;

	public Row(int id, String text, String status) {
		this.text = text;
		this.status = status;
		this.id = id;
	}

	public Row(int id, String text, boolean status) {
		this(id, text, String.valueOf(status));
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean getStatusBoolean() {
		return Boolean.parseBoolean(status);
	}
}