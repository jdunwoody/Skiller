package com.james.skiller;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.james.skiller.helper.DataHelper;
import com.james.skiller.helper.MegaListRowAdapter;
import com.james.skiller.helper.TaskToggler;
import com.james.skiller.model.MegaListRow;
import com.james.skiller.model.MegaListSkillTreeRow;
import com.james.skiller.model.MegaListTaskRow;

public class MegaListActivity extends ListActivity {
	private ProgressDialog progressDialog = null;
	public static final String LOG_TAG = "Skiller";
	private MegaListRowAdapter adapter;
	private List<MegaListRow> rows = null;
	private Runnable viewOrders;
	private final DataHelper dataHelper;

	public MegaListActivity() {
		super();
		this.dataHelper = new DataHelper();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.rows = new ArrayList<MegaListRow>();
		this.adapter = new MegaListRowAdapter(this, R.layout.row, rows);
		setListAdapter(this.adapter);

		getListView().setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				MegaListRow item = (MegaListRow) getListAdapter().getItem(position);
				if (item != null) {
					if (item.getClass() == MegaListTaskRow.class) {
						TaskToggler.toggleStatus(getResources(), (MegaListTaskRow) item);
						TextView textView = (TextView) ((LinearLayout) view).findViewById(R.id.toptext);
						textView.setText(item.getName() + " " + item.getStatus());
//						textView.setTextColor(R.color.faded);
						view.invalidate();
					} else if (item.getClass() == MegaListSkillTreeRow.class) {
						((MegaListSkillTreeRow) item).toggle_hidden();
					}
				}
			}
		});

		viewOrders = new Runnable() {
			public void run() {
				getData();
			}
		};

		Thread thread = new Thread(null, viewOrders, "MagentoBackground");
		thread.start();
		progressDialog = ProgressDialog.show(this, "Please wait...", "Retrieving data ...", true);
	}

	private void getData() {
		try {
			String url = getResources().getString(R.string.server_url) + "skill_trees/everything.json";
			String data = dataHelper.readData(url);
			rows = jsonToArray(data);
		} catch (Exception e) {
			Log.e(LOG_TAG, e.getMessage());
		}
		runOnUiThread(returnRes);
	}

	private Runnable returnRes = new Runnable() {
		public void run() {
			if (rows != null && rows.size() > 0) {
				adapter.notifyDataSetChanged();
				for (int i = 0; i < rows.size(); i++) {
					MegaListRow row = rows.get(i);
					adapter.add_row(row);
				}
			}
			progressDialog.dismiss();
			adapter.notifyDataSetChanged();
		}
	};

	private List<MegaListRow> jsonToArray(String data) {
		List<MegaListRow> results = new ArrayList<MegaListRow>();
		try {
			JSONArray jsonArray = new JSONArray(data);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				MegaListSkillTreeRow row = new MegaListSkillTreeRow(jsonObject.getInt("id"), jsonObject.getString("name"), jsonObject.getDouble("score"));

				JSONArray levels = jsonObject.getJSONArray("levels");
				for (int j = 0; j < levels.length(); j++) {
					JSONObject level = (JSONObject) levels.get(j);
					JSONArray tasks = level.getJSONArray("tasks");
					for (int k = 0; k < tasks.length(); k++) {
						JSONObject task = (JSONObject) tasks.get(k);
						row.add(new MegaListTaskRow(task.getInt("id"), task.getString("name"), task.getBoolean("status")));
					}
				}
				results.add(row);
			}
		} catch (Exception e) {
			Log.e(LOG_TAG, e.getMessage());
			e.printStackTrace();
		}
		return results;
	}
}
