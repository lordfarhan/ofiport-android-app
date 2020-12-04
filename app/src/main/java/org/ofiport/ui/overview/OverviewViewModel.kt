package org.ofiport.ui.overview

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import org.ofiport.data.Transaction
import org.ofiport.data.TransactionRepository
import org.ofiport.helper.DateTimeHelper

/**
 * @author farhan
 * created at at 12:22 on 17/10/2020.
 */
class OverviewViewModel(application: Application) : AndroidViewModel(application) {
  private var repository: TransactionRepository = TransactionRepository(application)
  private var transactions = repository.get()
  var currMonth = DateTimeHelper.getMonthFromTimestamp(DateTimeHelper.getCurrentTimestamp())
  var currYear = DateTimeHelper.getYearFromTimestamp(DateTimeHelper.getCurrentTimestamp())

  fun get(): LiveData<List<Transaction>> = transactions

  fun getAllTransactions(transactions: List<Transaction>) = transactions.sortedBy { it.date }

  fun getTransactionsInMonth(
    transactions: List<Transaction>,
  ): List<Transaction> {
    val transactionsInMonth: ArrayList<Transaction> = ArrayList()
    for (transaction in transactions) {
      if (currMonth.toInt() == DateTimeHelper.getMonthFromTimestamp(transaction.date)
          .toInt() && currYear.toInt() == DateTimeHelper.getYearFromTimestamp(
          transaction.date
        ).toInt()
      ) {
        transactionsInMonth.add(transaction)
      }
    }
    return transactionsInMonth.sortedBy { it.date }
  }

  fun nextMonth() {
    if (currMonth != "12") {
      currMonth = (currMonth.toInt() + 1).toString()
    } else {
      currMonth = "1"
      currYear = (currYear.toInt() + 1).toString()
    }
  }

  fun prevMonth() {
    if (currMonth != "1") {
      currMonth = (currMonth.toInt() - 1).toString()
    } else {
      currMonth = "12"
      currYear = (currYear.toInt() - 1).toString()
    }
  }

  fun getBalance(transactions: List<Transaction>, index: Int): Long {
    var balance = 0L
    for (i in 0..index) {
      if (transactions[i].type == 0) {
        balance -= transactions[i].amount
      } else {
        balance += transactions[i].amount
      }
    }
    return balance
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