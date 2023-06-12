/*
 *  Lecture     : Mobile App Programming Lab
 *  Task        : Programming Assignment 1
 *  File        : MainActivity.kt
 *  Date        : 2023-04-10
 *  SID         : 2020315798
 *  Name        : Choi Jin Woo
 */

package edu.skku.cs.pa1

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*

class MainActivity : AppCompatActivity() {
    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Components
        val editTextWordle = findViewById<EditText>(R.id.editTextWordle)
        val buttonSubmit = findViewById<Button>(R.id.buttonSubmit)
        val wordListView = findViewById<ListView>(R.id.wordListView)
        val letterListViewOut = findViewById<ListView>(R.id.letterListViewOut)
        val letterListViewBall = findViewById<ListView>(R.id.letterListViewBall)
        val letterListViewStrike = findViewById<ListView>(R.id.letterListViewStrike)

        // Read file 'wordle_words.txt' and randomly select the answer
        val inputStream = applicationContext.assets.open("wordle_words.txt")
        val wordDict = inputStream.readBytes().toString(Charsets.UTF_8)
        val wordDict2Array = wordDict.split("\n")
        val wordAnswer = wordDict2Array.random()

        // Variables for functionality
        val wordListData = ArrayList<WordList>()
        var letterListDataOut = ArrayList<Char>()
        var letterListDataBall = ArrayList<Char>()
        var letterListDataStrike = ArrayList<Char>()

        buttonSubmit.setOnClickListener{
            Log.i("answer", wordAnswer)
            var wordGuess = editTextWordle.text.toString().lowercase()              // Guessed word
            var wordGuessResultArr = IntArray(5)                                // Integer Array to save the result log

            if(wordGuess.length >= 5) {
                if (wordDict.contains(wordGuess)) {
                    // Save result to result array
                    for (i in 0..4) {
                        if (wordAnswer[i] == wordGuess[i]) {
                            wordGuessResultArr[i] = 2

                            if(!letterListDataStrike.contains(wordGuess[i])){
                                letterListDataStrike.add(wordGuess[i])
                            }
                            if(letterListDataBall.contains(wordGuess[i])){
                                letterListDataBall.remove(wordGuess[i])
                            }
                        }
                        else {
                            val beforeWordGuess = wordAnswer.substring(0, i)            // characters before the third position (index 2)
                            val afterWordGuess =  wordAnswer.substring(i + 1)   // characters after the third position
                            val combinedWordGuess = beforeWordGuess + afterWordGuess

                            if (combinedWordGuess.contains(wordGuess[i])) {
                                wordGuessResultArr[i] = 1

                                if(!letterListDataBall.contains(wordGuess[i]) && !letterListDataStrike.contains(wordGuess[i])){
                                    letterListDataBall.add(wordGuess[i])
                                }
                            }
                            else {
                                wordGuessResultArr[i] = 0

                                if(!letterListDataOut.contains(wordGuess[i])){
                                    letterListDataOut.add(wordGuess[i])
                                }
                            }
                        }
                    }

                    // Add WordList to wordListData
                    wordListData.add(WordList(wordGuess, wordGuessResultArr))

                    // Set Adapter
                    wordListView.adapter = WordListAdapter(wordListData, applicationContext)
                    letterListViewOut.adapter = LetterListAdapter(letterListDataOut, 0, applicationContext)
                    letterListViewBall.adapter = LetterListAdapter(letterListDataBall, 1, applicationContext)
                    letterListViewStrike.adapter = LetterListAdapter(letterListDataStrike, 2, applicationContext)

                    // Scroll down to the end of each list view
                    wordListView.post {
                        wordListView.setSelection(wordListData.size - 1)
                    }
                    letterListViewOut.post {
                        letterListViewOut.setSelection(letterListDataOut.size - 1)
                    }
                    letterListViewBall.post {
                        letterListViewBall.setSelection(letterListDataBall.size - 1)
                    }
                    letterListViewStrike.post {
                        letterListViewStrike.setSelection(letterListDataStrike.size - 1)
                    }

                    // Reset editTextWordle when submitted
                    editTextWordle.setText("")
                }
                else {
                    Toast.makeText(
                        applicationContext,
                        "Word '${wordGuess}' not in dictionary!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}