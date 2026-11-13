package com.mindera.alfie.data.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * Brand recent searches saved before ALFMOB-407 hold a mock-server slug in `slug`, which the BFF
 * can't resolve as a brand key, so they would open an empty listing. Only those rows go: the
 * schema is unchanged, and an explicit migration keeps the wishlist and query recent searches,
 * which the destructive fallback would wipe.
 */
internal val MIGRATION_4_5 = object : Migration(4, 5) {

    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("DELETE FROM recent_searches WHERE type = 'BRAND'")
    }
}
