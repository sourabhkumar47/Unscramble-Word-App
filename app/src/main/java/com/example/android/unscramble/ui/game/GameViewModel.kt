package com.example.android.unscramble.ui.game

import android.provider.Settings.Global.getString
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.unscramble.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class GameViewModel : ViewModel() {

    //Backing property
    private val _score = MutableLiveData(0)
    val score: LiveData<Int>
        get() = _score

    //Backing property
    private val _currentWordCount = MutableLiveData(0)
    val currentWordCount: LiveData<Int>
        get() = _currentWordCount

    //to hold a list of words you use in the game, to avoid repetitions.

    private var wordsList: MutableList<String> = mutableListOf()

    //to hold the word player trying to unscramble
    private lateinit var currentWord: String

    //Backing property
    //lateint to initialize later
    //MutableLiveData
    private val _currentScrambledWord = MutableLiveData<String>()
    val currentScrambledWord: LiveData<String>
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
            _currentScrambledWord.value = String(tempWord)
            //to increment the value
            _currentWordCount.value = (_currentWordCount.value)?.inc()
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
        return if (currentWordCount.value!! < MAX_NO_OF_WORDS) {
            getNestWord()
            true
        } else false
    }

    //Increase score when word is correct
    private fun increaseScore() {
        //show you an error because _score is no longer an integer, it's LiveData
        _score.value = (_score.value)?.plus(SCORE_INCREASE)
    }

    //Check word is correct or not
    fun isUserWordCorrect(playerWord: String): Boolean {
        if (playerWord.equals(currentWord, true)) {
            increaseScore()
            return true
        }
        return false
    }

    //Update game restart login (when playAgain is clicked)
    fun reinitializedata() {
        _score.value = 0
        _currentWordCount.value = 0
        wordsList.clear()
        getNestWord()
    }
}