package com.acme.tipcalculator.view

import android.databinding.DataBindingUtil
import android.os.Bundle
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

        /**
         * TODO Lab 4: Uncomment this line to assign a calculatorViewModel using the AC ViewModelProviders
         *        factory method and use it in the next line instead of constructing a new CalculatorViewModel() each time.
         */
        // calculatorViewModel = ViewModelProviders.of(this).get(CalculatorViewModel::class.java)
        calculatorViewModel = CalculatorViewModel()

        binding = DataBindingUtil.setContentView<ActivityTipCalculatorBinding>(this, R.layout.activity_tip_calculator)

        setSupportActionBar(getToolbar())

        binding.vm = calculatorViewModel
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
        Snackbar.make(getRootView(), "Loaded ${tipCalc.locationName}", Snackbar.LENGTH_SHORT).show()
    }

    override fun onSaveTip(name: String) {
        calculatorViewModel.saveCurrentTip(name)
        Snackbar.make(getRootView(), "Saved $name", Snackbar.LENGTH_SHORT).show()
    }

    private fun getRootView(): View {
        return binding.root
    }

    private fun TipCalculatorActivity.getToolbar(): Toolbar {
        return binding.toolbar
    }

}

