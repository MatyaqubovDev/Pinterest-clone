package dev.matyaqubov.pinterest.service


import dev.matyaqubov.pinterest.service.model.*
import retrofit2.Call
import retrofit2.http.*

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
        @Query("per_page") per_page: Int = 30
    ): Call<Search>

    @GET("topics")
    fun getTopics(): Call<TopicItem>
    

    @GET("photos/{id}")
    fun getPhoto(@Path("id") id: String): Call<SearchResultsItem>


}