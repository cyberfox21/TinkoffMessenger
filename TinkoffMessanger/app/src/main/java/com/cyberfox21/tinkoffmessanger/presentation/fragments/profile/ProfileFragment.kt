package com.cyberfox21.tinkoffmessanger.presentation.fragments.profile

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.cyberfox21.tinkoffmessanger.R
import com.cyberfox21.tinkoffmessanger.databinding.FragmentProfileBinding
import com.cyberfox21.tinkoffmessanger.domain.enums.ProfileMode

class ProfileFragment : Fragment() {

    lateinit var screenMode : ProfileMode

    private var _binding: FragmentProfileBinding? = null
    private val binding: FragmentProfileBinding
        get() = _binding ?: throw RuntimeException("FragmentProfileBinding = null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArguments()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        launchRightMode()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun parseArguments() {
        val args = requireArguments()
        if (!args.containsKey(EXTRA_MODE)) throw RuntimeException("Param mode is absent")
        val mode = args.get(EXTRA_MODE) as ProfileMode
        if (mode != ProfileMode.YOUR && mode != ProfileMode.STRANGER)
            throw RuntimeException("Unknown profile screen mode $mode")
        screenMode = mode
    }

    private fun launchRightMode() {
        if(screenMode == ProfileMode.STRANGER){
            binding.btnLogout.visibility = View.GONE
        }
    }

    companion object {

        private const val EXTRA_MODE = "extra_mode"

        fun newInstance(mode: ProfileMode): ProfileFragment {
            return ProfileFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(EXTRA_MODE, mode)
                }
            }
        }
    }
}
