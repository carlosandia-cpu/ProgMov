package pe.tecsup.clinicasaludplus

data class Cita(
    val medico: Medico,
    val fecha: String,
    val hora: String,
    val estado: String = "Confirmada"
)