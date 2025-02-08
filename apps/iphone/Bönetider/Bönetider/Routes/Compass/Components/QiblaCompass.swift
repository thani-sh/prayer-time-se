//
//  QiblaCompass.swift
//  Bönetider
//
//  Created by Thanish Nizam on 2025-02-03.
//

import SwiftUI

struct QiblaCompass: View {
  // MARK: - Properties
  
  let qibla: Double
  let heading: Double
  let accuracy: LocationService.HeadingAccuracy
  
  // MARK: - Private Properties
  
  // The size of dashes on the compass
  private let dashWidth: CGFloat = 1
  
  // MARK: - Computed Properties
  
  // Text to show on UI based on sensor accuracy
  private var priorityText: String {
    switch accuracy {
      case .high:
        return String(localized: "route_compass_priority_high")
      case .medium:
        return String(localized: "route_compass_priority_medium")
      case .low:
        return String(localized: "route_compass_priority_low")
      default:
        return String(localized: "route_compass_priority_error")
    }
  }
  
  // Color to use on UI based on sensor accuracy
  private var priorityColor: Color {
    switch accuracy {
      case .high:
        return .blue
      case .medium:
        return .blue
      case .low:
        return .gray
      default:
        return .red
    }
  }
  
  // MARK: - Method
  
  private func dashSegmentLength(geometry: GeometryProxy) -> CGFloat {
    let circumference = 2 * .pi * geometry.radius
    return (circumference / 60) - dashWidth
  }
  
  // MARK: - Components
  
  private func compassCircle(geometry: GeometryProxy) -> some View {
    Circle()
      .strokeBorder(
        Color.gray.opacity(0.3),
        style: StrokeStyle(
          lineWidth: 16,
          dash: [dashWidth, dashSegmentLength(geometry: geometry)],
          dashPhase: 0
        )
      )
      .rotationEffect(.radians(-heading))
  }
  
  private func northLabel(center: CGPoint, radius: CGFloat) -> some View {
    Text(String(localized: "route_compass_north"))
      .font(.system(size: 16))
      .position(x: center.x, y: center.y - radius - 20)
      .rotationEffect(.radians(-heading))
  }
  
  private func qiblaArrow(center: CGPoint, radius: CGFloat) -> some View {
    let arrowPath = Path { path in
      let tip = CGPoint(x: center.x, y: center.y - radius)
      path.move(to: center)
      path.addLine(to: tip)
      path.addLine(to: tip.applying(.init(translationX: 5, y: 8)))
      path.move(to: tip)
      path.addLine(to: tip.applying(.init(translationX: -5, y: 8)))
    }

    return arrowPath
      .stroke(priorityColor, style: StrokeStyle(lineWidth: 3, lineCap: .square))
      .rotationEffect(.radians(-(heading - qibla)))
  }
  
  private func priorityIndicator(center: CGPoint, radius: CGFloat) -> some View {
    Text(priorityText)
      .font(.system(size: 12))
      .foregroundColor(priorityColor)
      .position(x: center.x, y: center.y + radius + 100)
  }
  
  // MARK: - Renderer
  
  var body: some View {
    let padding: CGFloat = if UIDevice.current.userInterfaceIdiom == .pad { 256 } else { 64 }
    
    VStack {
      GeometryReader { geometry in
        let center = geometry.center
        let radius = geometry.radius
        
        ZStack {
          compassCircle(geometry: geometry)
          if accuracy != .unknown {
            northLabel(center: center, radius: radius)
            qiblaArrow(center: center, radius: radius)
          }
          priorityIndicator(center: center, radius: radius)
        }
      }
    }
    .padding(padding)
  }
}

// MARK: - GeometryProxy Extension

private extension GeometryProxy {
  var center: CGPoint {
    CGPoint(x: size.width/2, y: size.height/2)
  }
  
  var radius: CGFloat {
    min(size.width, size.height) / 2
  }
}
