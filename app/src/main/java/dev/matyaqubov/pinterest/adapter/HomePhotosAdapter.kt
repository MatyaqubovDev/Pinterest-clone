package dev.matyaqubov.pinterest.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dev.matyaqubov.pinterest.R
import dev.matyaqubov.pinterest.service.model.PhotosResponseItem

class HomePhotosAdapter(val lists: ArrayList<PhotosResponseItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PhotoListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_home_photos,parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item=lists[position]
        if (holder is PhotoListViewHolder){
            holder.apply {
                tv_describtion.text=item.description
                Glide.with(iv_home.context).load(item.urls!!.thumb).error(R.mipmap.ic_launcher).into(iv_home)
                Log.d("positionnnnnn", "onBindViewHolder: $position")
                iv_more.setOnClickListener {
                    Toast.makeText(iv_home.context, "Coming soon", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return lists.size
    }

    class PhotoListViewHolder(view: View):RecyclerView.ViewHolder(view){
        var tv_describtion=view.findViewById<TextView>(R.id.tv_describtion)
        var iv_home=view.findViewById<ImageView>(R.id.iv_home)
        var iv_more=view.findViewById<ImageView>(R.id.iv_share_more)
    }

}