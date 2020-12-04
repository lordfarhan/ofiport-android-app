package org.ofiport.ui.statistic

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import org.ofiport.R
import org.ofiport.databinding.ActivityStatisticBinding
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_statistic.*

/**
 * @author farhan
 * created at at 12:03 on 01/11/2020.
 */
class StatisticActivity : AppCompatActivity() {

  private lateinit var binding: ActivityStatisticBinding
  private lateinit var adapter: StatisticAdapter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = DataBindingUtil.setContentView(this, R.layout.activity_statistic)

    binding.imageViewActionBack.setOnClickListener { onBackPressed() }

    adapter = StatisticAdapter(this)
    binding.viewPager2Statistic.adapter = adapter
    binding.viewPager2Statistic.offscreenPageLimit = 2

    TabLayoutMediator(binding.tabLayoutStatistic, viewPager2_statistic) { tab, position ->
      when (position) {
        0 -> tab.text = resources.getString(R.string.landing_income)
        1 -> tab.text = resources.getString(R.string.landing_outcome)
      }
    }.attach()
  }
}