package com.tomazcuber.kleanble.scan.data.util

import com.tomazcuber.kleanble.scan.domain.util.Clock

/**
 * A concrete implementation of the [Clock] interface that uses the standard system clock.
 */
internal class SystemClock : Clock {
    override fun currentTimeMillis(): Long = System.currentTimeMillis()
}
