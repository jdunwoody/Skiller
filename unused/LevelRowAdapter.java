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
import com.james.skiller.model.LevelRow;

public class LevelRowAdapter extends ArrayAdapter<LevelRow> {
	private List<LevelRow> rows;
	private Context context;

	public LevelRowAdapter(Context context, int textViewResourceId, List<LevelRow> rows) {
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
		LevelRow row = rows.get(position);
		if (row != null) {
			updateRow(row, v);
		}
		return v;
	}

	public static void updateRow(LevelRow row, View v) {
		ImageView image = (ImageView) v.findViewById(R.id.icon);
		image.setImageResource(R.drawable.promotion);
		TextView tt = (TextView) v.findViewById(R.id.toptext);
		if (tt != null) {
			tt.setText(row.getName());
		}

		// ImageView status = (ImageView) v.findViewById(R.id.status);
		// status.setImageResource(renderScore(row.getScore()));

		v.postInvalidate();
	}

	private static int renderScore(double score) {
		if (score < 0.3) {
			return R.drawable.high_score;
		} else if (score < 0.6) {
			return R.drawable.medium_score;
		} else {
			return R.drawable.low_score;
		}
	}
}
