package dev.matyaqubov.pinterest.service.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Search(

	@field:SerializedName("total")
	val total: Int? = null,

	@field:SerializedName("total_pages")
	val totalPages: Int? = null,

	@field:SerializedName("results")
	val results: ArrayList<SearchResultsItem>? = null
)



data class SearchResultsItem(

	@field:SerializedName("current_user_collections")
	val currentUserCollections: List<Any?>? = null,

	@field:SerializedName("color")
	val color: String=" ",

	@field:SerializedName("sponsorship")
	val sponsorship: Any? = null,

	@field:SerializedName("created_at")
	val createdAt: String="",

	@field:SerializedName("description")
	val description: String="",

	@field:SerializedName("liked_by_user")
	val likedByUser: Boolean? = null,


	@field:SerializedName("urls")
	val urls: Urls? = null,

	@field:SerializedName("alt_description")
	val altDescription: String="",

	@field:SerializedName("updated_at")
	val updatedAt: String="",

	@field:SerializedName("width")
	val width: Int? = null,

	@field:SerializedName("blur_hash")
	val blurHash: String="",

	@field:SerializedName("links")
	val links: Links? = null,

	@field:SerializedName("id")
	val id: String="",

	@field:SerializedName("categories")
	val categories: List<Any?>? = null,

	@field:SerializedName("promoted_at")
	val promotedAt: String="",

	@field:SerializedName("user")
	val user: User? = null,

	@field:SerializedName("height")
	val height: Int? = null,

	@field:SerializedName("likes")
	val likes: Int? = null
) :Serializable


