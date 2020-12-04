package org.ofiport.ui.transaction

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import org.ofiport.R
import org.ofiport.data.Transaction
import org.ofiport.databinding.ActivityTransactionBinding
import org.ofiport.helper.DateTimeHelper
import org.ofiport.ui.landing.LandingActivity

/**
 * @author farhan
 * created at at 13:00 on 15/10/2020.
 */
class TransactionActivity : AppCompatActivity() {

  private lateinit var binding: ActivityTransactionBinding
  private lateinit var transaction: Transaction

  companion object {
    const val TRANSACTION = "org.ofiport.data.Transaction"
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding = DataBindingUtil.setContentView(this, R.layout.activity_transaction)
    binding.imageViewActionBack.setOnClickListener { onBackPressed() }

    if (intent.hasExtra(TRANSACTION)) {
      transaction = intent.getSerializableExtra(TRANSACTION) as Transaction
      binding.apply {
        cardViewActionDelete.visibility = View.VISIBLE
        textViewActionBarTitle.text = resources.getString(R.string.transaction_update)
        textViewActionSave.text = resources.getString(R.string.action_update)

        toggleSwitchType.setCheckedTogglePosition(transaction.type)
        editTextTransactionDescription.setText(transaction.description)
        editTextTransactionAmount.setText(transaction.amount.toString())
        datePickerTransactionDater.updateDate(
          DateTimeHelper.getYearFromTimestamp(transaction.date).toInt(),
          DateTimeHelper.getMonthFromTimestamp(transaction.date).toInt() - 1,
          DateTimeHelper.getDateFromTimestamp(transaction.date).toInt()
        )

        cardViewActionDelete.setOnClickListener {
          val type = toggleSwitchType.getCheckedTogglePosition()
          val description = editTextTransactionDescription.text.toString()
          val amount = editTextTransactionAmount.text.toString()
          val year = datePickerTransactionDater.year
          val month = datePickerTransactionDater.month + 1
          val date = datePickerTransactionDater.dayOfMonth
          if (description.isNotEmpty() && amount.isNotEmpty()) {
            submit(
              type,
              description,
              amount.toLong(),
              year.toString(),
              month.toString(),
              date.toString(),
              "delete"
            )
          }
        }
      }
    } else {
      binding.apply {
        cardViewActionDelete.visibility = View.GONE
        textViewActionBarTitle.text = resources.getString(R.string.transaction_create)
        textViewActionSave.text = resources.getString(R.string.action_save)
      }
    }

    binding.apply {
      cardViewActionSave.setOnClickListener {
        val type = toggleSwitchType.getCheckedTogglePosition()
        val description = editTextTransactionDescription.text.toString()
        val amount = editTextTransactionAmount.text.toString()
        val year = datePickerTransactionDater.year
        val month = datePickerTransactionDater.month + 1
        val date = datePickerTransactionDater.dayOfMonth
        if (description.isNotEmpty() && amount.isNotEmpty()) {
          submit(
            type,
            description,
            amount.toLong(),
            year.toString(),
            month.toString(),
            date.toString(),
            "update"
          )
        }
      }
    }
  }

  private fun submit(
    type: Int,
    description: String,
    amount: Long,
    year: String,
    month: String,
    date: String,
    action: String
  ) {
    var mMonth = month
    var mDate = date
    if (month.toInt() < 10) mMonth = "0$month"
    if (date.toInt() < 10) mDate = "0$date"
    val dateTimestamp = "$year-$mMonth-$mDate 00:00:00"

    val data = Intent().apply {
      putExtra(LandingActivity.TYPE, type)
      putExtra(LandingActivity.DESCRIPTION, description)
      putExtra(LandingActivity.AMOUNT, amount)
      putExtra(LandingActivity.DATE, dateTimestamp)
      if (intent.hasExtra(TRANSACTION)) {
        putExtra(LandingActivity.ID, transaction.id)
      }
      putExtra(LandingActivity.ACTION, action)
    }

    setResult(RESULT_OK, data)
    finish()
  }
}