package com.hussienfahmy.geologyofegyptformations.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "egypt_formations")
class EgyptFormationEntity(
    @PrimaryKey
    var id: Int,
    var area: String,
    var location: String,
    var succession: String,
    @ColumnInfo(name = "formation_name")
    var formationName: String,
    var author: String,
    var overlies: String,
    var underlies: String,
    var thickness: String,
    var lithology: String,
    var fossils: String,
    var age: String,
    @ColumnInfo(name = "economic_importance")
    var economicImportance: String,
    @ColumnInfo(name = "lec_num")
    var lectureNumber: Int  
)