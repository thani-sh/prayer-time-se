//
//  FadingHorizontalDivider.swift
//  Bönetider
//
//  Created by Thanish Nizam on 2025-02-01.
//

import SwiftUI

struct FadingHorizontalDivider: View {
  var body: some View {
    Rectangle()
      .fill(LinearGradient(
        gradient: Gradient(stops: [
          .init(color: .clear, location: 0),
          .init(color: .primary.opacity(0.1), location: 0.5),
          .init(color: .clear, location: 1)
        ]),
        startPoint: .leading,
        endPoint: .trailing
      ))
      .frame(height: 1)
      .frame(maxWidth: 320)
  }
}
