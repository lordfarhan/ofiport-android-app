package org.ofiport.util

import android.content.Context
import android.content.SharedPreferences

/**
 * @author farhan
 * created at at 11:47 on 15/12/20.
 */
class SharedPreferenceUtils(private val context: Context) {
    private val sharedPref: SharedPreferences =
        context.getSharedPreferences("userdata", Context.MODE_PRIVATE)

    fun putString(key: String, value: String) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun putInt(key: String, value: Int) {
        val editor = sharedPref.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    fun getString(key: String): String {
        return sharedPref.getString(key, "") ?: ""
    }

    fun getInt(key: String): Int {
        return sharedPref.getInt(key, -1)
    }
}