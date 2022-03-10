package dev.matyaqubov.pinterest.service


import dev.matyaqubov.pinterest.service.model.PhotosResponseItem
import dev.matyaqubov.pinterest.service.model.Search
import dev.matyaqubov.pinterest.service.model.SearchResultsItem
import dev.matyaqubov.pinterest.service.model.TopicItem
import retrofit2.Call
import retrofit2.http.GET

import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("photos")
    fun getPhotos(
        @Query("page") page: Int,
        @Query("per_page") per_page: Int = 20
    ): Call<ArrayList<PhotosResponseItem>>


    @GET("search/photos")
    fun getSearchResult(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("per_page") per_page: Int = 20
    ): Call<Search>

    @GET("topics")
    fun getTopics(): Call<TopicItem>

    @GET("photos/{id}")
    fun getPhoto(@Path("id") id: String): Call<PhotosResponseItem>


}