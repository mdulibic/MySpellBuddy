package hr.fer.mydictator.view.activity

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import dagger.hilt.android.AndroidEntryPoint
import hr.fer.mydictator.databinding.ActivityHomeBinding
import hr.fer.mydictator.util.extensions.viewBinding

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