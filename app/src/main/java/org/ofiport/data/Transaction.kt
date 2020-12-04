package org.ofiport.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

/**
 * @author farhan
 * created at at 13:44 on 15/10/2020.
 */
@Entity(tableName = "transactions")
data class Transaction(
  @PrimaryKey(autoGenerate = true) var id: Long = 0,
  var type: Int,
  var description: String,
  var date: String,
  var amount: Long,
  var createdAt: String,
  var updatedAt: String
) : Serializable {
}