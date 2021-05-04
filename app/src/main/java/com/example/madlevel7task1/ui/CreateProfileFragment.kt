package com.example.madlevel7task1.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.madlevel7task1.R
import com.example.madlevel7task1.databinding.FragmentCreateProfileBinding
import com.example.madlevel7task1.vm.ProfileViewModel

// A simple [Fragment] subclass as the default destination in the navigation.
class CreateProfileFragment : Fragment() {

    private var _binding: FragmentCreateProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by activityViewModels()

    private var profileImageUri: Uri? = null

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment.
        _binding = FragmentCreateProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnGallery.setOnClickListener { onGalleryClick() }
        binding.btnConfirm.setOnClickListener { onConfirmClick() }
    }

    private fun onGalleryClick() {
        // Create an Intent with action as ACTION_PICK.
        val galleryIntent = Intent(Intent.ACTION_PICK)

        // Set the type as image/*. This ensures only components of type image are selected.
        galleryIntent.type = "image/*"

        // Start the activity using the gallery intent.
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE)
    }

    companion object {
        const val GALLERY_REQUEST_CODE = 100
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // Check if the result code equals Activity.RESULT_OK and check if the requestCode equals GALLERY_REQUEST_CODE.
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                GALLERY_REQUEST_CODE -> {
                    // Get the URI from the selected image.
                    profileImageUri = data?.data
                    // Bind the ImageView to the selected profile picture.
                    binding.ivProfileImage.setImageURI(profileImageUri)
                }
            }
        }
    }

    private fun CharSequence?.ifNullOrEmpty(default: String) = if (this.isNullOrEmpty()) default else this.toString()

    private fun Uri?.ifNullOrEmpty() = this?.toString() ?: ""

    // Create a profile with the user input and navigate to the ProfileFragment.
    private fun onConfirmClick() {
        viewModel.createProfile(
            binding.etFirstName.text.ifNullOrEmpty(""),
            binding.etLastName.text.ifNullOrEmpty(""),
            binding.etProfileDescription.text.ifNullOrEmpty(""),
            profileImageUri.ifNullOrEmpty()
        )

        observeProfileCreation()

        findNavController().navigate(R.id.ProfileFragment)
    }

    // Display a Toast message which tells the user whether the profile was successfully created.
    private fun observeProfileCreation() {
        viewModel.createSuccess.observe(viewLifecycleOwner, {
            Toast.makeText(activity, R.string.successfully_created_profile, Toast.LENGTH_LONG).show()
            findNavController().popBackStack()
        })

        viewModel.errorText.observe(viewLifecycleOwner, {
            Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
        })
    }
}