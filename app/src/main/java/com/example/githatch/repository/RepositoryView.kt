import com.example.githatch.api.model.Author

interface RepositoryView {

    fun attachContributors(list: List<Author>)

    fun showProgressBar()

    fun hideProgressBar()

}