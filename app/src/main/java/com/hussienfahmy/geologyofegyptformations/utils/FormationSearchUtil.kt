package com.hussienfahmy.geologyofegyptformations.utils

import android.content.Context
import android.widget.Toast
import com.hussienfahmy.geologyofegyptformations.database.AppDataBase
import com.hussienfahmy.geologyofegyptformations.model.Formation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun searchFormationByName(context: Context, formationName: String): Formation? {

    val database = AppDataBase.getInstance(context)?: return null

    val locations = database.daoPracticalFormations()
        .getLocationsByFormationName(formationName)

    val egyptFormation = database.daoEgyptFormations().getEgyptFormationByName(formationName)
    if (egyptFormation != null) return Formation(egyptFormation, locations)

    val practicalFormation = database.daoPracticalFormations().getFormationByName(formationName)
    if (practicalFormation != null) return Formation(practicalFormation, locations)

    return null
}

suspend fun toastNotAvailable(context: Context) {
    withContext(Dispatchers.Main) {
        Toast.makeText(
            context,
            "Not Available",
            Toast.LENGTH_SHORT
        ).show()
    }
}