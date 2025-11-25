package cr.ac.utn.movil.data.identities

import cr.ac.utn.movil.identities.Identifier

data class ProdProductionOrder(
    var id: Int,
    var orderNumber: String,
    var products: List<String>,
    var quantity: Int,
    var startDate: String,
    var endDate: String,
    var company: String,
    override val FullDescription: String,
    override val FullName: String
) : Identifier()
