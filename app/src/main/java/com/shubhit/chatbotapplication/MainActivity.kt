package com.shubhit.chatbotapplication

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.shubhit.chatbotapplication.adapter.CustomSpinnerAdapter
import com.shubhit.chatbotapplication.adapter.MessageGridAdapter
import com.shubhit.chatbotapplication.adapter.SuggestionItemAdapter
import com.shubhit.chatbotapplication.databinding.ActivityMainBinding
import com.shubhit.chatbotapplication.model.Result
import com.shubhit.chatbotapplication.model.UserMessageModel
import com.shubhit.chatbotapplication.viewModel.ChatMessagesViewModel
import com.shubhit.chatbotapplication.viewModel.HomePageViewModel
import com.shubhit.chatbotapplication.viewModel.getViewModel


class MainActivity : AppCompatActivity(), RecognitionListener {

    private val REQUEST_CODE_SPEECH_INPUT = 100
    private lateinit var speechRecognizer: SpeechRecognizer
    private lateinit var suggestionAdapter: SuggestionItemAdapter
    private lateinit var messageAdapter: MessageGridAdapter
    private val conversationList = mutableListOf<Any>()
    private lateinit var binding: ActivityMainBinding
    private lateinit var mHomePageViewModel: HomePageViewModel
    private lateinit var mChatMessagesViewModel: ChatMessagesViewModel
    private lateinit var languages: ArrayList<String>
    private lateinit var suggetionItemList: ArrayList<Result>
    private var language: String? = null
    private lateinit var languageCodeMap: Map<String, String>
    private lateinit var micAnimation: AnimationDrawable



    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mHomePageViewModel = getViewModel { HomePageViewModel() }
        mChatMessagesViewModel = getViewModel { ChatMessagesViewModel() }
        suggetionItemList = ArrayList()

