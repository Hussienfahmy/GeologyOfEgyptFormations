package com.hussienfahmy.geologyofegyptformations.database

import com.hussienfahmy.geologyofegyptformations.model.Formation
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(
    private val practicalDao: DaoPracticalFormations,
    private val egyptDao: DaoEgyptFormations,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    val allFormationsNames: Flow<List<String>>
        get() = combine(
            practicalDao.getDistinctNames(),
            egyptDao.getDistinctNames()
        ) { practical, egypt ->
            practical + egypt
        }

    suspend fun searchFormationNames(queryFilter: String): List<String> {
        return withContext(ioDispatcher) {
            val resultFromEgypt = egyptDao.searchFormationsNames(queryFilter)
            val resultFromPractical = practicalDao.searchFormationsNames(queryFilter)

            (resultFromPractical + resultFromEgypt).distinct()
        }
    }

    fun getFormation(formationName: String): Flow<Formation> {
        TODO("Not yet implemented")
    }

}