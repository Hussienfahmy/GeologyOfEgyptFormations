package com.hussienfahmy.geologyofegyptformations.database

import androidx.room.Dao
import androidx.room.Query
import com.hussienfahmy.geologyofegyptformations.entity.PracticalFormationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DaoPracticalFormations {

    @Query("SELECT DISTINCT formation_name FROM practical_formations order by id")
    fun getDistinctNames(): Flow<List<String>>

    @Query("SELECT * FROM practical_formations")
    suspend fun getPracticalFormations(): List<PracticalFormationEntity>

    @Query("SELECT * FROM practical_formations WHERE formation_name = :formationName")
    suspend fun getFormationByName(formationName: String): PracticalFormationEntity?

    @Query("SELECT GROUP_CONCAT(area || ' ' || section, '\n') FROM practical_formations as location WHERE formation_name = :formationName")
    suspend fun getLocationsByFormationName(formationName: String): String?

    @Query("""
        SELECT formation_name FROM practical_formations
        WHERE formation_name LIKE '%'|| :queryFilter ||'%'
        COLLATE NOCASE
            """)
    suspend fun searchFormationsNames(queryFilter: String): List<String>

}