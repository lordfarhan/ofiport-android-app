package org.ofiport.ui.profile

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import org.ofiport.R
import org.ofiport.databinding.ActivityProfileEditBinding
import org.ofiport.util.*

/**
 * @author farhan
 * created at at 14:05 on 16/12/20.
 */
class ProfileEditActivity : AppCompatActivity() {
  private lateinit var binding: ActivityProfileEditBinding
  private lateinit var sharedPreferenceUtils: SharedPreferenceUtils

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding = DataBindingUtil.setContentView(this, R.layout.activity_profile_edit)
    sharedPreferenceUtils = SharedPreferenceUtils(this)

    binding.apply {
      imageViewActionBack.setOnClickListener { onBackPressed() }

      populateField()

      cardViewActionSave.setOnClickListener {
        val officeName = editTextOfficeName.text.toString()
        val officeEmail = editTextOfficeEmail.text.toString()
        val officeIndustryType = editTextIndustryType.text.toString()
        val officeEmployee = editTextEmployeeAmount.text.toString()
        val officeAddress = editTextOfficeAddress.text.toString()

        if (officeName.isNotEmpty()
          && officeEmail.isNotEmpty()
          && officeIndustryType.isNotEmpty()
          && officeEmployee.isNotEmpty()
          && officeAddress.isNotEmpty()
        ) {
          save(officeName, officeEmail, officeEmployee, officeIndustryType, officeAddress)
        } else {
          Toast.makeText(
            this@ProfileEditActivity,
            "Complete all field, please!",
            Toast.LENGTH_SHORT
          ).show()
        }
      }
    }
  }

  override fun onRestoreInstanceState(savedInstanceState: Bundle) {
    super.onRestoreInstanceState(savedInstanceState)
    populateField()
  }

  private fun populateField() {
    binding.apply {
      editTextOfficeName.setText(sharedPreferenceUtils.getString(OFFICE_NAME))
      editTextOfficeEmail.setText(sharedPreferenceUtils.getString(OFFICE_EMAIL))
      editTextEmployeeAmount.setText(sharedPreferenceUtils.getString(OFFICE_EMPLOYEE))
      editTextIndustryType.setText(sharedPreferenceUtils.getString(OFFICE_INDUSTRY_TYPE))
      editTextOfficeAddress.setText(sharedPreferenceUtils.getString(OFFICE_ADDRESS))
    }
  }

  private fun save(
    officeName: String,
    officeEmail: String,
    officeEmployee: String,
    officeIndustryType: String,
    officeAddress: String,
  ) {
    val data = Intent().apply {
      putExtra(OFFICE_NAME, officeName)
      putExtra(OFFICE_EMAIL, officeEmail)
      putExtra(OFFICE_EMPLOYEE, officeEmployee)
      putExtra(OFFICE_INDUSTRY_TYPE, officeIndustryType)
      putExtra(OFFICE_ADDRESS, officeAddress)
    }
    setResult(RESULT_OK, data)
    finish()
  }
}