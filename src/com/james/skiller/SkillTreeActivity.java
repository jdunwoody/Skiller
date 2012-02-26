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
import com.james.skiller.model.Row;

public class SkillTreeActivity extends ListActivity {
	private ProgressDialog progressDialog = null;
	private static final String LOG_TAG = SkillTreeActivity.class.toString();
	private RowAdapter adapter;
	private List<Row> rows = null;
	private Runnable viewOrders;
	private final DataHelper dataHelper;

	public SkillTreeActivity() {
		super();
		this.dataHelper = new DataHelper();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.rows = new ArrayList<Row>();
		this.adapter = new RowAdapter(this, R.layout.row, rows);
		setListAdapter(this.adapter);

		getListView().setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Row item = (Row) getListAdapter().getItem(position);

				Intent intent = new Intent(view.getContext(), TaskActivity.class);
				intent.putExtra("skill_tree_id", item.getId());
				startActivity(intent);
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
			String url = getResources().getString(R.string.server_url) + "/skill_trees.json";
			rows = jsonToArray(dataHelper.readData(url));
		} catch (Exception e) {
			Log.e("BACKGROUND_PROC", e.getMessage());
		}
		runOnUiThread(returnRes);
	}

	private List<Row> jsonToArray(String data) {
		List<Row> results = new ArrayList<Row>();
		try {
			JSONArray jsonArray = new JSONArray(data);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				Row row = new Row(jsonObject.getInt("id"), jsonObject.getString("name"), "TODO: status of child tasks");
				results.add(row);
			}
		} catch (Exception e) {
			Log.e(SkillTreeActivity.class.getName(), e.getMessage());
			e.printStackTrace();
		}
		return results;
	}
}
