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
	private final int blue;
	private final int faded;
	private final LayoutInflater layoutInflater;

	public MegaListRowAdapter(Context context, int textViewResourceId, List<MegaListRow> rows) {
		super(context, textViewResourceId, rows);

		this.black = context.getResources().getColor(R.color.black);
		this.faded = context.getResources().getColor(R.color.faded);
		this.blue = Color.BLUE;
		layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
			rowView = layoutInflater.inflate(R.layout.row, null);
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
			tt.setTextColor(blue);

			if (row.getClass() == MegaListSkillTreeRow.class) {
				MegaListSkillTreeRow skillTreeRow = (MegaListSkillTreeRow) row;
				tt.setText(row.getName() + " (" + skillTreeRow.getScore() + ")");
				tt.setTextSize(34.0f);
				// tt.setTextAppearance(getContext(), android.R.style.TextAppearance_Large);

				// Typeface face = Typeface.createFromAsset(getContext().getAssets(), "Roboto-Bold.ttf");
				// tt.setTypeface(face);

			} else {
				tt.setTextSize(24.0f);
				tt.setTextColor(calculateTextColour(row));
				tt.setText(row.getName());
				// tt.setTextAppearance(getContext(), android.R.style.TextAppearance_Medium);
			}
		}

		TextView statusText = (TextView) v.findViewById(R.id.status_text);
		if (row.getClass() == MegaListSkillTreeRow.class) {
			MegaListSkillTreeRow skillTreeRow = (MegaListSkillTreeRow) row;
			statusText.setText(String.valueOf(skillTreeRow.getScore()));

		} else {
			statusText.setText(row.getStatus() ? "Complete" : "Incomplete");
		}

		// ImageView image = (ImageView) v.findViewById(R.id.icon);
		//
		// if (row.getClass() == MegaListSkillTreeRow.class) {
		// image.setVisibility(View.INVISIBLE);
		//
		// } else {
		// // if (image == null) {
		// // LinearLayout layout = (LinearLayout) v;
		// // image = (ImageView) v.findViewById(R.id.icon);
		// // image = (ImageView) layoutInflater.inflate(R.layout.image, null);
		// // ImageView image = new ImageView(getContext());
		// // image.setId(R.id.icon);
		// // image.setId("+id/icon");
		// // layout.addView(image);
		// // } else {
		// image.setVisibility(View.VISIBLE);
		// // }
		// image.setImageResource(row.getStatus() ? R.drawable.complete : R.drawable.incomplete);
		//
		// // image.setOnClickListener(new OnClickListener() {
		// // public void onClick(View v) {
		// // ImageView iv = (ImageView) v;
		// // LinearLayout linearLayout = (LinearLayout) v.getParent();
		// //
		// // MegaListRow item = (MegaListRow) getListAdapter().getItem(position);
		// // if (item != null) {
		// // if (item.getClass() == MegaListTaskRow.class) {
		// // TaskToggler.toggleStatus(getResources(), (MegaListTaskRow) item);
		// // adapter.updateRow(item, view);
		// // }
		// // // else if (item.getClass() == MegaListSkillTreeRow.class) {
		// // // ((MegaListSkillTreeRow) item).toggle_hidden();
		// // // }
		// // }
		// // }
		// // });
		// }
		v.postInvalidate();
	}

	private void animateRow(View rowView) {
		Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.row_transition);
		rowView.startAnimation(animation);
	}

	private int calculateTextColour(MegaListRow row) {
		return blue;
		// return row.getStatus() ? blue : faded;
	}
}
