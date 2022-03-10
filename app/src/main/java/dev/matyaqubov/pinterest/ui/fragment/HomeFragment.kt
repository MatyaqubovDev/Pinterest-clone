package dev.matyaqubov.pinterest.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.pinterestclone.helper.EndlessRecyclerViewScrollListener
import dev.matyaqubov.pinterest.R
import dev.matyaqubov.pinterest.adapter.FilterAdapter
import dev.matyaqubov.pinterest.adapter.HomePhotosAdapter
import dev.matyaqubov.pinterest.service.RetrofitHttp
import dev.matyaqubov.pinterest.service.model.PhotosResponseItem
import dev.matyaqubov.pinterest.service.model.TopicItem
import dev.matyaqubov.pinterest.ui.helper.ProgressDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeFragment : Fragment() {
    private lateinit var rv_filter: RecyclerView
    private lateinit var rv_home_main: RecyclerView
    private lateinit var filters: ArrayList<TopicItem>
    var list = ArrayList<PhotosResponseItem>()
    var page = 0
    private lateinit var swipeRefreshLayout:SwipeRefreshLayout
    private lateinit var adapterHome: HomePhotosAdapter
    private lateinit var manager: StaggeredGridLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return initViews(inflater.inflate(R.layout.fragment_home, container, false))
    }

    private fun initViews(view: View): View {
        prepareFilters()
        rv_filter = view.findViewById(R.id.rv_filter)
        swipeRefreshLayout=view.findViewById(R.id.swipeRefresh2)
        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = false
            list.clear()
            getPhotoFromServer()
        }
        rv_filter.adapter = FilterAdapter(filters)
        rv_home_main = view.findViewById(R.id.rv_home_main)
        manager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        rv_home_main.layoutManager = manager
        refreshAdapter(list)
        getPhotoFromServer()

        val scrollListener = object : EndlessRecyclerViewScrollListener(manager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                getPhotoFromServer()
            }

        }
        rv_home_main.addOnScrollListener(scrollListener)
        return view
    }

    fun getPhotoFromServer() {
        ProgressDialog.showProgress(requireContext())
        RetrofitHttp.apiService.getPhotos(getPage())
            .enqueue(object : Callback<ArrayList<PhotosResponseItem>> {
                override fun onResponse(
                    call: Call<ArrayList<PhotosResponseItem>>,
                    response: Response<ArrayList<PhotosResponseItem>>
                ) {
                    list.addAll(response.body()!!)
                    adapterHome.notifyDataSetChanged()
                    swipeRefreshLayout.isRefreshing=false
                    ProgressDialog.dismissProgress()
                }

                override fun onFailure(call: Call<ArrayList<PhotosResponseItem>>, t: Throwable) {
                    ProgressDialog.dismissProgress()
                    Toast.makeText(requireContext(), "Intenet bilan muammo", Toast.LENGTH_SHORT)
                        .show()
                }

            })
    }

    @JvmName("getPage1")
    fun getPage(): Int {
        return ++page
    }


    private fun refreshAdapter(list: ArrayList<PhotosResponseItem>) {
        adapterHome = HomePhotosAdapter(list)
        rv_home_main.adapter = adapterHome
    }

    private fun prepareFilters() {
        filters = ArrayList()
        RetrofitHttp.apiService.getTopics().enqueue(object : Callback<TopicItem> {
            override fun onResponse(call: Call<TopicItem>, response: Response<TopicItem>) {
                filters.addAll(response.body() as ArrayList<TopicItem>)

            }

            override fun onFailure(call: Call<TopicItem>, t: Throwable) {

            }

        })
    }


}