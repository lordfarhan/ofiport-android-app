package org.ofiport.ui.landing

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import org.ofiport.data.Transaction
import org.ofiport.data.TransactionRepository
import org.ofiport.helper.DateTimeHelper

/**
 * @author farhan
 * created at at 9:42 on 16/10/2020.
 */
class LandingViewModel(application: Application) : AndroidViewModel(application) {
  private var repository: TransactionRepository = TransactionRepository(application)
  private var recentTransactions = repository.get()
  private var incomeTransaction = repository.getIncome()
  private var outcomeTransaction = repository.getOutcome()

  fun get(): LiveData<List<Transaction>> = recentTransactions

  fun getIncomeTransactions(): LiveData<List<Transaction>> = incomeTransaction

  fun getOutcomeTransactions(): LiveData<List<Transaction>> = outcomeTransaction

  fun getTransactionsInMonth(transactions: List<Transaction>): ArrayList<Transaction> {
    val transactionsInMonth: ArrayList<Transaction> = ArrayList()
    for (transaction in transactions) {
      if (DateTimeHelper.getMonthFromTimestamp(DateTimeHelper.getCurrentTimestamp())
          .toInt() == DateTimeHelper.getMonthFromTimestamp(
          transaction.date
        ).toInt()
        && DateTimeHelper.getYearFromTimestamp(DateTimeHelper.getCurrentTimestamp())
          .toInt() == DateTimeHelper.getYearFromTimestamp(
          transaction.date
        ).toInt()
      ) {
        transactionsInMonth.add(transaction)
      }
    }
    return transactionsInMonth
  }

  fun getCurrencyAmount(transactions: List<Transaction>): Long {
    var amount = 0L
    for (transaction in transactions) {
      amount += transaction.amount
    }
    return amount
  }

  fun insert(transaction: Transaction) {
    repository.insert(transaction)
  }

  fun update(transaction: Transaction) {
    repository.update(transaction)
  }

  fun delete(transaction: Transaction) {
    repository.delete(transaction)
  }

  fun deleteAll() {
    repository.deleteAll()
  }
}