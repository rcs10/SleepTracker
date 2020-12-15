package com.example.sleeptracker.sleeptracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.sleeptracker.R
import com.example.sleeptracker.database.SleepDataBase
import com.example.sleeptracker.databinding.FragmentSleepTrackerBinding
import com.google.android.material.snackbar.Snackbar

class Tracker : Fragment() {
    private lateinit var binding: FragmentSleepTrackerBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_sleep_tracker, container, false)
        binding.lifecycleOwner = this

        val application = requireNotNull(activity).application
        val sleepDataBaseDao = SleepDataBase.getInstance(application).sleepDataBaseDao
        val trackerViewModelFactory = TrackerViewModelFactory(application, sleepDataBaseDao)
        val trackerViewModel =
            ViewModelProviders.of(this, trackerViewModelFactory).get(TrackerViewModel::class.java)

        trackerViewModel.navigateToQuality.observe(viewLifecycleOwner){ night ->
            night?.let {
                findNavController().navigate(TrackerDirections.actionSleepTrackerToSleepQuality2(night.nightId))
                trackerViewModel.resetNavigation()
            }
        }

        trackerViewModel.showSnackbar.observe(viewLifecycleOwner){
            if(it){
                Snackbar.make(
                    requireActivity().findViewById(android.R.id.content),
                    getString(R.string.clear_msg),
                    Snackbar.LENGTH_SHORT
                ).setAnchorView(R.id.clear_button)
                    .show()
                trackerViewModel.resetNavigation()
            }
        }
        binding.trackerViewModel = trackerViewModel
        return binding.root
    }
}