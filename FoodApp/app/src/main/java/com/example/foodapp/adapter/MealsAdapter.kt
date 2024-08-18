package com.example.foodapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodapp.databinding.MealItemsBinding
import com.example.foodapp.pojo.Meal

class MealsAdapter : RecyclerView.Adapter<MealsAdapter.favouriteMealsViewHolder>() {

    inner class favouriteMealsViewHolder(var binding: MealItemsBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val diffUtil = object : DiffUtil.ItemCallback<Meal>() {
        override fun areItemsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem.idMeal == newItem.idMeal
        }

        override fun areContentsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): favouriteMealsViewHolder {
        return favouriteMealsViewHolder(
            MealItemsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: favouriteMealsViewHolder, position: Int) {
        val meals = differ.currentList[position]
        Glide.with(holder.itemView).load(meals.strMealThumb).into(holder.binding.imgMeal)
        holder.binding.txtMealName.text = meals.strMeal
    }
}