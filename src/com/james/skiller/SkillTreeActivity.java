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
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.james.skiller.model.Row;

public class SkillTreeActivity extends ListActivity {
	private ProgressDialog progressDialog = null;

	private static final String LOG_TAG = SkillTreeActivity.class.toString();
	private RowAdapter adapter;
	private List<Row> rows = null;
	private Runnable viewOrders;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.rows = new ArrayList<Row>();
		this.adapter = new RowAdapter(this, R.layout.row, rows);
		setListAdapter(this.adapter);

		getListView().setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(view.getContext(), TaskActivity.class);
				intent.putExtra("skill_tree_id", "1");
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
			rows = jsonToArray(readData(url));
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
				results.add(new Row(jsonObject.getString("name") + " Beginner 0%", false, 0));
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
