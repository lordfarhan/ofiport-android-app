package org.ofiport.ui.statistic.income

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.ofiport.R
import org.ofiport.data.Transaction
import org.ofiport.databinding.ItemTransactionBinding
import org.ofiport.helper.DateTimeHelper
import java.text.NumberFormat

/**
 * @author farhan
 * created at at 14:18 on 17/10/2020.
 */
class IncomeAdapter(private val context: Context) :
  ListAdapter<Transaction, IncomeAdapter.ViewHolder>(DiffCallback()) {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    return ViewHolder(
      ItemTransactionBinding.inflate(LayoutInflater.from(parent.context), parent, false),
      context
    )
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val transaction = getItem(position)
    holder.bind(transaction)
  }

  class ViewHolder(
    private val binding: ItemTransactionBinding,
    private val context: Context
  ) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(transaction: Transaction) {
      binding.apply {
        if (transaction.type == 0) {
          imageViewTypeTransactionItem.setImageResource(R.drawable.ic_lineofficestaff)
        } else {
          imageViewTypeTransactionItem.setImageResource(R.drawable.ic_lineinvestation)
        }

        textViewDescriptionTransactionItem.text = transaction.description
        textViewAmountTransactionItem.text =
          String.format("Rp. %s", NumberFormat.getInstance().format(transaction.amount))
        textViewDateTransactionItem.text = String.format(
          "%s %s %s",
          DateTimeHelper.getDateFromTimestamp(transaction.date),
          DateTimeHelper.getMonthStringFromTimeStamp(transaction.date),
          DateTimeHelper.getYearFromTimestamp(transaction.date)
        )

        cardViewContainerTransactionItem.setOnClickListener {

        }
      }
    }
  }

  class DiffCallback : DiffUtil.ItemCallback<Transaction>() {
    override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
      return newItem == oldItem
    }

    override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
      return newItem.id == oldItem.id
    }

  }
}