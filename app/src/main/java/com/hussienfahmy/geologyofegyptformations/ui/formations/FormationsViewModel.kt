package com.hussienfahmy.geologyofegyptformations.ui.formations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.hussienfahmy.geologyofegyptformations.database.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class FormationsViewModel @Inject constructor(
    repository: Repository
): ViewModel() {

    private val filterQuery = MutableStateFlow("")
    private val filteredFormationsNames = filterQuery.map {
        repository.searchFormationNames(it)
    }

    private val allFormationNames = repository.allFormationsNames

    @ExperimentalCoroutinesApi
    val displayedFormationsNames = filterQuery.flatMapLatest {
        if (filterQuery.value.isBlank()) allFormationNames
        else filteredFormationsNames
    }.asLiveData()


    fun submitFilterQuery(query: String?) {
        filterQuery.value = query?: ""
    }

    fun clearFilter() {
        filterQuery.value = ""
    }
}