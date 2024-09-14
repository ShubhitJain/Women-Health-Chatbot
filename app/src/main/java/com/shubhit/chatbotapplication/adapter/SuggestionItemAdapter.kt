package com.shubhit.chatbotapplication.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.shubhit.chatbotapplication.R
import com.shubhit.chatbotapplication.model.Result


class SuggestionItemAdapter(val suggestionList:ArrayList<Result>,private val onSuggestionClick: (String) -> Unit ) :RecyclerView.Adapter<SuggestionItemAdapter.SuggestionItemViewHolder>() {



    inner class SuggestionItemViewHolder(val itemView:View):RecyclerView.ViewHolder(itemView){
        fun bind(suggestion:Result){
            itemView.findViewById<TextView>(R.id.suggestionTitle).setText(suggestion.topic)
            itemView.findViewById<TextView>(R.id.suggestionDescription).setText(suggestion.info)

            itemView.setOnClickListener {
                onSuggestionClick(suggestion.topic)
            }


        }


    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SuggestionItemViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.suggestion_item_layout,parent,false)
        return SuggestionItemViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: SuggestionItemViewHolder,
        position: Int
    ) {
        holder.bind(suggestionList.get(position))

    }

    override fun getItemCount(): Int {
        return suggestionList.size
    }
}