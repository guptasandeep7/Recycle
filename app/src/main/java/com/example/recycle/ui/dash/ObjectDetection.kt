package com.example.recycle.ui.dash

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.PermissionChecker
import androidx.core.content.PermissionChecker.checkSelfPermission
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.recycle.R
import com.example.recycle.databinding.FragmentObjectDetectionBinding
import com.example.recycle.model.priceList
import com.google.mlkit.common.model.LocalModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.custom.CustomObjectDetectorOptions
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.URLEncoder


class ObjectDetection : Fragment() {
    val imageSize = 224
    var amount = ""
    var label = ""
    lateinit var sendImage:Bitmap
    companion object{lateinit var image: Bitmap}
    lateinit var binding:FragmentObjectDetectionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_object_detection, container, false)
        if (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                checkSelfPermission(requireContext(),Manifest.permission.CAMERA) == PermissionChecker.PERMISSION_GRANTED
            } else {
                TODO("VERSION.SDK_INT < M")
            }
        ) {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, 1)

        } else {
            //Request camera permission if we don't have it.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(arrayOf(Manifest.permission.CAMERA), 100)
            }
        }

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.sendButton.setOnClickListener {
//            shareImageToWhatsapp(sendImage, "$label \n$amount")
            val i = Intent(Intent.ACTION_VIEW)
            val phone = "+919918859354"
            try {
                val url =
                    "https://api.whatsapp.com/send?phone=$phone&text=" + URLEncoder.encode(
                        "Product Id - 1003\nProduct Name - $label\nPrice - $amount\nAddress - Ajay Kumar Garg Engineering College, Ghaziabad, Uttar Pradesh",
                        "UTF-8"
                    )
                i.setPackage("com.whatsapp")
                i.setData(Uri.parse(url))
                startActivity(i)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        binding.materialButton.setOnClickListener {
            findNavController().navigate(R.id.action_objectDetection_to_cartFragment)
        }

        binding.button2.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    fun shareImageToWhatsapp(bitmap: Bitmap, caption: String) {
        val shareIntent = Intent(Intent.ACTION_SEND)

        shareIntent.setPackage("com.whatsapp")

        // Convert bitmap to a shareable Uri
        val uri = getLocalBitmapUri(bitmap)
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
        shareIntent.setType("image/*")

        // Add caption as a separate text share
        val shareText = Intent.createChooser(Intent(Intent.ACTION_SEND), "Share Caption")
        shareText.putExtra(Intent.EXTRA_TEXT, caption)
        shareText.type = "text/plain"

        shareIntent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        startActivity(Intent.createChooser(shareIntent, "Share Image"))
    }

    private fun getLocalBitmapUri(bitmap: Bitmap): Uri {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val bytes = byteArrayOutputStream.toByteArray()
        val tempFile = File(activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "temp_image.jpg")
        try {
            val stream = FileOutputStream(tempFile)
            stream.write(bytes)
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return Uri.fromFile(tempFile)
    }



    private fun classifyImage(image: Bitmap) {

        val localModel = LocalModel.Builder()
            .setAssetFilePath("object_labeler.tflite")
            .build()

        val customObjectDetectorOptions = CustomObjectDetectorOptions.Builder(localModel)
            .setDetectorMode(CustomObjectDetectorOptions.SINGLE_IMAGE_MODE)
            .enableClassification()
            .setClassificationConfidenceThreshold(0.5f)
            .setMaxPerObjectLabelCount(3)
            .build()

        val objectDetector = ObjectDetection.getClient(customObjectDetectorOptions)

        val inputImage = InputImage.fromBitmap(image, 0)

        objectDetector.process(inputImage)
            .addOnSuccessListener { detectedObjects ->
//                Toast.makeText(requireContext(),"Success",Toast.LENGTH_LONG).show()
                for (detectedObject in detectedObjects) {
                    val boundingBox = detectedObject.boundingBox
                    val trackingId = detectedObject.trackingId
                    val label = detectedObject.labels.maxBy { it.confidence }.text
                    this.label = label
                    binding.ObjectDetectedName.text = label
                    amount = priceList.find { it.name == label}?.price.toString()
                    binding.ObjectDetectionDescription.text = amount
                    sendImage = image
//                    for (label in detectedObject.labels) {
//                        val text = label.text
//                        binding.ObjectDetectedName.append("$text ${priceList.find { it.name==text }?.price}\n")
//                        val conf = label.confidence
////                        confidence.append("$conf\n")
//                    }
                }
            }.addOnFailureListener {
                Toast.makeText(requireContext(), "Failure", Toast.LENGTH_SHORT).show()
            }.addOnCanceledListener {
                Toast.makeText(requireContext(), "Canceled", Toast.LENGTH_SHORT).show()
            }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1 && resultCode == AppCompatActivity.RESULT_OK) {
            image = data!!.extras!!["data"] as Bitmap
            val dimension = Math.min(image!!.width, image!!.height)
           image = ThumbnailUtils.extractThumbnail(image, dimension, dimension)
            binding.imageView3.setImageBitmap(image)

            image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false)
            classifyImage(image)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}