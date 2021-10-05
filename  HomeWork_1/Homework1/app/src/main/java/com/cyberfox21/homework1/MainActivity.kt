package com.cyberfox21.homework1

import android.Manifest
import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.cyberfox21.homework1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setBinding()
        setContentView(binding.root)
        addListener()
    }

    // result of requesting permissions
    private val requestPermissionResult =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) secondActivityResult.launch(SecondActivity.newIntent(this))
            else showError()
        }

    //result of second activity
    private val secondActivityResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { activityResult ->
            if (activityResult.resultCode == Activity.RESULT_OK) {
                // retrieve data
                val names =
                    activityResult.data?.getStringArrayListExtra(SecondActivity.CONTACT_NAME)
                var resultString = " "
                names?.forEach {
                    resultString += "$it "
                }
                binding.textView.text = resultString
            } else showError()
        }


    private fun setBinding() {
        binding = ActivityMainBinding.inflate(layoutInflater)
    }

    private fun addListener() =
        binding.btnLaunchSecondActivity.setOnClickListener {
            requestPermissionResult.launch(Manifest.permission.READ_CONTACTS)
        }

    private fun showError() {
        makeToast(getString(R.string.denied_permission))
        makeToast(getString(R.string.cant_load_contacts))
    }

    private fun makeToast(msg: String) =
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}