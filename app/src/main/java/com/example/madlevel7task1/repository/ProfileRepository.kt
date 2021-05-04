package com.example.madlevel7task1.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.madlevel7task1.model.Profile
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout

class ProfileRepository {

    // Make sure there is an instance of Firestore.
    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    // Reference to the document that is needed to store/retrieve the profile.
    private var profileDocument = firestore.collection("profiles").document("profile")

    private val _profile: MutableLiveData<Profile> = MutableLiveData()

    val profile: LiveData<Profile>
    get() = _profile

    // The CreateProfileFragment can use this to see if creation succeeded.
    private val _createSuccess: MutableLiveData<Boolean> = MutableLiveData()

    val createSuccess: LiveData<Boolean>
    get() = _createSuccess

    suspend fun getProfile() {
        try {
            withTimeout(5000) {
                val data = profileDocument.get().await()

                val firstName = data.getString("firstName").toString()
                val lastName = data.getString("lastName").toString()
                val description = data.getString("description").toString()
                val imageUri = data.getString("imageUri").toString()

                _profile.value = Profile(firstName, lastName, description, imageUri)
            }
        } catch (e: Exception) {
            throw ProfileRetrievalError("Retrieval-firebase-task was unsuccessful")
        }
    }

    suspend fun createProfile(profile: Profile) {
        // Persist data to Firestore.
        try {
            withTimeout(5000) {
                profileDocument.set(profile)

                _createSuccess.value = true
            }
        } catch (e: Exception) {
            throw ProfileSaveError(e.message.toString(), e)
        }
    }

    class ProfileSaveError(message: String, cause: Throwable) : Exception(message, cause)
    class ProfileRetrievalError(message: String) : Exception(message)
}