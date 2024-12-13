package hr.foi.air.baufind.ws.network

interface TokenProvider {
    fun getToken(): String?
}
