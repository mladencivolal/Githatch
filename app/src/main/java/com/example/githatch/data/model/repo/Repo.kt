package com.example.githatch.data.model.repo


import android.os.Parcel
import android.os.Parcelable
import androidx.room.PrimaryKey
import com.example.githatch.data.model.owner.Owner
import com.google.gson.annotations.SerializedName

data class Repo(
    @PrimaryKey
    @SerializedName("name") val name: String,
    @SerializedName("full_name") val fullName: String,
    @SerializedName("owner") val owner: Owner,
    @SerializedName("html_url") val htmlUrl: String,
    @SerializedName("description") val description: String?,
    @SerializedName("watchers_count") val watchersCount: Int,
    @SerializedName("forks_count") val forksCount: Int,
    @SerializedName("open_issues") val openIssues: Int,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String,
    @SerializedName("language") val language: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readParcelable<Owner>(Owner::class.java.classLoader)!!,
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString()
    )

    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(name)
        writeString(fullName)
        writeParcelable(owner, 0)
        writeString(htmlUrl)
        writeString(if (description == null || description.isEmpty()) "No Description" else description )
        writeInt(watchersCount)
        writeInt(forksCount)
        writeInt(openIssues)
        writeString(if(createdAt.length == 0) "N/A" else createdAt)
        writeString(if(updatedAt == null || updatedAt.isEmpty()) "N/A" else updatedAt)
        writeString(if(language == null || language.isEmpty()) "N/A" else language)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Repo> = object : Parcelable.Creator<Repo> {
            override fun createFromParcel(source: Parcel): Repo = Repo(source)
            override fun newArray(size: Int): Array<Repo?> = arrayOfNulls(size)
        }
    }
}
