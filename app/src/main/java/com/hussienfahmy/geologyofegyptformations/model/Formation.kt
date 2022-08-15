package com.hussienfahmy.geologyofegyptformations.model

import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil
import com.hussienfahmy.geologyofegyptformations.entity.EgyptFormationEntity
import com.hussienfahmy.geologyofegyptformations.entity.PracticalFormationEntity
import kotlinx.parcelize.Parcelize

//todo check in the data base that ids of the practical is different from egypt

// todo check if we need this parcelize
/**
 * data class to represent the formation and it's data on the screen
 * whether it's a [PracticalFormationEntity] or [EgyptFormationEntity]
 */
@Parcelize
data class Formation private constructor(
    val id: Int,
    val area: String = "N/A",
    val location: String = "N/A",
    val succession: String = "N/A",
    val name: String = "N/A",
    val author: String = "N/A",
    val overlies: String = "N/A",
    val underlies: String = "N/A",
    val thickness: String = "N/A",
    val lithology: String = "N/A",
    val fossils: String = "N/A",
    val age: String = "N/A",
    val economicImportance: String = "N/A",
    val lectureNumber: Int? = null,
    val locations: String = "N/A"
) : Parcelable {

    constructor(
        practicalFormationEntity: PracticalFormationEntity,
        locations: String = "N/A"
    ) : this(
        id = practicalFormationEntity.id,
        area = practicalFormationEntity.area,
        name = practicalFormationEntity.formationName,
        age = practicalFormationEntity.age,
        locations = locations
    )

    constructor(egyptFormationEntity: EgyptFormationEntity, locations: String = "N/A") : this(
        id = egyptFormationEntity.id,
        area = egyptFormationEntity.area,
        location = egyptFormationEntity.location,
        succession = egyptFormationEntity.succession,
        name = egyptFormationEntity.formationName,
        author = egyptFormationEntity.author,
        overlies = egyptFormationEntity.overlies,
        underlies = egyptFormationEntity.underlies,
        thickness = egyptFormationEntity.thickness,
        lithology = egyptFormationEntity.lithology,
        fossils = egyptFormationEntity.fossils,
        age = egyptFormationEntity.age,
        economicImportance = egyptFormationEntity.economicImportance,
        lectureNumber = egyptFormationEntity.lectureNumber,
        locations = locations
    )
}

class FormationDiffCallback : DiffUtil.ItemCallback<Formation>() {
    override fun areItemsTheSame(oldItem: Formation, newItem: Formation): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Formation, newItem: Formation): Boolean =
        oldItem == newItem

}