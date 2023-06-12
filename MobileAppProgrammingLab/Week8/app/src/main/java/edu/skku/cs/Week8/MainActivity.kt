package edu.skku.cs.Week8 // do not copy and paste package name :P

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import edu.skku.cs.week8.databinding.ActivityMainBinding
import splitties.toast.toast

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding // binding added
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater) // get binding from layout_file.xml => LayoutFileBinding
        setContentView(binding.root) // and call setContentView with root of binding

        binding.buttonShowToast.setOnClickListener {toast(binding.editTextToastMsg.text.toString())}
        // use binding.ID instead of findViewById<Type>(ID)
    }
}