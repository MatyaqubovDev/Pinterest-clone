package dev.matyaqubov.pinterest.ui.fragment

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.example.pinterestclone.helper.EndlessRecyclerViewScrollListener
import dev.matyaqubov.pinterest.R
import dev.matyaqubov.pinterest.adapter.SearchPhotosAdapter
import dev.matyaqubov.pinterest.service.RetrofitHttp
import dev.matyaqubov.pinterest.service.model.Search
import dev.matyaqubov.pinterest.service.model.SearchResultsItem
import dev.matyaqubov.pinterest.ui.helper.ProgressDialog
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@RequiresApi(Build.VERSION_CODES.M)
class DetailsFragment : Fragment() , View.OnTouchListener,View.OnScrollChangeListener{

    private lateinit var tv_username:TextView
    private lateinit var tv_comment_username:TextView
    private lateinit var tv_followers:TextView
    private lateinit var tv_describtion:TextView
    private lateinit var tv_alt_describtion:TextView
    private lateinit var tv_visit:TextView
    private lateinit var tv_save:TextView
    private lateinit var iv_main:ImageView
    private lateinit var iv_back:ImageView
    private lateinit var iv_profile:ImageView
    private lateinit var iv_comment_profile:ImageView
    private lateinit var iv_share:ImageView
    private lateinit var recyclerView: RecyclerView
    private var word :String=""
    private var photo:SearchResultsItem?=null
    private lateinit var adapter: SearchPhotosAdapter
    private lateinit var manager: StaggeredGridLayoutManager
    private var sendData: SearchFragment.SendData? = null
    private var list = ArrayList<SearchResultsItem>()
    private var page=1
    lateinit var nestedScrollView: NestedScrollView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return initViews(inflater.inflate(R.layout.fragment_details, container, false))
    }

    private fun initViews(view: View): View {

        recyclerView=view.findViewById(R.id.recyclerView)
        recyclerView.setHasFixedSize(true)
        iv_profile=view.findViewById(R.id.iv_profile)
        tv_username=view.findViewById(R.id.tv_username)
        tv_comment_username=view.findViewById(R.id.tv_comment_username)
        tv_followers=view.findViewById(R.id.tv_followers)
        tv_describtion=view.findViewById(R.id.tv_describtion)
        tv_alt_describtion=view.findViewById(R.id.tv_alt_describtion)
        tv_visit=view.findViewById(R.id.tv_visit)
        tv_save=view.findViewById(R.id.tv_save)
        iv_main=view.findViewById(R.id.iv_main)
        iv_back=view.findViewById(R.id.iv_back)
        iv_comment_profile=view.findViewById(R.id.iv_comment_profile)
        iv_share=view.findViewById(R.id.iv_share)
        nestedScrollView = view.findViewById(R.id.nestedScroll)
        setData()
        searchPhoto(word)


//        nestedScrollView.isNestedScrollingEnabled = false
//        val scrollListener = object : EndlessRecyclerViewScrollListener(manager) {
//            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
////                searchPhoto(word)
//                Toast.makeText(context, "???", Toast.LENGTH_SHORT).show()
//            }
//        }
//        recyclerView.addOnScrollListener(scrollListener)


        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (!recyclerView.canScrollVertically(1)) {
                    searchPhoto(word)
                    Toast.makeText(context, "last", Toast.LENGTH_SHORT).show()
                }
            }
        })

        iv_back.setOnClickListener {
            requireActivity().onBackPressed()
        }

        adapter.photoItemClick = {
            sendData!!.sendPhoto(it,word,page)
        }


        return view
    }

    private fun setData() {
        Glide.with(iv_main.context)
            .load(photo!!.urls!!.smallS3)
            .error(R.mipmap.ic_launcher)
            .placeholder(ColorDrawable(Color.parseColor(photo!!.color)))
            .into(iv_main)
        Glide.with(iv_profile.context).load(photo!!.user!!.profileImage!!.small).into(iv_profile)
        Glide.with(iv_profile.context).load(photo!!.user!!.profileImage!!.small).into(iv_comment_profile)
        tv_username.text=photo!!.user!!.username
        tv_comment_username.text=photo!!.user!!.username
        tv_describtion.text=photo!!.description
        tv_alt_describtion.text=photo!!.altDescription

        manager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.layoutManager = manager
        refreshAdapter(list)

    }

    private fun refreshAdapter(list: ArrayList<SearchResultsItem>) {
        adapter = SearchPhotosAdapter(list)
        recyclerView.adapter = adapter
    }

    fun receivedData(photo:SearchResultsItem,word: String,page:Int){
        this.photo=photo
        this.word=word
        this.page=page
    }

    private fun searchPhoto(word: String) {
        ProgressDialog.showProgress(requireContext())
        RetrofitHttp.apiService.getSearchResult(word, getPage())
            .enqueue(object : Callback<Search> {
                override fun onResponse(
                    call: Call<Search>,
                    response: Response<Search>
                ) {
                    if (response.body()!!.results == null)
                        Toast.makeText(context, "LIMIT", Toast.LENGTH_SHORT).show()
                    else
                    list.addAll(response.body()!!.results!!)
                    ProgressDialog.dismissProgress()
                    Log.d("requessssst", "onResponse: ${response.body()!!.results!!}")
                    adapter.notifyDataSetChanged()
                }

                override fun onFailure(call: Call<Search>, t: Throwable) {
                    ProgressDialog.dismissProgress()
                    Toast.makeText(requireContext(), "Check internet please", Toast.LENGTH_SHORT)
                        .show()
                }

            })

    }

    @JvmName("getPage1")
    private fun getPage(): Int {
        if (page < 250) {
            return page++
        } else {
            page = 1
            return page
        }
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        try {
            sendData = activity as SearchFragment.SendData
        } catch (e: Exception) {
            Toast.makeText(requireContext(), " must implement", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDetach() {
        sendData = null
        super.onDetach()
    }

    override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
        return false
    }

    override fun onScrollChange(p0: View?, p1: Int, p2: Int, p3: Int, p4: Int) {

    }


}