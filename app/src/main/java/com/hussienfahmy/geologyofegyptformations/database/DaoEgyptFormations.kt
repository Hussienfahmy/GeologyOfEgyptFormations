package com.hussienfahmy.geologyofegyptformations.database

import androidx.room.Dao
import androidx.room.Query
import com.hussienfahmy.geologyofegyptformations.entity.EgyptFormationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DaoEgyptFormations {

    @Query("SELECT DISTINCT formation_name FROM egypt_formations order by `lec_num`")
    fun getDistinctNames(): Flow<List<String>>

    @Query("SELECT * FROM egypt_formations WHERE formation_name = :formationName")
    suspend fun getEgyptFormationByName(formationName: String): EgyptFormationEntity?

    @Query("SELECT * FROM egypt_formations WHERE lec_num BETWEEN :minLec AND :maxLec")
    suspend fun getEgyptFormations(minLec: Int, maxLec: Int): List<EgyptFormationEntity>

    @Query("""
        SELECT formation_name FROM egypt_formations
        WHERE formation_name LIKE '%'|| :queryFilter ||'%'
        COLLATE NOCASE
            """)
    suspend fun searchFormationsNames(queryFilter: String): List<String>

}