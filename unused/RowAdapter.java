package com.james.skiller.helper;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.james.skiller.R;
import com.james.skiller.model.SomeRow;

public class RowAdapter extends ArrayAdapter<SomeRow> {
	private List<SomeRow> rows;
	private Context context;

	public RowAdapter(Context context, int textViewResourceId, List<SomeRow> rows) {
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
		SomeRow row = rows.get(position);
		if (row != null) {
			updateRow(row, v);
		}
		return v;
	}

	public static void updateRow(SomeRow row, View v) {
		ImageView image = (ImageView) v.findViewById(R.id.icon);
		image.setImageResource(row.getStatus() ? R.drawable.accept_item : R.drawable.done_item);
		TextView tt = (TextView) v.findViewById(R.id.toptext);
		if (tt != null) {
			tt.setText(row.getText());
		}
		// TextView bt = (TextView) v.findViewById(R.id.bottomtext);
		// if (bt != null) {
		// bt.setText(String.valueOf(row.getStatus()));
		// }

		v.postInvalidate();
	}
}