package com.acme.tipcalculator.viewmodel

import android.arch.lifecycle.LiveData
import com.acme.tipcalculator.model.Calculator
import com.acme.tipcalculator.model.TipCalculation


class CalculatorViewModel constructor(private val calculator: Calculator = Calculator()) {

    var checkAmtInput = ""
    var tipPctInput = ""

    var tipCalculation = TipCalculation()

    fun calculateTip() {

        val checkAmt = checkAmtInput.toDoubleOrNull()
        val tipPctAmt = tipPctInput.toIntOrNull()

        if (checkAmt != null && tipPctAmt != null) {
            tipCalculation = calculator.calculateTip(checkAmt, tipPctAmt)
        }
    }

    fun loadTipCalculation(tc: TipCalculation) {
        checkAmtInput = tc.checkAmount.toString()
        tipPctInput = tc.tipPct.toString()
        tipCalculation = tc
    }

    fun saveCurrentTip(name: String) {
        val tipToSave = tipCalculation.copy(locationName = name)
        calculator.saveTipCalculation(tipToSave)
        tipCalculation = tipToSave
    }

    fun loadSavedTipCalculations(): LiveData<List<TipCalculation>> {
        return calculator.loadSavedTipCalculations()
    }

}