package com.example.android.unscramble.ui.game

import android.provider.Settings.Global.getString
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.unscramble.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class GameViewModel : ViewModel() {

    //Backing property
    //    public var score = 0
    private var _score = 0
    val score: Int
        get() = _score

    //Backing property
//    public var _currentWordCount = 0
    private var _currentWordCount = 0
    val currentWordCount: Int
        get() = _currentWordCount

    //to hold a list of words you use in the game, to avoid repetitions.
    private var wordsList: MutableList<String> = mutableListOf()

    //to hold the word player trying to unscramble
    private lateinit var currentWord: String

    //Backing property
    //lateint to initialize later
    private lateinit var _currentScrambledWord: String
    val currentScrambledWord: String
        get() = _currentScrambledWord

    //Get random word
    private fun getNestWord() {
        currentWord = allWordsList.random()

        //Shuffle the word
        val tempWord = currentWord.toCharArray()
        tempWord.shuffle()

        //Sometime shuffle word is same as original , so shuffle until it differs
        while (String(tempWord).equals(currentWord, false)) {
            tempWord.shuffle()
        }

        //Check if the word has been used already
        if (wordsList.contains(currentWord)) {
            getNestWord()
        } else {
            _currentScrambledWord = String(tempWord)
            ++_currentWordCount
            wordsList.add(currentWord)
        }
    }


    //Add logging to understand ViewModel
    init {
        Log.d("GameFragment", "GameViewModel Created!")
        getNestWord()
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("GameFragment", "GameViewModel Destroyed")
    }

    //    Add a helper method
/*
* Returns true if the current word count is less than MAX_NO_OF_WORDS.
* Updates the next word.
*/
    fun nextWord(): Boolean {
        return if (currentWordCount < MAX_NO_OF_WORDS) {
            getNestWord()
            true
        } else false
    }

    //Increase score when word is correct
    private fun increaseScore() {
        _score += SCORE_INCREASE
    }

    //Check word is correct or not
    fun isUserWordCorrect(playerWord: String): Boolean {
        if (playerWord.equals(currentWord, true)) {
            increaseScore()
            return true
        }
        return false
    }
}