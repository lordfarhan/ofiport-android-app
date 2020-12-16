package org.ofiport.customview.toggle

import android.content.Context
import android.util.AttributeSet
import java.util.*

/**
 * @author farhan
 * created at at 19:42 on 16/10/2020.
 */
class MultipleToggleSwitch(context: Context, attrs: AttributeSet?) :
  BaseToggleSwitch(context, attrs) {
  private val mCheckedPositions: MutableSet<Int>

  constructor(context: Context) : this(context, null)

  fun setCheckedTogglePosition(position: Int) {
    mCheckedPositions.add(position)
    prepareToggleSwitchLayouts()
    notifyOnToggleChange(position)
  }

  fun setUncheckedTogglePosition(position: Int) {
    mCheckedPositions.remove(position)
    prepareToggleSwitchLayouts()
    notifyOnToggleChange(position)
  }

  private fun prepareToggleSwitchLayouts() {
    for (i in 0 until getNumButtons()) {
      if (isActive(i)) {
        activate(i)
        prepareSeparator(true, i)
      } else {
        disable(i)
        prepareSeparator(false, i)
      }
    }
  }

  fun getCheckedTogglePositions(): Set<Int> {
    return mCheckedPositions
  }

  private fun prepareSeparator(isActive: Boolean, position: Int) {
    if (!isLast(position) && isActive == isActive(position + 1)) getToggleSwitchButton(position).showSeparator() else getToggleSwitchButton(
      position
    ).hideSeparator()
  }

  override fun isActive(position: Int): Boolean {
    return mCheckedPositions.contains(position)
  }

  override fun onClickOnToggleSwitch(position: Int) {
    if (isActive(position)) setUncheckedTogglePosition(position) else setCheckedTogglePosition(
      position
    )
  }

  init {
    mCheckedPositions = TreeSet()
    prepareToggleSwitchLayouts()
  }
}