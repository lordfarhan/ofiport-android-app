package org.ofiport.ui.statistic.outcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import org.ofiport.databinding.FragmentOutcomeBinding

/**
 * @author farhan
 * created at at 12:43 on 01/11/2020.
 */
class OutcomeFragment : Fragment() {
  companion object {
    fun newInstance(): OutcomeFragment {
      val args = Bundle()

      val fragment = OutcomeFragment()
      fragment.arguments = args
      return fragment
    }
  }

  private lateinit var binding: FragmentOutcomeBinding
  private lateinit var viewModel: OutcomeViewModel
  private lateinit var adapter: OutcomeAdapter

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    binding = FragmentOutcomeBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    viewModel = ViewModelProvider(requireActivity())[OutcomeViewModel::class.java]

    adapter = OutcomeAdapter(requireContext())
    binding.recyclerViewOutcome.adapter = adapter
    viewModel.outcomes.observe(requireActivity(), {
      adapter.submitList(it)
    })
  }
}