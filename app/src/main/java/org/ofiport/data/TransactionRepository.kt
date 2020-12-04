package org.ofiport.data

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData

/**
 * @author farhan
 * created at at 14:00 on 15/10/2020.
 */
class TransactionRepository(application: Application) {

  private var dao: TransactionDao
  private var transactions: LiveData<List<Transaction>>

  init {
    val database: AppDatabase = AppDatabase.getInstance(
      application.applicationContext
    )!!
    dao = database.transactionDao()
    transactions = dao.get()
  }

  fun get() = transactions

  fun getIncome() = dao.getIncome()

  fun getOutcome() = dao.getOutcome()

  fun insert(transaction: Transaction) {
    val insertAsyncTask = InsertAsyncTask(dao).execute(transaction)
  }

  fun update(transaction: Transaction) {
    val updateAsyncTask = UpdateAsyncTask(dao).execute(transaction)
  }

  fun delete(transaction: Transaction) {
    val deleteAsyncTask = DeleteAsyncTask(dao).execute(transaction)
  }

  fun deleteAll() {
    val deleteAllAsyncTask = DeleteAllAsyncTask(dao).execute()
  }

  companion object {
    private class InsertAsyncTask(dao: TransactionDao) : AsyncTask<Transaction, Unit, Unit>() {
      val transactionDao = dao
      override fun doInBackground(vararg params: Transaction?) {
        transactionDao.insert(params[0]!!)
      }
    }

    private class UpdateAsyncTask(dao: TransactionDao) : AsyncTask<Transaction, Unit, Unit>() {
      val transactionDao = dao
      override fun doInBackground(vararg params: Transaction?) {
        transactionDao.update(params[0]!!)
      }
    }

    private class DeleteAsyncTask(dao: TransactionDao) : AsyncTask<Transaction, Unit, Unit>() {
      val transactionDao = dao
      override fun doInBackground(vararg params: Transaction?) {
        transactionDao.delete(params[0]!!)
      }
    }

    private class DeleteAllAsyncTask(dao: TransactionDao) : AsyncTask<Unit, Unit, Unit>() {
      val transactionDao = dao
      override fun doInBackground(vararg params: Unit?) {
        transactionDao.deleteAll()
      }
    }
  }
}