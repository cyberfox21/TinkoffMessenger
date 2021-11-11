package com.cyberfox21.tinkoffmessanger.presentation.fragments.chat

import com.cyberfox21.tinkoffmessanger.domain.entity.Reaction

sealed class ReactionsListState {

    class Result(val items: List<Reaction>) : ReactionsListState()

    object Loading : ReactionsListState()

    class Error(val error: Throwable) : ReactionsListState()

}
