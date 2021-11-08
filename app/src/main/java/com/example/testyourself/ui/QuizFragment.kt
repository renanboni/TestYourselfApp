package com.example.testyourself.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.constraintlayout.widget.Group
import androidx.core.content.ContentProviderCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.testyourself.R
import com.example.testyourself.services.QuizService
import com.example.testyourself.services.models.Response
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.google.android.material.textview.MaterialTextView
import com.google.gson.Gson

class QuizFragment : Fragment(R.layout.fragment_quiz) {

    lateinit var firstAnswer: MaterialCardView
    lateinit var secondAnswer: MaterialCardView
    lateinit var thirdAnswer: MaterialCardView
    lateinit var fourthAnswer: MaterialCardView
    private lateinit var txtQuestion: MaterialTextView
    lateinit var alternatives: Array<MaterialCardView?>
    private lateinit var groupViews: Group

    private lateinit var txtFirstAnswser: MaterialTextView
    private lateinit var txtSecondAnswser: MaterialTextView
    private lateinit var txtThirdAnswser: MaterialTextView
    private lateinit var txtFourthAnswser: MaterialTextView

    lateinit var incorrectAnswers: List<String>
    var btnContinue: MaterialButton? = null
    private var txtProgress: MaterialTextView? = null

    lateinit var result: Response
    lateinit var question: String
    private lateinit var progress: LinearProgressIndicator
    lateinit var backendJson: String
    lateinit var loadingSpinner: ProgressBar

    private val quizPresenter = QuizPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            quizPresenter.getQuiz()
        } else {
            val json: String? = savedInstanceState.getString(JSON)
            val objt = Gson().fromJson(json, Response::class.java)
            backendJson = json!!
            result = objt
            question = result.results[quizPresenter.currentQuestionIndex].question
            quizPresenter.correctAnswer =
                result.results[quizPresenter.currentQuestionIndex].correct_answer
            incorrectAnswers = result.results[quizPresenter.currentQuestionIndex].incorrect_answers
            quizPresenter.currentQuestionIndex =
                savedInstanceState.getInt(CURRENT_QUESTION_INDEX_KEY)
            quizPresenter.selectedIndex = savedInstanceState.getInt(SELECTED_INDEX_KEY)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(JSON, backendJson)
        outState.putInt(SELECTED_INDEX_KEY, quizPresenter.selectedIndex)
        outState.putInt(CURRENT_QUESTION_INDEX_KEY, quizPresenter.currentQuestionIndex)
    }

    fun setQuestionTxt(txt: String) {
        activity?.runOnUiThread { txtQuestion.text = txt }
    }

    fun setAnswers(first: String, second: String, third: String, fourth: String) {
        activity?.runOnUiThread {
            txtFirstAnswser.text = first
            txtSecondAnswser.text = second
            txtThirdAnswser.text = third
            txtFourthAnswser.text = fourth
        }
    }

    fun showError(error: String) {
        //Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
    }

    fun setMaxProgress(max: Int) {
        progress.max = max
    }

    fun setProgressText(actual: Int) {
        activity?.runOnUiThread {
            txtProgress?.text = "$actual/${progress.max}"
        }
    }

    fun showLoading() {
        loadingSpinner.visibility = View.VISIBLE
    }

    fun hideLoading() {
        activity?.runOnUiThread {
            loadingSpinner.visibility = View.INVISIBLE
        }
    }

    fun showViews() {
        activity?.runOnUiThread {
            groupViews.visibility = View.VISIBLE
        }
    }

    fun hideViews() {
        groupViews.visibility = View.INVISIBLE
    }

    fun enableContinueButton() {
        btnContinue?.isEnabled = true
    }

    fun disableContinueButton() {
        btnContinue?.isEnabled = false
    }

    fun setCardProperties(
        card: MaterialCardView?,
        background: Int = R.color.white,
        stroke: Int = R.color.white,
        strokeWidth: Int = 0
    ) {
        card?.setBackgroundColor(ContextCompat.getColor(requireContext(), background))
        card?.strokeColor = (ContextCompat.getColor(requireContext(), stroke))
        card?.strokeWidth = strokeWidth
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firstAnswer = view.findViewById(R.id.first_answer)
        secondAnswer = view.findViewById(R.id.second_answer)
        thirdAnswer = view.findViewById(R.id.third_answer)
        fourthAnswer = view.findViewById(R.id.fourth_answer)
        txtQuestion = view.findViewById(R.id.txt_question)
        txtFirstAnswser = view.findViewById(R.id.txt_first_answer)
        txtSecondAnswser = view.findViewById(R.id.txt_second_answer)
        txtThirdAnswser = view.findViewById(R.id.txt_third_answer)
        txtFourthAnswser = view.findViewById(R.id.txt_fourth_answer)
        btnContinue = view.findViewById(R.id.btn_continue)
        txtProgress = view.findViewById(R.id.txt_progress)
        progress = view.findViewById(R.id.progress)
        loadingSpinner = view.findViewById(R.id.loading)
        groupViews = view.findViewById(R.id.group_views)

        alternatives = arrayOf(firstAnswer, secondAnswer, thirdAnswer, fourthAnswer)

        alternatives.forEachIndexed { index, materialCardView ->
            alternatives[index]?.setOnClickListener {
               quizPresenter.setSelectedOption(index)
            }
        }


        btnContinue?.setOnClickListener {
            quizPresenter.setBtnContinueClick()
        }

        if (savedInstanceState != null) {
            //setCardTexts()
            // setProgress(currentQuestionIndex)
            quizPresenter.setSelectedOption(quizPresenter.selectedIndex)
            //setProgress(currentQuestionIndex)
        }
    }

    companion object {
        private const val SELECTED_INDEX_KEY = "selectedIndex"
        private const val CURRENT_QUESTION_INDEX_KEY = "currentQuestionIndex"
        private const val JSON = "json"
        private const val QUESTION_DELAY = 1000L
    }
}














