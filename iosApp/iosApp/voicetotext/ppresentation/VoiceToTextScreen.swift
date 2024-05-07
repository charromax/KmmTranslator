//
//  VoiceToTextScreen.swift
//  iosApp
//
//  Created by Manuel Gonzalez on 16/07/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared

struct VoiceToTextScreen: View {
    private let onResult: (String) -> Void
    
    @ObservedObject var viewModel: IOSVoiceToTextViewModel
    private let parser: any Voice2TextParser
    private let langCode: String
    
    @Environment(\.presentationMode) var presentation
    
    init(onResult: @escaping (String) -> Void, parser: any Voice2TextParser, langCode: String) {
        self.onResult = onResult
        self.parser = parser
        self.langCode = langCode
        self.viewModel = IOSVoiceToTextViewModel(parser: parser, languageCode: langCode)
        
    }
    var body: some View {
        VStack {
            Spacer()
            
            mainView
            
            Spacer()
            
            HStack {
                Spacer()
                VoiceRecorderButton(
                    displayState:viewModel.state.displayState ?? .waitingToTalk,
                    onClick: {
                        if viewModel.state.displayState != .displayResults {
                            viewModel.onEvent(event: VoiceToTextEvent.ToggleRecording(langCode: langCode))
                        } else {
                            onResult(viewModel.state.spokenText)
                            self.presentation.wrappedValue.dismiss()
                        }
                    }
                )
                if viewModel.state.displayState == .displayResults {
                    Button(action: {viewModel.onEvent(event: VoiceToTextEvent.ToggleRecording(langCode: langCode))}) {
                        Image(systemName: "error.clockwise")
                    }
                }
                Spacer()
            }
        }
        .onAppear {
            viewModel.startObserving()
        }
        .onDisappear {
            viewModel.dispose()
        }
        .background(Color.background)
    }
    
    var mainView: some View {
        switch viewModel.state.displayState {
        case DisplayState.waitingToTalk:
            return AnyView(
                Text("Click record and start talking").font(.title2)
            )
            
        case DisplayState.displayResults:
            return AnyView(Text(viewModel.state.spokenText).font(.title2))
            
        case DisplayState.error:
            return AnyView(Text(viewModel.state.recordErrorText ?? "Unknown error"))
            
        case DisplayState.speaking:
            return AnyView(
                VoiceRecorderDisplay(powerRatios: viewModel.state.powerRatios.map {
                    Double(truncating:$0)
                }).frame(maxHeight: 100.0))
            
        default: return AnyView(EmptyView())
        }
    }
}
