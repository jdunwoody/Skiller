package com.james.skiller;

public class TaskRow {
	private final String skillTree;
	private final String level;
	private final String task;
	private boolean status;
	private final String url;
	private final int taskId;

	public TaskRow(String skillTree, String level, String task, int taskId, boolean status, String url) {
		this.skillTree = skillTree;
		this.level = level;
		this.task = task;
		this.taskId = taskId;
		this.status = status;
		this.url = url;
	}

	public String toString() {
		return skillTree + "->" + level + ": " + task + "," + taskId + "[" + status + "]";
	}

	public int getTaskId() {
		return taskId;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public boolean getStatus() {
		return status;
	}
}