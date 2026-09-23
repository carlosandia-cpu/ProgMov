package com.example.tecsupfit.data

import com.example.tecsupfit.model.ClaseGimnasio

fun obtenerHorarios(clase: ClaseGimnasio): List<String> {
    return listOf(
        clase.horario,
        "8:00 pm",
        "9:00 pm"
    )
}