//
//  VoiceRecorderButton.swift
//  iosApp
//
//  Created by Manuel Gonzalez on 16/07/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared


struct VoiceRecorderButton: View {
    var displayState: DisplayState
    var onClick: () -> Void
    var body: some View {
        Button(action: onClick) {
            ZStack {
                Circle()
                    .foregroundColor(.primaryColor)
                    .padding()
                icon
                    .foregroundColor(.onPrimary)
            }
        }
        .frame(maxWidth: 100.0, maxHeight: 100.0)
    }
    
    var icon: some View {
        switch displayState {
        case .speaking:
            return  Image(systemName: "stop.fill")
        case .displayResults:
            return Image(systemName: "checkmark")
        default:
            return Image(uiImage: UIImage(named: "mic")!)
        }
    }
}

struct VoiceRecorderButton_Previews: PreviewProvider {
    static var previews: some View {
        VoiceRecorderButton(displayState: DisplayState.waitingToTalk, onClick: {}
        )
        
        
    }
}
