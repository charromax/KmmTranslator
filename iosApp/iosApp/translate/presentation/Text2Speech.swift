//
//  Text2Speech.swift
//  iosApp
//
//  Created by Manuel Gonzalez on 16/06/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared
import AVFoundation

struct Text2Speech {
    
    private let synthesizer = AVSpeechSynthesizer()
    
    func speak(text: String, language: String) {
        let utterance = AVSpeechUtterance(string: text)
        utterance.voice = AVSpeechSynthesisVoice(language: language)
        utterance.volume = 1
        synthesizer.speak(utterance)
    }
}
