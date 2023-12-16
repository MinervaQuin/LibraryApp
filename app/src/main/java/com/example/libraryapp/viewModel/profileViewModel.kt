package com.example.libraryapp.viewModel

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.libraryapp.model.firebaseAuth.FirestoreRepository
import com.example.libraryapp.model.firebaseAuth.FirebaseStorageRepository
import com.example.libraryapp.model.firebaseAuth.UserData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
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
    private val firestoreRepository: FirestoreRepository,
    private val firebaseStorage: FirebaseStorageRepository
): ViewModel(
) {


    private var _userData = MutableStateFlow<UserData?>(null)
    val userData = _userData.asStateFlow()


    private var _logoutCompleted = MutableStateFlow(false)
    var logoutCompleted = _logoutCompleted.asStateFlow()

    init {
        viewModelScope.launch {
            _userData.value = firestoreRepository.getuser()
        }

    }

    // Función para subir imagen
    fun uploadProfileImage(imageUri: Uri, onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {
        viewModelScope.launch {
            try {
                firebaseStorage.uploadImageToFirebase(imageUri, onSuccess, onFailure)
            } catch (e: Exception) {
                onFailure(e)
            }
        }
    }

    fun getProfileImage(onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {
        viewModelScope.launch {
            val userId = firebaseStorage.authConection?.currentUser?.uid
            if (userId != null) {
                try {
                    val url = firebaseStorage.getProfileImageUrl(userId)
                    onSuccess(url)
                } catch (e: Exception) {
                    val url = "https://i.imgur.com/xiL43UU.jpeg"
                    onSuccess(url)
                }
            }
        }
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

    fun logMeOutPLEASE(){
        viewModelScope.launch {
            firestoreRepository.signOut()
            _logoutCompleted.value = true
            delay(1000)
            _logoutCompleted.value = false
        }
    }
    fun resetLogoutCompleted() {
        _logoutCompleted.value = false
    }


}

