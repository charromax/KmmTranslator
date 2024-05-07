//
//  IOSVoiceToTextViewModel.swift
//  iosApp
//
//  Created by Manuel Gonzalez on 15/07/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared
import Combine

@MainActor class IOSVoiceToTextViewModel: ObservableObject {
    private var parser: any Voice2TextParser
    private let languageCode: String
    private let viewModel: VoiceToTextViewModel
    @Published var state = VoiceToTextState(powerRatios: [], spokenText: "", canRecord: false, recordErrorText: nil, displayState: nil)
    private var handle: DisposableHandle?
    
    init(parser: Voice2TextParser, languageCode: String) {
        self.parser = parser
        self.languageCode = languageCode
        self.viewModel = VoiceToTextViewModel(parser: parser, coroutineScope: nil)
        self.viewModel.onEvent(event: .PermissionResult(isGranted: true, isPermanentlyDeclinedL: false))
    }

    func onEvent(event: VoiceToTextEvent) {
        viewModel.onEvent(event: event)
    }
    
    func startObserving() {
        handle = viewModel.state.subscribe{ [weak self] state in
            if let state {
                self?.state = state
            }
            
        }
    }
    
    func dispose(){
        handle?.dispose()
        onEvent(event: VoiceToTextEvent.Reset())
    }
}
