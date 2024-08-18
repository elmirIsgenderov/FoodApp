package com.example.foodapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodapp.db.MealDataBase
import com.example.foodapp.pojo.Category
import com.example.foodapp.pojo.CategoryList
import com.example.foodapp.pojo.MealsByCategoryList
import com.example.foodapp.pojo.MealsByCategory
import com.example.foodapp.pojo.Meal
import com.example.foodapp.pojo.MealList
import com.example.foodapp.retrofit.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(private val mealDataBase: MealDataBase) : ViewModel() {

    private var randomMealLiveData = MutableLiveData<Meal>()
    private var popularItemsLiveData = MutableLiveData<List<MealsByCategory>>()
    private var categoriesLiveData = MutableLiveData<List<Category>>()
    private var favoriteMealsLiveData = mealDataBase.getMealDao().getAllMeals()
    private var btnsheetLiveData = MutableLiveData<Meal>()
    private var searchMealLiveData = MutableLiveData<List<Meal>>()

    fun getCategories() {
        RetrofitInstance.api.getCategories().enqueue(object : Callback<CategoryList> {
            override fun onResponse(p0: Call<CategoryList>, response: Response<CategoryList>) {
                response.body()?.let { categoryList ->
                    categoriesLiveData.postValue(categoryList.categories)
                }
            }

            override fun onFailure(p0: Call<CategoryList>, p1: Throwable) {
            }
        })
    }

    fun getPopularItem() {
        RetrofitInstance.api.getPopularItems("Seafood")
            .enqueue(object : Callback<MealsByCategoryList> {
                override fun onResponse(
                    p0: Call<MealsByCategoryList>,
                    response: Response<MealsByCategoryList>
                ) {
                    if (response.body() != null) {
                        popularItemsLiveData.value = response.body()!!.meals
                    }
                }

                override fun onFailure(p0: Call<MealsByCategoryList>, p1: Throwable) {
                }
            })
    }

    fun getRandomMeal() {
        RetrofitInstance.api.getRandomMeal().enqueue(object : Callback<MealList> {
            override fun onResponse(p0: Call<MealList>, response: Response<MealList>) {
                if (response.body() != null) {
                    randomMealLiveData.value = response.body()!!.meals[0]
                }
            }

            override fun onFailure(p0: Call<MealList>, p1: Throwable) {
                Log.d("Error", p1.message.toString())
            }
        })
    }

    fun searchMeal(searchQuery: String) {
        RetrofitInstance.api.searchMeal(searchQuery).enqueue(object : Callback<MealList> {
            override fun onResponse(p0: Call<MealList>, response: Response<MealList>) {
                val mealList = response.body()?.meals
                mealList?.let { i ->
                    searchMealLiveData.postValue(i)
                }
            }

            override fun onFailure(p0: Call<MealList>, p1: Throwable) {
                Log.d("Error", p1.message.toString())
            }
        })
    }

    fun deleteMeal(meal: Meal) {
        viewModelScope.launch {
            mealDataBase.getMealDao().delete(meal)
        }
    }

    fun insertMeal(meal: Meal) {
        viewModelScope.launch {
            mealDataBase.getMealDao().upsert(meal)
        }
    }

    fun getMealById(id: String) {
        RetrofitInstance.api.getMealDetails(id).enqueue(object : Callback<MealList> {
            override fun onResponse(p0: Call<MealList>, response: Response<MealList>) {
                val meal = response.body()?.meals?.first()
                btnsheetLiveData.postValue(meal!!)
            }

            override fun onFailure(p0: Call<MealList>, p1: Throwable) {
                Log.d("Fail", p1.message.toString())
            }
        })
    }

    fun observePopularItemLiveData(): MutableLiveData<List<MealsByCategory>> {
        return popularItemsLiveData
    }

    fun observeRandomMealLiveData(): LiveData<Meal> {
        return randomMealLiveData
    }

    fun observeCategoriesLiveData(): LiveData<List<Category>> {
        return categoriesLiveData
    }

    fun observeFavoriteMealsLiveData(): LiveData<List<Meal>> {
        return favoriteMealsLiveData
    }

    fun observeBtnSheetLiveData(): LiveData<Meal> = btnsheetLiveData

    fun observeSearchMealLiveData(): LiveData<List<Meal>> = searchMealLiveData

}