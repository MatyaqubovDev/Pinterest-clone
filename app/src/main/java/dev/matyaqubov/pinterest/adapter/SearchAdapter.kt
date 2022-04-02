package dev.matyaqubov.pinterest.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.imageview.ShapeableImageView
import dev.matyaqubov.pinterest.R
import dev.matyaqubov.pinterest.model.Home

class SearchAdapter(var items:ArrayList<Home>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var clickItem:((word: String)-> Unit)?=null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_search_layout,parent,false)
        return SearchViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val home = items[position]

        if (holder is SearchViewHolder) {
            val tv_title = holder.tv_title
            val iv_photo = holder.iv_photo
            holder.item.setOnClickListener {
                clickItem!!.invoke(home.title)
            }

            tv_title.text = home.title
            Glide.with(holder.itemView.context)
                .load(home.photo)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(iv_photo)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }


    class SearchViewHolder(view: View):RecyclerView.ViewHolder(view){
        var tv_title: TextView
        var iv_photo: ShapeableImageView
        var item:FrameLayout

        init {
            tv_title = view.findViewById(R.id.textView)
            iv_photo = view.findViewById(R.id.imageView)
            item=view.findViewById(R.id.item)
        }
    }
}