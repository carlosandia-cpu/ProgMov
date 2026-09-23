package pe.tecsup.clinicasaludplus

data class Medico(
    val nombre: String,
    val especialidad: String,
    val calificacion: String
)

val medicos = listOf(
    Medico("Dra. Ana Torres", "Cardiología", "4.9"),
    Medico("Dr. Luis Vega", "Pediatría", "4.7"),
    Medico("Dra. Rosa Díaz", "Dermatología", "4.8")
)