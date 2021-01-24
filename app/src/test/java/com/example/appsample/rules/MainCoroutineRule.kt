package com.example.appsample.rules

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.extension.Extension

@ExperimentalCoroutinesApi
class MainCoroutineRule(
    val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()
) : Extension {

    @BeforeAll
    fun beforeAll() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterAll
    fun afterAll() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }
}