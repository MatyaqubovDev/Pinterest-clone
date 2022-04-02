package dev.matyaqubov.pinterest.ui.fragment

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.pinterestclone.helper.EndlessRecyclerViewScrollListener
import dev.matyaqubov.pinterest.R
import dev.matyaqubov.pinterest.adapter.SearchAdapter
import dev.matyaqubov.pinterest.adapter.SearchPhotosAdapter
import dev.matyaqubov.pinterest.model.Home
import dev.matyaqubov.pinterest.service.RetrofitHttp
import dev.matyaqubov.pinterest.service.model.Search
import dev.matyaqubov.pinterest.service.model.SearchResultsItem
import dev.matyaqubov.pinterest.helper.ProgressDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchFragment : Fragment() {
    var page = 1
    private lateinit var rv_search_main: RecyclerView
    private lateinit var et_search: EditText
    private lateinit var tv_cancel: TextView
    private var word: String = ""
    var sendData: SendData? = null
    var list = ArrayList<SearchResultsItem>()
    lateinit var adapter: SearchPhotosAdapter
    private var photosOne = ArrayList<Home>()
    private var photosTwo = ArrayList<Home>()
    private lateinit var adapterOne: SearchAdapter
    private lateinit var adapterTwo: SearchAdapter
    private lateinit var manager: StaggeredGridLayoutManager
    private lateinit var nestedScrollView: NestedScrollView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return initViews(inflater.inflate(R.layout.fragment_search, container, false))
    }

    private fun initViews(view: View): View {

        rv_search_main = view.findViewById(R.id.rv_search_main)
        manager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        rv_search_main.layoutManager = manager
        refreshAdapter(list)
        et_search = view.findViewById(R.id.et_search)
        tv_cancel = view.findViewById(R.id.tv_cancel)
        nestedScrollView = view.findViewById(R.id.nestedScroll)
        et_search.addTextChangedListener {
            tv_cancel.visibility = View.VISIBLE
            et_search.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_baseline_camera_alt_24,
                0
            )
            word = it.toString()
            if (word != "") {
                tv_cancel.text = "Search"
            } else {
                tv_cancel.setText("Cancel")
            }
            nestedScrollView.visibility = View.GONE
        }
        et_search.setOnEditorActionListener { _, actionId, keyEvent ->
            if ((keyEvent != null && (keyEvent.keyCode == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_SEARCH)) {
                list.clear()
                searchPhoto(word)
            }
            false
        }
        initSearchRecyclerView(view)
        tv_cancel.setOnClickListener {
            list.clear()
            searchPhoto(word)

        }

        adapterOne.clickItem = {
            et_search.setText(it)
            list.clear()
            nestedScrollView.visibility = View.GONE
            searchPhoto(it)
        }

        adapterTwo.clickItem = {
            list.clear()
            et_search.setText(it)
            nestedScrollView.visibility = View.GONE
            searchPhoto(it)
        }

        val scrollListener = object : EndlessRecyclerViewScrollListener(manager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                searchPhoto(word)
            }

        }
        rv_search_main.addOnScrollListener(scrollListener)

        adapter.photoItemClick = {

            sendData!!.sendPhoto(it,word,page)
        }


        return view
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        try {
            sendData = activity as SendData
        } catch (e: Exception) {
            Toast.makeText(requireContext(), " must implement", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDetach() {
        sendData = null
        super.onDetach()
    }

    private fun refreshAdapter(list: ArrayList<SearchResultsItem>) {
        adapter = SearchPhotosAdapter(list)
        rv_search_main.adapter = adapter
    }

    private fun searchPhoto(word: String) {
        ProgressDialog.showProgress(requireContext())
        RetrofitHttp.apiService.getSearchResult(word, getPage())
            .enqueue(object : Callback<Search> {
                override fun onResponse(
                    call: Call<Search>,
                    response: Response<Search>
                ) {
                    if(response.body()!=null) {
                        list.addAll(response.body()!!.results!!)
                    }
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
        //ProgressDialog.dismissProgress()

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

    private fun initSearchRecyclerView(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewOne)
        val recyclerView2 = view.findViewById<RecyclerView>(R.id.recyclerViewTwo)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView2.layoutManager = GridLayoutManager(requireContext(), 2)
        getinits()
        adapterOne = SearchAdapter(photosOne)
        adapterTwo = SearchAdapter(photosTwo)
        recyclerView.adapter = adapterOne
        recyclerView2.adapter = adapterTwo
    }

    private fun getinits() {
        photosOne.clear()
        photosTwo.clear()
        photosOne.add(
            Home(
                "https://i.pinimg.com/236x/2a/77/70/2a7770e880c000ab530b7c889556ef3a.jpg",
                "Import Cars"
            )
        )
        photosOne.add(
            Home(
                "https://i.pinimg.com/564x/42/46/95/4246952430136a8a88551008c93b4224.jpg",
                "Studio setup"
            )
        )
        photosOne.add(
            Home(
                "https://i.pinimg.com/236x/b0/6f/09/b06f09d953a2a9b65c8c517f7ef8d1ef.jpg",
                "Camera Gear"
            )
        )
        photosOne.add(
            Home(
                "https://i.pinimg.com/236x/58/2e/94/582e94408f3d0ff2b13a94de48e2cfb7.jpg",
                "Truck bed camping"
            )
        )
        photosOne.add(
            Home(
                "https://i.pinimg.com/236x/09/f2/a8/09f2a8a98430c8d410bfd305b4d1973d.jpg",
                "Elliot erwitt"
            )
        )
        photosOne.add(
            Home(
                "https://i.pinimg.com/236x/46/44/6f/46446fdde15fe8ed6ad5aeef1b028d3c.jpg",
                "Afghan patterns"
            )
        )
        photosOne.add(
            Home(
                "https://i.pinimg.com/236x/c9/e4/5f/c9e45f8333ad25883019b2f13f718922.jpg",
                "Tennis shoes"
            )
        )
        photosOne.add(
            Home(
                "https://i.pinimg.com/236x/7c/61/14/7c611403c6eca5d00809b82e85cb0bb9.jpg",
                "Girls sneakers"
            )
        )

        photosTwo.add(
            Home(
                "https://i.pinimg.com/236x/06/cd/dd/06cddd0c5fe356b96f810d3fae7e10de.jpg",
                "Iphone Wallpaper"
            )
        )
        photosTwo.add(
            Home(
                "https://i.pinimg.com/236x/1e/ad/95/1ead95803a35f3ee4888e196a5618e1a.jpg",
                "Short hairstyle women"
            )
        )
        photosTwo.add(
            Home(
                "https://i.pinimg.com/236x/f6/cb/c4/f6cbc44893e481c0e9570980a7dc62c8.jpg",
                "Blue aesthetic"
            )
        )
        photosTwo.add(
            Home(
                "https://i.pinimg.com/236x/36/b4/6a/36b46a20e3a898c210c823c18d443d9a.jpg",
                "Digital art girl"
            )
        )
        photosTwo.add(
            Home(
                "https://i.pinimg.com/236x/48/19/8f/48198f333a0778c4bc1b5987408c2eae.jpg",
                "Elf on the shelf ideas"
            )
        )
        photosTwo.add(
            Home(
                "https://i.pinimg.com/236x/d2/9a/0e/d29a0eb76e27bb4d218cc2d8e382cc49.jpg",
                "Easy drawings"
            )
        )
    }

    interface SendData {
        fun sendPhoto(photo:SearchResultsItem,word: String,page:Int)
    }

}