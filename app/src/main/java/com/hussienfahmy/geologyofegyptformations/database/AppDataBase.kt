package com.hussienfahmy.geologyofegyptformations.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.hussienfahmy.geologyofegyptformations.entity.EgyptFormationEntity
import com.hussienfahmy.geologyofegyptformations.entity.PracticalFormationEntity

//todo make the auto migrations
//todo get the formation with locations list with multimap return in dao
@Database(
    entities = [PracticalFormationEntity::class, EgyptFormationEntity::class],
    version = 3,
    exportSchema = true
)
abstract class AppDataBase : RoomDatabase() {
    abstract val daoPracticalFormations: DaoPracticalFormations
    abstract val daoEgyptFormations: DaoEgyptFormations
}