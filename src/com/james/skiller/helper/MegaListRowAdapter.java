package com.james.skiller.helper;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.james.skiller.R;
import com.james.skiller.model.MegaListRow;

public class MegaListRowAdapter extends ArrayAdapter<MegaListRow> {

	public MegaListRowAdapter(Context context, int textViewResourceId, List<MegaListRow> rows) {
		super(context, textViewResourceId, rows);
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
		// if (row.getClass() != MegaListTaskRow.class) {
		if (row != null) {
			TextView textView = (TextView) rowView.findViewById(R.id.text);
			textView.setTextColor(calculateTextColour(row));
			// LayoutAnimationController hyperspaceJumpAnimation = AnimationUtils.loadLayoutAnimation(getContext(), R.anim.row_transition);
			// v.startAnimation(hyperspaceJumpAnimation);

			updateRow(row, rowView);

			animateRow(rowView);
		}
		// }
		return rowView;
	}

	private void animateRow(View rowView) {
		Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.row_transition);
		rowView.startAnimation(animation);
	}

	private int calculateTextColour(MegaListRow row) {
		return row.getStatus() ? R.color.black : R.color.faded;
	}

	private void updateRow(MegaListRow row, View v) {
		TextView tt = (TextView) v.findViewById(R.id.text);
		if (tt != null) {
			tt.setTextColor(R.color.light_blue);
			tt.setText(row.getName() + " " + row.getStatus());
		}

		ImageView image = (ImageView) v.findViewById(R.id.icon);
		image.setImageResource(row.getStatus() ? R.drawable.complete : R.drawable.incomplete);

		v.postInvalidate();
	}
}
