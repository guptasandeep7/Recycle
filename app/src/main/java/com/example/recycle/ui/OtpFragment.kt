package com.example.recycle.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.recycle.Activity.MainActivity
import com.example.recycle.Network.ServiceBuilder
import com.example.recycle.R
import com.example.recycle.Repo.Datastore
import com.example.recycle.databinding.FragmentOtpBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeUnit


class OtpFragment : Fragment() {
    lateinit var binding:FragmentOtpBinding
    private var mAuth: FirebaseAuth? = null
    var phoneNumber=""
    var name=""
    lateinit var datastore: Datastore

    private var verificationId: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        datastore = Datastore(requireContext())
        mAuth = FirebaseAuth.getInstance()
        phoneNumber= if(findNavController().previousBackStackEntry!!.destination.id==R.id.logIn) {
            "+91${LogIn.phoneNumber}"
        }
        else
        {
            "+91${SignUp.phoneNumber}"
        }
        lifecycleScope.launch {
            sendVerificationCode(phoneNumber)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_otp, container, false)
        binding.Verifybutton.setOnClickListener {
            if(binding.firstPinView.text.isNullOrBlank() || binding.firstPinView.text?.length!! < 6){
                Toast.makeText(requireContext(), "Please enter OTP", Toast.LENGTH_SHORT).show()
            }
            else{
                verifyCode(binding.firstPinView.text.toString())
            }
        }
        return binding.root
    }
    private val
            mCallBack: PhoneAuthProvider.OnVerificationStateChangedCallbacks =
        object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onCodeSent(
                s: String,
                forceResendingToken: PhoneAuthProvider.ForceResendingToken
            ) {
                super.onCodeSent(s, forceResendingToken)

                verificationId = s
            }


            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {

                val code = phoneAuthCredential.smsCode

                if (code != null) {
                    binding.firstPinView.setText(code)
                    verifyCode(code)
                }
            }


            override fun onVerificationFailed(e: FirebaseException) {
                Toast.makeText(requireActivity(), e.message, Toast.LENGTH_LONG).show()
            }
        }


    private fun sendVerificationCode(number: String) {

        val options = PhoneAuthOptions.newBuilder(mAuth!!)
            .setPhoneNumber(number) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(requireActivity()) // Activity (for callback binding)
            .setCallbacks(mCallBack) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun verifyCode(code: String) {

        val credential = PhoneAuthProvider.getCredential(verificationId!!, code)

        signInWithCredential(credential)
    }

    @SuppressLint("SuspiciousIndentation")
    private fun signInWithCredential(credential: PhoneAuthCredential) {

        mAuth!!.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    lifecycleScope.launch {
                        datastore.changeLoginState(true)
                    }
                    if(findNavController().previousBackStackEntry!!.destination.id==R.id.logIn)
                    {
                        var Api = ServiceBuilder.init()
                        Api.logIn(phoneNumber).enqueue(object : Callback<ResponseBody?> {
                            override fun onResponse(
                                call: Call<ResponseBody?>,
                                response: Response<ResponseBody?>
                            ) {
                                val intent=Intent(activity,MainActivity::class.java)
                                startActivity(intent)
//                                when {
//                                    response.isSuccessful -> {}
//                                    else->
//                                    {
//                                        Toast.makeText(requireContext(),response.code().toString(),Toast.LENGTH_LONG).show()
//                                    }
//                                }
                            }

                            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                                TODO("Not yet implemented")
                            }
                        })
                    }
                    else {
                        val Api=ServiceBuilder.init()
                        val call=Api.signUp(SignUp.profilename,phoneNumber)
                            call.enqueue(object : Callback<ResponseBody?> {
                            override fun onResponse(
                                call: Call<ResponseBody?>,
                                response: Response<ResponseBody?>
                            ) {
                                lifecycleScope.launch {
                                    datastore.saveToDatastore(
                                        Datastore.NAME_KEY,
                                        name,
                                        requireContext()
                                    )
                                    datastore.saveToDatastore(
                                        Datastore.PHONE_NUMBER,
                                        phoneNumber,
                                        requireContext()
                                    )

                                    val intent=Intent(activity,MainActivity::class.java)
                                    startActivity(intent)
                                }

//                                when {
//                                    response.isSuccessful ->{}
//                                    else->
//                                    {
//                                        Toast.makeText(requireContext(),response.message().toString(),Toast.LENGTH_LONG).show()
//                                    }
//                                }
                            }

                            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                                TODO("Not yet implemented")
                            }
                        })
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        task.exception?.message,
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
            }
    }



}