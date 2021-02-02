package com.partworm.sodakey.keyview

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class BackspaceKeyView : KeyView {

  private var keyRepeatingThread: Thread? = null
  private var isPressedMy = false

  constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
  constructor(context: Context?) : super(context)

  override fun onTouch(v: View, e: MotionEvent): Boolean {
    super.onTouch(v, e)
    return when (e.action) {
      MotionEvent.ACTION_DOWN -> {
        isPressedMy = true
        val handler = Handler(Looper.getMainLooper())
        Thread(Runnable {
          try {
            handler.post { keyboard!!.offerBackspace() }
            Thread.sleep(300)
            while (isPressedMy) {
              handler.post { keyboard!!.offerBackspace() }
              Thread.sleep(60)
            }
          } catch (ex: InterruptedException) {
          }
        }).also { keyRepeatingThread = it }.start()
        true
      }
      MotionEvent.ACTION_UP -> {
        isPressedMy = false
        keyRepeatingThread!!.interrupt()
        false
      }
      else -> {
        false
      }
    }
  }

}