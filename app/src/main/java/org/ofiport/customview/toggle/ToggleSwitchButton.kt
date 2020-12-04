package org.ofiport.customview.toggle

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import org.ofiport.R


/**
 * @author farhan
 * created at at 19:41 on 16/10/2020.
 */
class ToggleSwitchButton(private var view: View) {
  private var textView: TextView = view.findViewById(R.id.textView_toggle)
  private var separator: View = view.findViewById(R.id.view_toggleSeparator)

  constructor(context: Context?) : this(
    LayoutInflater.from(context).inflate(R.layout.item_widget_toggle_switch, null)
  ) {
  }

  fun getView(): View {
    return view
  }

  fun getTextView(): TextView {
    return textView
  }

  fun getSeparator(): View {
    return separator
  }

  fun showSeparator() {
    getSeparator().visibility = View.VISIBLE
  }

  fun hideSeparator() {
    getSeparator().visibility = View.INVISIBLE
  }

}