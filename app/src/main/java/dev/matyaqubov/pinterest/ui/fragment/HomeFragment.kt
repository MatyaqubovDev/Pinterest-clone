package dev.matyaqubov.pinterest.ui.fragment

import android.app.Activity
import android.os.Bundle
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
import dev.matyaqubov.pinterest.adapter.SearchPhotosAdapter
import dev.matyaqubov.pinterest.model.Filter
import dev.matyaqubov.pinterest.service.RetrofitHttp
import dev.matyaqubov.pinterest.service.model.Search
import dev.matyaqubov.pinterest.service.model.SearchResultsItem
import dev.matyaqubov.pinterest.helper.ProgressDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeFragment : Fragment() {
    private lateinit var rv_filter: RecyclerView
    private lateinit var rv_home_main: RecyclerView
    private lateinit var filters: ArrayList<Filter>
    var list = ArrayList<SearchResultsItem>()
    var page = 1
    var word: String = ""
    var sendData: SearchFragment.SendData? = null
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var adapterHome: SearchPhotosAdapter
    private lateinit var manager: StaggeredGridLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return initViews(inflater.inflate(R.layout.fragment_home, container, false))
    }

    private fun initViews(view: View): View {
        prepareFilters()
        word="service"
        rv_filter = view.findViewById(R.id.rv_filter)
        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh2)
        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = false
            list.clear()
            searchPhoto(word)
        }
        var filterAdapter =FilterAdapter(filters)
        rv_filter.adapter = filterAdapter

        filterAdapter.itemselected={ position ->
            list.clear()
            if (position!=0)
            word = filters[position].name
            else word="programming"
            searchPhoto(word)
        }
        rv_home_main = view.findViewById(R.id.rv_home_main)
        manager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        rv_home_main.layoutManager = manager
        refreshAdapter(list)
        searchPhoto(word)

        val scrollListener = object : EndlessRecyclerViewScrollListener(manager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                searchPhoto(word)
            }

        }
        rv_home_main.addOnScrollListener(scrollListener)

        adapterHome.photoItemClick = {
            sendData!!.sendPhoto(it,word,page)
        }



        return view
    }

    private fun searchPhoto(word: String) {
        ProgressDialog.showProgress(requireContext())
        RetrofitHttp.apiService.getSearchResult(word, getPage())
            .enqueue(object : Callback<Search> {
                override fun onResponse(
                    call: Call<Search>,
                    response: Response<Search>
                ) {
                    if (!response.body()!!.results.isNullOrEmpty()){
                        list.addAll(response.body()!!.results!!)
                        adapterHome.notifyDataSetChanged()
                    } else {
                        Toast.makeText(requireContext(), "Limit tugadi yoki bunday rasm topa oladim", Toast.LENGTH_SHORT).show()
                    }

                    ProgressDialog.dismissProgress()
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



    private fun refreshAdapter(list: ArrayList<SearchResultsItem>) {
        adapterHome = SearchPhotosAdapter(list)
        rv_home_main.adapter = adapterHome
    }

    private fun prepareFilters() {
        filters = ArrayList()
        filters.add(Filter("All", true))
        filters.add(Filter("Laptops"))
        filters.add(Filter("Mobiles"))
        filters.add(Filter("Natures"))
        filters.add(Filter("Buildings"))
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
}