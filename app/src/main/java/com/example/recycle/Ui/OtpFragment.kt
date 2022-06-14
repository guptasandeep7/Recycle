package com.example.recycle.Ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.recycle.R
import com.example.recycle.databinding.FragmentOtpBinding
import com.google.android.gms.dynamic.SupportFragmentWrapper
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit


class OtpFragment : Fragment() {
    lateinit var binding:FragmentOtpBinding
    private var mAuth: FirebaseAuth? = null
    var phoneNumber=""

    private var verificationId: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

            verifyCode(binding.firstPinView.getText().toString())
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

    private fun signInWithCredential(credential: PhoneAuthCredential) {

        mAuth!!.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(requireContext(),"Success",Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(
                        requireContext(),
                        task.exception!!.message,
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
            }
    }

}