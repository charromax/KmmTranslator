//
//  TranslateScreen.swift
//  iosApp
//
//  Created by Manuel Gonzalez on 14/06/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared

struct TranslateScreen: View {
    private var historyDataSource: HistoryDataSource
    private var translateUseCase: Translate
    @ObservedObject var viewModel: IOSTranslateViewModel
    
    init(historyDataSource: HistoryDataSource, translateUseCase: Translate) {
        self.historyDataSource = historyDataSource
        self.translateUseCase = translateUseCase
        self.viewModel = IOSTranslateViewModel(historyDataSource: historyDataSource, translateUseCase: translateUseCase)
    }
    
    var body: some View {
        ZStack {
            List {
                HStack(alignment: .center) {
                    LanguageDropDown(language: viewModel.state.fromLanguage, isOpen: viewModel.state.isChoosingFromLanguage, selectLanguage: {lang in
                        viewModel.onEvent(event: TranslateEvent.ChooseFromLanguage(language: lang))
                    })
                    Spacer()
                    SwapLanguageButton {
                        viewModel.onEvent(event: TranslateEvent.SwapLanguages())
                    }
                    Spacer()
                    LanguageDropDown(language: viewModel.state.toLanguage, isOpen: viewModel.state.isChoosingToLanguage, selectLanguage: {lang in
                        viewModel.onEvent(event: TranslateEvent.ChooseToLanguage(language: lang))
                    })
                }
                .listRowSeparator(.hidden)
                .listRowBackground(Color.background)
                TranslateTextField(
                    fromText: Binding(
                        get: { viewModel.state.fromText },
                        set: { viewModel.onEvent(event: TranslateEvent.ChangeTranslationText(text: $0)) }),
                    toText: viewModel.state.toText,
                    isTranslating: viewModel.state.isTranslating,
                    fromLanguage: viewModel.state.fromLanguage,
                    toLanguage: viewModel.state.toLanguage,
                    onTranslateEvent: { event in viewModel.onEvent(event: event) }
                )
                .listRowSeparator(.hidden)
                .listRowBackground(Color.background)
                
                if !viewModel.state.history.isEmpty {
                    Text("History")
                        .font(.title)
                        .bold()
                        .frame(maxWidth: .infinity, alignment: .leading)
                        .listRowSeparator(.hidden)
                        .listRowBackground(Color.background)
                }
                ForEach(viewModel.state.history, id: \.self.id) {item in
                    TranslateHistoryItem(item: item, onClick: {
                        viewModel.onEvent(event: TranslateEvent.SelectHistoryItem(item: item))
                    })
                    .listRowSeparator(.hidden)
                    .listRowBackground(Color.background)
                }
            }
            .listStyle(.plain)
            .buttonStyle(.plain)
            
            VStack {
                Spacer()
                NavigationLink(destination: Text("voice-to-text screen")) {
                    ZStack {
                        Circle()
                            .foregroundColor(.primaryColor)
                            .padding()
                        Image(uiImage: UIImage(named: "mic")!)
                            .foregroundColor(.onPrimary)
                    }
                    .frame(maxWidth: 100, maxHeight: 100)
                }
            }
        }
        .onAppear {
            viewModel.startObserving()
        }
        .onDisappear {
            viewModel.dispose()
        }
    }
}

