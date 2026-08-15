package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.data.AppDatabase
import com.example.data.ProductRepository
import com.example.ui.MainScreen
import com.example.ui.PosViewModel
import com.example.ui.PosViewModelFactory
import com.example.ui.theme.SchoolPOSTheme

class MainActivity : ComponentActivity() {

    private val viewModel: PosViewModel by viewModels {
        val database = AppDatabase.getDatabase(applicationContext)
        val repository = ProductRepository(database.productDao(), database.saleDao())
        PosViewModelFactory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SchoolPOSTheme {
                MainScreen(viewModel = viewModel)
            }
        }
    }
}

