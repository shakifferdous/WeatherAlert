package com.shakif.weatheralertapp.realm

import android.content.Context
import com.shakif.weatheralertapp.utility.StringConstants.REALM_NAME
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmModel

object DBManager {

    private const val schemaVersion: Long = 1

    fun init(context: Context) {
        Realm.init(context)
    }

    fun getRealm(): Realm {
        val realmConfig = RealmConfiguration.Builder()
            .name("$REALM_NAME.realm")
            .schemaVersion(schemaVersion)
            .allowWritesOnUiThread(true)
            .migration(DBMigration())
            .build()

        Realm.setDefaultConfiguration(realmConfig)
        return Realm.getDefaultInstance()
    }

    fun <T : RealmModel?> save(value: T) {
        getRealm().executeTransaction {
            it.copyToRealm(value)
        }
    }

    fun <T : RealmModel?> get(cls: Class<T>): T? {
        return getRealm().where(cls).findFirst()
    }

    fun <T : RealmModel?> deleteAll(cls: Class<T>) {
        getRealm().executeTransaction {
            it.delete(cls)
        }
    }
}