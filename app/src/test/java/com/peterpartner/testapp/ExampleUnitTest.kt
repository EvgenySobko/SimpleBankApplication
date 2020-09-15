package com.peterpartner.testapp

import com.nhaarman.mockitokotlin2.whenever
import com.peterpartner.testapp.api.ApiFactoryCurrency
import com.peterpartner.testapp.api.ApiFactoryUsers
import com.peterpartner.testapp.utils.logd
import kotlinx.coroutines.*
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import java.util.concurrent.Executor

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(MockitoJUnitRunner::class)
class ExampleUnitTest {

    @Test
    fun doesApiHoldersAvailable() =
        runBlockingTest {
            runBlocking { ApiFactoryUsers.service.getHolders() }
        }

    @Test
    fun doesApiCurrencyAvailable() =
        runBlockingTest {
            runBlocking { ApiFactoryCurrency.service.getCurrency() }
        }


    /* TODO more tests, these one just for example */
}