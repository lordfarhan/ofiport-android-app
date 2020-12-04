package org.ofiport.ui.statistic.income

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import org.ofiport.databinding.FragmentIncomeBinding

/**
 * @author farhan
 * created at at 12:41 on 01/11/2020.
 */
class IncomeFragment : Fragment() {
  companion object {
    fun newInstance(): IncomeFragment {
      val args = Bundle()

      val fragment = IncomeFragment()
      fragment.arguments = args
      return fragment
    }
  }

  private lateinit var binding: FragmentIncomeBinding
  private lateinit var viewModel: IncomeViewModel
  private lateinit var adapter: IncomeAdapter

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    binding = FragmentIncomeBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    viewModel = ViewModelProvider(requireActivity())[IncomeViewModel::class.java]

    adapter = IncomeAdapter(requireContext())
    binding.recyclerViewIncome.adapter = adapter
    viewModel.incomes.observe(requireActivity(), {
      adapter.submitList(it)
    })
  }
}