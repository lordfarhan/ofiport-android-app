package org.ofiport.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.ofiport.R
import org.ofiport.ui.landing.LandingActivity
import org.ofiport.util.SPLASH_SCREEN_DELAY_MILIS
import java.util.concurrent.TimeUnit

class SplashScreenActivity : AppCompatActivity() {

  private lateinit var subscribtions: CompositeDisposable

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_splash_screen)

    subscribtions = CompositeDisposable()

    redirectToLanding()
  }

  private fun redirectToLanding() {
    val timer = Observable.timer(SPLASH_SCREEN_DELAY_MILIS, TimeUnit.MILLISECONDS)
      .subscribe {
        startActivity(Intent(this, LandingActivity::class.java))
        finish()
      }
    subscribtions.add(timer)
  }

  override fun onStop() {
    super.onStop()
    subscribtions.dispose()
  }
}