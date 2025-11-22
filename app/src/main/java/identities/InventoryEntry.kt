package cr.ac.utn.movil.identities

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.text.format

class InventoryEntry() : Identifier() {

    // Person who made the entry
    private var _person: Person = Person()

    // Product information
    private var _productCode: String = ""
    private var _productName: String = ""
    private var _quantity: Int = 0

    // Entry details
    private var _entryDateTime: LocalDateTime? = null
    private var _supplier: String = ""
    private var _unitCost: Double = 0.0

    // Properties
    var Person: Person
        get() = this._person
        set(value) { this._person = value }

    var ProductCode: String
        get() = this._productCode
        set(value) { this._productCode = value }

    var ProductName: String
        get() = this._productName
        set(value) { this._productName = value }

    var Quantity: Int
        get() = this._quantity
        set(value) { this._quantity = value }

    var EntryDateTime: LocalDateTime?
        get() = this._entryDateTime
        set(value) { this._entryDateTime = value }

    var Supplier: String
        get() = this._supplier
        set(value) { this._supplier = value }

    var UnitCost: Double
        get() = this._unitCost
        set(value) { this._unitCost = value }

    // Calculated property - Total cost
    val TotalCost: Double
        get() = this._quantity * this._unitCost

    // Formatted date for display
    val FormattedDateTime: String
        get() = this._entryDateTime?.format(
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
        ) ?: "N/A"

    override val FullDescription: String
        get() = "Product: $ProductName ($ProductCode) | Qty: $Quantity | Supplier: $Supplier | " +
                "Person: ${Person.FullName} | Date: $FormattedDateTime | Total: $${"%.2f".format(TotalCost)}"

    override val FullName: String
        get() = "$ProductName - $ProductCode"
}