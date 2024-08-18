package com.example.foodapp.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.foodapp.R
import com.example.foodapp.activity.CategoryMealsActivity
import com.example.foodapp.activity.MainActivity
import com.example.foodapp.activity.MealActivity
import com.example.foodapp.adapter.CategooriesAdapter
import com.example.foodapp.adapter.MostPopularAdapter
import com.example.foodapp.databinding.FragmentHomeBinding
import com.example.foodapp.fragment.bottomsheet.MealBottomSheetFragment
import com.example.foodapp.pojo.Category
import com.example.foodapp.pojo.MealsByCategory
import com.example.foodapp.pojo.Meal
import com.example.foodapp.viewmodel.HomeViewModel

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var randomMeal: Meal
    private lateinit var popularItemsAdapter: MostPopularAdapter
    private lateinit var categoriesAdapter: CategooriesAdapter


    companion object {
        const val Meal_ID = "com.example.foodapp.activity.idMeal"
        const val Meal_NAME = "com.example.foodapp.activity.nameMeal"
        const val Meal_THUMB = "com.example.foodapp.activity.thumbMeal"
        const val Category_Name = " com.example.foodapp.fragment.categoryName"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).mainViewModel
        popularItemsAdapter = MostPopularAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        preparePopularItemsRecyclerView()
        viewModel.getRandomMeal()
        observerRandomMeal()
        onRandomMealClick()

        viewModel.getPopularItem()
        observePopularItemLiveData()
        onPopularItemClick()

        prepareCategoriesRecyclerView()
        viewModel.getCategories()
        observeCategoriesLiveData()
        onCategoryItemClick()

        onPopularItemLongClick()

        onSearchClick()

    }

    private fun onSearchClick() {
        binding.imgSearch.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
        }
    }

    private fun onPopularItemLongClick() {
        popularItemsAdapter.onLongItemClick = { meal ->
            val mealBtnSheetFragment = MealBottomSheetFragment.newInstance(meal.idMeal)
            mealBtnSheetFragment.show(childFragmentManager, "")
        }
    }

    private fun onCategoryItemClick() {
        categoriesAdapter.onItemClick = { category ->
            val intent = Intent(requireActivity(), CategoryMealsActivity::class.java)
            intent.putExtra(Category_Name, category.strCategory)
            startActivity(intent)
        }
    }

    private fun prepareCategoriesRecyclerView() {
        categoriesAdapter = CategooriesAdapter()
        binding.rvCategoriesItems.apply {
            layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
            adapter = categoriesAdapter
        }

    }

    private fun observeCategoriesLiveData() {
        viewModel.observeCategoriesLiveData().observe(viewLifecycleOwner, Observer { categories ->
            categoriesAdapter.setCategories(categories as ArrayList<Category>)
        })
    }

    private fun onPopularItemClick() {
        popularItemsAdapter.onItemClick = { meal ->
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(Meal_ID, meal.idMeal)
            intent.putExtra(Meal_NAME, meal.strMeal)
            intent.putExtra(Meal_THUMB, meal.strMealThumb)
            startActivity(intent)
        }
    }

    private fun preparePopularItemsRecyclerView() {
        binding.rvPopularItems.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = popularItemsAdapter
        }

    }

    private fun observePopularItemLiveData() {
        viewModel.observePopularItemLiveData().observe(
            viewLifecycleOwner
        ) { value ->
            popularItemsAdapter.setMeals(value as ArrayList<MealsByCategory>)
        }
    }

    private fun onRandomMealClick() {
        binding.randomMealCard.setOnClickListener {
            val intent = Intent(requireActivity(), MealActivity::class.java)
            intent.putExtra(Meal_ID, randomMeal.idMeal)
            intent.putExtra(Meal_NAME, randomMeal.strMeal)
            intent.putExtra(Meal_THUMB, randomMeal.strMealThumb)
            startActivity(intent)
        }
    }

    private fun observerRandomMeal() {
        viewModel.observeRandomMealLiveData().observe(
            viewLifecycleOwner
        ) { value ->
            Glide.with(this@HomeFragment)
                .load(value.strMealThumb)//Url
                .into(binding.imgRandomMeal)
            this.randomMeal = value
        }
    }
}