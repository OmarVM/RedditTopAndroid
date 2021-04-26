package com.example.reddittop

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.reddittop.data.model.TokenResponse
import com.example.reddittop.data.network.token.RequestTokenService
import com.example.reddittop.usecase.GetAccessTokenUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Matchers.any
import org.mockito.Mockito
import org.mockito.runners.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetAccessTokenUseCaseTest {

    private val dependencyServiceRemote: RequestTokenService = Mockito.mock(RequestTokenService::class.java)
    private val sut = GetAccessTokenUseCase(dependencyServiceRemote)

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @Test
    fun successValidToken() = runBlockingTest{
        val request =  TokenResponse("123", "bearer","311722b6", 3600, "read")
        Mockito.`when`(dependencyServiceRemote.requestToken(any(), any())).thenReturn(request)
        val result = sut.getToken()
        result.collect {
            assert(it == "311722b6" )
        }
    }
}