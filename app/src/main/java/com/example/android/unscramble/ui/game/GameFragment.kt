package com.example.android.unscramble.ui.game

import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.android.unscramble.R
import com.example.android.unscramble.databinding.GameFragmentBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * Fragment where the game is played, contains the game logic.
 */
class GameFragment : Fragment() {

    //Attach viewModel to Fragment
    private val viewModel: GameViewModel by viewModels()


    // Binding object instance with access to the views in the game_fragment.xml layout
    private lateinit var binding: GameFragmentBinding

    // Create a ViewModel the first time the fragment is created.
    // If the fragment is re-created, it receives the same GameViewModel instance created by the
    // first fragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout XML file and return a binding object instance
        //binding = GameFragmentBinding.inflate(inflater, container, false)
        //Updated to data binding
        binding = DataBindingUtil.inflate(inflater,R.layout.game_fragment,container, false)

        // log statement to log the creation of the fragment
        Log.d("GameFragment", "GameFragment Created/re-created")
        Log.d(
            "GameFragment", "Word: ${viewModel.currentScrambledWord}" +
                    "Score: ${viewModel.score} WordCount : $ { viewModel.currentWordCount }"
        )

        return binding.root
    }

    //will be called when the corresponding activity and fragment are destroyed
    override fun onDetach() {
        super.onDetach()
        Log.d("GameFragment", "GameFragment Destroyed!")
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup a click listener for the Submit and Skip buttons.
        binding.submit.setOnClickListener { onSubmitWord() }
        binding.skip.setOnClickListener { onSkipWord() }
        // Update the UI
//        updateNextWordOnScreen()
//        binding.score.text = getString(R.string.score, 0)
//        binding.wordCount.text = getString(
//            R.string.word_count, 0, MAX_NO_OF_WORDS
//        )

        // Observe the scrambledCharArray LiveData, passing in the LifecycleOwner and the observer.
        viewModel.currentScrambledWord.observe(viewLifecycleOwner,
            { newWord ->
                binding.textViewUnscrambledWord.text = newWord
            })
        //Observer of Score
        viewModel.score.observe(viewLifecycleOwner,
            { newScore ->
                binding.score.text = getString(R.string.score, newScore)
            })

        //Observer for currentWordCount
        viewModel.currentWordCount.observe(viewLifecycleOwner,
            { newWordCount ->
                binding.wordCount.text =
                    getString(R.string.word_count, newWordCount, MAX_NO_OF_WORDS)
            })
    }

    /*
    * Checks the user's word, and updates the score accordingly.
    * Displays the next scrambled word.
    */
    private fun onSubmitWord() {

        //taking string from text field
        val playerWord = binding.textInputEditText.text.toString()
//        currentScrambledWord = getNextScrambledWord()
//        currentWordCount++
//        score += SCORE_INCREASE
//        binding.wordCount.text = getString(R.string.word_count, currentWordCount, MAX_NO_OF_WORDS)
//        binding.score.text = getString(R.string.score, score)
//        setErrorTextField(false)
//        updateNextWordOnScreen()

        //validate the user's guess by checking against the original word
        if (viewModel.isUserWordCorrect(playerWord)) {
            setErrorTextField(false)
            if (!viewModel.nextWord()) {
//                updateNextWordOnScreen()
                showFinalScoreDialog()
            }
        } else {
            setErrorTextField(true)
        }
    }

    /*
     * Skips the current word without changing the score.
     * Increases the word count.
     */
    private fun onSkipWord() {
//        currentScrambledWord = getNextScrambledWord()
//        currentWordCount++
//        binding.wordCount.text = getString(R.string.word_count, currentWordCount, MAX_NO_OF_WORDS)
//        setErrorTextField(false)
//        updateNextWordOnScreen()

        if (viewModel.nextWord()) {
            setErrorTextField(false)
//            updateNextWordOnScreen()
        } else {
            showFinalScoreDialog()
        }
    }


    /*
     * Gets a random word for the list of words and shuffles the letters in it.
     */
    private fun getNextScrambledWord(): String {
        val tempWord = allWordsList.random().toCharArray()
        tempWord.shuffle()
        return String(tempWord)
    }


//Creates and shows an AlertDialog with the final score.

    private fun showFinalScoreDialog() {
        MaterialAlertDialogBuilder(requireContext())
            //Set the title on the alert dialog
            .setTitle(getString(R.string.congratulations))
            //Message
            .setMessage(getString(R.string.you_scored, viewModel.score.value))
            //make dialog box un cancellable .. during press back button
            .setCancelable(false)
            //text button EXIT and PLAYAGAIN
            .setNegativeButton(getString(R.string.exit)) { _, _ ->
                exitGame()
            }
            .setPositiveButton(getString(R.string.play_again)) { _, _ ->
                restartGame()
            }
            .show()
    }

    /*
     * Re-initializes the data in the ViewModel and updates the views with the new data, to
     * restart the game.
     */
    private fun restartGame() {
        viewModel.reinitializedata()
        setErrorTextField(false)
//        updateNextWordOnScreen()
    }

    /*
     * Exits the game.
     */
    private fun exitGame() {
        activity?.finish()
    }

    /*
    * Sets and resets the text field error status.
    */
    private fun setErrorTextField(error: Boolean) {
        if (error) {
            binding.textField.isErrorEnabled = true
            binding.textField.error = getString(R.string.try_again)
        } else {
            binding.textField.isErrorEnabled = false
            binding.textInputEditText.text = null
        }
    }

    /*
     * Displays the next scrambled word on screen.
     */
//    private fun updateNextWordOnScreen() {
//        binding.textViewUnscrambledWord.text = currentScrambledWord
//    }
    //Backing property
//    private fun updateNextWordOnScreen() {
//        binding.textViewUnscrambledWord.text = viewModel.currentScrambledWord
//    }
}
