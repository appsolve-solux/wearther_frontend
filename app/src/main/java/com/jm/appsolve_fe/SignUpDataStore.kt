package com.jm.appsolve_fe

import android.content.Context
import android.content.SharedPreferences

object SignUpDataStore {
    private const val PREFS_NAME  = "SignUpData"

    fun saveData(context: Context, key: String, value: String) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME , Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getData(context: Context, key: String): String? {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME , Context.MODE_PRIVATE)
        return sharedPreferences.getString(key, "")
    }
}