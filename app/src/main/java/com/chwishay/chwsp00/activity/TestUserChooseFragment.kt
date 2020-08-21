package com.chwishay.chwsp00.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.chwishay.chwsp00.databinding.FragmentTestUserChooseBinding

/**
 * A simple [Fragment] subclass.
 * Use the [TestUserChooseFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TestUserChooseFragment : Fragment() {

    private lateinit var binding: FragmentTestUserChooseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTestUserChooseBinding.inflate(inflater, container, false)
        binding.toolbar.setNavigationOnClickListener {
            it.findNavController().navigateUp()
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
         * @return A new instance of fragment TestUserChooseFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TestUserChooseFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}