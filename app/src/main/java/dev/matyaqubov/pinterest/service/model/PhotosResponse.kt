package dev.matyaqubov.pinterest.service.model

import com.google.gson.annotations.SerializedName

data class PhotosResponse(

	@field:SerializedName("PhotosResponse")
	val photosResponse: List<PhotosResponseItem?>? = null
)

data class Urls(

	@field:SerializedName("small")
	val small: String? = null,

	@field:SerializedName("small_s3")
	val smallS3: String? = null,

	@field:SerializedName("thumb")
	val thumb: String? = null,

	@field:SerializedName("raw")
	val raw: String? = null,

	@field:SerializedName("regular")
	val regular: String? = null,

	@field:SerializedName("full")
	val full: String? = null
)






data class Social(

	@field:SerializedName("twitter_username")
	val twitterUsername: String? = null,

	@field:SerializedName("instagram_username")
	val instagramUsername: Any? = null,

	@field:SerializedName("portfolio_url")
	val portfolioUrl: String? = null
)



data class ProfileImage(

	@field:SerializedName("small")
	val small: String? = null,

	@field:SerializedName("large")
	val large: String? = null,

	@field:SerializedName("medium")
	val medium: String? = null
)


data class User(
	@field:SerializedName("twitter_username")
	val twitterUsername: String? = null,

	@field:SerializedName("last_name")
	val lastName: String? = null,

	@field:SerializedName("bio")
	val bio: String? = null,

	@field:SerializedName("total_likes")
	val totalLikes: Int? = null,

	@field:SerializedName("portfolio_url")
	val portfolioUrl: String? = null,

	@field:SerializedName("profile_image")
	val profileImage: ProfileImage? = null,



	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("location")
	val location: Any? = null,

	@field:SerializedName("total_collections")
	val totalCollections: Int? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("first_name")
	val firstName: String? = null,

	@field:SerializedName("instagram_username")
	val instagramUsername: Any? = null,

	@field:SerializedName("username")
	val username: String? = null
)


data class PhotosResponseItem(


	@field:SerializedName("current_user_collections")
	val currentUserCollections: List<Any?>? = null,

	@field:SerializedName("color")
	val color: String? = null,

	@field:SerializedName("sponsorship")
	val sponsorship: Any? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("liked_by_user")
	val likedByUser: Boolean? = null,


	@field:SerializedName("urls")
	val urls: Urls? = null,

	@field:SerializedName("alt_description")
	val altDescription: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("width")
	val width: Int? = null,

	@field:SerializedName("blur_hash")
	val blurHash: String? = null,

	@field:SerializedName("links")
	val links: Links? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("categories")
	val categories: List<Any?>? = null,

	@field:SerializedName("promoted_at")
	val promotedAt: String? = null,

	@field:SerializedName("user")
	val user: User? = null,

	@field:SerializedName("height")
	val height: Int? = null,

	@field:SerializedName("likes")
	val likes: Int? = null
)
