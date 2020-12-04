package org.ofiport.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * @author farhan
 * created at at 13:55 on 15/10/2020.
 */
@Database(entities = [Transaction::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

  abstract fun transactionDao(): TransactionDao

  companion object {
    private var instance: AppDatabase? = null

    fun getInstance(context: Context): AppDatabase? {
      if (instance == null) {
        synchronized(AppDatabase::class) {
          instance = Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java, "pemudamu_db"
          )
//            .fallbackToDestructiveMigration()
            .build()
        }
      }
      return instance
    }

    fun destroyInstance() {
      instance = null
    }

  }
}