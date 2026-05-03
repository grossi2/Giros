package com.example.giros

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.giros.data.WheelRepository
import com.example.giros.data.WheelStorage
import com.example.giros.ui.screens.HomeScreen
import com.example.giros.ui.screens.EditWheelScreen
import com.example.giros.ui.screens.SpinScreen
import com.example.giros.ui.screens.WheelListScreen
import com.example.giros.ui.theme.GirosTheme
import com.example.giros.viewmodel.WheelViewModel

private enum class GirosScreen {
    Home,
    EditWheel,
    WheelList,
    Spin
}

class MainActivity : ComponentActivity() {
    private val wheelViewModel: WheelViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repository = WheelRepository(WheelStorage(applicationContext))
        wheelViewModel.initialize(repository)
        enableEdgeToEdge()
        setContent {
            GirosTheme {
                val editWheelUiState by wheelViewModel.editWheelUiState.collectAsState()
                val savedWheels by wheelViewModel.savedWheels.collectAsState()
                val spinUiState by wheelViewModel.spinUiState.collectAsState()
                var currentScreen by remember { mutableStateOf(GirosScreen.Home) }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    when (currentScreen) {
                        GirosScreen.Home -> {
                            HomeScreen(
                                onCreateWheelClick = {
                                    wheelViewModel.resetEditor()
                                    currentScreen = GirosScreen.EditWheel
                                },
                                onViewWheelsClick = {
                                    currentScreen = GirosScreen.WheelList
                                },
                                modifier = Modifier.padding(innerPadding)
                            )
                        }

                        GirosScreen.EditWheel -> {
                            EditWheelScreen(
                                uiState = editWheelUiState,
                                onBackClick = {
                                    wheelViewModel.resetEditor()
                                    currentScreen = GirosScreen.Home
                                },
                                onWheelNameChange = wheelViewModel::updateWheelName,
                                onOptionDraftChange = wheelViewModel::updateOptionDraft,
                                onAddOptionClick = wheelViewModel::addOption,
                                onRemoveOptionClick = wheelViewModel::removeOption,
                                onSaveClick = {
                                    val createdWheel = wheelViewModel.saveWheel()
                                    if (createdWheel != null) {
                                        wheelViewModel.resetEditor()
                                        currentScreen = GirosScreen.WheelList
                                    }
                                },
                                modifier = Modifier.padding(innerPadding)
                            )
                        }

                        GirosScreen.WheelList -> {
                            WheelListScreen(
                                wheels = savedWheels,
                                onBackClick = {
                                    currentScreen = GirosScreen.Home
                                },
                                onCreateWheelClick = {
                                    wheelViewModel.resetEditor()
                                    currentScreen = GirosScreen.EditWheel
                                },
                                onOpenWheelClick = { wheelId ->
                                    wheelViewModel.startSpinSession(wheelId)
                                    currentScreen = GirosScreen.Spin
                                },
                                onEditWheelClick = { wheelId ->
                                    wheelViewModel.startEditingWheel(wheelId)
                                    currentScreen = GirosScreen.EditWheel
                                },
                                onDeleteWheelClick = wheelViewModel::deleteWheel,
                                modifier = Modifier.padding(innerPadding)
                            )
                        }

                        GirosScreen.Spin -> {
                            val selectedWheel = spinUiState.selectedWheel
                            if (selectedWheel != null) {
                                SpinScreen(
                                    wheel = selectedWheel,
                                    remainingOptions = spinUiState.remainingOptions,
                                    ranking = spinUiState.ranking,
                                    lastResult = spinUiState.lastResult,
                                    onBackClick = {
                                        currentScreen = GirosScreen.WheelList
                                    },
                                    onSpinRequest = wheelViewModel::previewSpinOption,
                                    onSpinFinished = wheelViewModel::applySpinResult,
                                    onResetClick = wheelViewModel::resetSpinSession,
                                    modifier = Modifier.padding(innerPadding)
                                )
                            } else {
                                currentScreen = GirosScreen.WheelList
                            }
                        }
                    }
                }
            }
        }
    }
}
