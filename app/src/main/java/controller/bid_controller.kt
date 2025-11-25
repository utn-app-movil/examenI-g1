package controller

import cr.ac.utn.movil.data.MemoryDataManager
import identities.bid_control


class bid_controller {


    fun addBid(bid: bid_control) {
        bid.ID = "bid_${System.currentTimeMillis()}"
        MemoryDataManager.add(bid)
    }

    fun getAllBids(): List<bid_control> {

        return MemoryDataManager.getAll().filterIsInstance<bid_control>()
    }

    fun getBidById(id: String): bid_control? {
        val foundObject = MemoryDataManager.getById(id)
        return foundObject as? bid_control
    }

    fun updateBid(updatedBid: bid_control) {
        MemoryDataManager.update(updatedBid)
    }

    fun deleteBid(bid: bid_control) {
        MemoryDataManager.remove(bid.ID)
    }

    fun isBidAmountValid(newAmount: Double, articleCode: Int): Boolean {
        val bidsForArticle = getAllBids().filter { it.Code == articleCode }
        if (bidsForArticle.isEmpty()) {
            return newAmount > 0
        }
        val highestBid = bidsForArticle.maxByOrNull { it.Amount }
        return newAmount > (highestBid?.Amount ?: 0.0)
    }
}