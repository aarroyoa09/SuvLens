package net.azarquiel.suvlens.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.rowfav.view.*
import net.azarquiel.suvlens.model.Camera
import net.azarquiel.suvlens.views.FavActivity

class FavAdapter (
    val context: FavActivity,
    val layout: Int
) : RecyclerView.Adapter<FavAdapter.ViewHolder>() {

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
            itemView.tvfavrow.text = dataItem.name
            itemView.tvpricefavrow.text = dataItem.price.toString() + "€"
            Picasso.get().load(dataItem.photo).into(itemView.ivfavrow)
            itemView.tag = dataItem
        }

    }
}