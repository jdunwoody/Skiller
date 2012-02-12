package com.james.skiller;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;

public class DataLoader {
	static String FILENAME = "hello_file";
	String string = "hello world!";

	public static String[] load(Context context) throws IOException {
		FileInputStream fis = context.openFileInput(FILENAME);
		byte[] data = new byte[1024];
		int numBytes = fis.read(data);
		fis.close();
		return new String[] {};
	}

	public void save(Context context) throws IOException {
		FileOutputStream fos = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
		fos.write(string.getBytes());
		fos.close();
	}
}