package cr.ac.utn.movil.controller

import cr.ac.utn.movil.data.MemoryDataManager
import cr.ac.utn.movil.identities.PayPayroll

class PayPayrollController {

    fun add(payroll: PayPayroll) {
        // Duplicado: mismo empleado + mismo mes
        if (isDuplicated(payroll)) {
            throw Exception("Duplicated data")
        }
        MemoryDataManager.add(payroll)
    }

    fun update(payroll: PayPayroll) {
        MemoryDataManager.update(payroll)
    }

    fun remove(id: String) {
        MemoryDataManager.remove(id)
    }

    fun getAll(): List<PayPayroll> {
        return MemoryDataManager.getAll().filterIsInstance<PayPayroll>()
    }

    fun getById(id: String): PayPayroll? {
        val item = MemoryDataManager.getById(id)
        return if (item is PayPayroll) item else null
    }

    private fun isDuplicated(payroll: PayPayroll): Boolean {
        return getAll().any {
            it.EmployeeNumber.trim().equals(payroll.EmployeeNumber.trim(), ignoreCase = true) &&
                    it.PaymentMonth == payroll.PaymentMonth
        }
    }
}