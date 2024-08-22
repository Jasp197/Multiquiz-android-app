package edu.vt.cs5254.multiquiz

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import edu.vt.cs5254.multiquiz.databinding.ActivitySummaryBinding

private const val EXTRA_CORRECT = "edu.vt.cs5254.multiquiz.correct_answers"
private const val EXTRA_HINTS = "edu.vt.cs5254.multiquiz.hints_used"
const val RESET_ALL = "edu.vt.cs5254.multiquiz.reset_all"


class SummaryActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySummaryBinding

    private val summaryVM : SummaryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySummaryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.resetAllButton.setOnClickListener {
            summaryVM.setReset()
            intent.apply{
                putExtra(EXTRA_CORRECT, "0")
                putExtra(EXTRA_HINTS, "0")
            }
            updateView()
        }
        updateView()
    }

    private fun updateView() {

        val data = Intent().apply {
            putExtra(RESET_ALL, summaryVM.getReset)
        }
        setResult(Activity.RESULT_OK, data)

        binding.correctAnswersCount.text = intent.getIntExtra(EXTRA_CORRECT, 0).toString()
        binding.hintsUsedCount.text = intent.getIntExtra(EXTRA_HINTS, 0).toString()

        binding.resetAllButton.isEnabled = !summaryVM.getReset

    }

    companion object {
        fun newIntent(
            packageContext: Context,
            correctAnswers: Int,
            hintsUsed: Int
        ): Intent {
            return Intent(packageContext, SummaryActivity::class.java).apply {
                putExtra(EXTRA_CORRECT, correctAnswers)
                putExtra(EXTRA_HINTS, hintsUsed)
            }
        }
    }
}