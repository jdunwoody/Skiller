package com.james.skiller.helper;

import java.util.List;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.james.skiller.R;
import com.james.skiller.model.MegaListRow;
import com.james.skiller.model.MegaListSkillTreeRow;

public class MegaListRowAdapter extends ArrayAdapter<MegaListRow> {

	public MegaListRowAdapter(Context context, int textViewResourceId, List<MegaListRow> rows) {
		super(context, textViewResourceId, rows);
	}

	@Override
	public View getView(int position, View rowView, ViewGroup parent) {
		MegaListRow row = (MegaListRow) getItem(position);
		if (rowView == null) {
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = vi.inflate(R.layout.row, null);
			TextView textView = (TextView) rowView.findViewById(R.id.toptext);
			textView.setTextColor(calculateTextColour(row));
			// LayoutAnimationController hyperspaceJumpAnimation = AnimationUtils.loadLayoutAnimation(getContext(), R.anim.row_transition);
			// v.startAnimation(hyperspaceJumpAnimation);

			Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.row_transition);
			rowView.startAnimation(animation);

		}
		// if (row.getClass() != MegaListTaskRow.class) {
		if (row != null) {
			updateRow(row, rowView);
		}
		// }
		return rowView;
	}

	private int calculateTextColour(MegaListRow row) {
		return row.getStatus() ? R.color.black : R.color.faded;
	}

	public static void updateRow(MegaListRow row, View v) {
		ImageView image = (ImageView) v.findViewById(R.id.icon);

		image.setImageResource(row.getClass() == MegaListSkillTreeRow.class ? R.drawable.promotion : R.drawable.ic_launcher);

		TextView tt = (TextView) v.findViewById(R.id.toptext);
		if (tt != null) {
			tt.setText(row.getName() + " " + row.getStatus());
		}
		// TextView bt = (TextView) v.findViewById(R.id.bottomtext);
		// if (bt != null) {
		// bt.setText(String.valueOf(row.getStatus()));
		// }

		v.postInvalidate();
	}

	public void add_row(MegaListRow row) {
		add(row);
		for (MegaListRow child : row.getChildren()) {
			add(child);
		}
	}
}
