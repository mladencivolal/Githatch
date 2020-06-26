package com.example.githatch.api.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

//@Parcelize
data class Repository(
    @SerializedName("name") val name: String,
    @SerializedName("full_name") val fullName: String,
    @SerializedName("owner") val author: Author,
    @SerializedName("html_url") val htmlUrl: String,
    @SerializedName("description") val description: String,
    @SerializedName("watchers_count") val watchersCount: Int,
    @SerializedName("forks_count") val forksCount: Int,
    @SerializedName("open_issues") val openIssues: Int,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String,
    @SerializedName("language") val language: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readParcelable<Author>(Author::class.java.classLoader)!!,
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(name)
        writeString(fullName)
        writeParcelable(author, 0)
        writeString(htmlUrl)
        writeString(if (description == null || description.length == 0) "No Description" else description )
        writeInt(watchersCount)
        writeInt(forksCount)
        writeInt(openIssues)
        writeString(if(createdAt == null ||createdAt.length == 0) "N/A" else createdAt)
        writeString(if(updatedAt == null || updatedAt.length == 0) "N/A" else updatedAt)
        writeString(if(language == null || language.length == 0) "N/A" else language)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Repository> = object : Parcelable.Creator<Repository> {
            override fun createFromParcel(source: Parcel): Repository = Repository(source)
            override fun newArray(size: Int): Array<Repository?> = arrayOfNulls(size)
        }
    }
}

