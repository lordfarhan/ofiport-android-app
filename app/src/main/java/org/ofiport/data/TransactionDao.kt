package org.ofiport.data

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * @author farhan
 * created at at 13:51 on 15/10/2020.
 */
@Dao
interface TransactionDao {
  @Insert
  fun insert(transaction: Transaction)

  @Update
  fun update(transaction: Transaction)

  @Delete
  fun delete(transaction: Transaction)

  @Query("DELETE FROM transactions")
  fun deleteAll()

  @Query("SELECT * FROM transactions WHERE type = 1")
  fun getIncome(): LiveData<List<Transaction>>

  @Query("SELECT * FROM transactions WHERE type = 0")
  fun getOutcome(): LiveData<List<Transaction>>

  @Query("SELECT * FROM transactions")
  fun get(): LiveData<List<Transaction>>
}