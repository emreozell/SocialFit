package com.mte.fitnessapp.ui.home.guide.nutrients

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.mte.fitnessapp.R
import com.mte.fitnessapp.databinding.FragmentNutrientsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NutrientsFragment : Fragment() {

    private var _binding : FragmentNutrientsBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<NutrientsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getNutrients("kebab")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNutrientsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.button.setOnClickListener {
            val nutrient = binding.searchView.query.toString()
            viewModel.getNutrients(nutrient)
        }

        viewModel.nutrientsResponse.observe(viewLifecycleOwner, Observer {
            if(it.size == 1){
                binding.nutrient = it[0]
                val walkCalorie = (it[0].calories * 0.24).toInt()
                val workoutCalorie = (it[0].calories * 0.19).toInt()
                val runningCalorie = (it[0].calories * 0.14).toInt()
                val bicycleCalorie = (it[0].calories * 0.13).toInt()
                binding.textViewWalking.text = "You have to walk for " + walkCalorie.toString() + " minutes"
                binding.textViewWorkout.text = "You have to workout for " + workoutCalorie.toString() + " minutes"
                binding.textViewRunning.text = "You have to run for " + runningCalorie.toString() + " minutes"
                binding.textViewBicycle.text = "You have to cycle for " + bicycleCalorie.toString() + " minutes"
            }else if (it.size > 1){
                Toast.makeText(context,"Please only search for a single nutrient name",Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(context,"Please search for a valid nutrient name",Toast.LENGTH_SHORT).show()
            }
        })
    }

}