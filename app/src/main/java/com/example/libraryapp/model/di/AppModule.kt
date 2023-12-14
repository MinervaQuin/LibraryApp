package com.example.libraryapp.model.di

import android.content.Context
import com.example.libraryapp.model.firebaseAuth.FirestoreRepository
import com.example.libraryapp.model.validationModels.emailValidationUseCase.ValidateEmail
import com.example.libraryapp.model.validationModels.emailValidationUseCase.ValidatePassword
import com.example.libraryapp.model.validationModels.emailValidationUseCase.ValidateRepeatedPassword
import com.example.libraryapp.model.validationModels.emailValidationUseCase.ValidateTerms
import com.example.libraryapp.model.validationModels.emailValidationUseCase.ValidateUserName
import com.example.libraryapp.model.firebaseAuth.EmailAuthUiClient
import com.example.libraryapp.model.firebaseAuth.FirebaseStorageImpl
import com.example.libraryapp.model.firebaseAuth.FirebaseStorageRepository
import com.example.libraryapp.model.firebaseAuth.FirestoreRepositoryImpl
import com.example.libraryapp.model.firebaseAuth.GoogleAuthUiClient
import com.example.libraryapp.model.firebaseAuth.OrdersFirebaseRepository
import com.example.libraryapp.model.firebaseAuth.OrdersFirebaseRepositoryImpl
import com.example.libraryapp.model.firebaseAuth.UserData
import com.example.libraryapp.model.validationModels.paymentMethodUseCase.ValidateCard
import com.example.libraryapp.model.validationModels.paymentMethodUseCase.ValidateCvv
import com.example.libraryapp.model.validationModels.paymentMethodUseCase.ValidateExpirationDate
import com.example.libraryapp.model.validationModels.paymentMethodUseCase.ValidateOwnerName
import com.example.libraryapp.model.validationModels.shipmentValidationUseCase.ValidateAdress
import com.example.libraryapp.model.validationModels.shipmentValidationUseCase.ValidateDNI
import com.example.libraryapp.model.validationModels.shipmentValidationUseCase.ValidateName
import com.example.libraryapp.model.validationModels.shipmentValidationUseCase.ValidatePhoneNumber
import com.example.libraryapp.model.validationModels.shipmentValidationUseCase.ValidateZipCode
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideUserData(): UserData {
        // Obtiene los datos del usuario de alguna fuente
        return UserData("userId", "userName", "userEmail")
    }

    @Provides
    @Singleton
    fun provideGoogleAuthUiClient(@ApplicationContext context: Context): GoogleAuthUiClient {
        return GoogleAuthUiClient(
            context = context,
            oneTapClient = Identity.getSignInClient(context)
        )
    }
    @Provides
    @Singleton
    fun provideFirebaseStorage(): FirebaseStorage = FirebaseStorage.getInstance()
    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideOneTapClient(@ApplicationContext context: Context): SignInClient {
        return Identity.getSignInClient(context)
    }

    @Provides
    @Singleton
    fun provideValidateEmail(): ValidateEmail = ValidateEmail()

    @Provides
    @Singleton
    fun provideValidatePassword(): ValidatePassword = ValidatePassword()

    @Provides
    @Singleton
    fun provideValidateRepeatedPassword(): ValidateRepeatedPassword = ValidateRepeatedPassword()

    @Provides
    @Singleton
    fun provideValidateTerms(): ValidateTerms = ValidateTerms()

    @Provides
    @Singleton
    fun provideValidateUserName(): ValidateUserName = ValidateUserName()
    @Provides
    @Singleton
    fun provideFirestoreRepository(
        firebaseFirestore: FirebaseFirestore,
        firebaseAuth: FirebaseAuth,
        storage: FirebaseStorage,
        tapClient: SignInClient,
        @ApplicationContext appContext: Context
    ): FirestoreRepository = FirestoreRepositoryImpl(firebaseFirestore, firebaseAuth, storage, tapClient, appContext)

    @Provides
    @Singleton
    fun provideFirebaseStorageRepository(
        firebaseAuth: FirebaseAuth,
        storage: FirebaseStorage
    ): FirebaseStorageRepository {
        return FirebaseStorageImpl(firebaseAuth, storage)
    }

    @Provides
    @Singleton
    fun provideEmailAuthUiClient(auth: FirebaseAuth, storageRepository: FirebaseStorageRepository): EmailAuthUiClient {
        return EmailAuthUiClient(auth,storageRepository)
    }

    @Provides
    @Singleton
    fun provideOrdersRepository(
        firestore: FirebaseFirestore,
        firebaseAuth: FirebaseAuth
    ): OrdersFirebaseRepository{
        return OrdersFirebaseRepositoryImpl(firestore, firebaseAuth)
    }

    @Provides
    @Singleton
    fun provideValidateAdress(): ValidateAdress = ValidateAdress()

    @Provides
    @Singleton
    fun provideValidatePhoneNumber(): ValidatePhoneNumber = ValidatePhoneNumber()

    @Provides
    @Singleton
    fun provideValidateZipCode(): ValidateZipCode = ValidateZipCode()

    @Provides
    @Singleton
    fun provideValidateName(): ValidateName = ValidateName()

    @Provides
    @Singleton
    fun provideValidateCard(): ValidateCard = ValidateCard()

    @Provides
    @Singleton
    fun provideValidateCvv(): ValidateCvv = ValidateCvv()

    @Provides
    @Singleton
    fun provideExpirationDate(): ValidateExpirationDate = ValidateExpirationDate()

    @Provides
    @Singleton
    fun provideOwnerName(): ValidateOwnerName = ValidateOwnerName()


}