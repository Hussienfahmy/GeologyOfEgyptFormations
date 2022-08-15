package com.hussienfahmy.geologyofegyptformations.ui.questions

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.hussienfahmy.geologyofegyptformations.R
import com.hussienfahmy.geologyofegyptformations.databinding.FragmentPreQuestionsBinding
import com.hussienfahmy.geologyofegyptformations.enums.QuestionTopic
import com.hussienfahmy.geologyofegyptformations.enums.QuestionTopic.*
import com.hussienfahmy.geologyofegyptformations.model.Formation
import com.hussienfahmy.geologyofegyptformations.model.Question
import com.hussienfahmy.geologyofegyptformations.utils.getEgyptFormations
import com.hussienfahmy.geologyofegyptformations.utils.getPracticalFormations
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

private const val TAG = "PreQuestionsFragment"

class PreQuestionsFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentPreQuestionsBinding? = null
    private val binding: FragmentPreQuestionsBinding
        get() = _binding!!

    private val isTypeOfQuestionSelected: Boolean
        get() = (binding.formationChipGroup.checkedChipIds.size != 0
                || binding.practicalChipGroup.checkedChipIds.size != 0)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPreQuestionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.root.setOnClickListener(this)
        binding.startBtn.setOnClickListener(this)

        initLectureSlider()
    }

    private fun initLectureSlider() {
        binding.rangeSlider.setLabelFormatter { value -> "Lecture " + value.roundToInt() }
    }

    override fun onClick(v: View) {
        Log.d(TAG, "Am in ONClick")

        when (v.id) {
            R.id.startBtn -> {
                if (binding.numberQuestionEditTxt.text.isNullOrEmpty()) {
                    Toast.makeText(context, "Number Of Questions", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "I Need No. Of Questions")
                    return
                }
                if (binding.numberQuestionEditTxt.text.toString().toInt() < 0) {
                    Toast.makeText(context, "Really ?!! No Questions?", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "I Need No. Of Questions")
                    return
                }
                if (!isTypeOfQuestionSelected) {
                    Toast.makeText(context, "Type Of Questions", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "I Need The Topics")
                    return
                }
                GlobalScope.launch(Dispatchers.Main) {
                    enableInputs(false)
                }
                val selectedTopics = generateSelectedTopicsList()
                Log.d(TAG, "The Selected Topics Size " + selectedTopics.size)
                prepareQuestions(selectedTopics)
            }
            R.id.preQuestions_Layout -> activity?.let { activity ->
                activity.currentFocus?.let {
                    hideKeyboardFrom(it)
                }
            }
        }
    } //end onClick()

    private fun prepareQuestions(selectedTopics: List<QuestionTopic>) {
        Log.d(TAG, "AM In Prepare Source Of Questions")
        val context = context ?: return
        GlobalScope.launch(Dispatchers.IO) {
            val targetPracticalFormations = selectedTopics.contains(PRACTICAL_AGE)
            val targetEgyptFormations = selectedTopics.any {
                it == FORMATION_AGE ||
                        it == FORMATION_AUTHOR ||
                        it == FORMATION_LOCALITY ||
                        it == FORMATION_FOSSILS ||
                        it == FORMATION_DIACHRONIC ||
                        it == FORMATION_LITHOLOGY
            }

            val sourceOfQuestions = mutableListOf<Formation>()

            if (targetPracticalFormations) {
                getPracticalFormations(context)?.map { Formation(it) }?.forEach {
                    sourceOfQuestions.add(it)
                }
                Log.d(
                    TAG,
                    "I Get The Practical Entities , The source Size = " + sourceOfQuestions.size
                )
            }

            if (targetEgyptFormations) {
                val minLec = binding.rangeSlider.values[0].toInt()
                val maxLec = binding.rangeSlider.values[1].toInt()
                getEgyptFormations(context, minLec, maxLec)?.map { Formation(it) }?.forEach {
                    sourceOfQuestions.add(it)
                }
                Log.d(
                    TAG, "I Get The Egypt Entities With Criteria min_lec = " + minLec +
                            " ,max_lec = " + maxLec + " ,The Size become = " + sourceOfQuestions.size
                )
            }

            val lowTopic = getLowTopic(selectedTopics, sourceOfQuestions)
            if (lowTopic != null) {
                notifyAboutLowTopic(lowTopic)
                return@launch
            }

            Log.d(
                TAG, "I Got The Data for the selected Criteria ," +
                        " Source Of Questions List Size = " + sourceOfQuestions.size
            )

            generateQuestions(selectedTopics, sourceOfQuestions)
        }
    }

    private suspend fun notifyAboutLowTopic(lowTopic: String?) {
        withContext(Dispatchers.Main) {
            Toast.makeText(
                context,
                "No Data Available for $lowTopic, Adjust Your Choices",
                Toast.LENGTH_LONG
            ).show()
        }
        enableInputs(true)
    }

    private fun getLowTopic(
        selectedTopics: List<QuestionTopic>,
        sourceOfQuestions: MutableList<Formation>
    ): String? {
        val ageAnswers: MutableSet<String> = LinkedHashSet()
        val authorAnswers: MutableSet<String> = LinkedHashSet()
        val fossilsAnswers: MutableSet<String> = LinkedHashSet()
        val localityAnswers: MutableSet<String> = LinkedHashSet()
        val diachronicAnswers: MutableSet<String> = LinkedHashSet()
        val lithologyAnswers: MutableSet<String> = LinkedHashSet()
        for (topic in selectedTopics) {
            for (formation in sourceOfQuestions) {
                when (topic) {
                    FORMATION_AGE, PRACTICAL_AGE -> if (formation.area.isNotEmpty()) {
                        ageAnswers.add(formation.age)
                    }
                    FORMATION_AUTHOR -> if (formation.author.trim() != "N/A") {
                        authorAnswers.add(formation.author)
                    }
                    FORMATION_FOSSILS -> if (formation.fossils.trim() != "N/A") {
                        fossilsAnswers.add(formation.fossils)
                    }
                    FORMATION_LOCALITY -> {
                        var location = formation.area + " " + formation.location
                        location = location.replace("N/A", "")
                        if (location.trim().isNotEmpty()) {
                            localityAnswers.add(location)
                        }
                    }
                    FORMATION_DIACHRONIC -> if (formation.age.contains("To")) {
                        diachronicAnswers.add(formation.age)
                    }
                    FORMATION_LITHOLOGY -> if (formation.lithology.trim() != "N/A") {
                        lithologyAnswers.add(formation.lithology)
                    }
                }
            }
        }
        Log.d(TAG, "checkExistenceOfSelectedTopicQuestions: $selectedTopics")
        Log.d(TAG, "no_of_age_answers_exist = " + ageAnswers.size)
        Log.d(TAG, "no_of_author_answers_exist = " + authorAnswers.size)
        Log.d(TAG, "no_of_fossils_answers_exist = " + fossilsAnswers.size)
        Log.d(TAG, "no_of_locality_answers_exist = " + localityAnswers.size)
        Log.d(TAG, "no_of_diachronic_answers_exist = " + diachronicAnswers.size)
        Log.d(TAG, "no_of_lithology_answers_exist = " + lithologyAnswers.size)

        return checkLowTopic(
            selectedTopics,
            ageAnswers.size,
            authorAnswers.size,
            fossilsAnswers.size,
            localityAnswers.size,
            diachronicAnswers.size,
            lithologyAnswers.size
        )
    }

    private fun checkLowTopic(
        selectedTopics: List<QuestionTopic>,
        noOfAgeAnswersExist: Int,
        noOfAuthorAnswersExist: Int,
        noOfFossilsAnswersExist: Int,
        noOfLocalityAnswersExist: Int,
        noOfDiachronicAnswersExist: Int,
        noOfLithologyAnswersExist: Int
    ): String? {
        for (topic in selectedTopics) {
            when (topic) {
                PRACTICAL_AGE -> if (noOfAgeAnswersExist < 4) {
                    return "Practical Formation Age"
                }
                FORMATION_AGE -> if (noOfAgeAnswersExist < 4) {
                    return "Age"
                }
                FORMATION_AUTHOR -> if (noOfAuthorAnswersExist < 4) {
                    return "Author"
                }
                FORMATION_FOSSILS -> if (noOfFossilsAnswersExist < 4) {
                    return "Fossils"
                }
                FORMATION_LOCALITY -> if (noOfLocalityAnswersExist < 4) {
                    return "Locality"
                }
                FORMATION_DIACHRONIC -> if (noOfDiachronicAnswersExist < 1) {
                    return "Diachronic Formations"
                }
                FORMATION_LITHOLOGY -> if (noOfLithologyAnswersExist < 4) {
                    return "Lithology"
                }
            }
        }
        return null
    }

    private fun generateSelectedTopicsList(): List<QuestionTopic> {
        val selectedTopics: MutableList<QuestionTopic> = mutableListOf()
        val checkedChipIds = mutableListOf<Int>()
        checkedChipIds.addAll(binding.formationChipGroup.checkedChipIds)
        checkedChipIds.addAll(binding.practicalChipGroup.checkedChipIds)
        checkedChipIds.forEach { checkedChipId ->
            when (checkedChipId) {
                R.id.practical_age_chip -> selectedTopics.add(PRACTICAL_AGE)
                R.id.formation_ageChip -> selectedTopics.add(FORMATION_AGE)
                R.id.formation_authorChip -> selectedTopics.add(FORMATION_AUTHOR)
                R.id.formation_localityChip -> selectedTopics.add(FORMATION_LOCALITY)
                R.id.formation_fossilChip -> selectedTopics.add(FORMATION_FOSSILS)
                R.id.formation_diachronicChip -> selectedTopics.add(FORMATION_DIACHRONIC)
                R.id.formation_lithologyChip -> selectedTopics.add(FORMATION_LITHOLOGY)
            }
        }
        return selectedTopics
    }

    private suspend fun enableInputs(enable: Boolean) {
        withContext(Dispatchers.Main) {
            binding.startBtn.isEnabled = enable
            binding.editTextContainer.isEnabled = enable
            binding.countdownEditTxt.isEnabled = enable
            binding.progressBar.visibility =
                if (enable) View.GONE else View.VISIBLE
            binding.practicalAgeChip.isEnabled = enable
            binding.formationAgeChip.isEnabled = enable
            binding.formationAuthorChip.isEnabled = enable
            binding.formationLocalityChip.isEnabled = enable
            binding.formationFossilChip.isEnabled = enable
            binding.formationDiachronicChip.isEnabled = enable
            binding.formationLithologyChip.isEnabled = enable
            binding.rangeSlider.isEnabled = enable
        }
    }

    private suspend fun generateQuestions(
        selectedTopics: List<QuestionTopic>,
        sourceOfQuestions: MutableList<Formation>
    ) {
        Log.d(TAG, "Am In Load Questions And Start")
        withContext(Dispatchers.Default) {
            val numOfQuestions = binding.numberQuestionEditTxt.text.toString().toInt()
            val questionsList = mutableListOf<Question>()

            while (questionsList.size < numOfQuestions) {
                val randomTopic = selectedTopics.random()
                val randomFormation = sourceOfQuestions.random()

                if (randomTopic == FORMATION_DIACHRONIC) {
                    if (!randomFormation.age.contains("To")) continue
                }

                val correctAnswer = when (randomTopic) {
                    PRACTICAL_AGE, FORMATION_AGE -> randomFormation.age
                    FORMATION_AUTHOR -> randomFormation.author
                    FORMATION_LOCALITY -> "${randomFormation.area} ${randomFormation.location}"
                    FORMATION_FOSSILS -> randomFormation.fossils
                    FORMATION_DIACHRONIC -> randomFormation.name
                    FORMATION_LITHOLOGY -> randomFormation.lithology
                }.replace(
                    "N/A",
                    ""
                )

                if (correctAnswer.trim().isEmpty()) continue

                questionsList.add(
                    Question(
                        randomTopic,
                        randomFormation.name,
                        correctAnswer,
                        generateWrongAnswers(randomTopic, sourceOfQuestions, correctAnswer)
                    )
                )
            }
            navigateToQuestionFragment(questionsList)
        }
    } //end loadQuestionsAndStart()

    private suspend fun navigateToQuestionFragment(questionsList: MutableList<Question>) {
        withContext(Dispatchers.Main) {
            val navDirections =
                PreQuestionsFragmentDirections.actionPreQuestionsFragmentToQuestionsFragment(
                    questionsList.toTypedArray(),
                    getCountDown()
                )
            findNavController().navigate(navDirections)
        }
    }

    private fun getCountDown(): Int {
        val countdown =
            if (!binding.countdownEditTxt.text.isNullOrEmpty()) {
                binding.countdownEditTxt.text.toString().toInt()
            } else 0
        Log.d(TAG, "The CountDown = $countdown")
        return countdown
    }

    private fun generateWrongAnswers(
        randomTopic: QuestionTopic,
        sourceOfQuestions: MutableList<Formation>,
        correctAnswer: String
    ): List<String> {
        val wrongAnswers = mutableListOf<String>()
        while (wrongAnswers.size != 3) {
            val randomFormation = sourceOfQuestions.random()
            val possibleWrongAnswer = when (randomTopic) {
                PRACTICAL_AGE, FORMATION_AGE -> randomFormation.age
                FORMATION_AUTHOR -> randomFormation.author
                FORMATION_LOCALITY -> "${randomFormation.area} ${randomFormation.location}"
                FORMATION_FOSSILS -> randomFormation.fossils
                FORMATION_DIACHRONIC -> randomFormation.name
                FORMATION_LITHOLOGY -> randomFormation.lithology
            }.replace(
                "N/A",
                ""
            )

            if (possibleWrongAnswer.trim().isEmpty()) continue

            if (possibleWrongAnswer != correctAnswer)
                wrongAnswers.add(possibleWrongAnswer)
        }
        return wrongAnswers
    }
}

fun hideKeyboardFrom(view: View) {
    val imm =
        view.context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    view.clearFocus()
}