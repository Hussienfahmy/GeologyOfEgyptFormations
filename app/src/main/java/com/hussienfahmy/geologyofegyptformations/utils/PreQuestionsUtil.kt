package com.hussienfahmy.geologyofegyptformations.utils

import android.content.Context
import com.hussienfahmy.geologyofegyptformations.database.AppDataBase
import com.hussienfahmy.geologyofegyptformations.entity.EgyptFormationEntity
import com.hussienfahmy.geologyofegyptformations.entity.PracticalFormationEntity

suspend fun getPracticalFormations(context: Context): List<PracticalFormationEntity>? {
    val dataBase = AppDataBase.getInstance(context)
    return dataBase?.daoPracticalFormations()?.getPracticalFormationsList()
}

suspend fun getEgyptFormations(context: Context, minLec: Int, maxLec: Int): List<EgyptFormationEntity>? {
    val dataBase = AppDataBase.getInstance(context)
    return dataBase?.daoEgyptFormations()?.getEgyptFormationsList(minLec, maxLec)
}