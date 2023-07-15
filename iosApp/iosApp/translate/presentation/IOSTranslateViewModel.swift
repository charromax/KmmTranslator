//
//  IOSTranslateViewModel.swift
//  iosApp
//
//  Created by Manuel Gonzalez on 14/06/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared

extension TranslateScreen {
    @MainActor class IOSTranslateViewModel: ObservableObject {
        private var historyDataSource: HistoryDataSource
        private var translateUseCase: Translate
        
        private let viewModel: TranslateViewModel
        private var handle: DisposableHandle?
        
        // initialize state with default values
        @Published var state: TranslateState = TranslateState(fromText: "", toText: nil, isTranslating: false, fromLanguage: UiLanguage(language: .english, imageName: "english"), toLanguage: UiLanguage(language: .spanish, imageName: "spanish"), isChoosingFromLanguage: false, isChoosingToLanguage: false, error: nil, history: [])
        
        init(historyDataSource: HistoryDataSource, translateUseCase: Translate) {
            self.historyDataSource = historyDataSource
            self.translateUseCase = translateUseCase
            self.viewModel = TranslateViewModel(translate: translateUseCase, historyDataSource: historyDataSource, coroutineScope: nil)
        }
        
        func onEvent(event: TranslateEvent) {
            self.viewModel.onEvent(event: event)
        }
        
        func startObserving() {
            self.viewModel.state.subscribe() {state in
                if let state = state {
                    self.state = state
                }
            }
        }
        
        func dispose() {
            handle?.dispose()
        }
    }
}
