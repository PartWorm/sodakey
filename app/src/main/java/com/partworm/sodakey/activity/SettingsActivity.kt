package com.partworm.sodakey.activity

import android.app.Activity
import android.os.Bundle
import com.partworm.sodakey.R

internal class SettingsActivity : Activity() {
  public override fun onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_settings)
  }
}