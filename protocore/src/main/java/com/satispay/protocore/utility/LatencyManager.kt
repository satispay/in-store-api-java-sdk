package com.satispay.protocore.utility

/*
 *  Sliding time window with 10 values
 *  15 seconds as the maximum timeout
 *  When the average of the samples exceeds 12.75 (calculated as 15 * 0.85) then the connection is slow
 */

// Login + First shops page
// network 56k -> 3612ms,7264ms,10704ms,20353ms,22792ms,15884ms,13894ms,17033ms,19374ms,22854ms,15490ms,16890ms,17787ms
// network 128k -> 765ms,1196ms,1939ms,1822ms,1285ms,950ms,1022ms,1149ms,1260ms,905ms,743ms,977ms,871ms,
// network -> 512kb -> 200ms,396ms,304ms,414ms,322ms,307ms,474ms,892ms,1159ms,820ms,639ms,926ms,813ms

class LatencyManager(private val window: CircularQueue<Long> = CircularQueue(10)) {

    private val maxAverageValue = MAX_LATENCY * 0.85 // 1083.75ms

    fun addEvent(event: Long):Int {
        synchronized(this){
            window.add(event)
            return window.size
        }
    }

    /***
    Return a pair with isSlowConnection and teh value of teh average (on last 10 queue)
     */
    fun isSlowConnectionWithAverage(): Pair<Double,Boolean> {
        synchronized(this){
            val average=window.average()
            return Pair(average,average > maxAverageValue)
        }
    }

    companion object{
        private const val MAX_LATENCY = 1275 // iOS: 12750 // magic number: 15 * 0.85 = 12,7
    }
}