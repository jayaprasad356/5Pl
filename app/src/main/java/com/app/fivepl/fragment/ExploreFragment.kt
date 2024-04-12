package com.app.fivepl.fragment

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.fivepl.databinding.FragmentExploreBinding


class ExploreFragment : Fragment() {

    lateinit var binding: FragmentExploreBinding
    lateinit var activity: Activity
    lateinit var session: com.app.fivepl.helper.Session

    private var adapter: com.app.fivepl.adapter.SliderAdapterExample? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentExploreBinding.inflate(inflater, container, false)
        activity = requireActivity()
        session = com.app.fivepl.helper.Session(activity)

//        (activity as HomeActivity).binding.coordinatorLayout.setBackgroundColor(Color.WHITE)
//        (activity as HomeActivity).binding.rlToolbar.setBackgroundColor(Color.WHITE)


        return binding.root

    }


}