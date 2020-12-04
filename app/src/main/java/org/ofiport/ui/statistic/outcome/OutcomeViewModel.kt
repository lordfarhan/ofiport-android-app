package org.ofiport.ui.statistic.outcome

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import org.ofiport.data.TransactionRepository

/**
 * @author farhan
 * created at at 20:07 on 01/11/2020.
 */
class OutcomeViewModel(application: Application) : AndroidViewModel(application) {
  private val repository = TransactionRepository(application)
  val outcomes = repository.getOutcome()
}