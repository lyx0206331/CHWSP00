package com.chwishay.chwsp00.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.chwishay.chwsp00.R
import com.chwishay.chwsp00.btUtil.BtTestActivity
import com.chwishay.chwsp00.databinding.FragmentFeaturesEntryBinding
import com.chwishay.chwsp00.utils.isTestMode
import com.chwishay.commonlib.tools.getVersionName

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
        if (isTestMode) {
            binding.btnReg.visibility = View.GONE
            binding.btnUse.visibility = View.GONE
            binding.btnReport.visibility = View.GONE
            binding.btnCollectData.visibility = View.VISIBLE
            binding.btnCollectData.setOnClickListener {
                startActivity(Intent(activity, BtTestActivity::class.java))
            }
        } else {
            binding.btnReg.visibility = View.VISIBLE
            binding.btnUse.visibility = View.VISIBLE
            binding.btnReport.visibility = View.VISIBLE
            binding.btnCollectData.visibility = View.GONE
            binding.btnReg.setOnClickListener {
                it.findNavController().navigate(R.id.actionFeaturesEntry2newRegister)
            }
            binding.btnUse.setOnClickListener {
                it.findNavController().navigate(R.id.actionFeaturesEntry2testUserChoose)
            }
            binding.btnReport.setOnClickListener {
                it.findNavController().navigate(R.id.actionFeaturesEntry2recentLogin)
            }
        }
        binding.tvVersion.text = "v${context?.getVersionName()}"
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