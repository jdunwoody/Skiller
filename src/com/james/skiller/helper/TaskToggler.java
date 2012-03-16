package com.james.skiller.helper;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.res.Resources;
import android.util.Log;

import com.james.skiller.R;
import com.james.skiller.model.MegaListTaskRow;

public class TaskToggler {
	public static void toggleStatus(Resources resources, MegaListTaskRow item) {
		Log.i(Logger.LOG_TAG, "Toggling status for " + item);

		StringBuilder builder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();

		String url = resources.getString(R.string.server_url) + "tasks/" + item.getId() + "/toggle_complete.json";

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
				Log.e(Logger.LOG_TAG, "Failed to download file");
			}
		} catch (Exception e) {
			Log.e(Logger.LOG_TAG, e.getMessage());
			e.printStackTrace();
		}
		Log.i(Logger.LOG_TAG, "About to change item status from " + item.getStatus() + " to new value " + builder.toString() + " is: " + Boolean.parseBoolean(builder.toString()));
		item.setStatus(Boolean.parseBoolean(builder.toString()));
	}
}