package com.james.skiller.helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.james.skiller.R;
import com.james.skiller.model.Level;
import com.james.skiller.model.SkillTree;

public class ListHelper {
	public TextView loadHeaderRow(Context context, Level level) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		TextView headerRow = (TextView) inflater.inflate(R.layout.header_row, null);
		headerRow.setText(level.name);
		return headerRow;
	}

	public TextView loadHeaderRow(Context context, SkillTree skillTree) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		TextView headerRow = (TextView) inflater.inflate(R.layout.header_row, null);
		headerRow.setText(skillTree.name);
		return headerRow;
	}

	public TextView loadFooterRow(Context context, SkillTree skillTree) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		TextView headerRow = (TextView) inflater.inflate(R.layout.footer_row, null);
		headerRow.setText(skillTree.name);
		return headerRow;
	}

	public TextView loadFooterRow(Context context, Level level) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		TextView headerRow = (TextView) inflater.inflate(R.layout.footer_row, null);
		headerRow.setText(level.name);
		return headerRow;
	}
}