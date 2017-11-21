package com.github.jlcarveth.grocer.storage

import android.util.Log

/**
 * Created by John on 11/10/2017.
 *
 * Our DatabaseSubject class. This class has listeners
 * and tells them when the database has changed and that they should update their
 * data.
 */
val TAG = "DatabaseSubject"

class DatabaseSubject {
    companion object {
        private val observers = ArrayList<DatabaseObserver>()

        fun attach(observer : DatabaseObserver) {
            observers.add(observer)
        }

        fun detach(observer : DatabaseObserver) {
            observers.remove(observer)
        }

        fun notifyObservers() {
            val it = observers.iterator()

            Log.d(TAG, "Notifying Observers {${observers.size}}")

            while (it.hasNext()) {
                it.next().update()
            }
        }
    }


}