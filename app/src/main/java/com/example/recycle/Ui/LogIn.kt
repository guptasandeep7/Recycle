package com.example.recycle.Ui

import android.content.Context
import android.os.Bundle
import android.provider.ContactsContract
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.recycle.Network.Api
import com.example.recycle.Network.ServiceBuilder
import com.example.recycle.R
import com.example.recycle.databinding.FragmentLogInBinding
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LogIn : Fragment() {
    lateinit var binding:FragmentLogInBinding
    companion object
    {
        var phoneNumber=""
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_log_in, container, false)
        binding.getOtpLogin.setOnClickListener {
            phoneNumber=binding.logInMobileNo.text.toString()
            if(binding.logInMobileNo.text?.length == 10){
                findNavController().navigate(R.id.otpFragment)
            }
            else{
                binding.textInputLayout.helperText = "Enter valid phone number"
            }
        }
        return binding.root
    }
    private fun View.showKeyboard() {
        this.requestFocus()
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
    }

    override fun onResume() {
        super.onResume()

    }

}