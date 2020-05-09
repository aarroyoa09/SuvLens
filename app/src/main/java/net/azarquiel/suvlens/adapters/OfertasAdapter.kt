package net.azarquiel.suvlens.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.row2.view.*
import net.azarquiel.suvlens.model.Camera

class OfertasAdapter (
    val context: Context,
    val layout: Int
) : RecyclerView.Adapter<OfertasAdapter.ViewHolder>() {

    private var dataList: List<Camera> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val viewlayout = layoutInflater.inflate(layout, parent, false)
        return ViewHolder(viewlayout, context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    internal fun setCameras(cams: List<Camera>) {
        this.dataList = cams
        notifyDataSetChanged()
    }

    class ViewHolder(viewlayout: View, val context: Context) : RecyclerView.ViewHolder(viewlayout) {
        fun bind(dataItem: Camera) {
            // itemview es el item de diseño
            // al que hay que poner los datos del objeto dataItem
            itemView.tvnombreoferta.text = dataItem.name
            itemView.tvpriceoferta.text = dataItem.price.toString() + "€"
            Picasso.get().load(dataItem.photo).into(itemView.ivoferta)
            itemView.tag = dataItem
        }
    }
}