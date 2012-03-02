package com.james.skiller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.james.skiller.helper.DataHelper;
import com.james.skiller.model.Row;
import com.james.skiller.model.SkillTree;

public class TaskActivity extends ListActivity {
	private ProgressDialog progressDialog = null;
	private static final String LOG_TAG = TaskActivity.class.toString();
	private RowAdapter adapter;
	private List<Row> rows = null;
	private Runnable viewOrders;
	private final DataHelper dataHelper;
	private ListHelper listHelper;

	private TextView headerRow;
	private TextView footerRow;

	public TaskActivity() {
		super();
		this.listHelper = new ListHelper();
		this.dataHelper = new DataHelper();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final SkillTree skillTree = loadParameters();

		headerRow = listHelper.loadHeaderRow(getApplicationContext(), skillTree);
		footerRow = listHelper.loadFooterRow(getApplicationContext(), skillTree);

		getListView().addHeaderView(headerRow);
		getListView().addFooterView(footerRow);

		this.rows = new ArrayList<Row>();
		this.adapter = new RowAdapter(this, R.layout.row, rows);
		setListAdapter(this.adapter);

		getListView().setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Row item = (Row) getListView().getItemAtPosition(position);

				RowAdapter.updateRow(item, view);
				//
				// item.setStatus(toggleStatus(item));
				//
				// LinearLayout textView = (LinearLayout) view;
				// TextView bottomView = (TextView) view.findViewById(R.id.bottomtext);
				//
				// ImageView icon_image = (ImageView) view.findViewById(R.id.icon);
				// icon_image.setImageResource(item.getStatusBoolean() ? R.drawable.dramatic : R.drawable.ic_launcher);
				//
				// bottomView.setText(item.getStatusBoolean() ? "" : "Incomplete");
				// // updateColour(item, bottomView);
				// textView.invalidate();
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
			String url = getResources().getString(R.string.server_url) + "/skill_trees/" + skillTree.id + "/everything.json";
			rows = jsonToArray(dataHelper.readData(url));
			footerRow.setText(rows.size() + " tasks.");
		} catch (Exception e) {
			Log.e("BACKGROUND_PROC", e.getMessage());
		}
		runOnUiThread(returnRes);
	}

	private List<Row> jsonToArray(String data) {
		List<Row> results = new ArrayList<Row>();
		try {
			JSONObject skillTree = new JSONObject(data);
			JSONArray levels = skillTree.getJSONArray("levels");

			for (int i = 0; i < levels.length(); i++) {
				JSONObject level = levels.getJSONObject(i);
				String levelName = level.getString("name");

				JSONArray tasks = level.getJSONArray("tasks");

				for (int j = 0; j < tasks.length(); j++) {
					JSONObject task = tasks.getJSONObject(j);

					String taskName = task.getString("name");
					int task_id = task.getInt("id");
					String skillTreeName = skillTree.getString("name");
					String url = getResources().getString(R.string.server_url) + "/tasks/" + task_id + "/toggle_complete.json";
					boolean taskStatus = task.getString("status") == "true";

					results.add(new Row(task_id, taskName, taskStatus));
				}
			}
		} catch (Exception e) {
			Log.e(SkillTreeActivity.class.getName(), e.getMessage());
			e.printStackTrace();
		}
		return results;

	}

	public String readData(String url) {
		StringBuilder builder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		try {
			HttpResponse response = client.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(content));
				String line;
				while ((line = reader.readLine()) != null) {
					builder.append(line);
				}
			} else {
				Log.e(LOG_TAG, "Failed to download file");
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return builder.toString();
	}

	public String toggleStatus(Row item) {
		StringBuilder builder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();

		String url = getResources().getString(R.string.server_url) + "tasks/" + item.getId() + "/toggle_complete.json";

		HttpPut httpPut = new HttpPut(url);
		try {
			HttpResponse response = client.execute(httpPut);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(content));
				String line;
				while ((line = reader.readLine()) != null) {
					builder.append(line);
				}
			} else {
				Log.e(LOG_TAG, "Failed to download file");
			}
		} catch (Exception e) {
			Log.e(LOG_TAG, e.getMessage());
			e.printStackTrace();
		}
		return builder.toString();
	}

	private void updateColour(Row item, TextView textView) {
		int newColour = item.getStatusBoolean() ? R.color.light_cream : R.color.faded;
		textView.setTextColor(newColour);
	}
}