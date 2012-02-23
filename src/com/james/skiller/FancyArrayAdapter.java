package com.james.skiller;

import android.app.ListActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class FancyArrayAdapter extends ArrayAdapter<TaskRow> {
	public FancyArrayAdapter(ListActivity activity, TaskRow[] taskRows) {
		super(activity, R.layout.list_item, taskRows);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return super.getView(position, convertView, parent);
	}
}