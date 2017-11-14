package com.github.jlcarveth.grocer.storage

/**
 * Created by John on 11/10/2017.
 *
 * Our DatabaseSubject class. This class has listeners
 * and tells them when the database has changed and that they should update their
 * data.
 */
class DatabaseSubject {
    companion object {
        private val observers = ArrayList<DatabaseObserver>()

        fun attach(observer : DatabaseObserver) {
            observer
        }

        fun detach(observer : DatabaseObserver) {
            observers.remove(observer)
        }

        fun notifyObservers() {
            val it = observers.iterator()

            while (it.hasNext()) {
                it.next().update()
            }
        }
    }


}