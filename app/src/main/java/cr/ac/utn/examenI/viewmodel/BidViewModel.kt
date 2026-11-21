
package cr.ac.utn.examenI.viewmodel

import androidx.lifecycle.ViewModel
import cr.ac.utn.examenI.data.memory.MemoryDataManager
import cr.ac.utn.examenI.domain.model.Bid
import java.time.LocalDateTime
import java.util.UUID

class BidViewModel : ViewModel() {

    companion object {

        private val repository = MemoryDataManager()

        fun generateId(): String {
            return UUID.randomUUID().toString()
        }

        fun getAll(): List<Bid> {
            return repository.getAll().filterIsInstance<Bid>()
        }

        fun getById(id: String): Bid? {
            return repository.getById(id) as? Bid
        }

        fun save(bid: Bid): Boolean {
            if (getById(bid.id) != null) {
                return false
            }

            if (!isValidBid(bid)) {
                return false
            }

            repository.add(bid)
            return true
        }

        fun update(bid: Bid): Boolean {
            if (getById(bid.id) == null) {
                return false
            }

            if (!isValidBid(bid)) {
                return false
            }

            repository.update(bid)
            return true
        }

        fun delete(id: String): Boolean {
            if (getById(id) == null) {
                return false
            }

            repository.delete(id)
            return true
        }

        private fun isValidBid(bid: Bid): Boolean {

            if (bid.personId.isBlank() ||
                bid.name.isBlank() ||
                bid.articleCode.isBlank() ||
                bid.description.isBlank()
            ) {
                return false
            }

            if (!bid.bidAmount.isFinite() ||
                bid.bidAmount <= 0.0
            ) {
                return false
            }

            if (bid.bidDateTime.isAfter(LocalDateTime.now())) {
                return false
            }

            val highestBid = getAll()
                .filter {
                    it.articleCode == bid.articleCode &&
                            it.id != bid.id
                }
                .maxOfOrNull { it.bidAmount } ?: 0.0

            return bid.bidAmount > highestBid
        }
    }
}
