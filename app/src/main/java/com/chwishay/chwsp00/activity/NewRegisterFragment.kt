package com.chwishay.chwsp00.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.chwishay.chwsp00.R
import com.chwishay.chwsp00.databinding.FragmentNewRegisterBinding
import com.chwishay.chwsp00.model.UserInfo
import com.chwishay.commonlib.tools.logE

class NewRegisterFragment : Fragment() {

    private lateinit var binding: FragmentNewRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewRegisterBinding.inflate(inflater, container, false)
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationOnClickListener {
            it.findNavController().navigateUp()
        }
        binding.btnCommitReg.setOnClickListener {
            binding.userInfo.toString().logE(binding.userInfo?.name.orEmpty())
            it.findNavController().navigate(R.id.actionNewRegister2walkAbilityTest)
        }
        binding.userInfo = UserInfo(0, "测试")
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment NewRegisterFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NewRegisterFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}