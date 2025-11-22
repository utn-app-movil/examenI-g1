package cr.ac.utn.movil.controller

import cr.ac.utn.movil.data.identities.ProdProductionOrder

class ProdProductionOrderController {

    private val orders = mutableListOf<ProdProductionOrder>()
    private var nextId = 1

    fun create(order: ProdProductionOrder): Boolean {
        if (orders.any { it.orderNumber == order.orderNumber }) return false
        order.id = nextId++
        orders.add(order)
        return true
    }

    fun update(order: ProdProductionOrder): Boolean {
        val index = orders.indexOfFirst { it.orderNumber == order.orderNumber }
        if (index == -1) return false
        orders[index] = order
        return true
    }

    fun delete(orderNumber: String): Boolean {
        val index = orders.indexOfFirst { it.orderNumber == orderNumber }
        if (index == -1) return false
        orders.removeAt(index)
        return true
    }

    fun get(orderNumber: String): ProdProductionOrder? {
        return orders.firstOrNull { it.orderNumber == orderNumber }
    }

    fun getAll(): List<ProdProductionOrder> = orders.toList()
}
