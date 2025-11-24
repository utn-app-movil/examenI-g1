package controller

import model.fli_Flight

class fli_FlightController {

    companion object {
        private val fli_flights = mutableListOf<fli_Flight>()
    }

    fun validateFlightNumber(flightNumber: String): Boolean {
        val pattern = "^[A-Z]{2}\\d{4}$"
        return flightNumber.matches(pattern.toRegex())
    }

    fun validateDateTime(dateTime: String): Boolean {
        // Implement date and time validation logic here
        return true
    }

    fun validateCountries(origin: String, destination: String): Boolean {
        return origin != destination
    }

    fun registerFlight(flight: fli_Flight) {
        fli_flights.add(flight)
    }

    fun getFlights(): List<fli_Flight> {
        return fli_flights
    }

    fun updateFlight(updatedFlight: fli_Flight) {
        val index = fli_flights.indexOfFirst { it.fli_id == updatedFlight.fli_id }
        if (index != -1) {
            fli_flights[index] = updatedFlight
        }
    }

    fun deleteFlight(flightId: String) {
        fli_flights.removeAll { it.fli_id == flightId }
    }
}