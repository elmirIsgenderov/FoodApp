package com.example.foodapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.foodapp.pojo.Meal

@Database(entities = [Meal::class], version = 1)
@TypeConverters(MealTypeConverter::class)
 abstract class MealDataBase:RoomDatabase() {
    abstract fun getMealDao(): MealDao

    companion object {
        @Volatile
        var Instance: MealDataBase? = null

        @Synchronized
        fun getInstance(context: Context): MealDataBase {
            if (Instance == null) {
                Instance = Room.databaseBuilder(context, MealDataBase::class.java, "meal.db")
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return Instance as MealDataBase
        }
    }
}