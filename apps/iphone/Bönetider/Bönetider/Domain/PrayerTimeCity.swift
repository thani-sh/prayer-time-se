//
//  PrayerTimeCity.swift
//  Bönetider
//
//  Created by Thanish Nizam on 2025-01-31.
//

import SwiftUI

// PrayerTimeCity is the city the prayer time is calculated for
enum PrayerTimeCity: String, CaseIterable, Identifiable {
  case alingsas, amal, angelholm, avesta, bengtsfors, boden, bollnas, boras, borlange
  case eksjo, enkoping, eskilstuna, eslov, falkenberg, falkoping, filipstad, flen
  case gallivare, gavle, gislaved, gnosjo, goteborg, halmstad, haparanda, harnosand
  case hassleholm, helsingborg, hogsby, horby, hudiksvall, hultsfred, jokkmokk, jonkoping
  case kalmar, kalrshamn, karlskoga, karlskrona, karlstad, katrineholm, kiruna, koping
  case kristianstad, kristinehamn, laholm, landskrona, lessebo, lidkoping, linkoping
  case ludvika, lulea, lund, lysekil, malmo, mariestad, marsta, mellerud, mjolby, monsteras
  case munkedal, nassjo, norrkoping, norrtalje, nybro, nykoping, nynashamn, orebro
  case ornskoldsvik, oskarshamn, ostersund, oxelosund, pajala, pitea, ronneby
  case saffle, sala, savsjo, simrishamn, skara, skelleftea, skovde, soderhamn, sodertalje
  case solleftea, solvesborg, stockholm, strangnas, sundsvall, tierp, tranemo, trelleborg
  case trollhattan, uddevalla, ulricehamn, umea, uppsala, vanersborg, varberg, varnamo
  case vasteras, vastervik, vaxjo, vetlanda, vimmerby, visby, ystad
  
  var id: String { self.rawValue }
  
  /// Get localized city name
  func getLabel() -> String {
    NSLocalizedString("prayers_city_" + self.rawValue, comment: "")
  }
}

// PrayerTimeCityPicker is a SwiftUI View for selecting the city
struct PrayerTimeCityPicker: View {
  @AppStorage("PrayerTimeCity") private var selectedCity: String = PrayerTimeCity.stockholm.rawValue
  
  var body: some View {
    List {
      Picker(String(localized: "route_settings_city"), selection: Binding(
        get: { PrayerTimeCity(rawValue: selectedCity) ?? .stockholm },
        set: { selectedCity = $0.rawValue }
      )) {
        ForEach(PrayerTimeCity.allCases, id: \.self) { city in
          Text(city.getLabel()).tag(city)
        }
      }
    }
  }
}

struct PrayerTimeCityPicker_Previews: PreviewProvider {
  static var previews: some View {
    PrayerTimeCityPicker()
  }
}
