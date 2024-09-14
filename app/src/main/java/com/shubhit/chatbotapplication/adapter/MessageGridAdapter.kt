package com.shubhit.chatbotapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shubhit.chatbotapplication.R
import com.shubhit.chatbotapplication.model.ResultX
import com.shubhit.chatbotapplication.model.UserMessageModel

class MessageGridAdapter(private val items:List<Any>,private val onItemClick: (String) -> Unit):RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        private const val TYPE_ITEM = 1
        private const val TYPE_BOT_RESPONSE = 2
    }

    override fun getItemViewType(position: Int): Int {
        return when(items[position]){
            is UserMessageModel-> TYPE_ITEM
            is ResultX-> TYPE_BOT_RESPONSE
            else -> throw IllegalArgumentException("Invalid Item Type")
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {

            TYPE_ITEM -> {
                val view =
                    LayoutInflater.from(parent.context).inflate(R.layout.item_user_message, parent, false)
                ItemViewHolder(view)
            }
            TYPE_BOT_RESPONSE->{
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_bot_message, parent, false)
                BotResponseViewHolder(view)
            }
            else->throw IllegalArgumentException("Invalid view type")
        }


    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder.itemViewType){
            TYPE_ITEM -> (holder as ItemViewHolder).bind(items[position] as UserMessageModel)
            TYPE_BOT_RESPONSE -> (holder as BotResponseViewHolder).bind(items[position] as ResultX)

        }
    }

    override fun getItemCount(): Int {
       return items.size
    }

    inner class ItemViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        fun bind(item:UserMessageModel) {

            val tvUserMessage: TextView = itemView.findViewById(R.id.tv_user_message)
            tvUserMessage.text = item.input_string


        }
        }

    inner class BotResponseViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        private val tvBotMessage: TextView = itemView.findViewById(R.id.tv_bot_message)
        //private val tvSources: TextView = itemView.findViewById(R.id.tv_sources)
        private val tvFollowUpQuestions: TextView = itemView.findViewById(R.id.tv_follow_up_questions)

        private val followUpQuesRecyclerView: RecyclerView = itemView.findViewById(R.id.followUpQuesRV)

        private val sourceDocRv: RecyclerView = itemView.findViewById(R.id.sourceDocRV)
        fun bind(response: ResultX) {
            sourceDocRv.layoutManager = LinearLayoutManager(itemView.context,LinearLayoutManager.HORIZONTAL,false)
            followUpQuesRecyclerView.layoutManager=LinearLayoutManager(itemView.context,LinearLayoutManager.VERTICAL,false)




            tvBotMessage.text = response.result
            val source_doc_array=response.source_documents
            sourceDocRv.adapter=SourceDocumentAdapter(source_doc_array)
            followUpQuesRecyclerView.adapter=FollowUpQuesAdapter(response.follow_up_questions){
                onItemClick(it)

            }



       }

    }








}