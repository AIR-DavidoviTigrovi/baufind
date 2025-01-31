package hr.foi.air.baufind.helpers

import hr.foi.air.baufind.core.map.MapProvider
import hr.foi.air.baufind.example_map.ExampleMapProvider
import hr.foi.air.baufind.google_map.GoogleMapProvider
import hr.foi.air.baufind.open_street_map.OpenStreetMapProvider

class MapHelper {
    companion object {
        val mapProviders = discoverMapProviders()
        var mapProvider: MapProvider = mapProviders[2]

        private fun discoverMapProviders(): List<MapProvider> {
            val mapProviders = mutableListOf<MapProvider>()

            mapProviders.add(ExampleMapProvider())
            mapProviders.add(GoogleMapProvider())
            mapProviders.add(OpenStreetMapProvider())

            return mapProviders
        }
    }
}