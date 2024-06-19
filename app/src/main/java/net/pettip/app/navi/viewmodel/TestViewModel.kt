package net.pettip.app.navi.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import net.pettip.app.navi.datamodel.data.test.RTStoryListRes
import net.pettip.app.navi.utils.service.ApiService
import net.pettip.app.navi.utils.service.ExternalApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import kotlin.coroutines.resume

/**
 * @Project     : PetTip-Android
 * @FileName    : TestViewModel
 * @Date        : 2024-06-14
 * @author      : CareBiz
 * @description : net.pettip.app.navi.viewmodel
 * @see net.pettip.app.navi.viewmodel.TestViewModel
 */
@HiltViewModel
class TestViewModel @Inject constructor(
    private val apiService: ApiService
) : ViewModel() {

    private val _rtStoryList = MutableStateFlow<RTStoryListRes?>(null)
    val rtStoryList: StateFlow<RTStoryListRes?> = _rtStoryList.asStateFlow()

    suspend fun getRTStoryList():Boolean{
        val call = apiService.getRealTimeList()
        return suspendCancellableCoroutine { continuation ->
            call.enqueue(object : Callback<RTStoryListRes> {
                override fun onResponse(call: Call<RTStoryListRes>, response: Response<RTStoryListRes>) {
                    if (response.isSuccessful){
                        val body = response.body()
                        body?.let {
                            _rtStoryList.value = it
                            continuation.resume(true)
                        }
                    }else{
                        continuation.resume(false)
                    }
                }

                override fun onFailure(call: Call<RTStoryListRes>, t: Throwable) {
                    continuation.resume(false)
                }

            })
        }
    }
}