package com.example.android.unscramble.ui.game


import android.text.Spannable
import android.text.SpannableString
import android.text.style.TtsSpan
import android.util.Log
import androidx.lifecycle.*

class GameViewModel : ViewModel() {

    //Backing property
    private val _score = MutableLiveData(0)
    val score: LiveData<Int>
        get() = _score

    //Backing property
    private val _currentWordCount = MutableLiveData(0)
    val currentWordCount: LiveData<Int>
        get() = _currentWordCount

    private val _currentScrambledWord = MutableLiveData<String>()
    val currentScrambledWord: LiveData<Spannable> = Transformations.map(_currentScrambledWord) {
        if (it == null) {
            SpannableString("")
        } else {
            val scrambledWord = it.toString()
            val spannable: Spannable = SpannableString(scrambledWord)
            spannable.setSpan(
                TtsSpan.VerbatimBuilder(scrambledWord).build(),
                0,
                scrambledWord.length,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE
            )
            spannable
        }
    }

    //to hold the word player trying to unscramble
    private var wordsList: MutableList<String> = mutableListOf()

    //lateint to initialize later
    private lateinit var currentWord: String

    init {
        getNestWord()
    }


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

//override fun onCleared() {
//    super.onCleared()
//    Log.d("GameFragment", "GameViewModel Destroyed")
//}

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
