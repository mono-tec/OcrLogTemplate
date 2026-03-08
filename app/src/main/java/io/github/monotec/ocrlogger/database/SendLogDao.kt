package io.github.monotec.ocrlogger.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.github.monotec.ocrlogger.model.SendLogEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface SendLogDao {

    @Query("SELECT * FROM send_log_entry ORDER BY timestampEpochMillis DESC")
    fun observeAll(): Flow<List<SendLogEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: SendLogEntry)

    @Query("DELETE FROM send_log_entry")
    suspend fun clearAll()
}