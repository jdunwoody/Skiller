package com.james.skiller;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.james.skiller.helper.DataHelper;
import com.james.skiller.helper.SkillRowAdapter;
import com.james.skiller.model.SkillRow;

public class SkillTreeActivity extends ListActivity {
	private ProgressDialog progressDialog = null;
	public static final String LOG_TAG = "Skiller";
	private SkillRowAdapter adapter;
	private List<SkillRow> rows = null;
	private Runnable viewOrders;
	private final DataHelper dataHelper;

	public SkillTreeActivity() {
		super();
		this.dataHelper = new DataHelper();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.rows = new ArrayList<SkillRow>();
		this.adapter = new SkillRowAdapter(this, R.layout.row, rows);
		setListAdapter(this.adapter);

		getListView().setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				SkillRow item = (SkillRow) getListAdapter().getItem(position);
				if (item != null) {
					Intent intent = new Intent(view.getContext(), TaskActivity.class);
					intent.putExtra("skill_tree_id", item.getId());
					intent.putExtra("skill_tree_name", item.getName());
					startActivity(intent);
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

	private Runnable returnRes = new Runnable() {
		public void run() {
			if (rows != null && rows.size() > 0) {
				adapter.notifyDataSetChanged();
				for (int i = 0; i < rows.size(); i++)
					adapter.add(rows.get(i));
			}
			progressDialog.dismiss();
			adapter.notifyDataSetChanged();
		}
	};

	private void getData() {
		try {
			String url = getResources().getString(R.string.server_url) + "skill_trees.json";
			String data = dataHelper.readData(url);
			rows = jsonToArray(data);
		} catch (Exception e) {
			Log.e(LOG_TAG, e.getMessage());
		}
		runOnUiThread(returnRes);
	}

	private List<SkillRow> jsonToArray(String data) {
		List<SkillRow> results = new ArrayList<SkillRow>();
		try {
			JSONArray jsonArray = new JSONArray(data);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				SkillRow row = new SkillRow(jsonObject.getInt("id"), jsonObject.getString("name"), jsonObject.getDouble("score"));
				results.add(row);
			}
		} catch (Exception e) {
			Log.e(LOG_TAG, e.getMessage());
			e.printStackTrace();
		}
		return results;
	}
}
