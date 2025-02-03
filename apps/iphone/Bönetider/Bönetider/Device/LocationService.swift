//
//  LocationService.swift
//  Bönetider
//
//  Created by Thanish Nizam on 2025-02-03.
//

import Foundation
import CoreLocation

// LocationService is used to get
class LocationService: NSObject, ObservableObject, CLLocationManagerDelegate {
  // Simplified value for the heading accuracy
  enum HeadingAccuracy {
    case unknown
    case low
    case medium
    case high
    
    // Create an enum instance with given angle
    // TODO: this needs to be calibrated for UX
    init (_ angle: Double) {
      if angle < 0 {
        self = .unknown
      } else if angle < .pi / 16 {
        self = .high
      } else if angle < .pi / 8 {
        self = .medium
      } else if angle < .pi / 4 {
        self = .low
      } else {
        self = .unknown
      }
    }
  }
  
  // MARK: - Properties
  
  // Coordinates of makkah used to calculate the direction
  let makkah: CLLocationCoordinate2D = CLLocationCoordinate2D(
    latitude: 21.4225,
    longitude: 39.8262
  )
  
  let locationManager = CLLocationManager()
  
  @Published
  var heading: Double = 0
  
  @Published
  var location: CLLocationCoordinate2D?
  
  @Published
  var qibla: Double = 0
  
  @Published
  var accuracy = HeadingAccuracy.unknown
  
  // MARK: - Methods
  
  // start updating the location
  func start() {
    locationManager.delegate = self
    locationManager.desiredAccuracy = kCLLocationAccuracyBest
    locationManager.startUpdatingLocation()
    locationManager.startUpdatingHeading()
    locationManager.requestLocation()
  }
  
  // stop updating the location
  func stop() {
    locationManager.stopUpdatingLocation()
    locationManager.stopUpdatingHeading()
  }
  
  // listen to heading changes and update the published field
  func locationManager(_ manager: CLLocationManager, didUpdateHeading newHeading: CLHeading) {
    heading = newHeading.trueHeading * .pi / 180
    accuracy = HeadingAccuracy(newHeading.headingAccuracy * .pi / 180)
  }
  
  // listen to location changes and update the published field
  func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
    location = locations.last?.coordinate

    if let location = location {
      let lat = location.latitude * .pi / 180
      let lon = location.longitude * .pi / 180
      let mlat = makkah.latitude * .pi / 180
      let mlon = makkah.longitude * .pi / 180
      
      let y = sin(mlon - lon) * cos(mlat)
      let x = cos(lat) * sin(mlat) - sin(lat) * cos(mlat) * cos(mlon - lon)
      
      qibla = normalizeRadians(atan2(y, x))
    }
  }
  
  // listen to errors thrown when listening to location changes
  func locationManager(_ manager: CLLocationManager, didFailWithError error: Error) {
    print("Error: \(error)")
  }
  
  // MARK: - Helper Methods
  
  // Helper function to normalize calculated angle in radians
  private func normalizeRadians(_ angle: Double) -> Double {
    return ( angle + 2 * .pi ).truncatingRemainder(dividingBy: 2 * .pi)
  }
}
