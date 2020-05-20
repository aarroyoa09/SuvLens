package net.azarquiel.suvlens.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.rowinfo.view.*
import net.azarquiel.suvlens.model.Info

class InfoAdapter (val context: Context,
                   val layout: Int
) : RecyclerView.Adapter<InfoAdapter.ViewHolder>() {

    private var dataList: List<Info> = emptyList()

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

    internal fun setInfos(infos: List<Info>) {
        this.dataList = infos
        notifyDataSetChanged()
    }


    class ViewHolder(viewlayout: View, val context: Context) : RecyclerView.ViewHolder(viewlayout) {
        fun bind(dataItem: Info){
            // itemview es el item de diseño
            // al que hay que poner los datos del objeto dataItem
            itemView.tvtitlelibrary.text = dataItem.name
            itemView.tvgt.text = dataItem.link
            itemView.tag = dataItem

        }

    }
}