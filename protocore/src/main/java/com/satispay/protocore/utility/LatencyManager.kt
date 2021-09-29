package com.satispay.protocore.utility

/*
finestra di 10 campionamenti
15 secondi come timeout massimo
quando la media dei campionamenti supera i 12.75 (calcolato come 15 * 0.85) allora la connessione è lenta
 */

// Login + First shops page
// network 56k -> 3612ms,7264ms,10704ms,20353ms,22792ms,15884ms,13894ms,17033ms,19374ms,22854ms,15490ms,16890ms,17787ms
// network 128k -> 765ms,1196ms,1939ms,1822ms,1285ms,950ms,1022ms,1149ms,1260ms,905ms,743ms,977ms,871ms,
// network -> 512kb -> 200ms,396ms,304ms,414ms,322ms,307ms,474ms,892ms,1159ms,820ms,639ms,926ms,813ms

class LatencyManager(private val window: CircularQueue<Long> = CircularQueue(10)) {

    private val maxAverageValue = MAX_LATENCY * 0.85 // 1083.75ms

    fun addEvent(event: Long){
        window.add(event)
    }

    fun isSlowConnection() =
        window.average() > maxAverageValue

    companion object{
        private const val MAX_LATENCY = 1275 // iOS: 12750 // magic number: 15 * 0.85 = 12,7
    }
}