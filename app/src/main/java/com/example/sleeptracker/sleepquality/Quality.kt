package com.example.sleeptracker.sleepquality

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.sleeptracker.R
import com.example.sleeptracker.database.SleepDataBase
import com.example.sleeptracker.database.SleepDataBaseDao
import com.example.sleeptracker.databinding.FragmentSleepQualityBinding

class Quality : Fragment() {
    private lateinit var binding : FragmentSleepQualityBinding
    private lateinit var qualityViewModel: QualityViewModel
    private lateinit var qualityViewModelFactory: QualityViewModelFactory
    private lateinit var sleepDataBaseDao: SleepDataBaseDao

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_sleep_quality,container,false)
        binding.lifecycleOwner = this

        val nightKey = QualityArgs.fromBundle(requireArguments()).nightKey
        val application = requireNotNull(activity).application
        sleepDataBaseDao = SleepDataBase.getInstance(application).sleepDataBaseDao

        qualityViewModelFactory = QualityViewModelFactory(nightKey,sleepDataBaseDao)
        qualityViewModel = ViewModelProviders.of(this,qualityViewModelFactory).get(QualityViewModel::class.java)

        binding.qualityViewModel = qualityViewModel

        qualityViewModel.navigateBack.observe(viewLifecycleOwner){ canNavigateBack ->
            if(canNavigateBack){
                findNavController().navigate(QualityDirections.actionSleepQualityToSleepTracker3())
                qualityViewModel.resetNavigation()
            }
        }
        return binding.root
    }
}