package com.shakif.weatheralertapp.realm

import io.realm.DynamicRealm
import io.realm.RealmMigration

class DBMigration : RealmMigration {
    override fun equals(other: Any?): Boolean {
        return other != null && other is DBMigration
    }

    override fun hashCode(): Int {
        return DBMigration::class.java.hashCode()
    }

    override fun migrate(realm: DynamicRealm, oldVersion: Long, newVersion: Long) {
        val schema = realm.schema
        var existingVersion: Int = oldVersion.toInt()
    }
}