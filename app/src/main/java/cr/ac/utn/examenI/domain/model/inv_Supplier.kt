package cr.ac.utn.examenI.domain.model

data class inv_Supplier(
    override val id: String,
    val inv_name: String
) : Identifier(id) {

    override val fullName: String
        get() = inv_name

    override fun toString(): String {
        return inv_name
    }
}