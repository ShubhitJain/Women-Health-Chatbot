package com.shubhit.chatbotapplication.viewModel

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

inline fun <reified T: ViewModel> AppCompatActivity.getViewModel(noinline creator: (() -> T)? = null) : T{

    return ViewModelProvider(this).get(T::class.java) as T

}
inline fun <reified T : ViewModel> Fragment.getViewModel(noinline creator: (() -> T)? = null): T {
    return  ViewModelProvider(this).get(T::class.java) as T
}
