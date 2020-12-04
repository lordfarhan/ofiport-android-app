package org.ofiport

import android.app.Application
import android.content.Context

/**
 * @author farhan
 * created at at 9:16 on 13/10/2020.
 */
class App : Application() {

  companion object {
    private lateinit var instance: App

    fun getInstance(): App {
      return instance
    }

    fun getContext(): Context {
      return instance.applicationContext
    }

    var isShowingDialog: Boolean = false
  }

  override fun onCreate() {
    instance = this
    super.onCreate()
  }

}