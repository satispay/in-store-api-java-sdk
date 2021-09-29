package com.satispay.protocore.utility

import java.util.*

class CircularQueue<T>(private val capacity: Int = 10): LinkedList<T>() {

    override fun add(element: T): Boolean {
        if(size >= capacity)
            removeFirst()
        return super.add(element)
    }
}