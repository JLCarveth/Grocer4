package com.github.jlcarveth.grocer.storage

/**
 * Created by John on 11/13/2017.
 *
 * Interface for our databse observer.
 */
interface DatabaseObserver {
    /**
    * Called when the Database has updated
    */
    fun update()
}