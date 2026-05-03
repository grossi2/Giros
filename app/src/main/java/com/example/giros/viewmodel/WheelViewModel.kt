package com.example.giros.viewmodel

import androidx.lifecycle.ViewModel
import com.example.giros.data.WheelRepository
import com.example.giros.model.SpinResult
import com.example.giros.model.Wheel
import com.example.giros.model.WheelOption
import java.util.UUID
import kotlin.random.Random
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

data class EditWheelUiState(
    val editingWheelId: String? = null,
    val wheelName: String = "",
    val optionDraft: String = "",
    val options: List<WheelOption> = emptyList(),
    val validationMessage: String? = null
)

data class SpinUiState(
    val selectedWheel: Wheel? = null,
    val remainingOptions: List<WheelOption> = emptyList(),
    val ranking: List<SpinResult> = emptyList(),
    val lastResult: SpinResult? = null
)

class WheelViewModel : ViewModel() {
    private var repository: WheelRepository? = null
    private val _savedWheels = MutableStateFlow<List<Wheel>>(emptyList())
    val savedWheels: StateFlow<List<Wheel>> = _savedWheels

    private val _editWheelUiState = MutableStateFlow(EditWheelUiState())
    val editWheelUiState: StateFlow<EditWheelUiState> = _editWheelUiState

    private val _spinUiState = MutableStateFlow(SpinUiState())
    val spinUiState: StateFlow<SpinUiState> = _spinUiState

    fun initialize(repository: WheelRepository) {
        if (this.repository != null) return
        this.repository = repository
        _savedWheels.value = repository.getWheels()
    }

    fun updateWheelName(name: String) {
        _editWheelUiState.update { current ->
            current.copy(
                wheelName = name,
                validationMessage = null
            )
        }
    }

    fun updateOptionDraft(value: String) {
        _editWheelUiState.update { current ->
            current.copy(
                optionDraft = value,
                validationMessage = null
            )
        }
    }

    fun addOption() {
        val optionName = _editWheelUiState.value.optionDraft.trim()
        if (optionName.isEmpty()) {
            showValidation("La opción no puede estar vacía.")
            return
        }

        _editWheelUiState.update { current ->
            current.copy(
                optionDraft = "",
                options = current.options + WheelOption(
                    id = UUID.randomUUID().toString(),
                    name = optionName
                ),
                validationMessage = null
            )
        }
    }

    fun startEditingWheel(wheelId: String) {
        val wheel = _savedWheels.value.firstOrNull { it.id == wheelId } ?: return
        _editWheelUiState.value = EditWheelUiState(
            editingWheelId = wheel.id,
            wheelName = wheel.name,
            optionDraft = "",
            options = wheel.options,
            validationMessage = null
        )
    }

    fun removeOption(optionId: String) {
        _editWheelUiState.update { current ->
            current.copy(
                options = current.options.filterNot { it.id == optionId },
                validationMessage = null
            )
        }
    }

    fun saveWheel(): Wheel? {
        val current = _editWheelUiState.value
        val wheelName = current.wheelName.trim()

        return when {
            wheelName.isEmpty() -> {
                showValidation("La rueda debe tener un nombre.")
                null
            }

            current.options.size < 2 -> {
                showValidation("La rueda debe tener al menos 2 opciones.")
                null
            }

            else -> {
                _editWheelUiState.update { state -> state.copy(validationMessage = null) }
                val wheel = Wheel(
                    id = current.editingWheelId ?: UUID.randomUUID().toString(),
                    name = wheelName,
                    options = current.options
                )
                _savedWheels.update { wheels ->
                    val updated = if (current.editingWheelId == null) {
                        wheels + wheel
                    } else {
                        wheels.map { existing ->
                            if (existing.id == current.editingWheelId) wheel else existing
                        }
                    }
                    repository?.saveWheels(updated)
                    updated
                }
                wheel
            }
        }
    }

    fun deleteWheel(wheelId: String) {
        _savedWheels.update { wheels ->
            val updated = wheels.filterNot { it.id == wheelId }
            repository?.saveWheels(updated)
            updated
        }
    }

    fun startSpinSession(wheelId: String) {
        val selectedWheel = _savedWheels.value.firstOrNull { it.id == wheelId } ?: return
        _spinUiState.value = SpinUiState(
            selectedWheel = selectedWheel,
            remainingOptions = selectedWheel.options,
            ranking = emptyList(),
            lastResult = null
        )
    }

    fun previewSpinOption(): WheelOption? {
        val current = _spinUiState.value
        if (current.selectedWheel == null || current.remainingOptions.isEmpty()) return null
        return current.remainingOptions.random(Random.Default)
    }

    fun applySpinResult(selectedOption: WheelOption) {
        val current = _spinUiState.value
        if (current.selectedWheel == null || current.remainingOptions.isEmpty()) return

        val newPosition = current.ranking.size + 1
        val result = SpinResult(
            position = newPosition,
            selectedOption = selectedOption
        )

        _spinUiState.value = current.copy(
            remainingOptions = current.remainingOptions.filterNot { it.id == selectedOption.id },
            ranking = current.ranking + result,
            lastResult = result
        )
    }

    fun resetSpinSession() {
        val selectedWheel = _spinUiState.value.selectedWheel ?: return
        _spinUiState.value = SpinUiState(
            selectedWheel = selectedWheel,
            remainingOptions = selectedWheel.options,
            ranking = emptyList(),
            lastResult = null
        )
    }

    fun resetEditor() {
        _editWheelUiState.value = EditWheelUiState()
    }

    private fun showValidation(message: String) {
        _editWheelUiState.update { current ->
            current.copy(validationMessage = message)
        }
    }
}
