package ge.freeuni.quicktriviaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat

class QuestionActivity : AppCompatActivity() {

    private val questionIndex: Int
        get() = intent.getIntExtra(ARG_QUESTION_INDEX, 0)

    private var userChoice: Boolean? = null

    private val labelQuestion by lazy {
        findViewById<TextView>(R.id.label_question)
    }

    private val buttonYes by lazy {
        findViewById<Button>(R.id.button_yes)
    }

    private val buttonNo by lazy {
        findViewById<Button>(R.id.button_no)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)
        // restore state
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(KEY_USER_CHOICE)) {
                savedInstanceState.getBoolean(KEY_USER_CHOICE).let {
                    onUserChoice(it)
                }
            }
        }

        labelQuestion.text = QuizQuestions.questions[questionIndex].first
        buttonNo.setOnClickListener {
            onUserChoice(false)
            navigateToNextScreen()
        }
        buttonYes.setOnClickListener {
            onUserChoice(true)
            navigateToNextScreen()
        }
    }

    private fun onUserChoice(userChoice: Boolean) {
        this.userChoice = userChoice

        val selectionColor = ContextCompat.getColor(this, R.color.button_selected)
        val normalColor = ContextCompat.getColor(this, R.color.button_normal)

        if (userChoice) {
            buttonYes.setBackgroundColor(selectionColor)
            buttonNo.setBackgroundColor(normalColor)
        } else {
            buttonYes.setBackgroundColor(normalColor)
            buttonNo.setBackgroundColor(selectionColor)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        userChoice?.let {
            outState.putBoolean(KEY_USER_CHOICE, it)
        }
    }

    private fun navigateToNextScreen() {
        val isAnswerCorrect = QuizQuestions.questions[questionIndex].second == userChoice
        val numCorrectAnswers = intent.getIntExtra(ARG_NUM_CORRECT_ANSWERS, 0)
            .let { numCorrectAnswersUntilNow ->
                if (isAnswerCorrect) {
                    numCorrectAnswersUntilNow + 1
                } else {
                    numCorrectAnswersUntilNow
                }
            }

        val intent = if (questionIndex == QuizQuestions.questions.lastIndex) {
            Intent(this, SummaryActivity::class.java).also {
                it.putExtra(ARG_NUM_CORRECT_ANSWERS, numCorrectAnswers)
                // clear history.
                it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            }
        } else {
            Intent(this, QuestionActivity::class.java).also {
                it.putExtra(ARG_QUESTION_INDEX, questionIndex + 1)
                it.putExtra(ARG_NUM_CORRECT_ANSWERS, numCorrectAnswers)
            }
        }
        startActivity(intent)
    }

    companion object {
        private const val ARG_QUESTION_INDEX = "arg_question_index"
        private const val ARG_NUM_CORRECT_ANSWERS = "arg_num_correct_answers"
        private const val KEY_USER_CHOICE = "key_user_choice"
    }
}