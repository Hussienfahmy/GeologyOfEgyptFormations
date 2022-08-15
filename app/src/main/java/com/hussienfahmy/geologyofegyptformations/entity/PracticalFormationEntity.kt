package com.hussienfahmy.geologyofegyptformations.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "practical_formations")
class PracticalFormationEntity(
    @PrimaryKey
    val id: Int,
    val area: String,
    val section: String,
    @ColumnInfo(name = "formation_name")
    val formationName: String,
    val age: String
)