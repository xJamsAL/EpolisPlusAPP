package com.example.epolisplusapp.util

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.Gravity
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.epolisplusapp.R
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import eightbitlab.com.blurview.BlurView
import eightbitlab.com.blurview.RenderScriptBlur
import java.text.NumberFormat
import java.util.Locale

object CommonUtils {

    enum class ButtonStyle {
        ENABLED,
        DISABLED
    }

    @SuppressLint("UseCompatTextViewDrawableApis")
    fun updateButtonStyle(context: Context, button: MaterialButton, style: ButtonStyle) {
        when (style) {
            ButtonStyle.ENABLED -> {
                button.apply {
                    compoundDrawableTintList =
                        ContextCompat.getColorStateList(context, R.color.white)
                    setTextColor(ContextCompat.getColor(context, R.color.white))
                    backgroundTintList =
                        ContextCompat.getColorStateList(context, R.color.green)
                    strokeColor =
                        ContextCompat.getColorStateList(context, R.color.green)
                }
            }

            ButtonStyle.DISABLED -> {
                button.apply {
                    compoundDrawableTintList =
                        ContextCompat.getColorStateList(context, R.color.edit_text_color_disabled)
                    setTextColor(ContextCompat.getColor(context, R.color.edit_text_color_disabled))
                    backgroundTintList =
                        ContextCompat.getColorStateList(context, R.color.edit_disabled_back)
                    strokeColor =
                        ContextCompat.getColorStateList(context, R.color.edit_disabled_stroke)
                }
            }
        }
    }

    @SuppressLint("InflateParams")
    fun showCustomToast(context: Context, message: String) {
        val inflater = LayoutInflater.from(context)
        val layout = inflater.inflate(R.layout.custom_toast, null)

        val textView = layout.findViewById<TextView>(R.id.tvCustomSnackBar)
        textView.text = message

        val toast = Toast(context)
        toast.duration = Toast.LENGTH_LONG
        toast.view = layout
        toast.setGravity(Gravity.FILL_HORIZONTAL, 0, 10000)
        toast.show()
    }

