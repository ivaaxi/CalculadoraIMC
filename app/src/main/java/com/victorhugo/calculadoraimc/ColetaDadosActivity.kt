package com.victorhugo.calculadoraimc

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.progressindicator.CircularProgressIndicator

const val MILLISECONDS_LOADING_BAR = 2000L

class ColetaDadosActivity : AppCompatActivity() {

    private val edtPeso: EditText by lazy { findViewById(R.id.edtPeso) }
    private val edtAltura: EditText by lazy { findViewById(R.id.edtAltura) }
    private val btnCalcularImc: Button by lazy { findViewById(R.id.btnCalcularImc) }
    private val pbLoading: CircularProgressIndicator by lazy { findViewById(R.id.progressIndicator) }


    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.coleta_dados_activity)
        setupListeners()
    }

    private fun setupListeners() {
        setupButtonListener()
    }

    private fun setupButtonListener() {
        this.btnCalcularImc.setOnClickListener {
            if (!validateFields()) return@setOnClickListener

            val pesoTexto = edtPeso.text.toString().replace(",", ".").trim()
            val alturaTexto = edtAltura.text.toString().trim()

            val peso = normalizaPeso(pesoTexto)
            val altura = normalizarAltura(alturaTexto)

            if (peso == null) {
                edtPeso.error = "Peso inválido"
                return@setOnClickListener
            }

            if (altura == null) {
                edtAltura.error = "Altura inválida"
                return@setOnClickListener
            }

            hideKeyBoard()
            val intent = Intent(this, ResultadoActivity::class.java)
            intent.putExtra("peso", peso.toString())
            intent.putExtra("altura", altura.toString())
            setupProgressBar(intent)
        }
    }

    private fun hideKeyBoard(){
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }

    private fun normalizarAltura(input: String): Float? {
        val alturaLimpa = input.replace(",", ".").trim()

        return when {
            alturaLimpa.contains('.') -> {
                alturaLimpa.toFloatOrNull()
            }
            alturaLimpa.length == 3 -> {
                // Ex: 190 -> 1.90
                val alturaFormatada = alturaLimpa[0] + "." + alturaLimpa.substring(1)
                alturaFormatada.toFloatOrNull()
            }
            else -> {
                alturaLimpa.toFloatOrNull()
            }
        }
    }

    private fun normalizaPeso(input: String) : Float? {
        val pesoLimpo = input.replace(",", ".").trim()
        return pesoLimpo.toFloatOrNull()
    }

    private fun setupProgressBar(intent: Intent) {
        this.btnCalcularImc.isEnabled = false
        this.pbLoading.visibility = View.VISIBLE
        Handler(Looper.getMainLooper()).postDelayed({
            this.pbLoading.visibility = View.GONE
            this.btnCalcularImc.isEnabled = true
            startActivity(intent)
        }, MILLISECONDS_LOADING_BAR)
    }

    private fun validateFields(): Boolean {
        val peso = this.edtPeso.text.toString()
        val altura = this.edtAltura.text.toString()
        if (peso.isEmpty()) {
            this.edtPeso.error = "Campo obrigatório"
            return false
        }
        if (altura.isEmpty()) {
            this.edtAltura.error = "Campo obrigatório"
            return false
        }
        return true
    }
}