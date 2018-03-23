package com.acme.tipcalculator.view

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.acme.tipcalculator.R
import com.acme.tipcalculator.model.TipCalculation
import com.acme.tipcalculator.viewmodel.CalculatorViewModel

class TipCalculatorActivity : AppCompatActivity(),
        LoadDialogFragment.Callback,
        SaveDialogFragment.Callback {

    /**
     *  TODO Lab 1: After turning on databinding in your gradle file, and wrapping the layouts
     *  inside of `activity_tip_calculator.xml` in layout tags.  Uncomment the following
     *  lateinit var called binding of the generated binding type from `activity_tip_calculator.xml`
     **/
    // lateinit var binding: ActivityTipCalculatorBinding

    private lateinit var calculatorViewModel: CalculatorViewModel

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_tip_calculator, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.action_save -> {
                showSaveDialog()
                true
            }
            R.id.action_load -> {
                showLoadDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        calculatorViewModel = CalculatorViewModel()

        /**
         * TODO Lab 1: Use the static DataBindingUtil.setContentView function to set the content view
         * and generate the binding for this view in one step.
         *
         * Use this to set the lateinit binding var that you defined at the class level.
         */
         setContentView(R.layout.activity_tip_calculator)

        setSupportActionBar(getToolbar())

        getFloatingActionButton().setOnClickListener { _ ->

            calculatorViewModel.checkAmtInput = getInputCheckoutAmountValue()
            calculatorViewModel.tipPctInput = getInputTipPercentageValue()

            // Invoke Calculate Tip on the ViewModel
            calculatorViewModel.calculateTip()

            // After the calculation, we need to manually update our view elements
            updateView()
        }
    }

    fun showSaveDialog() {
        val saveFragment = SaveDialogFragment()
        saveFragment.show(supportFragmentManager, "SaveDialog")
    }

    fun showLoadDialog() {
        val loadFragment = LoadDialogFragment()
        loadFragment.show(supportFragmentManager, "LoadDialog")
    }

    override fun onTipSelected(tipCalc: TipCalculation) {
        calculatorViewModel.loadTipCalculation(tipCalc)
        updateView(true)
        Snackbar.make(getRootView(), "Loaded ${tipCalc.locationName}", Snackbar.LENGTH_SHORT).show()
    }

    override fun onSaveTip(name: String) {
        calculatorViewModel.saveCurrentTip(name)
        updateView()
        Snackbar.make(getRootView(), "Saved $name", Snackbar.LENGTH_SHORT).show()
    }

    /**
     * TODO Lab 1: Get views from the binding instead of using findViewById.
     */
    private fun getInputTipPercentageValue() : String {
        return (findViewById<EditText>(R.id.input_tip_percentage)).text.toString()
    }

    private fun getInputCheckoutAmountValue() : String {
        return (findViewById<EditText>(R.id.input_check_amount)).text.toString()
    }

    private fun getFloatingActionButton() : FloatingActionButton {
        return findViewById<FloatingActionButton>(R.id.calculate_fab)
    }

    private fun getRootView(): View {
        return window.decorView.findViewById<View>(android.R.id.content)
    }

    private fun TipCalculatorActivity.getToolbar(): Toolbar {
        return findViewById<Toolbar>(R.id.toolbar)
    }

    private fun updateView(inputsToo: Boolean = false) {

        if(inputsToo) {
            (findViewById<TextView>(R.id.input_check_amount)).text = calculatorViewModel.checkAmtInput
            (findViewById<TextView>(R.id.input_tip_percentage)).text = calculatorViewModel.tipPctInput
        }

        calculatorViewModel.tipCalculation.let { tc ->
            (findViewById<TextView>(R.id.bill_amount)).text = getString(R.string.dollar_amount, tc.checkAmount)
            (findViewById<TextView>(R.id.tip_dollar_amount)).text = getString(R.string.dollar_amount, tc.tipAmount)
            (findViewById<TextView>(R.id.total_dollar_amount)).text = getString(R.string.dollar_amount, tc.grandTotal)
            (findViewById<TextView>(R.id.calculation_name)).text = tc.locationName
        }

    }

}

