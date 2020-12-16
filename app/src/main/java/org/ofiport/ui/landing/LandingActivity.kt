package org.ofiport.ui.landing

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.dialog_incomplete_profile.view.*
import org.ofiport.R
import org.ofiport.data.Transaction
import org.ofiport.databinding.ActivityLandingBinding
import org.ofiport.helper.DateTimeHelper
import org.ofiport.helper.ItemDecorationHelper
import org.ofiport.ui.overview.OverviewActivity
import org.ofiport.ui.profile.ProfileActivity
import org.ofiport.ui.statistic.StatisticActivity
import org.ofiport.ui.transaction.TransactionActivity
import org.ofiport.util.OFFICE_NAME
import org.ofiport.util.SharedPreferenceUtils
import java.util.*


/**
 * @author farhan
 * created at at 9:17 on 13/10/2020.
 */
class LandingActivity : AppCompatActivity() {

  private lateinit var binding: ActivityLandingBinding
  private lateinit var viewModel: LandingViewModel
  private lateinit var adapter: LandingAdapter
  private lateinit var sharedPreferenceUtil: SharedPreferenceUtils

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
    sharedPreferenceUtil = SharedPreferenceUtils(this)

    checkIsProfileIncomplete()

    binding.apply {
      imageViewActionProfile.setOnClickListener {
        startActivity(
          Intent(
            this@LandingActivity,
            ProfileActivity::class.java
          )
        )
      }

      cardViewAddIncome.setOnClickListener {
        startActivityForResult(
          Intent(
            this@LandingActivity,
            TransactionActivity::class.java
          ).putExtra(TransactionActivity.TYPE, TransactionActivity.INCOME),
          CREATE_REQUEST
        )
      }

      cardViewAddOutcome.setOnClickListener {
        startActivityForResult(
          Intent(
            this@LandingActivity,
            TransactionActivity::class.java
          ).putExtra(TransactionActivity.TYPE, TransactionActivity.OUTCOME),
          CREATE_REQUEST
        )
      }

      cardViewOpenStatistics.setOnClickListener {
        startActivity(
          Intent(
            this@LandingActivity,
            StatisticActivity::class.java
          )
        )
      }

      cardViewOpenMonthReport.setOnClickListener {
        startActivity(
          Intent(
            this@LandingActivity,
            OverviewActivity::class.java
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

    subscribeInterface()
  }

  override fun onRestart() {
    super.onRestart()
    checkIsProfileIncomplete()
  }

  override fun onResume() {
    super.onResume()
    checkIsProfileIncomplete()
  }

  private fun checkIsProfileIncomplete() {
    if (sharedPreferenceUtil.getString(OFFICE_NAME).isEmpty()) {
      showIncompleteProfileDialog()
    }
  }

  private fun showIncompleteProfileDialog() {
    val builder = AlertDialog.Builder(this)
    val dialogView = layoutInflater.inflate(R.layout.dialog_incomplete_profile, null)
    builder.setView(dialogView)
    val alertDialog = builder.show()
    alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialogView.materialButton_actionOpenProfile.setOnClickListener {
      startActivity(
        Intent(this, ProfileActivity::class.java)
      )
    }
    alertDialog.setOnDismissListener {
      startActivity(
        Intent(this, ProfileActivity::class.java)
      )
    }
  }

  private fun subscribeInterface() {
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
          Toast.makeText(this, "Successfully deleted", Toast.LENGTH_SHORT).show()
        } else {
          viewModel.update(transaction)
          Toast.makeText(this, "Successfully updated", Toast.LENGTH_SHORT).show()
        }
      }
    }
  }

}

