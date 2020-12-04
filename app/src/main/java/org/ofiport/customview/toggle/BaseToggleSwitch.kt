package org.ofiport.customview.toggle

import android.content.Context
import android.content.res.Resources
import android.content.res.TypedArray
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RoundRectShape
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import org.ofiport.R


/**
 * @author farhan
 * created at at 19:37 on 16/10/2020.
 */
abstract class BaseToggleSwitch(context: Context, attrs: AttributeSet?) :
  LinearLayout(context, attrs), View.OnClickListener {
  interface OnToggleSwitchChangeListener {
    fun onToggleSwitchChangeListener(position: Int, isChecked: Boolean)
  }

  protected object Default {
    internal const val ACTIVE_BG_COLOR: Int = R.color.colorPrimary
    internal const val ACTIVE_TEXT_COLOR = R.color.colorWhite
    internal const val INACTIVE_BG_COLOR: Int = R.color.colorTextLight
    internal const val INACTIVE_TEXT_COLOR: Int = R.color.colorTextDark
    internal const val SEPARATOR_COLOR: Int = R.color.colorClouds
    internal const val CORNER_RADIUS_DP = 4
    internal const val TEXT_SIZE = 12f
    internal const val TOGGLE_WIDTH = 64f
    internal const val TOGGLE_HEIGHT = 48f
  }

  private var mOnToggleSwitchChangeListener: OnToggleSwitchChangeListener? = null
  private var activeBgColor = 0
  private var activeTextColor = 0
  private var inactiveBgColor = 0
  private var inactiveTextColor = 0
  private var separatorColor = 0
  private var textSize = 0
  private var cornerRadius = 0f
  private var toggleWidth = 0f
  private var toggleHeight = 0f
  private var mInflater: LayoutInflater? = null
  private var toggleSwitchesContainer: LinearLayout? = null
  private var mLabels: ArrayList<String>? = null
  private var mContext: Context? = null

  constructor(context: Context) : this(context, null) {}

  // *************** GETTERS AND SETTERS ****************
  fun getActiveBgColor(): Int {
    return activeBgColor
  }

  fun setActiveBgColor(activeBgColor: Int) {
    this.activeBgColor = activeBgColor
  }

  fun getActiveTextColor(): Int {
    return activeTextColor
  }

  fun setActiveTextColor(activeTextColor: Int) {
    this.activeTextColor = activeTextColor
  }

  fun getInactiveBgColor(): Int {
    return inactiveBgColor
  }

  fun setInactiveBgColor(inactiveBgColor: Int) {
    this.inactiveBgColor = inactiveBgColor
  }

  fun getInactiveTextColor(): Int {
    return inactiveTextColor
  }

  fun setInactiveTextColor(inactiveTextColor: Int) {
    this.inactiveTextColor = inactiveTextColor
  }

  fun getSeparatorColor(): Int {
    return separatorColor
  }

  fun setSeparatorColor(separatorColor: Int) {
    this.separatorColor = separatorColor
  }

  fun getTextSize(): Int {
    return textSize
  }

  fun setTextSize(textSize: Int) {
    this.textSize = textSize
  }

  fun getCornerRadius(): Float {
    return cornerRadius
  }

  fun setCornerRadius(cornerRadius: Float) {
    this.cornerRadius = cornerRadius
  }

  fun getToggleWidth(): Float {
    return toggleWidth
  }

  fun setToggleWidth(toggleWidth: Float) {
    this.toggleWidth = toggleWidth
  }

  fun getToggleHeight(): Float {
    return toggleHeight
  }

  fun setToggleHeight(toggleHeight: Float) {
    this.toggleHeight = toggleHeight
  }

  // *********** PRIVATE INSTANCE METHODS ************
  protected fun activate(position: Int) {
    setColors(getToggleSwitchButton(position), activeBgColor, activeTextColor)
  }

  private fun addToggleButton(text: String) {
    val toggleSwitchButton = ToggleSwitchButton(mContext)
    val toggleBtnTxt: TextView = toggleSwitchButton.getTextView()
    toggleBtnTxt.text = text
    toggleBtnTxt.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize.toFloat())
    val params = LayoutParams(toggleWidth.toInt(), toggleHeight.toInt())
    if (toggleWidth == 0f) params.weight = 1f
    toggleBtnTxt.layoutParams = params
    toggleSwitchButton.getSeparator().setBackgroundColor(separatorColor)
    toggleSwitchButton.getTextView().setOnClickListener(this)
    val layoutParams = LayoutParams(toggleWidth.toInt(), LayoutParams.MATCH_PARENT)
    if (toggleWidth == 0f) layoutParams.weight = 1f
    toggleSwitchesContainer!!.addView(toggleSwitchButton.getView(), layoutParams)

    // Disable last added button
    disable(toggleSwitchesContainer!!.childCount - 1)
  }

  private fun buildRect(toggleSwitchButton: ToggleSwitchButton): RoundRectShape {
    return if (isFirst(toggleSwitchButton)) RoundRectShape(
      floatArrayOf(cornerRadius, cornerRadius, 0f, 0f, 0f, 0f, cornerRadius, cornerRadius),
      null,
      null
    ) else if (isLast(toggleSwitchButton)) RoundRectShape(
      floatArrayOf(0f, 0f, cornerRadius, cornerRadius, cornerRadius, cornerRadius, 0f, 0f),
      null,
      null
    ) else RoundRectShape(
      floatArrayOf(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f),
      null,
      null
    )
  }

  protected open fun buildToggleButtons() {
    for (label in mLabels!!) addToggleButton(label)
  }

  protected fun disable(position: Int) {
    setColors(getToggleSwitchButton(position), inactiveBgColor, inactiveTextColor)
  }

  protected fun disableAll() {
    for (i in 0 until toggleSwitchesContainer!!.childCount) disable(i)
  }

  protected abstract fun isActive(position: Int): Boolean
  protected fun getNumButtons(): Int {
    return getToggleSwitchesContainer()!!.childCount
  }

  protected fun getToggleSwitchesContainer(): LinearLayout? {
    return toggleSwitchesContainer
  }

  protected fun getToggleIndex(toggleSwitchButton: ToggleSwitchButton): Int {
    return toggleSwitchesContainer!!.indexOfChild(toggleSwitchButton.getView())
  }

  protected fun getToggleSwitchButton(position: Int): ToggleSwitchButton {
    return ToggleSwitchButton(toggleSwitchesContainer!!.getChildAt(position))
  }

  protected fun isLast(position: Int): Boolean {
    return position == getToggleSwitchesContainer()!!.childCount - 1
  }

  override fun onClick(v: View) {
    val toggleSwitchButton = v.parent as LinearLayout
    val position = toggleSwitchesContainer!!.indexOfChild(toggleSwitchButton)
    onClickOnToggleSwitch(position)
  }

  protected abstract fun onClickOnToggleSwitch(position: Int)
  protected fun setColors(toggleSwitchButton: ToggleSwitchButton, bgColor: Int, textColor: Int) {
    val sd = ShapeDrawable(buildRect(toggleSwitchButton))
    sd.paint.color = bgColor
    toggleSwitchButton.getView().setBackground(sd)
    toggleSwitchButton.getTextView().setTextColor(textColor)
  }

  fun setLabels(labels: ArrayList<String>?) {
    if (labels == null || labels.isEmpty()) throw RuntimeException("The list of labels must contains at least 2 elements")
    mLabels = labels
    toggleSwitchesContainer!!.removeAllViews()
    buildToggleButtons()
  }

  fun setOnToggleSwitchChangeListener(onToggleSwitchChangeListener: OnToggleSwitchChangeListener?) {
    mOnToggleSwitchChangeListener = onToggleSwitchChangeListener
  }

  fun notifyOnToggleChange(position: Int) {
    if (mOnToggleSwitchChangeListener != null) mOnToggleSwitchChangeListener!!.onToggleSwitchChangeListener(
      position,
      isActive(position)
    )
  }

  fun setTogglePadding(left: Int, top: Int, right: Int, bottom: Int) {
    for (i in 0 until toggleSwitchesContainer!!.childCount) {
      val toggleSwitchButton = ToggleSwitchButton(
        toggleSwitchesContainer!!.getChildAt(i)
      )
      toggleSwitchButton.getTextView().setPadding(left, top, right, bottom)
    }
  }

  // **************** UTILS *****************
  private fun dp2px(context: Context, dp: Float): Float {
    val resources: Resources = context.resources
    val metrics: DisplayMetrics = resources.displayMetrics
    return dp * (metrics.densityDpi / 160f)
  }

  private fun isFirst(toggleSwitchButton: ToggleSwitchButton): Boolean {
    return toggleSwitchesContainer!!.indexOfChild(toggleSwitchButton.getView()) == 0
  }

  private fun isLast(toggleSwitchButton: ToggleSwitchButton): Boolean {
    val lastPosition = toggleSwitchesContainer!!.childCount - 1
    return toggleSwitchesContainer!!.indexOfChild(toggleSwitchButton.getView()) == lastPosition
  }

  init {
    if (attrs != null) {
      val attributes: TypedArray =
        context.obtainStyledAttributes(attrs, R.styleable.ToggleSwitchOptions, 0, 0)
      try {
        mContext = context
        mInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        mInflater?.inflate(R.layout.view_widget_toggle_switch, this, true)
        toggleSwitchesContainer =
          findViewById<View>(R.id.linearLayout_toggleContainer) as LinearLayout
        val centerToggleText =
          attributes.getString(R.styleable.ToggleSwitchOptions_textToggleCenter)
        val leftToggleText = attributes.getString(R.styleable.ToggleSwitchOptions_textToggleLeft)
        val rightToggleText = attributes.getString(R.styleable.ToggleSwitchOptions_textToggleRight)
        activeBgColor = attributes.getColor(
          R.styleable.ToggleSwitchOptions_activeBgColor,
          ContextCompat.getColor(context, Default.ACTIVE_BG_COLOR)
        )
        activeTextColor = attributes.getColor(
          R.styleable.ToggleSwitchOptions_activeTextColor,
          ContextCompat.getColor(context, Default.ACTIVE_TEXT_COLOR)
        )
        inactiveBgColor = attributes.getColor(
          R.styleable.ToggleSwitchOptions_inactiveBgColor,
          ContextCompat.getColor(context, Default.INACTIVE_BG_COLOR)
        )
        inactiveTextColor = attributes.getColor(
          R.styleable.ToggleSwitchOptions_inactiveTextColor,
          ContextCompat.getColor(context, Default.INACTIVE_TEXT_COLOR)
        )
        separatorColor = attributes.getColor(
          R.styleable.ToggleSwitchOptions_separatorColor,
          ContextCompat.getColor(context, Default.SEPARATOR_COLOR)
        )
        textSize = attributes.getDimensionPixelSize(
          R.styleable.ToggleSwitchOptions_android_textSize,
          dp2px(context, Default.TEXT_SIZE).toInt()
        )
        toggleWidth = attributes.getDimension(
          R.styleable.ToggleSwitchOptions_toggleWidth,
          dp2px(getContext(), Default.TOGGLE_WIDTH)
        )
        toggleHeight = attributes.getDimension(
          R.styleable.ToggleSwitchOptions_toggleHeight,
          dp2px(getContext(), Default.TOGGLE_HEIGHT)
        )
        cornerRadius = attributes.getDimensionPixelSize(
          R.styleable.ToggleSwitchOptions_cornerRadius,
          dp2px(context, Default.CORNER_RADIUS_DP.toFloat()).toInt()
        ).toFloat()
        if (leftToggleText != null && !leftToggleText.isEmpty() && rightToggleText != null && !rightToggleText.isEmpty()) {
          mLabels = ArrayList()
          mLabels!!.add(leftToggleText)
          if (centerToggleText != null && !centerToggleText.isEmpty()) mLabels!!.add(
            centerToggleText
          )
          mLabels!!.add(rightToggleText)
          buildToggleButtons()
        }
      } finally {
        attributes.recycle()
      }
    }
  }
}