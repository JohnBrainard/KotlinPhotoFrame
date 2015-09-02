package io.github.johnbrainard.kotlinphotoframe

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class PowerConnectionReceiver constructor() : BroadcastReceiver() {
	var disconnected: () -> Unit = {}

	constructor(disconnected:()->Unit) : this() {
		this.disconnected = disconnected
	}

	override fun onReceive(context: Context, intent: Intent) {
		when (intent.getAction()) {
			Intent.ACTION_POWER_CONNECTED -> {
				val activityIntent = Intent(context, javaClass<MainActivity>())
				activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
				context.startActivity(activityIntent)
			}

			Intent.ACTION_POWER_DISCONNECTED -> {
				disconnected?.invoke()
			}
		}
	}
}
