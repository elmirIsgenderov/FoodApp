package com.example.foodapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodapp.R
import com.example.foodapp.activity.MainActivity
import com.example.foodapp.adapter.MealsAdapter
import com.example.foodapp.databinding.FragmentSearchBinding
import com.example.foodapp.viewmodel.HomeViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var searchRvAdapter: MealsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = (activity as MainActivity).mainViewModel

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareRecycleView()

        binding.imgSearchArrow.setOnClickListener { searchMeals() }

        observeSearchMealLiveData()

        var searchJob:Job?= null
        binding.edtSearchBox.addTextChangedListener { searchQuery->
          searchJob?.cancel()
            searchJob=lifecycleScope.launch {
                delay(300)
                viewModel.searchMeal(searchQuery.toString())
            }
        }
}

    private fun observeSearchMealLiveData() {
        viewModel.observeSearchMealLiveData().observe(viewLifecycleOwner, Observer { mealsList ->
            searchRvAdapter.differ.submitList(mealsList)
        })
    }

    private fun searchMeals() {
        val searchQuery = binding.edtSearchBox.text.toString()
        if (searchQuery.isNotEmpty()) {
            viewModel.searchMeal(searchQuery)
        }
    }

    private fun prepareRecycleView() {
        searchRvAdapter = MealsAdapter()
        binding.rvSearch.apply {
            layoutManager = GridLayoutManager(requireActivity(), 2, GridLayoutManager.VERTICAL, false)
            adapter=searchRvAdapter
        }
    }
}