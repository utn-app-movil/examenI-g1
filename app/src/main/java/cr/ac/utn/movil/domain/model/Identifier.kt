package cr.ac.utn.movil.cr.ac.utn.movil.domain.model

abstract class Identifier (
    open val id: String
){
    abstract val fullName: String
}