package com.example.foodapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.foodapp.activity.MainActivity
import com.example.foodapp.adapter.CategooriesAdapter
import com.example.foodapp.databinding.FragmentCategoriesBinding
import com.example.foodapp.pojo.Category
import com.example.foodapp.pojo.CategoryList
import com.example.foodapp.viewmodel.HomeViewModel

class CategoriesFragment : Fragment() {
     private lateinit var binding: FragmentCategoriesBinding
     private lateinit var categoryAdapter: CategooriesAdapter
     private lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel=(activity as MainActivity).mainViewModel
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding= FragmentCategoriesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareRecyclerView()
        observeCategories()
    }

    private fun observeCategories() {
        viewModel.observeCategoriesLiveData().observe(viewLifecycleOwner, Observer { it->
            categoryAdapter.setCategories(it as ArrayList<Category>)
        })
    }

    private fun prepareRecyclerView() {
        categoryAdapter=CategooriesAdapter()
        binding.rvCategory.apply {
            layoutManager=GridLayoutManager(requireActivity(),3,GridLayoutManager.VERTICAL,false)
            adapter=categoryAdapter
        }


    }


}