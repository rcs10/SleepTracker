package com.example.sleeptracker.sleepnightdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.sleeptracker.R
import com.example.sleeptracker.database.SleepDataBase
import com.example.sleeptracker.databinding.FragmentSingleNightDetailsBinding

class SingleNightDetails : Fragment() {
    private lateinit var binding: FragmentSingleNightDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
            binding = DataBindingUtil.inflate(inflater,R.layout.fragment_single_night_details,container,false)
        val application = requireNotNull(activity).application
        val sleepDataBaseDao = SleepDataBase.getInstance(application).sleepDataBaseDao
        val nightId = SingleNightDetailsArgs.fromBundle(requireArguments()).nightId
        val singleNightViewModelFactory = SingleNightViewModelFactory(nightId,sleepDataBaseDao)
        val singleNightViewModel = ViewModelProviders.of(this,singleNightViewModelFactory).get(SingleNightViewModel::class.java)

        binding.lifecycleOwner = this
        singleNightViewModel.oneNight.observe(viewLifecycleOwner){ oneNight ->
            oneNight?.apply{
                binding.oneNight = oneNight
                binding.executePendingBindings()
            }
        }
        binding.singleNightViewModel = singleNightViewModel

        singleNightViewModel.backClicked.observe(viewLifecycleOwner){
            it?.apply {
                if (it){
                    findNavController().navigate(SingleNightDetailsDirections.actionSingleNightDetailsToSleepTracker())
                    singleNightViewModel.resetBackClick()
                }
            }
        }

        return binding.root
    }
}