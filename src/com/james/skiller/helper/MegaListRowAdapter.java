package com.james.skiller.helper;

import java.util.List;

import com.james.skiller.R;
import com.james.skiller.model.MegaListRow;
import com.james.skiller.model.MegaListSkillTreeRow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MegaListRowAdapter extends ArrayAdapter<MegaListRow> {

	public MegaListRowAdapter(Context context, int textViewResourceId, List<MegaListRow> rows) {
		super(context, textViewResourceId, rows);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.row, null);
		}
		MegaListRow row = (MegaListRow) getItem(position);
		if (row != null) {
			updateRow(row, v);
		}
		return v;
	}

	public static void updateRow(MegaListRow row, View v) {
		ImageView image = (ImageView) v.findViewById(R.id.icon);

		image.setImageResource(row.getClass() == MegaListSkillTreeRow.class ? R.drawable.promotion : R.drawable.ic_launcher);

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
