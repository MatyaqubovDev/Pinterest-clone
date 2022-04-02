package dev.matyaqubov.pinterest.ui.fragment

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import dev.matyaqubov.pinterest.R
import dev.matyaqubov.pinterest.adapter.SearchPhotosAdapter
import dev.matyaqubov.pinterest.helper.ProgressDialog
import dev.matyaqubov.pinterest.service.RetrofitHttp
import dev.matyaqubov.pinterest.service.model.Search
import dev.matyaqubov.pinterest.service.model.SearchResultsItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import androidx.core.app.ActivityCompat
import android.os.Environment
import java.io.File
import android.graphics.drawable.Drawable
import android.graphics.drawable.BitmapDrawable
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.annotation.Nullable
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import dev.matyaqubov.pinterest.database.AppDatabase
import dev.matyaqubov.pinterest.database.entitiy.Photos
import java.io.FileOutputStream
import java.io.OutputStream


class DetailsFragment : Fragment() {

    private lateinit var tv_username: TextView
    private lateinit var tv_comment_username: TextView
    private lateinit var tv_followers: TextView
    private lateinit var tv_describtion: TextView
    private lateinit var tv_alt_describtion: TextView
    private lateinit var tv_visit: TextView
    private lateinit var tv_save: TextView
    private lateinit var iv_main: ImageView
    private lateinit var iv_back: ImageView
    private lateinit var iv_profile: ImageView
    private lateinit var iv_comment_profile: ImageView
    private lateinit var iv_share: ImageView
    private lateinit var l_download:LottieAnimationView
    private lateinit var recyclerView: RecyclerView
    private var word: String = ""
    lateinit var appDatabase: AppDatabase
    private var photo: SearchResultsItem? = null
    private lateinit var adapter: SearchPhotosAdapter
    private lateinit var manager: StaggeredGridLayoutManager
    private var sendData: SearchFragment.SendData? = null
    private var list = ArrayList<SearchResultsItem>()
    private var page = 1
    lateinit var nestedScrollView: NestedScrollView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return initViews(inflater.inflate(R.layout.fragment_details, container, false))
    }

    private fun initViews(view: View): View {
        appDatabase= AppDatabase.getInstance(requireContext())
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.isNestedScrollingEnabled = false
        iv_profile = view.findViewById(R.id.iv_profile)
        tv_username = view.findViewById(R.id.tv_username)
        l_download=view.findViewById(R.id.l_download)
        tv_comment_username = view.findViewById(R.id.tv_comment_username)
        tv_followers = view.findViewById(R.id.tv_followers)
        tv_describtion = view.findViewById(R.id.tv_describtion)
        tv_alt_describtion = view.findViewById(R.id.tv_alt_describtion)
        tv_visit = view.findViewById(R.id.tv_visit)
        tv_save = view.findViewById(R.id.tv_save)
        iv_main = view.findViewById(R.id.iv_main)
        iv_back = view.findViewById(R.id.iv_back)
        iv_comment_profile = view.findViewById(R.id.iv_comment_profile)
        iv_share = view.findViewById(R.id.iv_share)
        nestedScrollView = view.findViewById(R.id.nestedScroll)
        setData()
        searchPhoto(word)


        //To achieve endless scrolling for recycler view which is under NestedScrollView,
        // you can use "NestedScrollView.OnScrollChangeListener"
//        Here v.getChildCount() -1 should give you the recycler view for which you be implementing endless scrolling.
//Also scrollY > oldScrollY confirms that the page is being scrolled down.

        nestedScrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v: NestedScrollView, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
            if (v.getChildAt(v.childCount - 1) != null) {
                if (scrollY >= v.getChildAt(v.childCount - 1).measuredHeight - v.measuredHeight && scrollY > oldScrollY) {
                    searchPhoto(word)
                }
            }
        } as NestedScrollView.OnScrollChangeListener)

        iv_back.setOnClickListener {
            requireActivity().onBackPressed()
        }

        adapter.photoItemClick = {
            sendData!!.sendPhoto(it, word, page)
        }

        tv_save.setOnClickListener {
            savetoData()
        }

        l_download.setOnClickListener {

            downloading(photo!!.links!!.download)
        }


        return view
    }

    private fun savetoData() {
        var photos=Photos(photo!!.id,photo!!.color,photo!!.description.let { " " },photo!!.altDescription.let { " " },photo!!.urls!!.thumb!!,word)
        appDatabase.photoDao().savePhoto(photos )
        Toast.makeText(requireContext(), "Saved", Toast.LENGTH_SHORT).show()
    }


    private fun downloading(imageURL: String?) {
       if (!verifyPermissions()){
           downloading(imageURL)
       }

        val fileName: String = photo!!.id!!
        l_download.playAnimation()
        Glide.with(this)
            .load(imageURL)
            .into(object : CustomTarget<Drawable?>() {

                override fun onResourceReady(
                    resource: Drawable,
                    @Nullable transition: Transition<in Drawable?>?
                ) {
                    val bitmap = (resource as BitmapDrawable).bitmap

                    saveImage(bitmap, fileName)
                }

                override fun onLoadCleared(@Nullable placeholder: Drawable?) {}
                override fun onLoadFailed(@Nullable errorDrawable: Drawable?) {
                    super.onLoadFailed(errorDrawable)

                }
            })


    }

    private fun saveImage(image: Bitmap, imageFileName: String) {


        //Generating a file name
        val filename = "${imageFileName}.jpg"

        //Output stream
        var fos: OutputStream? = null

//        For devices running android >= Q
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            //getting the contentResolver
            context?.contentResolver?.also { resolver ->

                //Content resolver will process the contentvalues
                val contentValues = ContentValues().apply {

                    //putting file information in content values
                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }

                //Inserting the contentValues to contentResolver and getting the Uri
                val imageUri: Uri? =
                    resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

                //Opening an outputstream with the Uri that we got
                fos = imageUri?.let { resolver.openOutputStream(it) }
            }
        } else {
            //These for devices running on android < Q
            //So I don't think an explanation is needed here
            val imagesDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val image = File(imagesDir, filename)
            fos = FileOutputStream(image)
        }

        fos?.use {
            //Finally writing the bitmap to the output stream that we opened
            image.compress(Bitmap.CompressFormat.JPEG, 100, it)

            Toast.makeText(context, "Downloaded", Toast.LENGTH_SHORT).show()
        }
    }


    private fun verifyPermissions(): Boolean {
        val permissionExternalMemory =
            ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)

        if (permissionExternalMemory != PackageManager.PERMISSION_GRANTED) {
            val STORAGE_PERMISSIONS = arrayOf<String>(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            // If permission not granted then ask for permission real time.
            ActivityCompat.requestPermissions(requireActivity(), STORAGE_PERMISSIONS, 1)
            return false
        }

        return true

    }

    private fun setData() {
        Glide.with(iv_main.context).load(photo!!.urls!!.smallS3).error(R.mipmap.ic_launcher)
            .placeholder(ColorDrawable(Color.parseColor(photo!!.color))).into(iv_main)
        Glide.with(iv_profile.context).load(photo!!.user!!.profileImage!!.small).into(iv_profile)
        Glide.with(iv_profile.context).load(photo!!.user!!.profileImage!!.small)
            .into(iv_comment_profile)
        tv_username.text = photo!!.user!!.username
        tv_comment_username.text = photo!!.user!!.username
        tv_describtion.text = photo!!.description
        tv_alt_describtion.text = photo!!.altDescription

        manager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.layoutManager = manager
        refreshAdapter(list)

    }

    private fun refreshAdapter(list: ArrayList<SearchResultsItem>) {
        adapter = SearchPhotosAdapter(list)
        recyclerView.adapter = adapter
    }

    fun receivedData(photo: SearchResultsItem, word: String, page: Int) {
        this.photo = photo
        this.word = word
        this.page = page
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




}