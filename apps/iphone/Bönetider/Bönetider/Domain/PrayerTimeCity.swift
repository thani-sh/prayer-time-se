//
//  PrayerTimeCity.swift
//  Bönetider
//
//  Created by Thanish Nizam on 2025-01-31.
//

import SwiftUI

// PrayerTimeCity is the city the prayer time is calculated for.
// This value van be used on SwiftUI views using the snippet given below.
//
//   @AppStorage(PrayerTimeCity.key)
//   private var city: PrayerTimeCity = PrayerTimeCity.standard
//
// To update the stored value, set the PrayerTimeCity.current value.
//
//   PrayerTimeCity.current = PrayerTimeCity.uppsala
//
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
  
  // Key used to store notification offset on UserDefaults
  static let key: String = "PrayerTimeCity"
  
  // The default value to use before a value is picked by the user
  static let standard: PrayerTimeCity = .stockholm
  
  // The current prayer time city value stored in UserDefaults
  static var current : PrayerTimeCity {
    get {
      let storedValue = UserDefaults.standard.string(forKey: key)
      return Self(rawValue: storedValue ?? "") ?? PrayerTimeCity.standard
    }
    set { UserDefaults.standard.set(newValue.id, forKey: key) }
  }
  
  // MARK: - Computed Properties
  
  // Unique and machine friendly identifier used for matching
  var id: String { self.rawValue }
  
  // Localized string of the prayer time cuty
  var label: String {
    switch self {
      case .alingsas:
        return String(localized: "prayers_city_alingsas")
      case .amal:
        return String(localized: "prayers_city_amal")
      case .angelholm:
        return String(localized: "prayers_city_angelholm")
      case .avesta:
        return String(localized: "prayers_city_avesta")
      case .bengtsfors:
        return String(localized: "prayers_city_bengtsfors")
      case .boden:
        return String(localized: "prayers_city_boden")
      case .bollnas:
        return String(localized: "prayers_city_bollnas")
      case .boras:
        return String(localized: "prayers_city_boras")
      case .borlange:
        return String(localized: "prayers_city_borlange")
      case .eksjo:
        return String(localized: "prayers_city_eksjo")
      case .enkoping:
        return String(localized: "prayers_city_enkoping")
      case .eskilstuna:
        return String(localized: "prayers_city_eskilstuna")
      case .eslov:
        return String(localized: "prayers_city_eslov")
      case .falkenberg:
        return String(localized: "prayers_city_falkenberg")
      case .falkoping:
        return String(localized: "prayers_city_falkoping")
      case .filipstad:
        return String(localized: "prayers_city_filipstad")
      case .flen:
        return String(localized: "prayers_city_flen")
      case .gallivare:
        return String(localized: "prayers_city_gallivare")
      case .gavle:
        return String(localized: "prayers_city_gavle")
      case .gislaved:
        return String(localized: "prayers_city_gislaved")
      case .gnosjo:
        return String(localized: "prayers_city_gnosjo")
      case .goteborg:
        return String(localized: "prayers_city_goteborg")
      case .halmstad:
        return String(localized: "prayers_city_halmstad")
      case .haparanda:
        return String(localized: "prayers_city_haparanda")
      case .harnosand:
        return String(localized: "prayers_city_harnosand")
      case .hassleholm:
        return String(localized: "prayers_city_hassleholm")
      case .helsingborg:
        return String(localized: "prayers_city_helsingborg")
      case .hogsby:
        return String(localized: "prayers_city_hogsby")
      case .horby:
        return String(localized: "prayers_city_horby")
      case .hudiksvall:
        return String(localized: "prayers_city_hudiksvall")
      case .hultsfred:
        return String(localized: "prayers_city_hultsfred")
      case .jokkmokk:
        return String(localized: "prayers_city_jokkmokk")
      case .jonkoping:
        return String(localized: "prayers_city_jonkoping")
      case .kalmar:
        return String(localized: "prayers_city_kalmar")
      case .kalrshamn:
        return String(localized: "prayers_city_kalrshamn")
      case .karlskoga:
        return String(localized: "prayers_city_karlskoga")
      case .karlskrona:
        return String(localized: "prayers_city_karlskrona")
      case .karlstad:
        return String(localized: "prayers_city_karlstad")
      case .katrineholm:
        return String(localized: "prayers_city_katrineholm")
      case .kiruna:
        return String(localized: "prayers_city_kiruna")
      case .koping:
        return String(localized: "prayers_city_koping")
      case .kristianstad:
        return String(localized: "prayers_city_kristianstad")
      case .kristinehamn:
        return String(localized: "prayers_city_kristinehamn")
      case .laholm:
        return String(localized: "prayers_city_laholm")
      case .landskrona:
        return String(localized: "prayers_city_landskrona")
      case .lessebo:
        return String(localized: "prayers_city_lessebo")
      case .lidkoping:
        return String(localized: "prayers_city_lidkoping")
      case .linkoping:
        return String(localized: "prayers_city_linkoping")
      case .ludvika:
        return String(localized: "prayers_city_ludvika")
      case .lulea:
        return String(localized: "prayers_city_lulea")
      case .lund:
        return String(localized: "prayers_city_lund")
      case .lysekil:
        return String(localized: "prayers_city_lysekil")
      case .malmo:
        return String(localized: "prayers_city_malmo")
      case .mariestad:
        return String(localized: "prayers_city_mariestad")
      case .marsta:
        return String(localized: "prayers_city_marsta")
      case .mellerud:
        return String(localized: "prayers_city_mellerud")
      case .mjolby:
        return String(localized: "prayers_city_mjolby")
      case .monsteras:
        return String(localized: "prayers_city_monsteras")
      case .munkedal:
        return String(localized: "prayers_city_munkedal")
      case .nassjo:
        return String(localized: "prayers_city_nassjo")
      case .norrkoping:
        return String(localized: "prayers_city_norrkoping")
      case .norrtalje:
        return String(localized: "prayers_city_norrtalje")
      case .nybro:
        return String(localized: "prayers_city_nybro")
      case .nykoping:
        return String(localized: "prayers_city_nykoping")
      case .nynashamn:
        return String(localized: "prayers_city_nynashamn")
      case .orebro:
        return String(localized: "prayers_city_orebro")
      case .ornskoldsvik:
        return String(localized: "prayers_city_ornskoldsvik")
      case .oskarshamn:
        return String(localized: "prayers_city_oskarshamn")
      case .ostersund:
        return String(localized: "prayers_city_ostersund")
      case .oxelosund:
        return String(localized: "prayers_city_oxelosund")
      case .pajala:
        return String(localized: "prayers_city_pajala")
      case .pitea:
        return String(localized: "prayers_city_pitea")
      case .ronneby:
        return String(localized: "prayers_city_ronneby")
      case .saffle:
        return String(localized: "prayers_city_saffle")
      case .sala:
        return String(localized: "prayers_city_sala")
      case .savsjo:
        return String(localized: "prayers_city_savsjo")
      case .simrishamn:
        return String(localized: "prayers_city_simrishamn")
      case .skara:
        return String(localized: "prayers_city_skara")
      case .skelleftea:
        return String(localized: "prayers_city_skelleftea")
      case .skovde:
        return String(localized: "prayers_city_skovde")
      case .soderhamn:
        return String(localized: "prayers_city_soderhamn")
      case .sodertalje:
        return String(localized: "prayers_city_sodertalje")
      case .solleftea:
        return String(localized: "prayers_city_solleftea")
      case .solvesborg:
        return String(localized: "prayers_city_solvesborg")
      case .stockholm:
        return String(localized: "prayers_city_stockholm")
      case .strangnas:
        return String(localized: "prayers_city_strangnas")
      case .sundsvall:
        return String(localized: "prayers_city_sundsvall")
      case .tierp:
        return String(localized: "prayers_city_tierp")
      case .tranemo:
        return String(localized: "prayers_city_tranemo")
      case .trelleborg:
        return String(localized: "prayers_city_trelleborg")
      case .trollhattan:
        return String(localized: "prayers_city_trollhattan")
      case .uddevalla:
        return String(localized: "prayers_city_uddevalla")
      case .ulricehamn:
        return String(localized: "prayers_city_ulricehamn")
      case .umea:
        return String(localized: "prayers_city_umea")
      case .uppsala:
        return String(localized: "prayers_city_uppsala")
      case .vanersborg:
        return String(localized: "prayers_city_vanersborg")
      case .varberg:
        return String(localized: "prayers_city_varberg")
      case .varnamo:
        return String(localized: "prayers_city_varnamo")
      case .vasteras:
        return String(localized: "prayers_city_vasteras")
      case .vastervik:
        return String(localized: "prayers_city_vastervik")
      case .vaxjo:
        return String(localized: "prayers_city_vaxjo")
      case .vetlanda:
        return String(localized: "prayers_city_vetlanda")
      case .vimmerby:
        return String(localized: "prayers_city_vimmerby")
      case .visby:
        return String(localized: "prayers_city_visby")
      case .ystad:
        return String(localized: "prayers_city_ystad")
    }
  }
}
