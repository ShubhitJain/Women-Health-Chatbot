package com.shubhit.chatbotapplication.adapter

import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.shubhit.chatbotapplication.R

class SourceDocumentAdapter(private val sourceDocument: List<String>):RecyclerView.Adapter<SourceDocumentAdapter.SourceDocumentViewHolder>() {
    inner class SourceDocumentViewHolder(itemView: View):RecyclerView.ViewHolder(itemView),View.OnClickListener{
        private val textView: TextView = itemView.findViewById(R.id.sourceDocItem)
        private var documentUrl: String = ""

        init {
            textView.setOnClickListener(this)
        }

        fun bind(sourceDocument: String){
            val documentName = sourceDocument.substring(sourceDocument.lastIndexOf('/') + 1)
            textView.text = documentName
            textView.setTypeface(null, Typeface.BOLD)

            documentUrl = sourceDocument

        }

        override fun onClick(v: View?) {
            v?.context?.let { context ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(documentUrl))
                context.startActivity(intent)

//                val intent = Intent(context, WebViewActivity::class.java).apply {
//                    putExtra("url", documentUrl)
//                }
//                context.startActivity(intent)
            }


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SourceDocumentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_source_document, parent, false)
        return SourceDocumentViewHolder(view)

    }

    override fun getItemCount(): Int {
        return sourceDocument.size

    }

    override fun onBindViewHolder(holder: SourceDocumentViewHolder, position: Int) {
        holder.bind(sourceDocument[position])
    }


}