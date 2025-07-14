package com.arkhe.rbi.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.arkhe.rbi.data.local.dao.UserDao

@Database(
    entities = [UserEntity::class],
    version = 1,
    exportSchema = false
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        const val DATABASE_NAME = "app_database"
    }
}