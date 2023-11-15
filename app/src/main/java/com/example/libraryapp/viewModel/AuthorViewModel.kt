package com.example.libraryapp.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.libraryapp.model.Author
import com.example.libraryapp.model.Book
import androidx.compose.runtime.*


class AuthorViewModel : ViewModel() {
    var Autor by mutableStateOf(Author(
        1,
        "Arturo Pérez Reverte","Arturo Pérez-Reverte nació en Cartagena, España, en 1951. Fue reportero de guerra durante veintiún años y cubrió dieciocho conflictos armados para los diarios y la televisión. Con más de veinte millones de lectores en el mundo, traducido a cuarenta idiomas, muchas de sus obras han sido llevadas al cine y la televisión. Hoy comparte su vida entre la literatura, el mar y la navegación. Es miembro de la Real Academia Española y de la Asociación de Escritores de Marina de Francia. Arturo Pérez-Reverte nació en Cartagena, España, en 1951. Fue reportero de guerra durante veintiún años y cubrió dieciocho conflictos armados para los diarios y la televisión. Con más de veinte millones de lectores en el mundo, traducido a cuarenta idiomas, muchas de sus obras han sido llevadas al cine y la televisión. Hoy comparte su vida entre la literatura, el mar y la navegación. Es miembro de la Real Academia Española y de la Asociación de Escritores de Marina de Francia.Arturo Pérez-Reverte nació en Cartagena, España, en 1951. Fue reportero de guerra durante veintiún años y cubrió dieciocho conflictos armados para los diarios y la televisión. Con más de veinte millones de lectores en el mundo, traducido a cuarenta idiomas, muchas de sus obras han sido llevadas al cine y la televisión. Hoy comparte su vida entre la literatura, el mar y la navegación. Es miembro de la Real Academia Española y de la Asociación de Escritores de Marina de Francia.Arturo Pérez-Reverte nació en Cartagena, España, en 1951. Fue reportero de guerra durante veintiún años y cubrió dieciocho conflictos armados para los diarios y la televisión. Con más de veinte millones de lectores en el mundo, traducido a cuarenta idiomas, muchas de sus obras han sido llevadas al cine y la televisión. Hoy comparte su vida entre la literatura, el mar y la navegación. Es miembro de la Real Academia Española y de la Asociación de Escritores de Marina de Francia.",
        arrayOf(
            Book(1,10,"Arturo Pérez Reverte","Linea de fuego","hola hola", 3, "Tapa Dura", 20),
            Book(1,10,"Arturo Pérez Reverte","Linea de fuego","hola hola", 7, "Tapa Dura",20),
            Book(1,10,"Arturo Pérez Reverte","Linea de fuego","hola hola", 7, "Tapa Dura",20),
            Book(1,10,"Arturo Pérez Reverte","Linea de fuego","hola hola", 7, "Tapa Dura",20),
            Book(1,10,"Arturo Pérez Reverte","Linea de fuego","hola hola", 7, "Tapa Dura",20),
            Book(1,10,"Arturo Pérez Reverte","Linea de fuego","hola hola", 7, "Tapa Dura",20),
        )
    ))
        private set

    init {
        // Inicializa la lista de libros con datos de prueba o desde algún origen de datos
    }

}