        mHomePageViewModel.getHomePageData()
        binding.progressBar.visibility = View.VISIBLE
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
        speechRecognizer.setRecognitionListener(this)
        micAnimation =
            ContextCompat.getDrawable(this, R.drawable.mic_animation) as AnimationDrawable
        binding.micButton.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this@MainActivity,
                    Manifest.permission.RECORD_AUDIO
                )
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this@MainActivity,
                    arrayOf(Manifest.permission.RECORD_AUDIO),
                    REQUEST_CODE_SPEECH_INPUT
                )
            } else {
                startListening(language)
            }
            //  startVoiceInput(language)
        }
        binding.sendButton.setOnClickListener {
            sendMessage()
        }

        binding.recyclerView.layoutManager = GridLayoutManager(this, 2) // 2 columns in the grid
        binding.conversationRecyclerView.layoutManager = LinearLayoutManager(this)

        binding.deleteButton.setOnClickListener {
            conversationList.clear()
            messageAdapter.notifyDataSetChanged()
            binding.conversationRecyclerView.scrollToPosition(conversationList.size - 1)
            binding.helpText.visibility = View.VISIBLE
            binding.conversationRecyclerView.visibility = View.GONE
            binding.recyclerView.visibility = View.VISIBLE
            hideKeyboard()
        }



        mHomePageViewModel.homePageResult.observe(this) { home ->
            binding.progressBar.visibility = View.GONE
            languages = ArrayList()
            suggetionItemList.clear()
            home.languages.forEach {
                languages.add(it.lang_title)
            }
            suggetionItemList.addAll(home.result)

            suggestionAdapter = SuggestionItemAdapter(suggetionItemList) {
                binding.messageInput.setText(it)
                sendMessage()
            }
            binding.recyclerView.adapter = suggestionAdapter


            Glide.with(this)
                .load(home.site_logo)
                .placeholder(R.drawable.nov)
                .error(R.drawable.nov)
                .into(binding.logoImage)

            binding.title.setText(home.site_title)

            if (home.is_mic_enable == "Yes") {
                binding.micButton.visibility = View.VISIBLE
            } else {
                binding.micButton.visibility = View.GONE
            }

            if (home.is_multilingual == "Yes") {
                setupLanguageSpinner()
            }

            languageCodeMap = home.languages.associate { it.lang_title to it.lang_code }


        }
        mHomePageViewModel.error.observe(this) {
            binding.progressBar.visibility = View.GONE
            Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
        }
        mChatMessagesViewModel.chatMessagesResult.observe(this) {
            binding.progressBar.visibility = View.GONE

            println("Bot Message result is : ${it.result}")
            conversationList.add(it.result)
            //  messageAdapter.notifyItemInserted(conversationList.size-1)
            messageAdapter.notifyDataSetChanged()
            binding.conversationRecyclerView.scrollToPosition(conversationList.size - 1)

        }

        mChatMessagesViewModel.error.observe(this) {
            binding.progressBar.visibility = View.GONE
            Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            println("Bot Message result is : ${it.exception}  ${it.message}")
        }


        messageAdapter = MessageGridAdapter(conversationList) {
            if (it is String) {
                binding.messageInput.setText(it)
                sendMessage()
            }

        }
        binding.conversationRecyclerView.adapter = messageAdapter

        binding.sendButton.isEnabled = false



        binding.messageInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.micButton.visibility = if (s.isNullOrEmpty()) View.VISIBLE else View.GONE
                binding.recyclerView.visibility =  View.GONE
                binding.helpText.visibility = View.GONE
                binding.deleteButton.visibility = if (s.isNullOrEmpty()) View.VISIBLE else View.GONE
                binding.sendButton.isEnabled = !s.isNullOrEmpty()

            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.speakBtn.setOnClickListener{
            val text = "Hello, how are you today?"
          //  speakText(text)
        }


    }

    private fun setupLanguageSpinner() {
        val adapter = CustomSpinnerAdapter(
            this,
            R.layout.spinner_selected_item,
            languages
        )

        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        binding.languageSpinner.adapter = adapter

        binding.languageSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val selectedLanguage = parent?.getItemAtPosition(position).toString()
                    language = languageCodeMap[selectedLanguage]

                    // Handle language change logic here
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Do nothing
                }
            }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE_SPEECH_INPUT -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    startListening()
                    //   startVoiceInput()
                } else {
                    // Permission denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()

                }
                return
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_SPEECH_INPUT -> {
                if (resultCode == RESULT_OK && data != null) {
                    val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    result?.let {
                        if (it.isNotEmpty()) {
                            binding.messageInput.setText(it[0])
                            sendMessage()

                        }
                    }
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun sendMessage() {
        println("Button Clicked ")
        val message = binding.messageInput.text.toString().trim()
        if (message.trim().isNotEmpty()) {
            conversationList.add(UserMessageModel("", message))
            mChatMessagesViewModel.botMessage()
            binding.progressBar.visibility = View.VISIBLE
            // messageAdapter.notifyItemInserted(conversationList.size - 1)
            messageAdapter.notifyDataSetChanged()
            binding.conversationRecyclerView.scrollToPosition(conversationList.size - 1)
            binding.messageInput.text.clear()
            binding.recyclerView.visibility = View.GONE
            binding.helpText.visibility = View.GONE
            binding.conversationRecyclerView.visibility = View.VISIBLE

            hideKeyboard()

        }
    }


    private fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.messageInput.windowToken, 0)
    }

    override fun onReadyForSpeech(params: Bundle?) {
        //   TODO("Not yet implemented")
    }

    override fun onBeginningOfSpeech() {
        // Toast.makeText(this, "Start Speaking", Toast.LENGTH_SHORT).show()
    }

    override fun onRmsChanged(rmsdB: Float) {
        //  TODO("Not yet implemented")
    }

    override fun onBufferReceived(buffer: ByteArray?) {
        //  TODO("Not yet implemented")
    }

    override fun onEndOfSpeech() {
        //TODO("Not yet implemented")
    }

    override fun onError(error: Int) {
        binding.micButton.isEnabled = true
        if (error != SpeechRecognizer.ERROR_SPEECH_TIMEOUT && error != SpeechRecognizer.ERROR_CLIENT && error != SpeechRecognizer.ERROR_NO_MATCH) {
            speechRecognizer.stopListening()
            micAnimation.stop()
            binding.micButton.setImageResource(R.drawable.baseline_mic_24) // Change to your mic button drawable
            Toast.makeText(this, "Error: $error", Toast.LENGTH_SHORT).show()

        } else if (error == SpeechRecognizer.ERROR_NO_MATCH) {
            speechRecognizer.stopListening()
            micAnimation.stop()
            binding.micButton.setImageResource(R.drawable.baseline_mic_24) // Change to your mic button drawable
            Toast.makeText(this, "Can't understand , Try Again!", Toast.LENGTH_SHORT).show()


        }


    }

    override fun onResults(results: Bundle?) {
        binding.micButton.isEnabled = true
        val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
        if (!matches.isNullOrEmpty()) {
            val text = matches[0]
            //Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
            binding.messageInput.setText(text)
            sendMessage()
            println("Text is : $text")
            speechRecognizer.stopListening()
            micAnimation.stop()
            binding.micButton.setImageResource(R.drawable.baseline_mic_24) // Change to your mic button drawable


        }
    }

    override fun onPartialResults(partialResults: Bundle?) {
        // TODO("Not yet implemented")
    }

    override fun onEvent(eventType: Int, params: Bundle?) {

    }

    private fun startListening(languageCode: String? = "en-Us") {
        binding.micButton.isEnabled = false
        Toast.makeText(this, "Start Speaking", Toast.LENGTH_SHORT).show()
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, languageCode)
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now...")

        speechRecognizer.startListening(intent)
        binding.micButton.setImageDrawable(micAnimation)
        micAnimation.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        speechRecognizer.destroy()
    }




}


//
//
