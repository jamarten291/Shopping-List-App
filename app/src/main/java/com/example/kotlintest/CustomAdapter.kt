package com.example.kotlintest

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CustomAdapter(private val dataSet: ArrayList<Producto>) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNombre: TextView
        val tvCantidad: TextView

        init {
            // Define click listener for the ViewHolder's View
            tvNombre = view.findViewById(R.id.tv_nombre)
            tvCantidad = view.findViewById(R.id.tv_cantidad)
        }

        fun setElement(p: Producto) {
            tvNombre.text = p.nombre
            tvCantidad.text = p.cantidad.toString()
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_view, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.setElement(dataSet[position])
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateDataSet(newList: ArrayList<Producto>) {
        dataSet.clear()
        dataSet.addAll(newList)
        notifyDataSetChanged()
    }

}