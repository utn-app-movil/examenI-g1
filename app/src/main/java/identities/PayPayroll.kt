package  cr.ac.utn.movil.identities

class PayPayroll : Person() {

    var EmployeeNumber: String = ""
    var JobPosition: String = ""
    var SalaryAmount: Double = 0.0
    var IbanAccount: String = ""
    var PaymentMonth: Int = 0   // 1..12
    var BankName: String = ""
}