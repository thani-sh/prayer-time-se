//
//  RelatedLinksList.swift
//  Bönetider
//
//  Created by Thanish Nizam on 2025-01-31.
//

import SwiftUI

struct RelatedLinksList: View {
    var body: some View {
      Link(destination: URL(string: "https://github.com/thani-sh/prayer-time-se")!) {
        HStack {
          Image (systemName: "link")
          Text(String(localized: "route_settings_open_github"))
        }
      }
      Link(destination: URL(string: "https://xn--bnetider-n4a.nu")!) {
        HStack {
          Image (systemName: "link")
          Text(String(localized: "route_settings_open_website"))
        }
      }
      Link(destination: URL(string: "https://xn--bnetider-n4a.nu/docs/privacy")!) {
        HStack {
          Image (systemName: "link")
          Text(String(localized: "route_settings_open_privacy"))
        }
      }
    }
}
