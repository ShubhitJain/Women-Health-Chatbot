package com.shubhit.chatbotapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.shubhit.chatbotapplication.R

class FollowUpQuesAdapter(private val followUpQuestions: List<String>,
                          private val onItemClick: (String) -> Unit) :
    RecyclerView.Adapter<FollowUpQuesAdapter.FollowUpQuesViewHolder>() {
    inner class FollowUpQuesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView.findViewById(R.id.sourceDocItem)
        fun bind(followUpQues: String) {
            textView.text = followUpQues
            textView.setBackgroundResource(R.drawable.rounded_background)
            textView.setTextColor(itemView.context.getColor(android.R.color.black))

            itemView.setOnClickListener {
                onItemClick(followUpQues)
            }


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowUpQuesViewHolder{
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_source_document, parent, false)
        return FollowUpQuesViewHolder(view)

    }

    override fun getItemCount(): Int {
        return followUpQuestions.size

    }

    override fun onBindViewHolder(holder: FollowUpQuesViewHolder, position: Int) {
        holder.bind(followUpQuestions[position])
    }


}