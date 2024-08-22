package edu.vt.cs5254.multiquiz

import androidx.lifecycle.ViewModel

class SummaryViewModel : ViewModel() {

    private var isReset: Boolean = false

    val getReset
        get() = isReset

    fun setReset() {
        isReset = true
    }

}