package com.looker.kenko.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDate

@Entity(
    tableName = "plan_history",
    foreignKeys = [
        ForeignKey(
            entity = PlanEntity::class,
            parentColumns = ["id"],
            childColumns = ["planId"],
            onDelete = ForeignKey.Companion.SET_NULL
        )
    ],
    indices = [
        Index("planId", "start", "end")
    ]
)
data class PlanHistoryEntity(
    val planId: Int?,
    val start: LocalDate,
    @ColumnInfo(defaultValue = "NULL")
    val end: LocalDate? = null,
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0
)
