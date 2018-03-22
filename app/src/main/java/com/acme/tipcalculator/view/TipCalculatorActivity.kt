package com.acme.tipcalculator.view

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
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

        /**
         * TODO Lab 1: Access the toolbar from the binding directly instead of using findViewById(..)
         * Bonus Question:  Is this more efficient? why or why not?
         */
        setSupportActionBar(findViewById(R.id.toolbar))

        /** TODO Lab 1: Access the fab from the binding directly instead of using findViewById(..) */
        findViewById<FloatingActionButton>(R.id.calculate_fab).setOnClickListener { _ ->

            /** TODO Lab 1: Everywhere inside of this action block replace the findViewById(..) lookup
             *         with a property lookup on the binding.
             */
            calculatorViewModel.checkAmtInput = (findViewById<EditText>(R.id.input_check_amount)).text.toString()
            calculatorViewModel.tipPctInput = (findViewById<EditText>(R.id.input_tip_percentage)).text.toString()

            // Invoke Calculate Tip on the ViewModel
            calculatorViewModel.calculateTip()

            // After the calculation, we need to manually update our view elements
            calculatorViewModel.tipCalculation.let { tc ->
                (findViewById<TextView>(R.id.bill_amount)).text = getString(R.string.dollar_amount, tc.checkAmount)
                (findViewById<TextView>(R.id.tip_dollar_amount)).text = getString(R.string.dollar_amount, tc.tipAmount)
                (findViewById<TextView>(R.id.total_dollar_amount)).text = getString(R.string.dollar_amount, tc.grandTotal)
                (findViewById<TextView>(R.id.calculation_name)).text = tc.locationName
            }
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

        /**
         *  TODO Lab 1: Everywhere inside of this block replace the findViewById(..) lookup
         *         with a property lookup on the binding.
         */
         (findViewById<TextView>(R.id.input_check_amount)).text = calculatorViewModel.checkAmtInput
         (findViewById<TextView>(R.id.input_tip_percentage)).text = calculatorViewModel.tipPctInput

         calculatorViewModel.tipCalculation.let { tc ->
            (findViewById<TextView>(R.id.bill_amount)).text = getString(R.string.dollar_amount, tc.checkAmount)
            (findViewById<TextView>(R.id.tip_dollar_amount)).text = getString(R.string.dollar_amount, tc.tipAmount)
            (findViewById<TextView>(R.id.total_dollar_amount)).text = getString(R.string.dollar_amount, tc.grandTotal)
            (findViewById<TextView>(R.id.calculation_name)).text = tc.locationName
        }

        /** TODO Lab 1: Replace this findViewById with the property that the binding gives you to access the root view. */
        Snackbar.make(window.decorView.findViewById(android.R.id.content), "Loaded ${tipCalc.locationName}", Snackbar.LENGTH_SHORT).show()
    }

    override fun onSaveTip(name: String) {

        calculatorViewModel.saveCurrentTip(name)

        /**
         *  TODO Lab 1: Everywhere inside of this block replace the findViewById(..) lookup
         *         with a property lookup on the binding.
         */
        calculatorViewModel.tipCalculation.let { tc ->
            (findViewById<TextView>(R.id.bill_amount)).text = getString(R.string.dollar_amount, tc.checkAmount)
            (findViewById<TextView>(R.id.tip_dollar_amount)).text = getString(R.string.dollar_amount, tc.tipAmount)
            (findViewById<TextView>(R.id.total_dollar_amount)).text = getString(R.string.dollar_amount, tc.grandTotal)
            (findViewById<TextView>(R.id.calculation_name)).text = tc.locationName
        }

        /** TODO Lab 1: Replace this findViewById with the property that the binding gives you to access the root view. */
        Snackbar.make(window.decorView.findViewById(android.R.id.content), "Saved $name", Snackbar.LENGTH_SHORT).show()
    }

}

