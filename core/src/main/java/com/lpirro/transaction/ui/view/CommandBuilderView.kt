package com.lpirro.transaction.ui.view

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.inputmethod.InputMethodManager
import androidx.constraintlayout.widget.ConstraintLayout
import com.lpirro.transaction.databinding.ViewCommandBuilderBinding

class CommandBuilderView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding: ViewCommandBuilderBinding
    lateinit var sendCommandListener: (() -> Unit)

    var commandInput: String = ""
        set(value) {
            field = value
            binding.commandInputEditText.setText(value)
        }
        get() = binding.commandInputEditText.text.toString()

    init {
        binding = ViewCommandBuilderBinding.inflate(LayoutInflater.from(context), this, true)
        binding.sendCommandButton.setOnClickListener { sendCommandListener.invoke() }
        binding.setChip.setOnClickListener { quickCommandClick("SET ") }
        binding.getChip.setOnClickListener { quickCommandClick("GET ") }
        binding.beginChip.setOnClickListener { quickCommandClick("BEGIN") }
        binding.commitChip.setOnClickListener { quickCommandClick("COMMIT") }
        binding.rollbackChip.setOnClickListener { quickCommandClick("ROLLBACK") }
        binding.deleteChip.setOnClickListener { quickCommandClick("DELETE ") }

        binding.commandInputEditText.setOnEditorActionListener { _, _, _ ->
            sendCommandListener.invoke()
            true
        }
    }

    private fun quickCommandClick(value: String) {
        binding.commandInputEditText.apply {
            setText(value)
            setSelection(this.length())
            requestFocus()
            val inputMethodManager = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.showSoftInput(this, 0)
        }
    }
}
