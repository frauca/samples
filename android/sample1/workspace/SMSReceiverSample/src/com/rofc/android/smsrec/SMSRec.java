package com.rofc.android.smsrec;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class SMSRec extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Log.i(context.getResources().getString(R.string.app_name), "service listen");
		Toast.makeText(context, "here i am", Toast.LENGTH_SHORT).show();
	}

}
