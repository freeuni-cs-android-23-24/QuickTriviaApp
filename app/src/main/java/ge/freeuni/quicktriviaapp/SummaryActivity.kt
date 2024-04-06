package ge.freeuni.quicktriviaapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class SummaryActivity : AppCompatActivity() {

    private val numCorrectAnswers: Int
        get() = intent.getIntExtra(ARG_NUM_CORRECT_ANSWERS, 0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_summary)

        val summaryText = """
            Your Score is $numCorrectAnswers/${QuizQuestions.questions.size}
        """.trimIndent()
        findViewById<TextView>(R.id.label_summary).text = summaryText
    }

    companion object {
        private const val ARG_NUM_CORRECT_ANSWERS = "arg_num_correct_answers"
    }
}