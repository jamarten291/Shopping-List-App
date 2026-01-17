package com.example.kotlintest

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity :
    AppCompatActivity(),
    DataArguments {
    private val dbHelper = ListaCompraDatabaseAdapter(this)
    private val adapter = CustomAdapter(ArrayList())
    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data: Intent? = result.data

                if (data != null) {
                    val nombre = data.getStringExtra(ARG_1)
                    val cantidad = data.getIntExtra(ARG_2, 0)

                    if (nombre != null) dbHelper.crearElemento(nombre, cantidad)
                    reloadRecyclerList()
                }
            } else {
                // Cancelado o ERROR
                Toast.makeText(this, R.string.toast_cancelar, Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        dbHelper.open()
        reloadRecyclerList()

        val recycler = findViewById<RecyclerView>(R.id.rv_productos)
        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(this)

        findViewById<Button>(R.id.bt_agregar).setOnClickListener { _ ->
            val intent = Intent(this, CrearProducto::class.java)
            launcher.launch(intent)
        }

        findViewById<Button>(R.id.bt_borrar).setOnClickListener { _ ->
            dbHelper.limpiarTabla()
            reloadRecyclerList()
        }

        findViewById<Button>(R.id.bt_salir).setOnClickListener { _ ->
            dbHelper.close()
            finishAffinity()
        }
    }

    fun reloadRecyclerList() {
        val updatedDataSet = ArrayList<Producto>()

        dbHelper.obtenerTodosElementos().forEach {
            updatedDataSet.add(
                Producto(
                    getInt(0),
                    getString(1),
                    getInt(2),
                ),
            )
        }
        adapter.updateDataSet(updatedDataSet)
    }

    fun Cursor.forEach(block: Cursor.() -> Unit) {
        use {
            if (moveToFirst()) {
                do {
                    block()
                } while (moveToNext())
            }
        }
    }
}
