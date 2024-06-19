package net.pettip.app.navi.utils.module

import android.content.Context
import androidx.camera.lifecycle.ProcessCameraProvider
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.NaverMapOptions
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.pettip.app.navi.datamodel.repository.LoginRepository
import net.pettip.app.navi.utils.service.ApiService
import net.pettip.app.navi.utils.service.ExternalApiService
import net.pettip.app.navi.utils.singleton.RetrofitClientServer
import javax.inject.Singleton

/**
 * @Project     : PetTip-Android
 * @FileName    : AppModule
 * @Date        : 2024-05-14
 * @author      : CareBiz
 * @description : net.pettip.app.navi.utils.module
 * @see net.pettip.app.navi.utils.module.AppModule
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofitClientServer(): ApiService {
        return RetrofitClientServer.instance
    }

    @Provides
    @Singleton
    fun provideApiService(): ExternalApiService {
        return RetrofitClientServer.naverInstance
    }

    @Provides
    @Singleton
    fun provideLoginRepository(apiService: ApiService): LoginRepository {
        return LoginRepository(apiService)
    }

}