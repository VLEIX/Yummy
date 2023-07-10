package com.frantun.yummy.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.frantun.yummy.data.local.entity.OriginEntity

@Dao
interface OriginDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrigin(origin: OriginEntity)
}