    fun setupToolbar(toolbar: Toolbar, activity: AppCompatActivity) {
        activity.setSupportActionBar(toolbar)
        activity.supportActionBar?.setDisplayShowTitleEnabled(true)
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            activity.finish()
        }
        toolbar.setOnClickListener {
            activity.finish()
        }
    }

    fun setupPasswordVisibility(editText: EditText, isPasswordVisible: Boolean): Boolean {
        if (isPasswordVisible) {
            editText.transformationMethod = PasswordTransformationMethod.getInstance()
            editText.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.auth_input_eye,
                0
            )
        } else {
            editText.transformationMethod = HideReturnsTransformationMethod.getInstance()
            editText.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.auth_input_crossed_eye,
                0
            )
        }
        editText.setSelection(editText.text.length)
        return !isPasswordVisible
    }

    fun hideKeyboard(activity: AppCompatActivity) {
        val inputMethodManager =
            activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val currentFocusView = activity.currentFocus
        if (currentFocusView != null) {
            inputMethodManager.hideSoftInputFromWindow(currentFocusView.windowToken, 0)
        }
    }

    fun setupEditTexts(activity: AppCompatActivity, containerId: Int, editTextIds: IntArray) {
        val container: TextView = activity.findViewById(containerId)
        val editTexts = editTextIds.map { activity.findViewById<EditText>(it) }.toTypedArray()

        container.setOnClickListener {
            val firstEmptyEditText = editTexts.find { it.text.isEmpty() }
            if (firstEmptyEditText != null) {
                firstEmptyEditText.requestFocus()
                val imm =
                    activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(firstEmptyEditText, InputMethodManager.SHOW_IMPLICIT)
            } else {
                val lastEditText = editTexts.last()
                lastEditText.requestFocus()
                val imm =
                    activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(lastEditText, InputMethodManager.SHOW_IMPLICIT)
            }
        }

        for (i in editTexts.indices) {
            val editText = editTexts[i]

            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s != null) {
                        if (s.length == 1 && i < editTexts.size - 1) {
                            val nextEditText = editTexts[i + 1]
                            nextEditText.requestFocus()
                            nextEditText.setSelection(nextEditText.text.length)
                        } else if (s.length > 1 && i < editTexts.size - 1) {
                            val nextEditText = editTexts[i + 1]
                            nextEditText.requestFocus()
                            nextEditText.setSelection(nextEditText.text.length)
                        }
                    }
                }

                override fun afterTextChanged(s: Editable?) {}
            })

            editText.setOnKeyListener { _, keyCode, event ->
                if (event?.action == KeyEvent.ACTION_DOWN) {
                    when (keyCode) {
                        KeyEvent.KEYCODE_DEL -> {
                            if (i > 0) {
                                val prevEditText = editTexts[i - 1]
                                prevEditText.setSelection(prevEditText.text.length)
                                prevEditText.requestFocus()
                            }
                        }

                        in KeyEvent.KEYCODE_0..KeyEvent.KEYCODE_9 -> {
                            if (editText.text.isNotEmpty() && i < editTexts.size - 1) {
                                val nextEditText = editTexts[i + 1]
                                nextEditText.requestFocus()
                                nextEditText.setSelection(nextEditText.text.length)
                                return@setOnKeyListener true
                            }
                        }
                    }
                }
                false
            }
        }
    }

    fun setupEditTextsDialog(
        fragment: BottomSheetDialogFragment,
        containerId: Int,
        editTextIds: IntArray,
        limits: IntArray
    ) {
        val view = fragment.requireView()
        val container: View = view.findViewById(containerId)
        val editTexts = editTextIds.map { view.findViewById<EditText>(it) }.toTypedArray()

        container.setOnClickListener {
            val firstEmptyEditText = editTexts.find { it.text.isEmpty() }
            if (firstEmptyEditText != null) {
                firstEmptyEditText.requestFocus()
                val imm = fragment.requireContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(firstEmptyEditText, InputMethodManager.SHOW_IMPLICIT)
            } else {
                val lastEditText = editTexts.last()
                lastEditText.requestFocus()
                val imm = fragment.requireContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(lastEditText, InputMethodManager.SHOW_IMPLICIT)
            }
        }

        for (i in editTexts.indices) {
            val editText = editTexts[i]
            val limit = limits.getOrNull(i) ?: 2

            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s != null) {

                        if (s.length >= limit && i < editTexts.size - 1) {
                            val nextEditText = editTexts[i + 1]
                            nextEditText.requestFocus()
                            nextEditText.setSelection(nextEditText.text.length)
                        }
                    }
                }

                override fun afterTextChanged(s: Editable?) {}
            })

            editText.setOnKeyListener { _, keyCode, event ->
                if (event?.action == KeyEvent.ACTION_DOWN) {
                    when (keyCode) {
                        KeyEvent.KEYCODE_DEL -> {
                            if (i > 0 && editText.text.isEmpty()) {
                                val prevEditText = editTexts[i - 1]
                                prevEditText.requestFocus()
                                prevEditText.setSelection(prevEditText.text.length)
                            }
                        }

                        in KeyEvent.KEYCODE_0..KeyEvent.KEYCODE_9 -> {
                            if (editText.text.length >= limit && i < editTexts.size - 1) {
                                val nextEditText = editTexts[i + 1]
                                nextEditText.requestFocus()
                                nextEditText.setSelection(nextEditText.text.length)
                                return@setOnKeyListener true
                            }
                        }
                    }
                }
                false
            }
        }
    }

    fun setupEditTextsFrag(
        fragment: Fragment,
        containerId: Int,
        editTextIds: IntArray,
        limits: IntArray
    ) {
        val view = fragment.requireView()
        val container: View = view.findViewById(containerId)
        val editTexts = editTextIds.map { view.findViewById<EditText>(it) }.toTypedArray()

        container.setOnClickListener {
            val firstEmptyEditText = editTexts.find { it.text.isEmpty() }
            if (firstEmptyEditText != null) {
                firstEmptyEditText.requestFocus()
                val imm = fragment.requireContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(firstEmptyEditText, InputMethodManager.SHOW_IMPLICIT)
            } else {
                val lastEditText = editTexts.last()
                lastEditText.requestFocus()
                val imm = fragment.requireContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(lastEditText, InputMethodManager.SHOW_IMPLICIT)
            }
        }

        for (i in editTexts.indices) {
            val editText = editTexts[i]
            val limit = limits.getOrNull(i) ?: 2

            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s != null) {

                        if (s.length >= limit && i < editTexts.size - 1) {
                            val nextEditText = editTexts[i + 1]
                            nextEditText.requestFocus()
                            nextEditText.setSelection(nextEditText.text.length)
                        }
                    }
                }

                override fun afterTextChanged(s: Editable?) {}
            })

            editText.setOnKeyListener { _, keyCode, event ->
                if (event?.action == KeyEvent.ACTION_DOWN) {
                    when (keyCode) {
                        KeyEvent.KEYCODE_DEL -> {
                            if (i > 0 && editText.text.isEmpty()) {
                                val prevEditText = editTexts[i - 1]
                                prevEditText.requestFocus()
                                prevEditText.setSelection(prevEditText.text.length)
                            }
                        }

                        in KeyEvent.KEYCODE_0..KeyEvent.KEYCODE_9 -> {
                            if (editText.text.length >= limit && i < editTexts.size - 1) {
                                val nextEditText = editTexts[i + 1]
                                nextEditText.requestFocus()
                                nextEditText.setSelection(nextEditText.text.length)
                                return@setOnKeyListener true
                            }
                        }
                    }
                }
                false
            }
        }
    }


    fun formatPhoneNumber(phoneNumber: String): String {
        return phoneNumber.filter { it.isDigit() }
    }

    fun setupBlurView(context: Context, blurView: BlurView, rootView: View) {
        val radius = 5f

        val windowBackground: Drawable? =
            (context as? AppCompatActivity)?.window?.decorView?.background

        blurView.setupWith(rootView as ViewGroup, RenderScriptBlur(context))
            .setFrameClearDrawable(windowBackground)
            .setBlurRadius(radius)

    }

    @SuppressLint("ClickableViewAccessibility")
    fun setupBlurViewForFragment(fragment: Fragment, blurView: BlurView) {
        val radius = 5f
        val context = fragment.requireContext()
        val rootView = fragment.requireView()
        val windowBackground: Drawable? =
            (fragment.requireActivity() as? AppCompatActivity)?.window?.decorView?.background

        blurView.apply {
            setupWith(rootView as ViewGroup, RenderScriptBlur(context))
                .setFrameClearDrawable(windowBackground)
                .setBlurRadius(radius)


            isClickable = true
            isFocusable = true
            setOnTouchListener { _, _ -> true }
        }


    }

    fun hideKeyboard(fragment: Fragment) {
        val inputMethodManager = fragment.requireContext()
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val currentFocusView = fragment.view?.rootView ?: return
        inputMethodManager.hideSoftInputFromWindow(
            currentFocusView.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }

    fun hidekeyboardDialog(dialog: BottomSheetDialogFragment?) {
        val inputMethodManager = dialog?.requireContext()
            ?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val currentFocusView = dialog?.view?.rootView ?: return
        inputMethodManager.hideSoftInputFromWindow(
            currentFocusView.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }


    fun setupBottomSheetBehavior(view: View, dialog: BottomSheetDialogFragment?) {
        val bottomSheet = view.parent as View
        val behavior = BottomSheetBehavior.from(bottomSheet)
        dialog?.dialog?.window?.setDimAmount(1.0f)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        behavior.isHideable = false
        behavior.isDraggable = false
        behavior.peekHeight = ViewGroup.LayoutParams.MATCH_PARENT


    }

    fun setupToolbarDialog(toolbar: MaterialToolbar, fragment: BottomSheetDialogFragment) {
        toolbar.setNavigationOnClickListener {
            fragment.dismiss()
        }
        toolbar.setOnClickListener { fragment.dismiss() }

    }

    fun setupToolbarFrag(toolbar: MaterialToolbar, fragment: Fragment) {
        toolbar.setNavigationOnClickListener {
            fragment.requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        toolbar.setOnClickListener { fragment.requireActivity().onBackPressedDispatcher.onBackPressed() }
    }

    fun reloadFragment(fragmentManager: FragmentManager, fragment: Fragment) {
        fragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, fragment)
            .commit()
    }

    fun formatAndSetTexts(
        context: Context,
        pairs: List<Pair<TextView, Int>>,
    ) {

        pairs.forEach{(textView, value) ->
            textView.text = formatSumWithSeparatorAndCurrency(
                value,
                context
            )
        }
    }

    fun formatSumWithSeparatorAndCurrency(
        total: Int,
        context: Context,
        currencyResId: Int = R.string.dop_uslugi_currency
    ): String {
        val locale = Locale("ru", "Ru")
        val formatter = NumberFormat.getInstance(locale)
        formatter.isGroupingUsed = true
        val formattedSum = formatter.format(total)
        val currency = context.getString(currencyResId)
        return "$formattedSum $currency"
    }


    fun EditText.addDateMask() {
        this.addTextChangedListener(object : TextWatcher {
            private var isUpdating = false
            private val dateFormat = "dd.mm.yyyy"
            private val divider = "."

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (isUpdating) return
                isUpdating = true
                val input = s.toString().replace(divider.toString(), "")
                val formatted = StringBuilder()

                for (i in input.indices) {
                    formatted.append(input[i])
                    if ((i == 1 || i == 3) && i != input.length - 1) {
                        formatted.append(divider)
                    }
                }
                this@addDateMask.setText(formatted.toString())
                this@addDateMask.setSelection(formatted.length)
                isUpdating = false
            }

        })
    }


}
