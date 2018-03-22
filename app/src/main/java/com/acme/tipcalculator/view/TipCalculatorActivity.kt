package com.acme.tipcalculator.view

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.acme.tipcalculator.R
import com.acme.tipcalculator.databinding.ActivityTipCalculatorBinding
import com.acme.tipcalculator.model.TipCalculation
import com.acme.tipcalculator.viewmodel.CalculatorViewModel

class TipCalculatorActivity : AppCompatActivity(),
        LoadDialogFragment.Callback,
        SaveDialogFragment.Callback {

    private lateinit var binding: ActivityTipCalculatorBinding

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

        binding = DataBindingUtil.setContentView<ActivityTipCalculatorBinding>(this, R.layout.activity_tip_calculator)
        setSupportActionBar(binding.toolbar)

        /**
         * TODO Lab 2: Remove this entire FloatingActionButton listener block.  We're going to let Data Binding do the work
         *        of binding viewModel actions to the view and react to viewModel updates.
         *
         */
        binding.calculateFab.setOnClickListener { _ ->
            binding.content?.apply {

                // Without data binding, we have to manually set the inputs on our view model.
                calculatorViewModel.checkAmtInput = inputCheckAmount.text.toString()
                calculatorViewModel.tipPctInput = inputTipPercentage.text.toString()

                // Invoke Calculate Tip on the ViewModel
                calculatorViewModel.calculateTip()

                // After the calculation, we need to manually update our view elements
                calculatorViewModel.tipCalculation.let { tc ->
                    billAmount.text = getString(R.string.dollar_amount, tc.checkAmount)
                    tipDollarAmount.text = getString(R.string.dollar_amount, tc.tipAmount)
                    totalDollarAmount.text = getString(R.string.dollar_amount, tc.grandTotal)
                    calculationName.text = tc.locationName
                }
            }
        }

        calculatorViewModel = CalculatorViewModel()

        /**
         * TODO Lab 2: Uncomment this line to assign `calculatorViewModel` to the view's `vm` variable.
         */
        // binding.vm = calculatorViewModel
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
        Snackbar.make(binding.root, "Loaded ${tipCalc.locationName}", Snackbar.LENGTH_SHORT).show()
    }

    override fun onSaveTip(name: String) {

        calculatorViewModel.saveCurrentTip(name)

        /**
         * TODO Lab 2: Remove this entire block to update the view after saving the tip.  We're going to let
         *        data binding react to the changed ViewModel state.
         */
        binding.content?.apply {
            calculatorViewModel.tipCalculation.let { tc ->
                billAmount.text = getString(R.string.dollar_amount, tc.checkAmount)
                tipDollarAmount.text = getString(R.string.dollar_amount, tc.tipAmount)
                totalDollarAmount.text = getString(R.string.dollar_amount, tc.grandTotal)
                calculationName.text = tc.locationName
            }
        }

        Snackbar.make(binding.root, "Saved $name", Snackbar.LENGTH_SHORT).show()
    }

}

