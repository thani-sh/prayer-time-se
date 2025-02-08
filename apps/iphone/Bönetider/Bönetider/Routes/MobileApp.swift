//
//  MobileApp.swift
//  Bönetider
//
//  Created by Thanish Nizam on 2025-02-08.
//

import SwiftUI

struct MobileApp: View {
  var body: some View {
    if UIDevice.current.userInterfaceIdiom == .pad {
      NavigationSplitView {
        SettingsRoute()
      }
      detail: {
        TabView {
          ForEach(ContentRoutes.indices, id: \.self) { index in
            let route = ContentRoutes[index]
            AnyView(route.content).tabItem {
              Image(systemName: route.icon)
              Text(route.text)
            }
          }
        }
      }
    } else {
      TabView {
        ForEach(AvailableRoutes.indices, id: \.self) { index in
          let route = AvailableRoutes[index]
          AnyView(route.content).tabItem {
            Image(systemName: route.icon)
            Text(route.text)
          }
        }
      }
    }
  }
}
