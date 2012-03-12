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
import com.james.skiller.helper.LevelRowAdapter;
import com.james.skiller.model.LevelRow;
import com.james.skiller.model.SkillTree;

public class LevelActivity extends ListActivity {
	private ProgressDialog progressDialog = null;
	private LevelRowAdapter adapter;
	private List<LevelRow> rows = null;
	private Runnable viewOrders;
	private final DataHelper dataHelper;

	public LevelActivity() {
		super();
		this.dataHelper = new DataHelper();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final SkillTree skillTree = loadParameters();

		this.rows = new ArrayList<LevelRow>();
		this.adapter = new LevelRowAdapter(this, R.layout.row, rows);
		setListAdapter(this.adapter);

		getListView().setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				LevelRow item = (LevelRow) getListAdapter().getItem(position);
				if (item != null) {
					Intent intent = new Intent(view.getContext(), TaskActivity.class);
					intent.putExtra("level_id", item.getId());
					intent.putExtra("level_name", item.getName());
					startActivity(intent);
				}
			}
		});

		viewOrders = new Runnable() {
			public void run() {
				getData(skillTree);
			}
		};

		Thread thread = new Thread(null, viewOrders, "MagentoBackground");
		thread.start();
		progressDialog = ProgressDialog.show(this, "Please wait...", "Retrieving data ...", true);
	}

	private SkillTree loadParameters() {
		Bundle extras = getIntent().getExtras();
		int id = 1;
		String name = "none";
		if (extras != null) {
			id = extras.getInt("skill_tree_id");
			name = extras.getString("skill_tree_name");
		}
		return new SkillTree(id, name);
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

	private void getData(SkillTree skillTree) {
		try {
			String url = getResources().getString(R.string.server_url) + "skill_trees/" + skillTree.id + "/levels.json";
			String data = dataHelper.readData(url);
			rows = jsonToArray(data);
		} catch (Exception e) {
			Log.e(SkillTreeActivity.LOG_TAG, e.getMessage());
		}
		runOnUiThread(returnRes);
	}

	private List<LevelRow> jsonToArray(String data) {
		List<LevelRow> results = new ArrayList<LevelRow>();
		try {
			JSONArray jsonArray = new JSONArray(data);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				LevelRow row = new LevelRow(jsonObject.getInt("id"), jsonObject.getString("name"), jsonObject.getDouble("score"));
				results.add(row);
			}
		} catch (Exception e) {
			Log.e(SkillTreeActivity.LOG_TAG, e.getMessage());
			e.printStackTrace();
		}
		return results;
	}
}
