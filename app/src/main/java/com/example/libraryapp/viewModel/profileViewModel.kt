package com.example.libraryapp.viewModel

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canhub.cropper.CropImage
import com.example.libraryapp.model.FirestoreRepository
import com.example.libraryapp.model.firebaseAuth.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject


@HiltViewModel
class profileViewModel @Inject constructor(
    private val firestoreRepository: FirestoreRepository
): ViewModel(
) {


    private var _userData = MutableStateFlow<UserData?>(null)
    val userData = _userData.asStateFlow()


    init {
        viewModelScope.launch {
            _userData.value = firestoreRepository.getuser()
        }
    }

    // Función para subir imagen
    fun uploadProfileImage(imageUri: Uri, onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {
        viewModelScope.launch {
            try {
                firestoreRepository.uploadImageToFirebase(imageUri, onSuccess, onFailure)
            } catch (e: Exception) {
                onFailure(e)
            }
        }
    }

    fun getProfileImage(onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {
        val userId = firestoreRepository.authConection?.currentUser?.uid ?: return
        firestoreRepository.getProfileImageUrl(userId, onSuccess, onFailure)
    }


    fun createImageUri(context: Context): Uri? {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return try {
            val file = File.createTempFile(
                "JPEG_${timeStamp}_", /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
            )

            // Para compatibilidad con Android N y posteriores, usas FileProvider
            val authority = "${context.packageName}.provider"
            FileProvider.getUriForFile(context, authority, file)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }


}

