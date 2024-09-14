package com.shubhit.chatbotapplication.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.shubhit.chatbotapplication.R

class CustomSpinnerAdapter(context: Context,
    private val resource: Int,
    private val items: ArrayList<String>):ArrayAdapter<String>(context,resource,items) {


    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View = convertView ?: inflater.inflate(R.layout.spinner_selected_item, parent, false)
        val textView = view.findViewById<TextView>(R.id.spinnerSelectedItem)
        textView.text = items[position]
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View = convertView ?: inflater.inflate(R.layout.spinner_dropdown_item, parent, false)
        val textView = view.findViewById<TextView>(R.id.spinnerDropdownItem)
        textView.setText(items[position])
            return view
    }



}