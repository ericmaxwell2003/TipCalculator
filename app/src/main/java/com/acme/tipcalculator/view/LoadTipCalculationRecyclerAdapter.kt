package com.acme.tipcalculator.view

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.acme.tipcalculator.R
import com.acme.tipcalculator.model.TipCalculation

class LoadTipCalculationRecyclerAdapter(
        val savedTipCalculations: MutableList<TipCalculation> = mutableListOf<TipCalculation>(),
        val onTipCalcSelected: (tc: TipCalculation) -> Unit = {}) :
        RecyclerView.Adapter<LoadTipCalculationRecyclerAdapter.LoadTipCalculationViewHolder>() {

    fun updateList(updates: List<TipCalculation>) {
        savedTipCalculations.clear()
        savedTipCalculations.addAll(updates)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: LoadTipCalculationViewHolder, position: Int) {
        holder.bind(savedTipCalculations[position])
    }

    override fun getItemCount() = savedTipCalculations.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoadTipCalculationViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val root = inflater.inflate(R.layout.saved_tip_calculations_list_item, parent, false)
        return LoadTipCalculationViewHolder(root)
    }

    inner class LoadTipCalculationViewHolder(val root: View) : RecyclerView.ViewHolder(root) {

        fun bind(tipCalc: TipCalculation) {
            root.findViewById<TextView>(R.id.locationName)?.text = tipCalc.locationName
            root.findViewById<TextView>(R.id.total_dollar_amount)?.text =
                    root.resources.getString(R.string.dollar_amount, tipCalc.grandTotal)
            root.setOnClickListener { onTipCalcSelected(tipCalc) }
        }
    }

}