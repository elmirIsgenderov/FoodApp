package com.example.foodapp.fragment.bottomsheet

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.foodapp.activity.MainActivity
import com.example.foodapp.activity.MealActivity
import com.example.foodapp.databinding.FragmentMealBottomSheetBinding
import com.example.foodapp.fragment.HomeFragment
import com.example.foodapp.viewmodel.HomeViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


private const val Meal_Id = "param1"

class MealBottomSheetFragment : BottomSheetDialogFragment() {

    private var meal_id: String? = null
    private var meal_name: String? = null
    private var meal_thumb: String? = null

    private lateinit var binding:FragmentMealBottomSheetBinding
    private lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            meal_id = it.getString(Meal_Id)
        }
        viewModel=(activity as MainActivity).mainViewModel
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding= FragmentMealBottomSheetBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        meal_id?.let {viewModel.getMealById(it)}

        observeBtnSheetMeal()
        onBottomSheetDialogClick()

    }

    private fun onBottomSheetDialogClick() {
        binding.bottomSheet.setOnClickListener {
            val intent=Intent(requireActivity(),MealActivity::class.java)
            intent.apply {
                intent.putExtra(HomeFragment.Meal_ID,meal_id)
                intent.putExtra(HomeFragment.Meal_NAME,meal_name)
                intent.putExtra(HomeFragment.Meal_THUMB,meal_thumb)
            }
            startActivity(intent)
        }

    }

    private fun observeBtnSheetMeal() {
        viewModel.observeBtnSheetLiveData().observe(viewLifecycleOwner, Observer {i->
            Glide.with(this).load(i.strMealThumb).into(binding.imgBtnsheet)
            binding.txtBtnsheetArea.text=i.strArea
            binding.txtBtnsheetCategory.text=i.strCategory
            binding.txtBtnsheetMealName.text=i.strMeal

            meal_name=i.strMeal
            meal_thumb=i.strMealThumb
        })
    }

    companion object {
        fun newInstance(param1: String) =
            MealBottomSheetFragment().apply {
                arguments = Bundle().apply {
                    putString(Meal_Id, param1)
                }
            }
    }
}