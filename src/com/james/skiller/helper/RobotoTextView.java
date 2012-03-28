package com.james.skiller.helper;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

public class RobotoTextView extends TextView {

	private final String TAG = getClass().getName();
	private String ttfName;

	public RobotoTextView(Context context, AttributeSet attrs) {
		super(context, attrs);

		for (int i = 0; i < attrs.getAttributeCount(); i++) {
			Log.i(TAG, attrs.getAttributeName(i));

			this.ttfName = attrs.getAttributeValue("http://schemas.android.com/apk/res/com.james.skiller", "ttf_name");
			init();
		}
	}

	@Override
	public void setTypeface(Typeface tf) {
		super.setTypeface(tf);
	}

	private void init() {
		setTypeface(Typeface.createFromAsset(getContext().getAssets(), ttfName));
	}
}