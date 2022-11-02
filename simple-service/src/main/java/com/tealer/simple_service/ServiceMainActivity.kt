package com.tealer.simple_service

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tealer.simple_service.databinding.ActivityServiceMainLayoutBinding

class ServiceMainActivity :AppCompatActivity() {

    private lateinit var binding:ActivityServiceMainLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityServiceMainLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}