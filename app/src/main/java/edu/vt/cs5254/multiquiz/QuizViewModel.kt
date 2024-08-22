package edu.vt.cs5254.multiquiz

import androidx.lifecycle.ViewModel

class QuizViewModel : ViewModel() {

    private val questionList = listOf(
        Question(
            R.string.question_0, listOf(
                Answer(R.string.q0answer0, true),
                Answer(R.string.q0answer1, false),
                Answer(R.string.q0answer2, false),
                Answer(R.string.q0answer3, false)
            )
        ),
        Question(
            R.string.question_1, listOf(
                Answer(R.string.q1answer0, false),
                Answer(R.string.q1answer1, true),
                Answer(R.string.q1answer2, false),
                Answer(R.string.q1answer3, false)
            )
        ),
        Question(
            R.string.question_2, listOf(
                Answer(R.string.q2answer0, false),
                Answer(R.string.q2answer1, false),
                Answer(R.string.q2answer2, true),
                Answer(R.string.q2answer3, false)
            )
        ),
        Question(
            R.string.question_3, listOf(
                Answer(R.string.q3answer0, false),
                Answer(R.string.q3answer1, false),
                Answer(R.string.q3answer2, false),
                Answer(R.string.q3answer3, true)
            )
        )
    )


    private var currentIndex = 0

    val questionResId
        get() = questionList[currentIndex].questionResId

    val answerList
        get() = questionList[currentIndex].answerList

    fun nextQuestion() {
        currentIndex = (currentIndex + 1) % questionList.size
    }

    val isLastQuestion
        get() = currentIndex == questionList.size - 1

    fun resetAll() {
        questionList.flatMap{ question-> question.answerList}
            .forEach { answer->
                answer.isEnabled = true
                answer.isSelected = false
            }
    }

    val hintsUsed
        get() = questionList.flatMap { it.answerList }
            .filter { answer ->
                !answer.isEnabled
            }.size

    val correctAnswers
        get() = questionList.flatMap { question-> question.answerList}
            .filter {answer->
                answer.isCorrect && answer.isSelected
            }.size


}