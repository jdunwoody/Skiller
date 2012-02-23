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
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class TaskActivity extends ListActivity {

	private static final String LOG_TAG = TaskActivity.class.toString();
	private String skill_tree_id;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle extras = getIntent().getExtras();
		if (extras == null) {
			skill_tree_id = "1";
			Toast.makeText(getApplicationContext(), "No skill_tree_id supplied. Defaulting to 1", Toast.LENGTH_SHORT).show();

		} else {
			skill_tree_id = extras.getString("skill_tree_id");
			Log.w(LOG_TAG, "Looking up skill_tree: " + skill_tree_id);
		}
		setListAdapter(new FancyArrayAdapter(this, translateToView(readData())));
		
		setupListView();
	}

	private TaskRow[] translateToView(String data) {
		List<TaskRow> results = new ArrayList<TaskRow>();
		try {
			Log.i(LOG_TAG, "Data is " + data);

			JSONObject skillTree = new JSONObject(data);
			Log.i(LOG_TAG, "Skill tree: " + skillTree);

			JSONArray levels = skillTree.getJSONArray("levels");

			for (int i = 0; i < levels.length(); i++) {
				JSONObject level = levels.getJSONObject(i);
				Log.i(LOG_TAG, "Level: " + level);
				String levelName = level.getString("name");
				Log.i(LOG_TAG, "Level name: " + levelName);

				JSONArray tasks = level.getJSONArray("tasks");

				for (int j = 0; j < tasks.length(); j++) {
					JSONObject task = tasks.getJSONObject(j);

					String taskName = task.getString("name");
					int task_id = task.getInt("id");
					String skillTreeName = skillTree.getString("name");
					String url = getResources().getString(R.string.server_url) + "/tasks/" + task_id + "/toggle_complete.json";
					boolean taskStatus = task.getString("status") == "true";

					Log.i(LOG_TAG, "Task: " + task);
					Log.i(LOG_TAG, "task id: " + task_id);
					Log.i(LOG_TAG, "task name: " + taskName);
					Log.i(LOG_TAG, "task status: " + task.getString("status"));

					results.add(new TaskRow(skillTreeName, levelName, taskName, task_id, taskStatus, url));
				}
			}
		} catch (Exception e) {
			Log.e(SkillTreeActivity.class.getName(), e.getMessage());
			e.printStackTrace();
		}
		return results.toArray(new TaskRow[results.size()]);
	}

	// {"created_at":"2012-02-11T03:59:49Z","id":1,"name":"Parenting","updated_at":"2012-02-12T02:27:11Z",
	// "levels":[
	// {"created_at":"2012-02-12T12:41:28Z","id":3,"name":"Level1","skill_tree_id":1,"updated_at":"2012-02-12T12:41:28Z","tasks":[
	// ]
	// },
	// {"created_at":"2012-02-12T12:41:29Z","id":4,"name":"Level1","skill_tree_id":1,"updated_at":"2012-02-12T12:41:29Z","tasks":[
	// {"created_at":"2012-02-12T12:41:40Z","description":"","frequency_id":null,"id":3,"level_id":4,"name":"Simple Task","status":null,"updated_at":"2012-02-12T12:41:40Z","url":""}
	// ]}
	// ]}

	private void setupListView() {
		ListView lv = getListView();
		lv.setTextFilterEnabled(true);

		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Log.i(LOG_TAG, "Clicked view at postition: " + position + " with id: " + id);
				TextView textView = (TextView) view;
				// Toast.makeText(getApplicationContext(), textView.getText(), Toast.LENGTH_SHORT).show();
				TaskRow item = (TaskRow) getListAdapter().getItem(position);

				item.setStatus(toggleStatus(item));

				textView.setText(item.toString());
				updateColour(item, textView);
				textView.invalidate();
				Log.i(LOG_TAG, item.toString());

				// startActivity(getIntent());
				// finish();
				// textView.invalidate();
				// ArrayAdapter listAdaptor = (ArrayAdapter) getListAdapter();
				// listAdaptor.notifyDataSetChanged();

				// ArrayAdapter<TaskRow> listAdapter = new ArrayAdapter<TaskRow>(this, R.layout.list_item, list);
				// ((BaseAdapter) listAdapter).notifyDataSetChanged();
			}

		});
	}

	private void updateColour(TaskRow item, TextView textView) {
		int newColour = item.getStatus() ? R.color.light_cream : R.color.faded;
		textView.setBackgroundColor(newColour);
		Log.i(LOG_TAG, "updatedColour: " + newColour);
	}

	private boolean readStatus(String data) {
		try {
			JSONObject obj = new JSONObject(data);
		} catch (JSONException e) {
			Log.e(LOG_TAG, e.getMessage());
			e.printStackTrace();
		}

		return true;
	}

	public boolean toggleStatus(TaskRow item) {
		StringBuilder builder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();

		String url = getResources().getString(R.string.server_url) + "tasks/" + item.getTaskId() + "/toggle_complete.json";

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
		return Boolean.parseBoolean(builder.toString());
	}

	public String readData() {
		StringBuilder builder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		Log.i(LOG_TAG, "Accessing url " + skill_tree_id);

		String tasksInSkillTreeUrl = getResources().getString(R.string.server_url) + "/skill_trees/" + skill_tree_id + "/everything.json";

		HttpGet httpGet = new HttpGet(tasksInSkillTreeUrl);
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
		} catch (Exception e) {
			Log.e(LOG_TAG, e.getMessage());
			e.printStackTrace();
		}

		return builder.toString();
	}
}