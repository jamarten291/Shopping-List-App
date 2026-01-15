package com.example.kotlintest

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.sql.SQLException

class ListaCompraDatabaseAdapter(private val dbContext: Context) {

    companion object {
        // Definición de la base de datos y las tablas
        private const val DATABASE_NOMBRE = "dbCompra"
        private const val DATABASE_TABLA = "productos"
        private const val DATABASE_VERSION = 1
        private const val TAG = "ListaCompraDatabaseAdapter"

        // Constantes para definir los nombres de las columnas
        const val CLAVE_PRODUCTO = "producto"
        const val CLAVE_CANTIDAD = "cantidad"
        const val CLAVE_ID = "_id"

        // Columnas para la proyección de las consultas
        private val COLUMNAS_CONSULTA =
            arrayOf(CLAVE_ID, CLAVE_PRODUCTO, CLAVE_CANTIDAD)

        // Sentencia SQL para la creación de la base de datos
        private const val CREAR_DATABASE =
            "CREATE TABLE $DATABASE_TABLA (" +
                    "$CLAVE_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$CLAVE_PRODUCTO TEXT NOT NULL, " +
                    "$CLAVE_CANTIDAD INTEGER NOT NULL);"
    }

    private var dbHelper: DatabaseHelper? = null
    private var dbCompra: SQLiteDatabase? = null

    // Clase interna SQLiteOpenHelper
    private inner class DatabaseHelper(context: Context) :
        SQLiteOpenHelper(context, DATABASE_NOMBRE, null, DATABASE_VERSION) {

        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL(CREAR_DATABASE)
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            Log.w(TAG, "Actualizando BD")
            db.execSQL("DROP TABLE IF EXISTS $DATABASE_TABLA")
            onCreate(db)
        }
    }

    // Método para abrir la conexión a la base de datos
    @Throws(SQLException::class)
    fun open(): ListaCompraDatabaseAdapter {
        dbHelper = DatabaseHelper(dbContext)
        dbCompra = dbHelper!!.writableDatabase
        return this
    }

    // Método para cerrar la conexión a la BD
    fun close() {
        dbHelper?.close()
    }

    // Método para insertar un elemento en la BD
    fun crearElemento(nombre: String, cantidad: Int): Long {
        val valoresProducto = ContentValues().apply {
            put(CLAVE_PRODUCTO, nombre)
            put(CLAVE_CANTIDAD, cantidad)
        }
        return dbCompra!!.insert(DATABASE_TABLA, null, valoresProducto)
    }

    // Método para borrar un elemento en la BD
    fun borrarElemento(rowId: Long): Boolean {
        return dbCompra!!.delete(
            DATABASE_TABLA,
            "$CLAVE_ID=$rowId",
            null
        ) > 0
    }

    // Método para borrar todos los elementos de la tabla
    fun limpiarTabla(): Boolean {
        return dbCompra!!.delete(DATABASE_TABLA, null, null) > 0
    }

    // Método para obtener todos los elementos de la tabla
    fun obtenerTodosElementos(): Cursor {
        return dbCompra!!.query(
            DATABASE_TABLA,
            COLUMNAS_CONSULTA,
            null,
            null,
            null,
            null,
            null
        )
    }

    // Método para obtener un elemento por su ID
    @Throws(SQLException::class)
    fun obtenerElemento(rowId: Long): Cursor {
        val cursor = dbCompra!!.query(
            true,
            DATABASE_TABLA,
            COLUMNAS_CONSULTA,
            "$CLAVE_ID=$rowId",
            null,
            null,
            null,
            null,
            null
        )
        cursor.moveToFirst()
        return cursor
    }

    // Método para actualizar un elemento
    fun actualizarElemento(rowId: Long, producto: String, cantidad: Int): Boolean {
        val args = ContentValues().apply {
            put(CLAVE_PRODUCTO, producto)
            put(CLAVE_CANTIDAD, cantidad)
        }
        return dbCompra!!.update(
            DATABASE_TABLA,
            args,
            "$CLAVE_ID=$rowId",
            null
        ) > 0
    }
}
