package com.tealer.simple_service

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tealer.process.service.ResponseServiceListManager
import com.tealer.simple_service.databinding.ActivityServiceMainLayoutBinding
import com.tealer.simple_service.response.custom.UserInformationResponse

class ServiceMainActivity :AppCompatActivity() {

    private lateinit var binding:ActivityServiceMainLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityServiceMainLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnRegister.text="扫描到接口个数：${ResponseServiceListManager.getResponseServiceLists().size}"
        binding.btnRegister.setOnClickListener{
            ResponseServiceListManager.register(UserInformationResponse())
        }

    }
}