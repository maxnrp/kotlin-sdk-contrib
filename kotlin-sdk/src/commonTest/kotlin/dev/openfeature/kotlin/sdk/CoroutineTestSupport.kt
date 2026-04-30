package dev.openfeature.kotlin.sdk

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

/**
 * A [kotlinx.coroutines.test.runTest] body uses a [kotlinx.coroutines.test.StandardTestDispatcher] that does
 * not run tasks scheduled on [Dispatchers.Default]. The shared [OpenFeatureAPI.observe] upstream collects on
 * Default, so tests that collect that flow should call this after virtual-time advancement (e.g.
 * [kotlinx.coroutines.test.TestCoroutineScheduler.advanceUntilIdle]) so emissions reach collectors.
 *
 * Uses a short real-time [delay] on the Default scheduler so pending collectors reliably run across JVM/JS.
 *
 * @param durationMs How long to stay on Default; increase if a test flakes under slow CI or unusually heavy work.
 */
suspend fun flushDispatchersDefault(durationMs: Long = 20L) {
    require(durationMs > 0L) { "durationMs must be positive" }
    withContext(Dispatchers.Default) {
        delay(durationMs)
    }
}
