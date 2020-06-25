
import android.content.Context
import com.example.githatch.App
import com.example.githatch.api.model.Author
import retrofit2.Callback
import retrofit2.Response

class RepositoryPresenterImplementation(private var context: Context) : RepositoryPresenter {

    private var repoView = context as RepositoryView

    override fun getContributors(ownerName: String, repoName: String) {

        App.apiService.getContributors(ownerName, repoName).enqueue(object: Callback<List<Author>> {
            override fun onFailure(call: retrofit2.Call<List<Author>>, t: Throwable) {
                repoView.showProgressBar()
            }

            override fun onResponse(call: retrofit2.Call<List<Author>>, response: Response<List<Author>>) {
                if (response.body() != null) {
                    repoView.attachContributors(response.body()!!)
                    repoView.hideProgressBar()
                }
            }

        })

    }

}
