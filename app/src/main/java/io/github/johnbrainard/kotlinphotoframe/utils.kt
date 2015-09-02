package io.github.johnbrainard.kotlinphotoframe

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager

val Context.isPowerConnected:Boolean get() {
	val intent = registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
	val plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1)
	return plugged == BatteryManager.BATTERY_PLUGGED_AC || plugged == BatteryManager.BATTERY_PLUGGED_USB
}
