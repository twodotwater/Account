package com.lzhn.utils.os;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {

	private static final Object ACTION_BOOT_COMPLETED = "android.intent.action.BOOT_COMPLETED";

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(ACTION_BOOT_COMPLETED)) {
			Intent intent2 = new Intent(context,
					com.yqy.account.MainActivity.class);
			intent2.setAction("android.intent.action.MAIN");
			intent2.addCategory("android.intent.category.LAUNCHER");
			intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent2);
		}
	}

}
