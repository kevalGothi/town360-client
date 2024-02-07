package com.example.town360client

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.town360client.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(Home())

        binding.bottomNavigationView.setOnItemSelectedListener {


            when(it.itemId){

                R.id.home -> replaceFragment(Home())
                R.id.emergency -> replaceFragment(emergency())
                R.id.aboutus -> replaceFragment(About_us())


                else ->{

                }

            }

            true

        }


    }

    private fun replaceFragment(fragment: Fragment){

        val fragmentManeger = supportFragmentManager
        val fragmentTrancection = fragmentManeger.beginTransaction()
        fragmentTrancection.replace(R.id.frame_layout,fragment)
        fragmentTrancection.commit()

    }
}
