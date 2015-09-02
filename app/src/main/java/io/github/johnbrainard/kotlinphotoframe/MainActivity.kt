package io.github.johnbrainard.kotlinphotoframe

import android.content.Intent
import android.content.IntentFilter
import android.graphics.drawable.Drawable
import android.graphics.drawable.TransitionDrawable
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import kotlinx.android.synthetic.activity_main.imageView
import kotlin.concurrent.thread

public class MainActivity : AppCompatActivity() {

	var images: ImageSource? = null

	val handler = Handler()
	val callback = Runnable { selectNextImage() }

	var currentDrawable:Drawable? = null

	private val powerReceiver = PowerConnectionReceiver() {
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
	}

	override fun onResume() {
		super.onResume()

		val imageNames = getResources().getStringArray(R.array.images)
		images = ImageSource(imageNames)

		selectNextImage()

		registerReceiver(powerReceiver, IntentFilter(Intent.ACTION_POWER_DISCONNECTED))

		if (isPowerConnected)
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
	}

	override fun onPause() {
		super.onPause()
		handler.removeCallbacks(callback)
		unregisterReceiver(powerReceiver)
	}

	fun selectNextImage() {
		if (images == null)
			return

		val imageName = images!!.nextImage()

		thread(name="image-loader") {
			val drawable = Drawable.createFromStream(getAssets().open(imageName), imageName)

			runOnUiThread {
				if (currentDrawable != null) {
					val transition = TransitionDrawable(arrayOf(currentDrawable, drawable))
					imageView.setImageDrawable(transition)
					transition.startTransition(250)
				} else {
					imageView.setImageDrawable(drawable)
				}

				currentDrawable = drawable
				handler.postDelayed(callback, 10000)
			}
		}
	}

	override fun onCreateOptionsMenu(menu: Menu): Boolean {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu)
		return true
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		val id = item.getItemId()

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true
		}

		return super.onOptionsItemSelected(item)
	}
}
