package com.aoenang.mmgd;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils {
	private static Toast mToast;

	/**
	 * show a toast
	 * 
	 * @param context
	 * @param text
	 */
	public static void showToast(Context context, String text) {
		if (mToast == null) {
			mToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
		}
		mToast.setText(text);
		mToast.show();
	}
}
