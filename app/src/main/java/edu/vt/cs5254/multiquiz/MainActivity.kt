package edu.vt.cs5254.multiquiz

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import edu.vt.cs5254.multiquiz.databinding.ActivityMainBinding

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    //Name: Jaspreet Chowdhary
    //PID: jaspreet
    private lateinit var binding : ActivityMainBinding

    private val vm: QuizViewModel by viewModels()

    private lateinit var buttonList : List<Button>

    private val summaryLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        vm.nextQuestion()
        if(result.resultCode == Activity.RESULT_OK) {
            val reset = result.data?.getBooleanExtra(RESET_ALL, false) ?: false
            if(reset) {
                vm.resetAll()
                updateView()
            }
        }
        updateView()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        buttonList = listOf(
            binding.answer0Button,
            binding.answer1Button,
            binding.answer2Button,
            binding.answer3Button
        )

        binding.hintButton.setOnClickListener {
            vm.answerList.filter {answer ->
                !answer.isCorrect && answer.isEnabled
            }.random()
                .let {
                    it.isEnabled = false
                    it.isSelected = false
                }
            updateView(false)
        }

        binding.submitButton.setOnClickListener {
            if (vm.isLastQuestion) {
                val intent = SummaryActivity.newIntent(
                    this,
                    vm.correctAnswers,
                    vm.hintsUsed
                )
                summaryLauncher.launch(intent)
            } else {
                vm.nextQuestion()
                updateView()
            }

        }
        updateView()
    }

    private fun updateView(fullUpdate: Boolean = true) {

        if(fullUpdate) {
            binding.questionTextView.setText(vm.questionResId)
            vm.answerList.zip(buttonList).forEach { (answer, button) ->
                button.setText(answer.textResId)
                button.setOnClickListener {
                    vm.answerList.filter { it != answer }
                        .forEach { nonClickedAnswer ->
                            nonClickedAnswer.isSelected = false
                        }
                    answer.isSelected = !answer.isSelected
                    updateView(false)
                }
            }
        }

        vm.answerList.zip(buttonList)
            .forEach { (answer, button) ->
                button.isEnabled = answer.isEnabled
                button.isSelected = answer.isSelected
                button.updateColor()
            }
        binding.hintButton.isEnabled = vm.answerList.any { !it.isCorrect && it.isEnabled }
        binding.submitButton.isEnabled = vm.answerList.any { it.isSelected }

    }

}