package dev.matyaqubov.pinterest.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import dev.matyaqubov.pinterest.R
import dev.matyaqubov.pinterest.model.Filter
import dev.matyaqubov.pinterest.service.model.TopicItem

class FilterAdapter(var items:ArrayList<Filter>) :RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var selectedIndex:Int=0
    var itemselected:((position:Int)->Unit)?=null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return FilterViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_filter,parent,false))
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is FilterViewHolder){
            holder.apply {
                tv_filter.text=items[position].name
                item.isSelected=items[position].isSelected
                if (item.isSelected){
                    selectedIndex=position
                    tv_filter.setTextColor(Color.WHITE)
                }else {
                    tv_filter.setTextColor(Color.BLACK)
                }
                item.setOnClickListener {
                    itemselected!!.invoke(position)
                    change(position)
                }
            }

        }
    }

    private fun change(position: Int) {
        items[selectedIndex].isSelected=false
        notifyItemChanged(selectedIndex)
        if (!items[position].isSelected) items[position].isSelected=true
        notifyItemChanged(position)


    }

    override fun getItemCount(): Int {
       return items.size
    }
    class FilterViewHolder(view: View):RecyclerView.ViewHolder(view){
        var tv_filter=view.findViewById<TextView>(R.id.tv_filter)
        var item=view.findViewById<LinearLayout>(R.id.item)
    }
}