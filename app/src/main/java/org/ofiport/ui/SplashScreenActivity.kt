package org.ofiport.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import org.ofiport.R
import org.ofiport.ui.landing.LandingActivity
import org.ofiport.util.SPLASH_SCREEN_DELAY_MILIS

class SplashScreenActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_splash_screen)

    Handler().postDelayed({
      startActivity(Intent(this, LandingActivity::class.java))
      finish()
    }, SPLASH_SCREEN_DELAY_MILIS)
  }
}