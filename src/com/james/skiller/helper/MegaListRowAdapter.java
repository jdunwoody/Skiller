package com.james.skiller.helper;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.james.skiller.R;
import com.james.skiller.model.MegaListRow;
import com.james.skiller.model.MegaListSkillTreeRow;

public class MegaListRowAdapter extends ArrayAdapter<MegaListRow> {

	private final int black;
	private final int faded;

	public MegaListRowAdapter(Context context, int textViewResourceId, List<MegaListRow> rows) {
		super(context, textViewResourceId, rows);

		this.black = context.getResources().getColor(R.color.black);
		this.faded = context.getResources().getColor(R.color.faded);
	}

	public void add_row(MegaListRow row) {
		add(row);
		for (MegaListRow child : row.getChildren()) {
			add(child);
		}
	}

	@Override
	public View getView(int position, View rowView, ViewGroup parent) {
		MegaListRow row = getItem(position);
		if (rowView == null) {
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = vi.inflate(R.layout.row, null);
		}
		if (row != null) {
			updateRow(row, rowView);
			// animateRow(rowView);
		}
		return rowView;
	}

	public void updateRow(MegaListRow row, View v) {
		TextView tt = (TextView) v.findViewById(R.id.text);
		if (tt != null) {
			if (row.getClass() == MegaListSkillTreeRow.class) {
				MegaListSkillTreeRow skillTreeRow = (MegaListSkillTreeRow) row;
				tt.setTextSize(28.0f);
				tt.setTextColor(Color.BLUE);
				tt.setText(row.getName() + " (" + skillTreeRow.getScore() + ")");
			} else {
				tt.setTextSize(18.0f);
				tt.setTextColor(calculateTextColour(row));
				tt.setText(row.getName());
			}
		}

		if (row.getClass() == MegaListSkillTreeRow.class) {
		} else {
			// ImageView image = (ImageView) v.findViewById(R.id.icon);
			// if (image == null) {
			// LinearLayout layout = (LinearLayout) v;
			// image = new ImageView(getContext()); // (ImageView) v.findViewById(R.id.icon);
			// image.setId(R.id.icon);
			// layout.addView(image);
			// }
			// image.setImageResource(row.getStatus() ? R.drawable.complete : R.drawable.incomplete);

		}
		v.postInvalidate();
	}

	private void animateRow(View rowView) {
		Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.row_transition);
		rowView.startAnimation(animation);
	}

	private int calculateTextColour(MegaListRow row) {
		return row.getStatus() ? black : faded;
	}
}
