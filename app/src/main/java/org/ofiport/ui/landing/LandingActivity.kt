package org.ofiport.ui.landing

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import org.ofiport.R
import org.ofiport.data.Transaction
import org.ofiport.databinding.ActivityLandingBinding
import org.ofiport.helper.DateTimeHelper
import org.ofiport.helper.ItemDecorationHelper
import org.ofiport.ui.overview.OverviewActivity
import org.ofiport.ui.statistic.StatisticActivity
import org.ofiport.ui.transaction.TransactionActivity
import java.text.NumberFormat
import java.util.*


/**
 * @author farhan
 * created at at 9:17 on 13/10/2020.
 */
class LandingActivity : AppCompatActivity() {

  private lateinit var binding: ActivityLandingBinding
  private lateinit var viewModel: LandingViewModel
  private lateinit var adapter: LandingAdapter

  companion object {
    const val CREATE_REQUEST = 0
    const val UPDATE_REQUEST = 1

    const val ID = "org.ofiport.data.Transaction.id"
    const val TYPE = "org.ofiport.data.Transaction.type"
    const val DESCRIPTION = "org.ofiport.data.Transaction.description"
    const val AMOUNT = "org.ofiport.data.Transaction.amount"
    const val DATE = "org.ofiport.data.Transaction.date"

    const val ACTION = "action"
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding = DataBindingUtil.setContentView(this, R.layout.activity_landing)
    viewModel = ViewModelProvider(this).get(LandingViewModel::class.java)

    sayGreeting()

    binding.apply {
      cardViewOverviewReport.setOnClickListener {
        startActivity(
          Intent(
            this@LandingActivity,
            OverviewActivity::class.java
          )
        )
      }
      cardViewCreateReport.setOnClickListener {
        startActivityForResult(
          Intent(
            this@LandingActivity,
            TransactionActivity::class.java
          ),
          CREATE_REQUEST
        )
      }

      cardViewIncome.setOnClickListener {
        startActivity(
          Intent(
            this@LandingActivity,
            StatisticActivity::class.java
          )
        )
      }
      cardViewOutcome.setOnClickListener {
        startActivity(
          Intent(
            this@LandingActivity,
            StatisticActivity::class.java
          )
        )
      }
      adapter = LandingAdapter(this@LandingActivity)
      recyclerViewRecentTransaction.adapter = adapter
      recyclerViewRecentTransaction.addItemDecoration(
        ItemDecorationHelper(
          this@LandingActivity,
          R.dimen.medium_space,
          ItemDecorationHelper.BOTTOM
        )
      )
    }

    viewModel.get().observe(this, {
      if (it.size > 10)
        adapter.submitList(it.take(10))
      else
        adapter.submitList(it)
      var income = 0L
      var outcome = 0L
      for (transaction in it) {
        if (transaction.type == 0) {
          outcome += transaction.amount
        } else {
          income += transaction.amount
        }
      }
      binding.apply {
        textViewLandingBalance.text =
          String.format("Rp. %s", NumberFormat.getInstance().format(income - outcome))
      }
    })

    viewModel.getIncomeTransactions().observe(this, {
      val incomeInMonth = viewModel.getTransactionsInMonth(it)
      binding.apply {
        textViewIncome.text = String.format(
          "Rp. %s",
          NumberFormat.getInstance().format(viewModel.getCurrencyAmount(incomeInMonth))
        )
        textViewIncomeMonth.text = String.format(
          "%s %s",
          DateTimeHelper.getMonth(
            DateTimeHelper.getMonthFromTimestamp(DateTimeHelper.getCurrentTimestamp()).toInt()
          ),
          DateTimeHelper.getYearFromTimestamp(DateTimeHelper.getCurrentTimestamp())
        )
      }
    })

    viewModel.getOutcomeTransactions().observe(this, {
      val outcomeInMonth = viewModel.getTransactionsInMonth(it)
      binding.apply {
        textViewOutcome.text = String.format(
          "Rp. %s",
          NumberFormat.getInstance().format(viewModel.getCurrencyAmount(outcomeInMonth))
        )
        textViewOutcomeMonth.text = String.format(
          "%s %s",
          DateTimeHelper.getMonth(
            DateTimeHelper.getMonthFromTimestamp(DateTimeHelper.getCurrentTimestamp()).toInt()
          ),
          DateTimeHelper.getYearFromTimestamp(DateTimeHelper.getCurrentTimestamp())
        )
      }
    })
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)

    if (requestCode == CREATE_REQUEST && resultCode == RESULT_OK) {
      val transaction = Transaction(
        type = data!!.getIntExtra(TYPE, 0),
        description = data.getStringExtra(DESCRIPTION)!!,
        date = data.getStringExtra(DATE)!!,
        amount = data.getLongExtra(AMOUNT, 0),
        createdAt = DateTimeHelper.getCurrentTimestamp(),
        updatedAt = DateTimeHelper.getCurrentTimestamp()
      )
      viewModel.insert(transaction)
      Toast.makeText(this, "Berhasil menyimpan", Toast.LENGTH_SHORT).show()
    } else if (requestCode == UPDATE_REQUEST && resultCode == RESULT_OK) {
      val id = data!!.getLongExtra(ID, -1)
      val transaction = Transaction(
        type = data.getIntExtra(TYPE, 0),
        description = data.getStringExtra(DESCRIPTION)!!,
        date = data.getStringExtra(DATE)!!,
        amount = data.getLongExtra(AMOUNT, 0),
        createdAt = DateTimeHelper.getCurrentTimestamp(),
        updatedAt = DateTimeHelper.getCurrentTimestamp()
      )
      transaction.id = id
      if (id == -1L) {
        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
      } else {
        if (data.getStringExtra(ACTION) == "delete") {
          viewModel.delete(transaction)
          Toast.makeText(this, "Berhasil menghapus", Toast.LENGTH_SHORT).show()
        } else {
          viewModel.update(transaction)
          Toast.makeText(this, "Berhasil memperbarui", Toast.LENGTH_SHORT).show()
        }
      }
    }
  }

  private fun sayGreeting() {
    val calendar: Calendar = Calendar.getInstance()
    val hour: Int = calendar.get(Calendar.HOUR_OF_DAY)
    binding.apply {
      when (hour) {
        in 4..9 -> {
          imageViewLandingGreeting.setImageResource(R.drawable.img_sky_morning)
        }
        in 10..13 -> {
          imageViewLandingGreeting.setImageResource(R.drawable.img_sky_afternoon)
        }
        in 14..17 -> {
          imageViewLandingGreeting.setImageResource(R.drawable.img_sky_without_sun)
        }
        else -> {
          imageViewLandingGreeting.setImageResource(R.drawable.img_sky_night)
        }
      }
    }
  }
}