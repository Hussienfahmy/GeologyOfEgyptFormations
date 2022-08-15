package com.hussienfahmy.geologyofegyptformations.ui.formations

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.hussienfahmy.geologyofegyptformations.database.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FormationDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    repository: Repository
): ViewModel() {
//todo create a stack containing unique values
    private val formationName =
        FormationDetailsFragmentArgs.fromSavedStateHandle(savedStateHandle).formationName

    val formation = repository.getFormation(formationName).asLiveData()

    fun goToFormation(formationName: String) {
        TODO()
    }
}
