package com.example.kotlintest

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class CrearProducto : AppCompatActivity(), DataArguments {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_crear_producto)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val etNombre = findViewById<EditText>(R.id.et_nombre)
        val etCantidad = findViewById<EditText>(R.id.et_cantidad)

        val validInput: () -> Boolean = {
            !etNombre.text.toString().isEmpty() &&
            !etCantidad.text.toString().isEmpty()
        }

        findViewById<Button>(R.id.bt_confirmar).setOnClickListener { v ->
            if (validInput()) {
                val finishIntent = Intent().apply {
                    putExtra(ARG_1, etNombre.text.toString())
                    putExtra(ARG_2, etCantidad.text.toString().toInt())
                }
                setResult(RESULT_OK, finishIntent)
                finish()
            } else {
                Toast.makeText(this, "Debes rellenar todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        findViewById<Button>(R.id.bt_cancelar).setOnClickListener { v ->
            setResult(RESULT_CANCELED)
            finish()
        }
    }
}