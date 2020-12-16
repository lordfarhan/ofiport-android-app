package org.ofiport.customview.toggle

import android.content.Context
import android.util.AttributeSet

/**
 * @author farhan
 * created at at 19:40 on 16/10/2020.
 */
class ToggleSwitch(context: Context, attrs: AttributeSet?) :
  BaseToggleSwitch(context, attrs) {
  private var mCheckedTogglePosition = 0

  constructor(context: Context) : this(context, null)

  fun getCheckedTogglePosition(): Int {
    return mCheckedTogglePosition
  }

  override fun onClickOnToggleSwitch(position: Int) {
    setCheckedTogglePosition(position)
  }

  fun setCheckedTogglePosition(position: Int) {
    setCheckedTogglePosition(position, true)
  }

  fun setCheckedTogglePosition(position: Int, notifyListener: Boolean) {
    disableAll()
    activate(position)
    setSeparatorVisibility(position)
    mCheckedTogglePosition = position
    if (notifyListener) notifyOnToggleChange(position)
  }

  private fun setSeparatorVisibility(activeIndex: Int) {
    for (i in 0 until getToggleSwitchesContainer()!!.childCount - 1) {
      val toggleSwitchButton = ToggleSwitchButton(
        getToggleSwitchesContainer()!!.getChildAt(i)
      )
      if (i == activeIndex || i == activeIndex - 1) toggleSwitchButton.hideSeparator() else toggleSwitchButton.showSeparator()
    }
  }

  override fun buildToggleButtons() {
    super.buildToggleButtons()
    setCheckedTogglePosition(0)
  }

  override fun isActive(position: Int): Boolean {
    return mCheckedTogglePosition == position
  }
}