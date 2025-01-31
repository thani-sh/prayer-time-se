//
//  BottomNavigation.swift
//  Bönetider
//
//  Created by Thanish Nizam on 2025-01-31.
//

import SwiftUI

struct Navigation: View {
    var body: some View {
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

#Preview {
    Navigation()
}
