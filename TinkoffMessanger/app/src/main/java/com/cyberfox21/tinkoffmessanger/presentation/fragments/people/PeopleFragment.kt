package com.cyberfox21.tinkoffmessanger.presentation.fragments.people

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.cyberfox21.tinkoffmessanger.R
import com.cyberfox21.tinkoffmessanger.databinding.FragmentPeopleBinding
import com.cyberfox21.tinkoffmessanger.presentation.fragments.profile.ProfileFragment
import com.cyberfox21.tinkoffmessanger.presentation.fragments.profile.ProfileMode

class PeopleFragment : Fragment() {

    private lateinit var viewModel: PeopleFragmentViewModel

    private var _binding: FragmentPeopleBinding? = null
    private val binding: FragmentPeopleBinding
        get() = _binding ?: throw RuntimeException("FragmentPeopleBinding = null")

    private val peopleRecyclerAdapter = PeopleRecyclerAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPeopleBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViewModel()
        setupRecyclerView()
        addListeners()
        observeViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setViewModel() {
        viewModel = ViewModelProvider(this)[PeopleFragmentViewModel::class.java]
    }

    private fun setupRecyclerView() {
        binding.peopleRecyclerView.adapter = peopleRecyclerAdapter
    }

    private fun addListeners() {
        peopleRecyclerAdapter.onPersonClickListener =
            object : PeopleRecyclerAdapter.OnPersonClickListener {
                override fun onPersonClick() {
                    showUserProfile()
                }

            }
    }

    private fun showUserProfile() {
        parentFragmentManager.beginTransaction()
            .addToBackStack(PEOPLE_FRAGMENT_NAME)
            .replace(
                R.id.main_fragment_container,
                ProfileFragment.newInstance(ProfileMode.STRANGER)
            )
            .commit()
    }

    private fun observeViewModel() {
        viewModel.peopleScreenState.observe(viewLifecycleOwner, {
            processPeopleScreenState(it)
        })
    }

    private fun processPeopleScreenState(it: UsersScreenState) {
        when (it) {
            is UsersScreenState.Result -> {
                peopleRecyclerAdapter.submitList(it.items)
                binding.pbLoading.isVisible = false
            }
            UsersScreenState.Loading -> {
                binding.pbLoading.isVisible = true
            }
            is UsersScreenState.Error -> {
                Toast.makeText(this.context, "${it.error.message}", Toast.LENGTH_SHORT).show()
                binding.pbLoading.isVisible = false
            }
        }
    }

    companion object {
        const val PEOPLE_FRAGMENT_NAME = "people_fragment"

        fun newInstance(): PeopleFragment {
            return PeopleFragment()
        }
    }
}
