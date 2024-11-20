package com.rida.supermachine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.rida.supermachine.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var solde = 100
    private var codeSecret = "Mellouki&&Mehdi"
    private val random = Random(System.currentTimeMillis())
    private val images = listOf(R.drawable.banane, R.drawable.charbon, R.drawable.diamant, R.drawable.emeraude, R.drawable.img7,
        R.drawable.piece, R.drawable.rubis, R.drawable.sacargent, R.drawable.tresor, R.drawable.fsa)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.textViewSolde.text = getString(R.string.solde, solde);

        updateActifs()
        binding.radioGroupMise.setOnCheckedChangeListener{group, checkedId ->
            updateActifs()
        }
        binding.button.setOnClickListener(){
            val bet = getMise();
            if (bet > solde && bet != 0) {
                binding.button.isEnabled = true ;
                Toast.makeText(this, "Solde insuffisant", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            playGame(bet);
        }
        binding.editTextText.addTextChangedListener {
            if(it.toString() ==codeSecret){
                solde+=100 ;

                binding.textViewSolde.text =  getString(R.string.solde, solde)

                binding.editTextText.text.clear()

                updateActifs();
            }
        }


    }

    private fun getMise(): Int {
        return when (binding.radioGroupMise.checkedRadioButtonId) {
            R.id.radioButton2 -> 1
            R.id.radioButton1 -> 2
            R.id.radioButton3 -> 5
            else -> 0
        }
    }
    private fun playGame(bet: Int) {
        val imageIndices = List(3) { random.nextInt(images.size) }

        binding.imageView.setImageDrawable(ContextCompat.getDrawable(this, images[imageIndices[0]]))
        binding.imageView2.setImageDrawable(ContextCompat.getDrawable(this, images[imageIndices[1]]))
        binding.imageView3.setImageDrawable(ContextCompat.getDrawable(this, images[imageIndices[2]]))
        val cassCouMode = binding.checkBox.isChecked

        val win = calWins(imageIndices, bet, cassCouMode)

        solde += win - bet
        binding.textViewSolde.text =  getString(R.string.solde, solde);

        if (win > 0) {
            Toast.makeText(this, "Vous avez gagné $win$!", Toast.LENGTH_SHORT).show()
        }
        updateActifs()
    }

    private fun updateActifs() {
        binding.radioButton2.isEnabled = solde >= 1
        binding.radioButton1.isEnabled = solde >= 2
        binding.radioButton3.isEnabled = solde >= 5
        binding.button.isEnabled = (getMise() != 0 && solde >= 1)

    }

    private fun calWins(indices: List<Int>, bet: Int, cassCouMode: Boolean): Int {
        return if (cassCouMode) {
            when {
                indices.all {it == 9 } -> bet * 100
                indices.distinct().size == 2 -> bet * 10
                else -> 0
            }
        } else {
            when {
                indices.distinct().size == 2 -> bet
                indices.distinct().size == 1 -> bet * 25
                else -> 0
            }
        }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("SOLDE", solde)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        solde = savedInstanceState.getInt("SOLDE", 100)
        binding.textViewSolde.text = getString(R.string.solde, solde)
        updateActifs()
    }
}