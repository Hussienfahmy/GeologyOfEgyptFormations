package com.hussienfahmy.geologyofegyptformations.ui.questions

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.button.MaterialButton
import com.hussienfahmy.geologyofegyptformations.R
import com.hussienfahmy.geologyofegyptformations.databinding.FragmentQuestionsBinding
import com.hussienfahmy.geologyofegyptformations.model.Question
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates

private const val TAG = "QuestionsFragment"

class QuestionsFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentQuestionsBinding? = null
    private val binding: FragmentQuestionsBinding
        get() = _binding!!

    private val args: QuestionsFragmentArgs by navArgs()

    private lateinit var questionsQueue: Queue<Question>
    private var countdownLimit: Int = 0
    private var totalNumOfQuestions by Delegates.notNull<Int>()

    private var correctAnswers = 0

    private var countDownTimer: CountDownTimer? = null

    private var correctAnswerMediaPlayer: MediaPlayer? = null
    private var wrongAnswerMediaPlayer: MediaPlayer? = null

    private var totalSecondsTakenToAnswer = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        questionsQueue = LinkedList(args.questions.toList())
        countdownLimit = args.countdown
        totalNumOfQuestions = questionsQueue.size
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuestionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        enableNextBtn(false)
        binding.nextBtn.setOnClickListener(this)
        binding.choice1.setOnClickListener(this)
        binding.choice2.setOnClickListener(this)
        binding.choice3.setOnClickListener(this)
        binding.choice4.setOnClickListener(this)
        binding.questionNumberProgressTxtView.text = "1"
        binding.totalNumQuestions.text = totalNumOfQuestions.toString()
        correctAnswerMediaPlayer = MediaPlayer.create(context, R.raw.correct)
        wrongAnswerMediaPlayer = MediaPlayer.create(context, R.raw.wrong)

        questionsQueue.peek()?.let {
            displayAQuestion(it)
        }
    }

    private fun enableNextBtn(enable: Boolean) {
        binding.nextBtn.isEnabled = enable
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer?.cancel()
        _binding = null
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.nextBtn -> {
                // here will peeked to display and will poll when to check the answer
                val question = questionsQueue.peek()
                if (question == null) {
                    showResultDialog()
                } else {
                    handleNextClick(question)
                }
            }
            else -> {
                val question = questionsQueue.poll()

                if (question == null) {
                    binding.nextBtn.text = getString(R.string.finish)
                } else {
                    handleChoicesClick(v as MaterialButton, question)
                }
            }
        }
    } //end on Click

    private fun handleNextClick(question: Question) {
        Log.d(TAG, "Am In handleNextClick")
        clearAllColorsOfChoices()
        displayAQuestion(question)
        increaseCurrentQuestionNumber()
    }

    private fun increaseCurrentQuestionNumber() {
        binding.questionNumberProgressTxtView.text =
            binding.questionNumberProgressTxtView.text.toString().toInt().inc().toString()
    }

    private fun handleChoicesClick(choiceBtn: MaterialButton, question: Question) {
        enabledChoices(false)
        enableNextBtn(true)
        addTakenSeconds()

        val chosenAnswer = choiceBtn.text.toString()

        if (question.isAnswerCorrect(chosenAnswer)) {
            playCorrectSound()
            increaseCorrectAnswer()
            choiceBtn.setGreenBackground()
        } else {
            playWrongSound()
            displayTheCorrectAnswer(question.correctAnswer)
            choiceBtn.setRedBackground()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun startCountdown(countdown: Int, correctAnswer: String) {
        if (countdownLimit == 0) return
        countDownTimer = object : CountDownTimer(
            TimeUnit.SECONDS.toMillis(countdown.toLong()),
            TimeUnit.SECONDS.toMillis(1)
        ) {
            override fun onTick(millisUntilFinished: Long) {
                binding.countdownTxtView.text =
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished).toString()
            }

            override fun onFinish() {
                binding.countdownTxtView.text = "Time Out"
                addTakenSeconds()
                playWrongSound()
                questionsQueue.peek()?.let {
                    displayTheCorrectAnswer(it.correctAnswer)
                }
                enabledChoices(false)
                enableNextBtn(true)
                displayTheCorrectAnswer(correctAnswer)
            }
        }.start()
    }

    private fun addTakenSeconds() {
        countDownTimer?.cancel()
        val timeRemaining: Int = try {
            binding.countdownTxtView.text.toString().toInt()
        } catch (e: IllegalArgumentException) {
            0
        }
        val secondsToAdd = countdownLimit - timeRemaining
        totalSecondsTakenToAnswer += secondsToAdd
    }

    private fun increaseCorrectAnswer() {
        correctAnswers++
    }

    private fun playCorrectSound() {
        correctAnswerMediaPlayer?.start()
    }

    private fun playWrongSound() {
        wrongAnswerMediaPlayer?.start()
    }

    private fun MaterialButton.setGreenBackground() {
        setBackgroundColor(Color.GREEN)
    }

    private fun MaterialButton.setRedBackground() {
        setBackgroundColor(Color.RED)
    }

    private fun displayTheCorrectAnswer(correctAnswer: String) {
        when (correctAnswer) {
            binding.choice1.text.toString() -> {
                binding.choice1.setBackgroundColor(Color.GREEN)
            }
            binding.choice2.text.toString() -> {
                binding.choice2.setBackgroundColor(Color.GREEN)
            }
            binding.choice3.text.toString() -> {
                binding.choice3.setBackgroundColor(Color.GREEN)
            }
            binding.choice4.text.toString() -> {
                binding.choice4.setBackgroundColor(Color.GREEN)
            }
        }
    }

    private fun clearAllColorsOfChoices() {
        Log.d(TAG, "clearAllColorsOfChoices")
        binding.choice1.setBackgroundColor(Color.WHITE)
        binding.choice2.setBackgroundColor(Color.WHITE)
        binding.choice3.setBackgroundColor(Color.WHITE)
        binding.choice4.setBackgroundColor(Color.WHITE)
    }

    private fun showResultDialog() {
        val builder = AlertDialog.Builder(activity)
        val resultPercentage = 100.00 * correctAnswers / totalNumOfQuestions
        when {
            resultPercentage >= 90 -> {
                //awesome
                builder.setIcon(R.drawable.ic_awesome)
            }
            resultPercentage >= 80 -> {
                //good
                builder.setIcon(R.drawable.ic_good)
            }
            resultPercentage >= 60 -> {
                // not bad
                builder.setIcon(R.drawable.ic_not_bad)
            }
            resultPercentage >= 40 -> {
                // angry
                builder.setIcon(R.drawable.ic_angry)
            }
            else -> {
                //really?
                builder.setIcon(R.drawable.ic_really)
            }
        }
        builder.setTitle("Result")
        builder.setMessage(
            """Your Result is: $correctAnswers/$totalNumOfQuestions
Percentage : ${String.format(Locale.getDefault(), "%.2f", resultPercentage)}%
In Time: $totalSecondsTakenToAnswer Seconds"""
        )
            .setPositiveButton("TRY AGAIN") { _, _ -> navigateBack() }
            .create().show()
    }

    private fun navigateBack() {
        findNavController().navigateUp()
    }

    private fun displayAQuestion(question: Question) {
        binding.questionTxtView.text = question.content
        val answersList = mutableListOf<String>()
        answersList.add(question.correctAnswer)
        answersList.addAll(question.choices)
        answersList.shuffle()
        binding.choice2.text = answersList[0]
        binding.choice3.text = answersList[1]
        binding.choice1.text = answersList[2]
        binding.choice4.text = answersList[3]
        enabledChoices(true)
        enableNextBtn(false)
        startCountdown(countdownLimit, question.correctAnswer)
    }

    private fun enabledChoices(enabled: Boolean) {
        binding.choice2.isEnabled = enabled
        binding.choice3.isEnabled = enabled
        binding.choice1.isEnabled = enabled
        binding.choice4.isEnabled = enabled
    }
}