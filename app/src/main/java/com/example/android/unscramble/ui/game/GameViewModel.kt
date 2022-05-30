package com.example.android.unscramble.ui.game

import android.util.Log
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {

    public var score = 0
    public var currentWordCount = 0

    //to hold a list of words you use in the game, to avoid repetitions.
    private var wordsList: MutableList<String> = mutableListOf()

    //to hold the word player trying to unscramble
    private lateinit var currentWord: String

    //Backing property
    private var _currentScrambledWord = "test"
    val currentScrambledWord: String
        get() = _currentScrambledWord

    //Get random word
    private fun getNestWord() {
        currentWord = allWordsList.random()

        //Shuffle the word
        val tempWord = currentWord.toCharArray()
        tempWord.shuffle()

        //Sometime shuffle word is same as original , so shuffle untill it differs
        while (String(tempWord).equals(currentWord, false)) {
            tempWord.shuffle()
        }
    }


    //Add logging to understand ViewModel
    init {
        Log.d("GameFragment", "GameViewModel Created!")
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("GameFragment", "GameViewModel Destroyed")
    }

    //Get next word

}