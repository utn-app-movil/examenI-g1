package cr.ac.utn.examenI.domain.model

enum class Country(
    val displayName: String,
    val code: String,
    val dialCode: String
) {
    ARGENTINA("Argentina", "AR", "+54"),
    BOLIVIA("Bolivia", "BO", "+591"),
    BRASIL("Brasil", "BR", "+55"),
    CANADA("Canadá", "CA", "+1"),
    CHILE("Chile", "CL", "+56"),
    COLOMBIA("Colombia", "CO", "+57"),
    COSTA_RICA("Costa Rica", "CR", "+506"),
    CUBA("Cuba", "CU", "+53"),
    DOMINICAN_REPUBLIC("República Dominicana", "DO", "+1-809"),
    ECUADOR("Ecuador", "EC", "+593"),
    EL_SALVADOR("El Salvador", "SV", "+503"),
    GUATEMALA("Guatemala", "GT", "+502"),
    HONDURAS("Honduras", "HN", "+504"),
    MEXICO("México", "MX", "+52"),
    NICARAGUA("Nicaragua", "NI", "+505"),
    PANAMA("Panamá", "PA", "+507"),
    PARAGUAY("Paraguay", "PY", "+595"),
    PERU("Perú", "PE", "+51"),
    PUERTO_RICO("Puerto Rico", "PR", "+1-787"),
    SPAIN("España", "ES", "+34"),
    UNITED_STATES("Estados Unidos", "US", "+1"),
    URUGUAY("Uruguay", "UY", "+598"),
    VENEZUELA("Venezuela", "VE", "+58");

    override fun toString(): String = displayName
}