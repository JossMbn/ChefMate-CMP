package com.jmabilon.chefmate.feature.cookbook.cookbooklist.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmabilon.chefmate.feature.cookbook.cookbooklist.domain.GetCookbookListSortedCookbooksUseCase
import com.jmabilon.chefmate.feature.cookbook.cookbooklist.presentation.mapper.toAsyncUiModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlin.time.Duration.Companion.seconds

class CookbookListViewModel(
    getCookbookListSortedCookbooksUseCase: GetCookbookListSortedCookbooksUseCase
) : ViewModel() {

    // =============================================================================================
    // Private Properties
    // =============================================================================================

    private val _event = Channel<CookbookListEvent>()

    private val _internalState = MutableStateFlow(CookbookListState())

    // =============================================================================================
    // Public Properties
    // =============================================================================================

    val event = _event.receiveAsFlow()

    val state = getCookbookListSortedCookbooksUseCase()
        .combine(_internalState) { cookbooks, internalState ->
            CookbookListState(
                cookbooks = cookbooks.toAsyncUiModel(),
                dialogState = internalState.dialogState
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5.seconds),
            initialValue = CookbookListState()
        )

    // =============================================================================================
    // Public Methods
    // =============================================================================================

    fun onAction(action: CookbookListAction) {
        when (action) {
            CookbookListAction.OnDismissDialog -> {
                _internalState.update { it.copy(dialogState = null) }
            }

            is CookbookListAction.OnAddCookbookClick -> {
                _internalState.update { it.copy(dialogState = CookbookListDialogState.CreateCookbook) }
            }
        }
    }
}
