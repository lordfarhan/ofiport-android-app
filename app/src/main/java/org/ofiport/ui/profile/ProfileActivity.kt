package org.ofiport.ui.profile

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import org.ofiport.R
import org.ofiport.databinding.ActivityProfileBinding
import org.ofiport.util.*

/**
 * @author farhan
 * created at at 14:03 on 16/12/20.
 */
class ProfileActivity : AppCompatActivity() {
  private lateinit var binding: ActivityProfileBinding
  private lateinit var sharedPreferenceUtil: SharedPreferenceUtils

  companion object {
    const val INSERT_DATA = 0
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding = DataBindingUtil.setContentView(this, R.layout.activity_profile)
    sharedPreferenceUtil = SharedPreferenceUtils(this)

    checkIsProfileIncomplete()

    binding.apply {
      imageViewActionBack.setOnClickListener { onBackPressed() }
      imageViewActionEdit.setOnClickListener {
        startActivityForResult(
          Intent(this@ProfileActivity, ProfileEditActivity::class.java),
          INSERT_DATA
        )
      }
    }

    subscribeInterface()
  }

  private fun checkIsProfileIncomplete() {
    if (sharedPreferenceUtil.getString(OFFICE_NAME).isEmpty()) {
      startActivityForResult(
        Intent(this@ProfileActivity, ProfileEditActivity::class.java), INSERT_DATA
      )
    }
  }

  private fun subscribeInterface() {
    binding.apply {
      textViewOfficeName.text = sharedPreferenceUtil.getString(OFFICE_NAME)
      textViewNumOfEmployee.text = sharedPreferenceUtil.getString(OFFICE_EMPLOYEE)
      textViewEmailAddress.text = sharedPreferenceUtil.getString(OFFICE_EMAIL)
      textViewOfficeAddress.text = sharedPreferenceUtil.getString(OFFICE_ADDRESS)
      textViewTypeOfBusiness.text = sharedPreferenceUtil.getString(OFFICE_INDUSTRY_TYPE)
    }
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (requestCode == INSERT_DATA && resultCode == RESULT_OK) {
      sharedPreferenceUtil.putString(
        OFFICE_NAME, data?.getStringExtra(OFFICE_NAME)!!
      )
      sharedPreferenceUtil.putString(
        OFFICE_EMPLOYEE, data.getStringExtra(OFFICE_EMPLOYEE)!!
      )
      sharedPreferenceUtil.putString(
        OFFICE_EMAIL, data.getStringExtra(OFFICE_EMAIL)!!
      )
      sharedPreferenceUtil.putString(
        OFFICE_ADDRESS, data.getStringExtra(OFFICE_ADDRESS)!!
      )
      sharedPreferenceUtil.putString(
        OFFICE_INDUSTRY_TYPE, data.getStringExtra(OFFICE_INDUSTRY_TYPE)!!
      )

      subscribeInterface()
    }
  }
}