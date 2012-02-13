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
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class TaskActivity extends ListActivity {

	private static final String LOG_TAG = TaskActivity.class.toString();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle extras = getIntent().getExtras();
		String skill_tree_id;
		if (extras == null) {
			skill_tree_id = "1";
			Log.w(LOG_TAG, "No skill_tree_id supplied. Defaulting to 1");
		} else {
			skill_tree_id = extras.getString("skill_tree_id");
			Log.w(LOG_TAG, "Looking up skill_tree: " + skill_tree_id);
		}

		String url = getResources().getString(R.string.server_url) + "/skill_trees/" + skill_tree_id + "/everything.json";

		setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, translateToView(readData(url))));
		setupListView();
	}

	private String[] translateToView(String data) {
		List<String> results = new ArrayList<String>();
		try {
			Log.i(LOG_TAG, "Data is " + data);

			JSONObject skillTree = new JSONObject(data);
			Log.i(LOG_TAG, "Skill tree: " + skillTree);

			JSONArray levels = skillTree.getJSONArray("levels");

			for (int i = 0; i < levels.length(); i++) {
				JSONObject level = levels.getJSONObject(i);
				Log.i(LOG_TAG, "Level: " + level);
				Log.i(LOG_TAG, "Level name: " + level.getString("name"));

				JSONArray tasks = level.getJSONArray("tasks");

				for (int j = 0; j < tasks.length(); j++) {
					JSONObject task = tasks.getJSONObject(j);
					Log.i(LOG_TAG, "Task: " + task);
					Log.i(LOG_TAG, "task name: " + task.getString("name"));
					Log.i(LOG_TAG, "task status: " + task.getString("status"));
					results.add(level.getString("name") + ": " + task.getString("name") + ": " + task.getString("status"));
				}
			}
		} catch (Exception e) {
			Log.e(SkillTreeActivity.class.getName(), e.getMessage());
			e.printStackTrace();
		}
		return results.toArray(new String[results.size()]);
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
				Intent intent = new Intent(TaskActivity.class.getName());
				startActivity(intent);

				// Toast.makeText(getApplicationContext(), ((TextView) view).getText(), Toast.LENGTH_SHORT).show();
			}
		});
	}

	public String readData(String url) {
		StringBuilder builder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		Log.i(LOG_TAG, "Accessing url " + url);

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
}