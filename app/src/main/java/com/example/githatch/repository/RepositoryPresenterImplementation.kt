import android.content.Context
import android.util.Log
import com.example.githatch.App
import com.example.githatch.api.ApiService
import com.example.githatch.api.model.Author
import com.example.githatch.api.model.RetrofitFactory
import kotlinx.coroutines.*
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class RepositoryPresenterImplementation(private var context: Context) : RepositoryPresenter {

    private var repoView = context as RepositoryView

    override fun getContributors(ownerName: String, repoName: String) {
        val parentJob = Job()
        val coroutineScope = CoroutineScope(Dispatchers.IO + parentJob)
        val service = RetrofitFactory.makeRetrofitService()

        coroutineScope.launch(Dispatchers.IO) {
            val response = service.getContributors(ownerName, repoName)
            try {
                withContext(Dispatchers.Main) {
                    if (response.isNotEmpty()) {
                        repoView.attachContributors(response)
                        repoView.hideProgressBar()
                    } else {
                        repoView.hideProgressBar()
                    }
                }
            } catch (e: HttpException) {
                Log.e("REQUEST", "Exception ${e.message}")
            } catch (e: Throwable) {
                Log.e("REQUEST", "Ooops: Something else went wrong")
            }
        }
    }
}
