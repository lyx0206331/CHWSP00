package com.chwishay.chwsp00.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chwishay.chwsp00.R
import com.chwishay.chwsp00.databinding.FragmentFeaturesEntryBinding
import org.jetbrains.anko.sdk27.coroutines.onClick

class FeaturesEntryFragment : Fragment() {

    private lateinit var binding: FragmentFeaturesEntryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFeaturesEntryBinding.inflate(inflater, container, false)
        binding.btnReg.onClick {  }
        binding.btnUse.onClick {  }
        binding.btnReport.onClick {  }
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FeaturesEntryFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FeaturesEntryFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}