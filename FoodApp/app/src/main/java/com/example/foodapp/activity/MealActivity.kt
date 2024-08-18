package com.example.foodapp.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.foodapp.R
import com.example.foodapp.databinding.ActivityMealBinding
import com.example.foodapp.db.MealDataBase
import com.example.foodapp.fragment.HomeFragment
import com.example.foodapp.pojo.Meal
import com.example.foodapp.viewmodel.HomeViewModel
import com.example.foodapp.viewmodel.HomeViewModelFactory
import com.example.foodapp.viewmodel.MealViewModel
import com.example.foodapp.viewmodel.MealViewModelFactory

class MealActivity : AppCompatActivity() {

    private lateinit var mealId: String
    private lateinit var mealName: String
    private lateinit var mealThumb: String
    private lateinit var mealMvvm: MealViewModel
    private lateinit var youtubeLink: String
    private var mealToSave: Meal? = null

    private lateinit var binding: ActivityMealBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMealBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val mealDataBase = MealDataBase.getInstance(this)
        val viewModelFactory = MealViewModelFactory(mealDataBase)
        mealMvvm = ViewModelProvider(this, viewModelFactory)[MealViewModel::class.java]

        getMealInformationFromIntent()
        setInformationOnView()
        loadingCase()
        mealMvvm.getMealDetails(mealId)
        observeMealDetailsLiveData()
        onYoutubeImageClick()
        onFavouriteClick()
    }

    private fun onFavouriteClick() {
        binding.btnFab.setOnClickListener {
            mealToSave?.let {
                mealMvvm.insertMeal(it)
                Toast.makeText(this, "Meal saved", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onYoutubeImageClick() {
        binding.imgYoutube.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(youtubeLink)))
        }
    }

    private fun observeMealDetailsLiveData() {
        mealMvvm.observeMealDetailsLiveData().observe(this, object : Observer<Meal> {
            override fun onChanged(value: Meal) {
                responseCase()
                mealToSave = value

                binding.txtCategory.text = "Category : ${value.strCategory}"
                binding.txtArea.text = "Area : ${value.strArea}"
                binding.txtInstruction.text = value.strInstructions
                youtubeLink = value.strYoutube!!
            }
        })
    }

    private fun loadingCase() {
        binding.progressBar.visibility = View.VISIBLE
        binding.txtCategory.visibility = View.INVISIBLE
        binding.txtArea.visibility = View.INVISIBLE
        binding.txtInstruction.visibility = View.INVISIBLE
        binding.imgYoutube.visibility = View.INVISIBLE
    }

    private fun responseCase() {
        binding.progressBar.visibility = View.INVISIBLE
        binding.txtCategory.visibility = View.VISIBLE
        binding.txtArea.visibility = View.VISIBLE
        binding.txtInstruction.visibility = View.VISIBLE
        binding.imgYoutube.visibility = View.VISIBLE
    }

    private fun setInformationOnView() {
        Glide.with(applicationContext)
            .load(mealThumb)
            .into(binding.imgMealDetails)

        binding.collapToolbar.title = mealName
        binding.collapToolbar.setCollapsedTitleTextColor(resources.getColor(R.color.white))
        binding.collapToolbar.setExpandedTitleColor(resources.getColor(R.color.white))
    }

    private fun getMealInformationFromIntent() {
        mealId = intent.getStringExtra(HomeFragment.Meal_ID)!!
        mealName = intent.getStringExtra(HomeFragment.Meal_NAME)!!
        mealThumb = intent.getStringExtra(HomeFragment.Meal_THUMB)!!
    }
}