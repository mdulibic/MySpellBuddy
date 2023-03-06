package hr.fer.myspellbuddy.view.activity

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import dagger.hilt.android.AndroidEntryPoint
import hr.fer.myspellbuddy.databinding.ActivityHomeBinding
import hr.fer.myspellbuddy.util.extensions.viewBinding

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityHomeBinding::inflate)
    private var currentNavController: LiveData<NavController>? = null

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: super.onSupportNavigateUp()
    }

}