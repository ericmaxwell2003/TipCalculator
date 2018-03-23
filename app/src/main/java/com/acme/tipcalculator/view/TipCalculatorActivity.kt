package com.acme.tipcalculator.view

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
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

        calculatorViewModel = CalculatorViewModel()

        binding = DataBindingUtil.setContentView<ActivityTipCalculatorBinding>(this, R.layout.activity_tip_calculator)

        /**
         * TODO Lab 2: Uncomment this line to assign `calculatorViewModel` to the view's `vm` variable.
         */
        // binding.vm = calculatorViewModel

        setSupportActionBar(binding.toolbar)

        /**
         * TODO Lab 2: Remove this entire FloatingActionButton listener block.  We're going to let Data Binding do the work
         *        of binding viewModel actions to the view and react to viewModel updates.
         *
         */
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
        /**
         * TODO Lab 2: Remove this, we don't have to manually update our view with databinding
         */
        updateView(true)
        Snackbar.make(getRootView(), "Loaded ${tipCalc.locationName}", Snackbar.LENGTH_SHORT).show()
    }

    override fun onSaveTip(name: String) {

        calculatorViewModel.saveCurrentTip(name)
        /**
         * TODO Lab 2: Remove this, we don't have to manually update our view with databinding
         */
        updateView()
        Snackbar.make(getRootView(), "Saved $name", Snackbar.LENGTH_SHORT).show()
    }

    /**
     * TODO Lab 2: Remove lookup methods for view variables that are no longer needed because
     *             the activity is managing all of them.
     */
    private fun getInputTipPercentageValue() : String {
        return binding.content?.inputTipPercentage?.text.toString()
    }

    private fun getInputCheckoutAmountValue() : String {
        return binding.content?.inputTipPercentage?.text.toString()
    }

    private fun getFloatingActionButton() : FloatingActionButton {
        return binding.calculateFab
    }

    /**
     * TODO LAB 2: Keep this one, we're still using it.
     */
    private fun getRootView(): View {
        return binding.root
    }

    private fun TipCalculatorActivity.getToolbar(): Toolbar {
        return binding.toolbar
    }

    private fun updateView(inputsToo: Boolean = false) {

        if(inputsToo) {
            binding.content?.inputCheckAmount?.setText(calculatorViewModel.checkAmtInput)
            binding.content?.inputTipPercentage?.setText(calculatorViewModel.tipPctInput)
        }

        calculatorViewModel.tipCalculation.let { tc ->
            binding.content?.billAmount?.setText(getString(R.string.dollar_amount, tc.checkAmount))
            binding.content?.tipDollarAmount?.setText(getString(R.string.dollar_amount, tc.tipAmount))
            binding.content?.totalDollarAmount?.setText(getString(R.string.dollar_amount, tc.grandTotal))
            binding.content?.calculationName?.setText(tc.locationName)
        }

    }

}

