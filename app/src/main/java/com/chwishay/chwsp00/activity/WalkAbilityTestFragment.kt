package com.chwishay.chwsp00.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.chwishay.chwsp00.R
import com.chwishay.chwsp00.databinding.FragmentWalkAbilityTestBinding

/**
 * A simple [Fragment] subclass.
 * Use the [WalkAbilityTestFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WalkAbilityTestFragment : Fragment() {

    private lateinit var binding: FragmentWalkAbilityTestBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWalkAbilityTestBinding.inflate(inflater, container, false)
        binding.toolbar.setNavigationOnClickListener {
            it.findNavController().popBackStack(R.id.fragNewRegister, true)
        }
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment WalkAbilityTestFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            WalkAbilityTestFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}