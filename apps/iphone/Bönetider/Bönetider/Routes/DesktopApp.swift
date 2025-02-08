//
//  DesktopApp.swift
//  Bönetider
//
//  Created by Thanish Nizam on 2025-02-08.
//

import SwiftUI

struct DesktopApp: View {
  var body: some View {
    NavigationSplitView {
      SettingsRoute()
    }
    detail: {
      HomeRoute()
    }
  }
}
