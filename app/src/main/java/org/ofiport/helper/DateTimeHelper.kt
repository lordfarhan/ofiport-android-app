package org.ofiport.helper

import android.annotation.SuppressLint
import org.ofiport.App
import org.ofiport.R
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * @author farhan
 * created at at 22:08 on 13/06/2020.
 */
class DateTimeHelper {
  companion object {
    const val HMS = "%02d:%02d:%02d"

    /**
     * get current timestamp
     */
    @SuppressLint("SimpleDateFormat")
    fun getCurrentTimestamp(): String {
      val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
      return sdf.format(Date())
    }

    /**
     * get date formatted from timestamp
     * @param timestamp in String
     * @return date/month/year in String (ex: 12/05/2000)
     */
    fun getDateFormattedFromTimestamp(timestamp: String, appendix: String = "/"): String {
      val dateString = timestamp.split(" ")[0]
      val date = dateString.split("-")[2]
      val month = dateString.split("-")[1]
      val year = dateString.split("-")[0]
      return "$date$appendix$month$appendix$year"
    }

    /**
     * get date from timestamp
     * @param timestamp in String
     * @return date in String (ex: 31)
     * */
    fun getDateFromTimestamp(timestamp: String): String {
      val dateString = timestamp.split(" ")[0]
      return dateString.split("-")[2]
    }

    /**
     * get month in number from timestamp
     * @param timestamp in String
     * @return month in number as String (ex: 05)
     */
    fun getMonthFromTimestamp(timestamp: String): String {
      val dateString = timestamp.split(" ")[0]
      return dateString.split("-")[1]
    }

    /**
     * Get month in word from timestamp
     * @param timestamp in String
     * @return month in word as String (ex: May)
     */
    fun getMonthStringFromTimeStamp(timestamp: String): String {
      val dateString = timestamp.split(" ")[0]
      return getMonth(dateString.split("-")[1].toInt())
    }

    /**
     * Get year from timestamp
     * @param timestamp in String
     * @return year as String (ex: 2000)
     */
    fun getYearFromTimestamp(timestamp: String): String {
      val dateString = timestamp.split(" ")[0]
      return dateString.split("-")[0]
    }

    /**
     * Get time in HH:mm format from timestamp
     * @param timestamp: String
     * @return time as String (ex: 02:00)
     */
    fun getTimeFromTimestamp(timestamp: String): String {
      val timeString = timestamp.split(" ")[1]
      return String.format("%s:%s", timeString.split(":")[0], timeString.split(":")[1])
    }

    /**
     * Get time in HHmm format from timestamp
     * @param timestamp: String
     * @return time in number as Integer (ex: 0200)
     */
    fun getTimeIntFromTimestamp(timestamp: String): Int {
      val timeString = timestamp.split(" ")[1]
      return String.format("%s%s", timeString.split(":")[0], timeString.split(":")[1]).toInt()
    }

    fun getTimestampInt(timestamp: String): Long {
      val timeString = timestamp.split(" ")
      return String.format(
        "%s%s%s%s%s%s",
        timeString[0].split("-")[0],
        timeString[0].split("-")[1],
        timeString[0].split("-")[2],
        timeString[1].split(":")[0],
        timeString[1].split(":")[1],
        timeString[1].split(":")[2]
      ).toLong()
    }

    /**
     * Parse milliseconds to time format
     * @param millis: Long
     * @param format: String
     * @return time: String
     */
    fun parseMillis(millis: Long, format: String): String = String.format(
      Locale.US,
      format, TimeUnit.MILLISECONDS.toHours(millis),
      TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
      TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1)
    )

    fun getMonth(month: Int): String =
      when (month) {
        1 -> App.getContext().resources.getString(R.string.january)
        2 -> App.getContext().resources.getString(R.string.february)
        3 -> App.getContext().resources.getString(R.string.march)
        4 -> App.getContext().resources.getString(R.string.april)
        5 -> App.getContext().resources.getString(R.string.may)
        6 -> App.getContext().resources.getString(R.string.june)
        7 -> App.getContext().resources.getString(R.string.july)
        8 -> App.getContext().resources.getString(R.string.august)
        9 -> App.getContext().resources.getString(R.string.september)
        10 -> App.getContext().resources.getString(R.string.october)
        11 -> App.getContext().resources.getString(R.string.november)
        12 -> App.getContext().resources.getString(R.string.december)
        else -> App.getContext().resources.getString(R.string.january)
      }
  }
}