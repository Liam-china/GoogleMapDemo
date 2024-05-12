package com.veo.googlemapdemo.model

// Google map directions
data class DirectionsResponse(
    val geocoded_waypoints: List<GeoCodedWayPoint> = emptyList(),
    val status: String = "",
    val routes: List<Route> = emptyList()
)

// geocoded_waypoints
data class GeoCodedWayPoint(
    val geocoder_status: String? = "", val place_id: String = "",
    val types: List<String> = emptyList()
)

// routes
data class Route(
    val bounds: Bounds?, val copyrights: String = "",
    val legs: List<Leg> = emptyList(),
    val overview_polyline: OverviewPolyline?
)

// bounds
data class Bounds(val northeast: Bound?, val southwest: Bound?)

// bound
data class Bound(val lat: String = "", val lng: String = "")

data class Leg(
    val distance: Distance, val duration: Duration,
    val end_address: String = "", val end_location: Location,
    val start_address: String = "", val start_location: Location
)

data class Distance(val text: String = "", val value: Int)

data class Duration(val text: String = "", val value: Int)

data class Location(val lat: Double, val lng: Double)

// overview_polyline
data class OverviewPolyline(val points: String = "")
