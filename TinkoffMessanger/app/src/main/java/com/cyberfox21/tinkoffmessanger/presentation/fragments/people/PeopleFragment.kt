package com.cyberfox21.tinkoffmessanger.presentation.fragments.people

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.cyberfox21.tinkoffmessanger.R
import com.cyberfox21.tinkoffmessanger.databinding.FragmentPeopleBinding
import com.cyberfox21.tinkoffmessanger.domain.entity.User
import com.cyberfox21.tinkoffmessanger.presentation.NavigationHolder
import com.cyberfox21.tinkoffmessanger.presentation.fragments.profile.ProfileFragment

class PeopleFragment : Fragment() {

    private lateinit var viewModel: PeopleFragmentViewModel

    private lateinit var searchView: SearchView

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
        setupStatusBar()
        setupNavigation()
        setViewModel()
        setupSearchPanel()
        setupRecyclerView()
        addListeners()
        observeViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupStatusBar() {
        activity?.window?.statusBarColor =
            ContextCompat.getColor(requireContext(), R.color.bottom_navigation_background)
    }

    private fun setupNavigation() {
        (activity as NavigationHolder).showNavigation()
    }

    private fun setViewModel() {
        viewModel = ViewModelProvider(this)[PeopleFragmentViewModel::class.java]
    }

    private fun setupSearchPanel() {
        binding.toolbarLayout.toolbar.setTitleTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.white
            )
        )
        binding.toolbarLayout.toolbar.title = getString(R.string.people)
        val searchMenuItem = binding.toolbarLayout.toolbar.menu.findItem(R.id.actionSearch)
        val searchManager = context?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView = searchMenuItem.actionView as SearchView
        searchMenuItem.isVisible = true
        searchView.apply {
            findViewById<ImageView>(R.id.search_close_btn).setImageResource(R.drawable.ic_close)
            setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))
            setIconifiedByDefault(false)
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String): Boolean {
//                    onQueryChanged(newText)
                    return true
                }

            })
        }
    }

    private fun setupRecyclerView() {
        binding.peopleRecyclerView.setHasFixedSize(true)
        binding.peopleRecyclerView.adapter = peopleRecyclerAdapter
    }

    private fun addListeners() {
        peopleRecyclerAdapter.onPersonClickListener =
            object : PeopleRecyclerAdapter.OnPersonClickListener {
                override fun onPersonClick(user: User) {
                    showUserProfile(user)
                }
            }
    }

    private fun showUserProfile(user: User) {
        (requireActivity() as NavigationHolder).startFragment(
            ProfileFragment.newInstanceStranger(
                user
            ),
            PEOPLE_FRAGMENT_NAME
        )
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
