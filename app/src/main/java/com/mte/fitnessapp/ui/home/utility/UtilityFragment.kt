package com.mte.fitnessapp.ui.home.utility

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.mte.fitnessapp.R
import com.mte.fitnessapp.databinding.FragmentUtilityBinding

class UtilityFragment : Fragment() {

    private var _binding : FragmentUtilityBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUtilityBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.bmi.setOnClickListener {
            val navigation = UtilityFragmentDirections.actionUtilityFragmentToBmiFragment()
            Navigation.findNavController(it).navigate(navigation)
        }
        binding.onerep.setOnClickListener {
            val navigation = UtilityFragmentDirections.actionUtilityFragmentToOnerepmaxFragment()
            Navigation.findNavController(it).navigate(navigation)
        }
        binding.calorie.setOnClickListener {
            val navigation = UtilityFragmentDirections.actionUtilityFragmentToCalorieFragment()
            Navigation.findNavController(it).navigate(navigation)
        }
        binding.bodyfat.setOnClickListener {
            val navigation = UtilityFragmentDirections.actionUtilityFragmentToBodyFatFragment()
            Navigation.findNavController(it).navigate(navigation)
        }

    }

}