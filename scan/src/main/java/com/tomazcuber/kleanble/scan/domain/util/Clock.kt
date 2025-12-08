package com.tomazcuber.kleanble.scan.domain.util

/**
 * An abstraction for the system clock to facilitate testing.
 */
interface Clock {
    fun currentTimeMillis(): Long
}
