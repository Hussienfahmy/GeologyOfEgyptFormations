package com.hussienfahmy.geologyofegyptformations.model

import android.os.Parcelable
import com.hussienfahmy.geologyofegyptformations.enums.QuestionTopic
import com.hussienfahmy.geologyofegyptformations.enums.QuestionTopic.*
import kotlinx.parcelize.Parcelize

// todo check if we need this parcelize

/**
 * data class to represent a question displayed to the user
 */
@Parcelize
data class Question(
    val content: String,
    val correctAnswer: String,
    val choices: List<String>
) : Parcelable {

    constructor(questionTopic: QuestionTopic, formationName: String, correctAnswer: String, wrongAnswers: List<String>) : this(
        generateQuestionString(questionTopic, formationName),
        correctAnswer,
        mutableListOf<String>().apply {
            addAll(wrongAnswers)
            add(correctAnswer)
        }
    )

    fun isAnswerCorrect(answer: String) = answer == correctAnswer
}

private fun generateQuestionString(questionTopic: QuestionTopic, formationName: String): String {
    // examples
    // What is the Age of (FormationName)
    // what is the Diachronic formation
    return "What is the ${
        when (questionTopic) {
            PRACTICAL_AGE, FORMATION_AGE -> "Age"
            FORMATION_AUTHOR -> "Author"
            FORMATION_LOCALITY -> "Locality"
            FORMATION_FOSSILS -> "Fossils"
            FORMATION_DIACHRONIC -> "Diachronic Formation"
            FORMATION_LITHOLOGY -> "Lithology"
        }
    } ${if (questionTopic != FORMATION_DIACHRONIC) "of" else ""} $formationName"
}
