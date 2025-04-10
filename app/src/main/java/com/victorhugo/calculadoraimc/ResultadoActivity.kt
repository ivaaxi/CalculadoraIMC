package com.victorhugo.calculadoraimc

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

class ResultadoActivity : AppCompatActivity() {
    private val txtPesoInformado: TextView by lazy { findViewById(R.id.txtPesoInformado) }
    private val txtAlturaInformada: TextView by lazy { findViewById(R.id.txtAlturaInformada) }
    private val txtResultado: TextView by lazy { findViewById(R.id.txtResultado) }
    private val txtRecomendacao: TextView by lazy { findViewById(R.id.txtRecomendacao) }
    private val btnRecalcular: Button by lazy { findViewById(R.id.btnRecalcular) }

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.resultado_activity)
        setupUI()
    }

    private fun setupUI() {
        setupTextViews()
        setupListeners()
    }
    private fun setupListeners() {
        btnRecalcular.setOnClickListener {
            finish()
        }
    }

    private fun setupTextViews() {
        val peso = intent.getStringExtra("peso")?.toDoubleOrNull() ?: 0.0
        val altura = intent.getStringExtra("altura")?.toDoubleOrNull() ?: 0.0
        val resultadoImc = calcularIMC(peso, altura)
        calcularPesoIdeal(altura)
        val precisaPerder = precisaPerder(peso, altura)


        txtPesoInformado.text =
            getString(
                R.string.txtPesoInformado,
                peso
            )
        txtAlturaInformada.text =
            getString(
                R.string.txtAlturaInformada,
                altura
            )
        txtResultado.text =
            getString(
                R.string.txtResultado,
                faixaIMC(resultadoImc)
            )
        txtRecomendacao.text = if (precisaPerder > 0) {
            getString(
                R.string.txtRecomendacao,
                precisaPerder
            )
        } else {
            "Você está dentro do peso considerado saudável!"
        }


    }

    private fun calcularIMC(peso: Double, altura: Double): Double {
        return peso / (altura * altura)
    }

    private fun faixaIMC(resultadoImc: Double): String {
        return when {
            resultadoImc < 18.5 -> "Abaixo do peso"
            resultadoImc < 24.9 -> "Peso normal"
            resultadoImc < 29.9 -> "Sobrepeso"
            resultadoImc < 34.9 -> "Obesidade grau 1"
            resultadoImc < 39.9 -> "Obesidade grau 2"
            else -> "Obesidade grau 3"
        }
    }

    private fun pesoMaximoNormal(altura: Double): Double {
        return 24.9 * (altura * altura)
    }

    private fun precisaPerder(peso: Double, altura: Double): Double {
        val pesoLimite = pesoMaximoNormal(altura)
        return peso - pesoLimite
    }

    private fun calcularPesoIdeal(altura: Double): Double {
        return 72.7 * altura - 58
    }

}