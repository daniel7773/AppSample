package com.example.appsample.framework.presentation.auth.register

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.Toast
import android.widget.ViewFlipper
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.example.appsample.R
import com.example.appsample.framework.base.presentation.BaseFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import java.util.*

/**
 *  PLEASE READ this before hate me for this shit code -->
 *  This fragment is needed just to show working with xml files
 */

@FlowPreview
@ExperimentalCoroutinesApi
class RegisterFragment : BaseFragment(R.layout.fragment_register),
    DatePickerDialog.OnDateSetListener {

    private lateinit var viewFlipper: ViewFlipper

    //first stage
    private lateinit var emailEditText: TextInputEditText
    private lateinit var passwordEditText: TextInputEditText
    private lateinit var confirmPasswordEditText: TextInputEditText

    private lateinit var emailHint: MaterialTextView
    private lateinit var passwordHint: MaterialTextView
    private lateinit var confirmPasswordHint: MaterialTextView
    private lateinit var continueButtonStage1: MaterialButton

    //second stage
    private lateinit var arrowBackStage2: MaterialTextView
    private lateinit var userAvatar: ImageView
    private lateinit var firstName: TextInputEditText
    private lateinit var secondName: TextInputEditText
    private lateinit var sexMale: MaterialTextView
    private lateinit var sexFemale: MaterialTextView
    private lateinit var continueButtonStage2: MaterialButton

    //third stage
    private lateinit var arrowBackStage3: MaterialTextView
    private lateinit var birtdateEditText: TextInputEditText
    private lateinit var continueButtonStage3: MaterialButton

    //fourth stage
    private lateinit var arrowBackStage4: MaterialTextView
    private lateinit var trainingProfile: Spinner
    private lateinit var desiredUniversity: TextInputEditText
    private lateinit var registerButton: MaterialButton
    private var progressBar: ProgressBar? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewFlipper = view.findViewById(R.id.view_flipper)

        //first stage
        emailEditText = view.findViewById(R.id.input_email)
        emailHint = view.findViewById(R.id.email_hint)
        passwordEditText = view.findViewById(R.id.input_password)
        passwordHint = view.findViewById(R.id.password_hint)
        confirmPasswordEditText = view.findViewById(R.id.input_confirm_password)
        confirmPasswordHint = view.findViewById(R.id.confirm_password_hint)
        continueButtonStage1 = view.findViewById(R.id.btn_continue_stage_1)
        continueButtonStage1.setOnClickListener { checkFirstStage() }

        // second stage
        arrowBackStage2 = view.findViewById(R.id.back_stage_2)
        userAvatar = view.findViewById<ImageView>(R.id.user_avatar)
        firstName = view.findViewById(R.id.edit_text_first_name)
        secondName = view.findViewById(R.id.edit_text_second_name)
        sexMale = view.findViewById(R.id.text_view_male)
        sexFemale = view.findViewById(R.id.text_view_female)
        continueButtonStage2 = view.findViewById(R.id.continue_button_stage_2)
        arrowBackStage2.setOnClickListener { showPreviousStage() }
        sexMale.setOnClickListener { onSexSelected("male") }
        sexFemale.setOnClickListener { onSexSelected("female") }
        continueButtonStage2.setOnClickListener { checkSecondStage() }

        //third stage
        arrowBackStage3 = view.findViewById(R.id.back_stage_3)
        birtdateEditText = view.findViewById(R.id.edit_text_birtdate)
        continueButtonStage3 = view.findViewById(R.id.continue_button_stage_3)
        arrowBackStage3.setOnClickListener { showPreviousStage() }
        birtdateEditText.setOnClickListener { onCreateDialog().show() }
        continueButtonStage3.setOnClickListener { showNextStage() }

        //fourth stage
        arrowBackStage4 = view.findViewById(R.id.back_stage_4)
        trainingProfile = view.findViewById(R.id.spinner_training_profile)
        val adapter = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            arrayOf("-", "Гуманитарный", "Технический", "Научный")
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        trainingProfile.adapter = adapter
        trainingProfile.setSelection(0)


        desiredUniversity = view.findViewById(R.id.input_desired_university)
        registerButton = view.findViewById(R.id.register_button)
        arrowBackStage4.setOnClickListener { showPreviousStage() }
        registerButton.setOnClickListener { onRegisterClicked() }

        progressBar = view.findViewById(R.id.progress_bar)

        emailEditText.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                emailHint.text = ""
                (v as TextInputEditText).setBackgroundResource(0)
                v.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_edittext)
            }
        }
        passwordEditText.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                (v as TextInputEditText).setBackgroundResource(0)
                v.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_edittext)
                passwordHint.text = ""
            }
        }
        confirmPasswordEditText.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                (v as TextInputEditText).setBackgroundResource(0)
                v.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_edittext)
                confirmPasswordHint.text = ""
            }
        }
        firstName.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                (v as TextInputEditText).setBackgroundResource(0)
                v.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_edittext)
                firstName.hint = "Имя"
            }
        }

        subscribeObservers()
    }

    private fun checkSecondStage() {
        if (checkFirstName()) {
            showNextStage()
        }
    }

    private fun checkFirstStage() {
        if (checkEmail() and checkPassword() and checkConfirmPassword()) {
            showNextStage()
        }
    }

    fun onCreateDialog(): Dialog { // Use the current date as the default date in the picker
        val c = Calendar.getInstance()
        val year = c[Calendar.YEAR]
        val month = c[Calendar.MONTH]
        val day = c[Calendar.DAY_OF_MONTH]
        // Create a new instance of DatePickerDialog and return it
        return DatePickerDialog(requireContext(), this, year, month, day)
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        val dayString = if (day < 10) "0$day" else day.toString()
        val monthString =
            if (month < 10) 0.toString() + "" + (month + 1) else month.toString()
        birtdateEditText.setText("$dayString.$monthString.$year")
    }

    fun showNextStage() {
        val animationFlipIn = AnimationUtils.loadAnimation(
            requireContext(),
            R.anim.slide_from_right_transparent
        )
        val animationFlipOut = AnimationUtils.loadAnimation(
            requireContext(),
            R.anim.slide_to_left
        )
        viewFlipper.inAnimation = animationFlipIn
        viewFlipper.outAnimation = animationFlipOut
        viewFlipper.showNext()
    }

    fun showPreviousStage() {
        if (viewFlipper.displayedChild == 0) return
        val animationFlipIn = AnimationUtils.loadAnimation(
            requireContext(),
            R.anim.slide_from_left
        )
        val animationFlipOut = AnimationUtils.loadAnimation(
            requireContext(),
            R.anim.slide_to_right
        )
        viewFlipper.inAnimation = animationFlipIn
        viewFlipper.outAnimation = animationFlipOut
        viewFlipper.showPrevious()
    }

    private fun onSexSelected(string: String) {
        if (string == "male") {
            sexMale.background = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.rounded_corners_gray_selected
            )
            sexFemale.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.rounded_corners_gray)
        } else {
            sexMale.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.rounded_corners_gray)
            sexFemale.background = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.rounded_corners_gray_selected
            )
        }
    }

    private fun subscribeObservers() {}

    private fun showProgressBar(isVisible: Boolean) {
        if (isVisible) {
            progressBar!!.visibility = View.VISIBLE
        } else {
            progressBar!!.visibility = View.GONE
        }
    }

    private fun onRegisterSuccess() {
        findNavController().navigate(R.id.action_registrationFragment_to_authFragment)
    }

    private fun onRegisterClicked() {
        if (checkEmail() and checkPassword() and checkConfirmPassword() and checkFirstName()) {
            onRegisterSuccess()
        } else {
            Toast.makeText(
                requireContext(),
                R.string.some_error_during_registration,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun checkEmail(): Boolean {
        if (TextUtils.isEmpty(emailEditText.text)) {
            emailEditText.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.bg_edittext_error)
            emailHint.text = "Вы забыли ввести почту"
            return false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailEditText.text.toString()).matches()) {
            emailEditText.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.bg_edittext_error)
            emailHint.text = "Вы ввели некорректную почту"
            return false
        }
        return true
    }

    private fun checkPassword(): Boolean {
        if (TextUtils.isEmpty(passwordEditText.text)) {
            passwordEditText.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.bg_edittext_error)
            passwordHint.text = "Вы забыли ввести пароль"
            return false
        }
        return true
    }

    private fun checkConfirmPassword(): Boolean {

        if (TextUtils.isEmpty(confirmPasswordEditText.text)) {
            confirmPasswordEditText.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.bg_edittext_error)
            confirmPasswordHint.text = "Вы забыли ввести пароль ещё раз"
            return false
        } else if (passwordEditText.text.toString() != confirmPasswordEditText.text.toString()) {
            confirmPasswordEditText.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.bg_edittext_error)
            confirmPasswordHint.text = getString(R.string.passwords_not_match)
            Toast.makeText(requireContext(), R.string.passwords_not_match, Toast.LENGTH_SHORT)
                .show()
            return false
        }
        return true
    }

    private fun checkFirstName(): Boolean {
        if (TextUtils.isEmpty(firstName.text)) {
            firstName.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.bg_edittext_error)
            return false
        }
        return true
    }

}