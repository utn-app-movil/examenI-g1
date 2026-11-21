
package cr.ac.utn.examenI.viewmodel

import cr.ac.utn.examenI.data.memory.MemoryDataManager
import cr.ac.utn.examenI.domain.model.PayEmployee

object PayViewModel {

    private val repository = MemoryDataManager()

    fun getAll(): List<PayEmployee> {
        return repository.getAll()
            .filterIsInstance<PayEmployee>()
    }

    fun getById(id: String): PayEmployee? {
        return repository.getById(id) as? PayEmployee
    }

    fun isDuplicate(id: String): Boolean {
        return getById(id) != null
    }

    fun isEmployeeNumberDuplicate(
        employeeNumber: String,
        excludedId: String? = null
    ): Boolean {
        return getAll().any {
            it.employeeNum == employeeNumber &&
                    it.id != excludedId
        }
    }

    fun save(employee: PayEmployee): Boolean {
        if (isDuplicate(employee.id) ||
            isEmployeeNumberDuplicate(employee.employeeNum)) {
            return false
        }

        repository.add(employee)
        return true
    }

    fun update(employee: PayEmployee): Boolean {
        if (getById(employee.id) == null ||
            isEmployeeNumberDuplicate(employee.employeeNum, employee.id)) {
            return false
        }

        repository.update(employee)
        return true
    }

    fun delete(id: String): Boolean {
        if (getById(id) == null) return false

        repository.delete(id)
        return true
    }
}
