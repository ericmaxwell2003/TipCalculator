package com.acme.tipcalculator.viewmodel

import android.arch.lifecycle.LiveData
import android.databinding.BaseObservable
import android.databinding.Bindable
import com.acme.tipcalculator.BR
import com.acme.tipcalculator.model.Calculator
import com.acme.tipcalculator.model.TipCalculation


/**
 * TODO Lab 2: Binding Data - Extend the abstract base class BaseObservable so that this class can provide
 * observable data for the view and notify the view when properties change.
 *
 */
class CalculatorViewModel constructor(private val calculator: Calculator = Calculator()) {

    var checkAmtInput = ""
    var tipPctInput = ""

    /**
     * TODO Lab 2: Mark tipCalculation as a Bindable variable so that the view can register
     *        a property change listener and receive updates when this property changes.
     */
    var tipCalculation = TipCalculation()

    fun calculateTip() {

        val checkAmt = checkAmtInput.toDoubleOrNull()
        val tipPctAmt = tipPctInput.toIntOrNull()

        if(checkAmt != null && tipPctAmt != null) {
            tipCalculation = calculator.calculateTip(checkAmt, tipPctAmt)

            /**
             * TODO Lab 2: Add a line below this comment to call the appropriate BaseObservable function
             *        to notify the view that the BR.tipCalculation property has changed.
             */
        }
    }

    fun loadTipCalculation(tc: TipCalculation) {
        checkAmtInput = tc.checkAmount.toString()
        tipPctInput = tc.tipPct.toString()
        tipCalculation = tc

        /**
         * TODO Lab 2: Add a line below this comment to call the appropriate BaseObservable function
         *        to notify the view that all properties of this viewModel have changed.
         */
    }

    fun saveCurrentTip(name: String) {
        val tipToSave = tipCalculation.copy(locationName = name)
        calculator.saveTipCalculation(tipToSave)
        tipCalculation = tipToSave

        /**
         * TODO Lab 2: Add a line below this comment to call the appropriate BaseObservable function
         *        to notify the view that the tipCalculation property has changed.
         */
    }

    fun loadSavedTipCalculations() : LiveData<List<TipCalculation>> {
        return calculator.loadSavedTipCalculations()
    }


}