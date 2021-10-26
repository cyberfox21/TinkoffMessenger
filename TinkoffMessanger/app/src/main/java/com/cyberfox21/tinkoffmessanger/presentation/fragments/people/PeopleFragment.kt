package com.cyberfox21.tinkoffmessanger.presentation.fragments.people

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.cyberfox21.tinkoffmessanger.R
import com.cyberfox21.tinkoffmessanger.databinding.FragmentPeopleBinding
import com.cyberfox21.tinkoffmessanger.domain.enums.ProfileMode
import com.cyberfox21.tinkoffmessanger.presentation.fragments.profile.ProfileFragment

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
                override fun onPeopleClick() {
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
        viewModel.users.observe(viewLifecycleOwner, {
            peopleRecyclerAdapter.submitList(it)
        })
    }

    companion object {
        const val PEOPLE_FRAGMENT_NAME = "people_fragment"

        fun newInstance(): PeopleFragment {
            return PeopleFragment()
        }
    }
}
