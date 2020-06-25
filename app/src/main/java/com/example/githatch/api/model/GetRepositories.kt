import com.example.githatch.api.model.Repository
import com.google.gson.annotations.SerializedName

class GetRepositories(
    @SerializedName("total_count") val totalCount: Int,
    @SerializedName("incomplete_results") val incompleteResults: Boolean,
    @SerializedName("items") val repos: List<Repository>
)