package com.example.foodapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodapp.databinding.CategoryItemsBinding
import com.example.foodapp.pojo.Category

class CategooriesAdapter : RecyclerView.Adapter<CategooriesAdapter.categoriesHolderView>() {

    inner class categoriesHolderView(val binding: CategoryItemsBinding) :
        RecyclerView.ViewHolder(binding.root)

    private var categoriesList = ArrayList<Category>()
    var onItemClick: ((Category) -> Unit)? = null

    fun setCategories(categoryList: ArrayList<Category>) {
        this.categoriesList = categoryList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): categoriesHolderView {
        return categoriesHolderView(CategoryItemsBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun getItemCount(): Int {
        return categoriesList.size
    }

    override fun onBindViewHolder(holder: categoriesHolderView, position: Int) {
        val category = categoriesList[position]

        Glide.with(holder.itemView).load(category.strCategoryThumb)
            .into(holder.binding.imgCategotyItem)

        holder.binding.txtCategoryName.text = category.strCategory

        holder.binding.imgCategotyItem.setOnClickListener {
            onItemClick?.invoke(category)
        }
    }

}