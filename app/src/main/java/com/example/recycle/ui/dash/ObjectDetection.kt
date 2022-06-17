package com.example.recycle.Ui.dash

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.ThumbnailUtils
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.PermissionChecker
import androidx.core.content.PermissionChecker.checkSelfPermission
import androidx.databinding.DataBindingUtil
import com.example.recycle.Activity.CameraActivity
import com.example.recycle.Activity.MainActivity
import com.example.recycle.R
import com.example.recycle.databinding.FragmentObjectDetectionBinding
import com.google.mlkit.common.model.LocalModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.custom.CustomObjectDetectorOptions

class ObjectDetection : Fragment() {
    val imageSize = 224
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
                    for (label in detectedObject.labels) {
                        val text = label.text
                        binding.ObjectDetectedName.append("$text\n")
                        val conf = label.confidence
//                        confidence.append("$conf\n")
                    }
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