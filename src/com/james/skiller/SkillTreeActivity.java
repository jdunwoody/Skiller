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

import com.james.skiller.model.Order;

public class SkillTreeActivity extends ListActivity {
	private ProgressDialog m_ProgressDialog = null;

	private static final String LOG_TAG = SkillTreeActivity.class.toString();
	private OrderAdapter m_adapter;
	private List<Order> m_orders = null;
	private Runnable viewOrders;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.m_orders = new ArrayList<Order>();
		this.m_adapter = new OrderAdapter(this, R.layout.row, m_orders);
		setListAdapter(this.m_adapter);
		
		getListView().setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(view.getContext(), TaskActivity.class);
				intent.putExtra("skill_tree_id", "1");
				startActivity(intent);
			}
		});

		viewOrders = new Runnable() {
			public void run() {
				getOrders();
			}
		};
		Thread thread = new Thread(null, viewOrders, "MagentoBackground");
		thread.start();
		m_ProgressDialog = ProgressDialog.show(this, "Please wait...", "Retrieving data ...", true);

	}

	private Runnable returnRes = new Runnable() {
		public void run() {
			if (m_orders != null && m_orders.size() > 0) {
				m_adapter.notifyDataSetChanged();
				for (int i = 0; i < m_orders.size(); i++)
					m_adapter.add(m_orders.get(i));
			}
			m_ProgressDialog.dismiss();
			m_adapter.notifyDataSetChanged();
		}
	};

	private void getOrders() {
		try {
			String url = getResources().getString(R.string.server_url) + "/skill_trees.json";
			m_orders = jsonToArray(readData(url));
		} catch (Exception e) {
			Log.e("BACKGROUND_PROC", e.getMessage());
		}
		runOnUiThread(returnRes);
	}

	private List<Order> jsonToArray(String data) {
		List<Order> results = new ArrayList<Order>();
		try {
			JSONArray jsonArray = new JSONArray(data);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				results.add(new Order(jsonObject.getString("name"), "Beginner 0%"));
			}
		} catch (Exception e) {
			Log.e(SkillTreeActivity.class.getName(), e.getMessage());
			e.printStackTrace();
		}
		return results;
	}

	private void setupListView() {
		ListView lv = getListView();
		lv.setTextFilterEnabled(true);

		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(view.getContext(), TaskActivity.class);
				intent.putExtra("skill_tree_id", "1");
				startActivity(intent);
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

	private class OrderAdapter extends ArrayAdapter<Order> {

		private List<Order> items;

		public OrderAdapter(Context context, int textViewResourceId, List<Order> m_orders) {
			super(context, textViewResourceId, m_orders);
			this.items = m_orders;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.row, null);
			}
			Order o = items.get(position);
			if (o != null) {
				TextView tt = (TextView) v.findViewById(R.id.toptext);
				TextView bt = (TextView) v.findViewById(R.id.bottomtext);
				if (tt != null) {
					tt.setText("Name: " + o.getOrderName());
				}
				if (bt != null) {
					bt.setText("Status: " + o.getOrderStatus());
				}
			}
			return v;
		}
	}
}
