package com.james.skiller.helper;

import java.util.List;

import com.james.skiller.R;
import com.james.skiller.R.drawable;
import com.james.skiller.R.id;
import com.james.skiller.R.layout;
import com.james.skiller.model.SkillRow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SkillRowAdapter extends ArrayAdapter<SkillRow> {
	private List<SkillRow> rows;
	private Context context;

	public SkillRowAdapter(Context context, int textViewResourceId, List<SkillRow> rows) {
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
		SkillRow row = rows.get(position);
		if (row != null) {
			updateRow(row, v);
		}
		return v;
	}

	public static void updateRow(SkillRow row, View v) {
		ImageView image = (ImageView) v.findViewById(R.id.icon);
		image.setImageResource(R.drawable.promotion);
		TextView tt = (TextView) v.findViewById(R.id.toptext);
		if (tt != null) {
			tt.setText(row.getName());
		}
		// TextView bt = (TextView) v.findViewById(R.id.bottomtext);
		// if (bt != null) {
		// bt.setText(String.valueOf(row.getStatus()));
		// }

		v.postInvalidate();
	}
}
