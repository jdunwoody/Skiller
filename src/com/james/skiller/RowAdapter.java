package com.james.skiller;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.james.skiller.model.Row;

public class RowAdapter extends ArrayAdapter<Row> {

	private List<Row> rows;
	private Context context;

	public RowAdapter(Context context, int textViewResourceId, List<Row> rows) {
		super(context, textViewResourceId, rows);
		this.rows = rows;
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.row, null);
		}
		Row row = rows.get(position);
		if (row != null) {
			TextView tt = (TextView) v.findViewById(R.id.toptext);
			TextView bt = (TextView) v.findViewById(R.id.bottomtext);
			if (tt != null) {
				tt.setText("Name: " + row.getText());
			}
			if (bt != null) {
				bt.setText("Status: " + row.getStatus());
			}
		}
		return v;
	}
}