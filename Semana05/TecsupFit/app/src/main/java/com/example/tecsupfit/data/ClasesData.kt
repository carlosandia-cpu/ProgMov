package com.example.tecsupfit.data

import com.example.tecsupfit.model.ClaseGimnasio

val clasesDisponibles = listOf(
    ClaseGimnasio(
        id = 1,
        nombre = "Yoga funcional",
        horario = "7:00 am",
        sala = "Sala 2",
        duracion = "45 min",
        descripcion = "Clase enfocada en flexibilidad, equilibrio y bienestar físico.",
        cuposDisponibles = 10,
        categoria = "Hoy"
    ),
    ClaseGimnasio(
        id = 2,
        nombre = "Cross Training",
        horario = "6:00 pm",
        sala = "Sala 1",
        duracion = "45 min",
        descripcion = "Entrenamiento funcional de alta intensidad con cupos limitados.",
        cuposDisponibles = 8,
        categoria = "Hoy"
    ),
    ClaseGimnasio(
        id = 3,
        nombre = "Spinning",
        horario = "7:30 pm",
        sala = "Sala 3",
        duracion = "50 min",
        descripcion = "Entrenamiento cardiovascular realizado en bicicleta estacionaria.",
        cuposDisponibles = 12,
        categoria = "Esta semana"
    )
)