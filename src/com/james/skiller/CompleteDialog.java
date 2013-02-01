package com.james.skiller;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

import com.james.skiller.helper.TaskToggler;
import com.james.skiller.model.MegaListTaskRow;

public class CompleteDialog extends Activity {
	private MegaListTaskRow taskRow;

	private final OnClickListener toggleStatusListener = new OnClickListener() {
		public void onClick(View v) {
			TaskToggler.toggleStatus(getResources(), taskRow);
			taskRow.toggle_status();

			finish();
		}
	};

	/**
	 * Initialization of the Activity after it is first created. Must at least call {@link android.app.Activity#setContentView setContentView()} to describe what is to be displayed
	 * in the screen.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_LEFT_ICON);

		setContentView(R.layout.dialog_activity);

		Bundle extras = getIntent().getExtras();
		taskRow = (MegaListTaskRow) extras.get(MegaListActivity.LIST_ROW);

		getWindow().setTitle("This is just a test");
		getWindow().setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, android.R.drawable.ic_dialog_alert);

		Button button = (Button) findViewById(R.id.toggle_status);
		button.setOnClickListener(toggleStatusListener);
	}
}