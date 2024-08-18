package com.example.foodapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.foodapp.activity.MainActivity
import com.example.foodapp.adapter.MealsAdapter
import com.example.foodapp.databinding.FragmentFavouriteBinding
import com.example.foodapp.viewmodel.HomeViewModel
import com.google.android.material.snackbar.Snackbar

class FavouriteFragment : Fragment() {
    private lateinit var binding: FragmentFavouriteBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var favouriteAdapter: MealsAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).mainViewModel
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavouriteBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeFavourites()
        prepareRecyclerView()

        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
        ) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder)
            =true

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position=viewHolder.adapterPosition
                val mealPosition=favouriteAdapter.differ.currentList[position]
                viewModel.deleteMeal(mealPosition)

                Snackbar.make(requireView(),"Meal delated",Snackbar.LENGTH_LONG).setAction(
                    "Undo", View.OnClickListener {
                        viewModel.insertMeal(mealPosition)
                    }
                ).show()
            }
        }
        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(binding.rvFavouriteFragment)
    }
    private fun prepareRecyclerView() {
        favouriteAdapter = MealsAdapter()
        binding.rvFavouriteFragment.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = favouriteAdapter
        }
    }
    private fun observeFavourites() {
        viewModel.observeFavoriteMealsLiveData().observe(viewLifecycleOwner, Observer { meal ->
            favouriteAdapter.differ.submitList(meal)
        })
    }
}