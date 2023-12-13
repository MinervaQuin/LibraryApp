package com.example.libraryapp.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.libraryapp.model.BookDetailsUiState
import com.example.libraryapp.model.firebaseAuth.FirestoreRepository
import com.example.libraryapp.model.LibraryAppState
import com.example.libraryapp.model.firebaseAuth.UserData
import com.example.libraryapp.model.resources.Review
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.lang.Math.ceil
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject


@HiltViewModel
class BookDetailsViewModel @Inject constructor(
    private val firestoreRepository: FirestoreRepository,
    val libraryAppState: LibraryAppState

): ViewModel(){
    private val _bookUiState = MutableStateFlow(BookDetailsUiState())
    val bookUiState: StateFlow<BookDetailsUiState> = _bookUiState.asStateFlow()

    private var _userData = MutableStateFlow<UserData?>(null)
    val userData = _userData.asStateFlow()

    private var userReview : Review? = null
    var reviews: List<Review?> = emptyList()
    var refreshReviews = MutableStateFlow<Boolean>(false )

    init {
        _bookUiState.update { currentState ->
            currentState.copy(
                reviewScore = libraryAppState.getBook()?.score ?: 0
            )
        }
    }

    fun setNewAutorId(new : String){
        libraryAppState.setautorId(new)
    }
    fun sendReview(comment: String, reviewScore: Int){
        _bookUiState.update { currentState ->
            currentState.copy(
                comment = comment,
                showDialog = false
            )
        }

        viewModelScope.launch {
            try {
                val newData = mapOf(
                    "description" to comment,
                    "score" to reviewScore,
                )
                // Perform asynchronous operation
                if (userReview != null) {
                    firestoreRepository.updateReview(bookId= libraryAppState.getBookId(), reviewId = userReview!!.reviewId, newData=newData)
                    userReview!!.description = comment
                    Log.d("firebase", "REVIEW UPDATED")
                }else {
                    val newReview = Review(
                        userId=_userData!!.value!!.userId,
                        userName = _userData!!.value!!.userName?: "user",
                        score = _bookUiState.value.reviewScore.toDouble(),
                        description = _bookUiState.value.comment,
                        date = LocalDate.now()
                    )

                    firestoreRepository.upLoadReview(bookId= libraryAppState.getBookId(), review = newReview)
                }
                getReviews(libraryAppState.getBookId())
                refreshReviews.value = !refreshReviews.value

                var reviewSum = 0
                for (review in reviews) {
                    if (review != null) {
                        reviewSum += ceil(review.score).toInt()
                    }
                }


                var newScore= reviewSum/reviews.size
                Log.d("Reviews", "newScore: " + newScore)
                firestoreRepository.uploadBookScore(bookId= libraryAppState.getBookId(), newScore= ceil(newScore.toDouble()).toInt())
                val currentBook = libraryAppState.getBook()

                val updatedBook = currentBook?.apply {
                    score = newScore
                }

                if (updatedBook != null) {
                    libraryAppState.setBook(updatedBook)
                }


            } catch (e: Exception) {
                Log.d("firebase", "REVIEW NOT UPDATED/CREATED")
                Log.e("firebase", "Error al crear review", e)
            }
        }

    }

    fun updateRating(reviewScore: Int){
        _bookUiState.update { currentState ->
            currentState.copy(
                reviewScore = reviewScore,
            )
        }
    }

    suspend fun getReviews(bookId: String): List<Review?>{

        reviews = firestoreRepository.getReviewsFromABook(bookId)

        reviews = reviews.filterNotNull()

        // Ordenar las revisiones por fecha en orden ascendente (de la más antigua a la más reciente)
        reviews = reviews.sortedByDescending { it?.date }
        return reviews
    }
//
//    fun getUserReview(): Review?{
//        userReview = getReviewFromUser()
//        Log.d("firebase", "qweret" + (userReview?.userId ?: "NO" ))
//        return userReview
//    }



    fun updateComment(comment: String){
        _bookUiState.update { currentState ->
            currentState.copy(
                comment = comment,
            )
        }
    }

    fun closeDialog(){
        _bookUiState.update { currentState ->

            currentState.copy(
                showDialog = false
            )
        }
    }

    fun showDialog(value: Boolean){
        _bookUiState.update { currentState ->
            currentState.copy(
                showDialog = value
            )
        }
    }


    fun getFormattedTimeAgo(commentDateTime: LocalDate?): String {
        val commentCast = LocalDateTime.of(commentDateTime, LocalTime.MIDNIGHT)

        val currentDateTime = LocalDateTime.now()
        val difference = Duration.between(commentCast, currentDateTime)

        return when {
            difference.seconds < 60 -> "hace un momento"
            difference.toMinutes() < 60 -> "hace ${difference.toMinutes()} minutos"
            difference.toHours() < 24 -> "hace ${difference.toHours()} horas"
            commentCast.toLocalDate() == currentDateTime.toLocalDate() -> "hoy"
            else -> commentCast.format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        }
    }

    fun getReviewFromUser(): Review? {
        viewModelScope.launch {
            try {
                // Perform asynchronous operation

                _userData.value = firestoreRepository.getuser()
                _userData.value?.let { userReview = firestoreRepository.getReviewFromUserId(libraryAppState.getBookId(), it.userId) }
//
//                if (userReview != null) {
//                    Log.d("firebase", "Review conseguida" + userReview!!.description)
//                }

            } catch (e: Exception) {
                Log.d("firebase", "Review error")
            }
        }
        return userReview
    }

}