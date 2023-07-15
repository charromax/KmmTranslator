//
//  Colors.swift
//  iosApp
//
//  Created by Manuel Gonzalez on 13/06/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import SwiftUI
import shared

// extension of color used to be able to define colors using our shared values
// returns SwiftUI color type from kotlin hex values
extension Color {
    init(hex: Int64, alpha: Double = 1) {
        self.init(
            .sRGB,
             red: Double((hex >> 16) & 0xff) / 255,
             green: Double((hex >> 08) & 0xff) / 255,
             blue: Double((hex >> 00) & 0xff) / 255,
            opacity: alpha
        )
    }
    
    // define theme using shared color values
    private static let colors = Colors()
    static let lightBlue = Color(hex: colors.LightBlue)
    static let lightBlueGray = Color(hex: colors.LightBlueGrey)
    static let accentViolet = Color(hex: colors.AccentViolet)
    static let textBlack = Color(hex: colors.TextBlack)
    static let darkGrey = Color(hex: colors.DarkGrey)
    
    static let primaryColor = Color(light: .accentViolet, dark:.accentViolet)
    static let background = Color(light: .lightBlueGray, dark:.darkGrey)
    static let onPrimary = Color(light: .white, dark:.white)
    static let onBackground = Color(light: .textBlack, dark:.white)
    static let surface = Color(light: .white, dark:.darkGrey)
    static let onSurface = Color(light: .textBlack, dark:.white)
}

// define color pairs for light and dark themes
private extension Color {
    init(light: Self, dark: Self) {
        self.init(uiColor: UIColor(light: UIColor(light),
                                   dark: UIColor(dark)))
    }
}

// react automatically to theme changes in users device using userInterfaceStyle
private extension UIColor {
    convenience init(light: UIColor, dark: UIColor) {
        self.init { traits in
            switch traits.userInterfaceStyle {
            case .light, .unspecified:
                return light
            case .dark:
                return dark
            @unknown default:
                return light
            }
        }
    }
}
