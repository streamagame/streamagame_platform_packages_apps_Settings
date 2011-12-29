/*
 * Copyright (C) 2006 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.settings;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.util.Slog;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceGroup;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.util.Log;
import android.view.WindowManager;
import android.content.Context;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import android.os.Handler;

public class PowerOff extends SettingsPreferenceFragment{
	private Context mContext;
	public void onCreate(Bundle icicle) {
	        super.onCreate(icicle);
		}
	public void onStart()	{
		super.onStart();
		beginshutdown();
		}

	public void beginshutdown()
	    {
		final CloseDialogReceiver closer = new CloseDialogReceiver(getActivity());
	        final AlertDialog dialog = new AlertDialog.Builder(getActivity())
	        .setIconAttribute(android.R.attr.alertDialogIcon)
	        .setTitle(com.android.internal.R.string.power_off)
	        .setMessage(com.android.internal.R.string.shutdown_confirm_question)
	        .setPositiveButton(com.android.internal.R.string.yes, new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) {
			Intent shutdown = new Intent(Intent.ACTION_REQUEST_SHUTDOWN);
	                shutdown.putExtra(Intent.EXTRA_KEY_CONFIRM, false);
	                shutdown.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(shutdown);
	            }
	        })
	        .setNegativeButton(com.android.internal.R.string.no, new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) {
	                Intent close = new Intent(Intent.ACTION_MAIN);
	                close.addCategory(Intent.CATEGORY_HOME);
	                close.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	                startActivity(close);
	                finish();
            }
	        })
	        .create();
	        closer.dialog = dialog;
	        dialog.setOnDismissListener(closer);
	        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
	        if (!getActivity().getResources().getBoolean(
                    com.android.internal.R.bool.config_sf_slowBlur)) {
                dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
		}
	        dialog.show();

	}
	private static class CloseDialogReceiver extends BroadcastReceiver
	    implements DialogInterface.OnDismissListener {
		private Context mContext;
		public Dialog dialog;

		CloseDialogReceiver(Context context) {
				mContext = context;
				IntentFilter filter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
				context.registerReceiver(this, filter);
		}

		@Override
		public void onReceive(Context context, Intent intent) {
			dialog.cancel();
		}

		public void onDismiss(DialogInterface unused) {
			mContext.unregisterReceiver(this);
		}
	    }
	public void onResume()	{
		super.onResume();
		}
	public void onPause()	{
		super.onPause();
		}
	public void onStop()	{
		super.onStop();
		}
	public void onDestroy()	{
		super.onDestroy();
		}
	}
