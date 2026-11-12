package cr.ac.utn.examenI.domain.model

abstract class Identifier (
    open val id: String
){
    abstract val fullName: String
}