package com.looker.kenko.data.local.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.looker.kenko.data.model.Plan
import com.looker.kenko.data.model.Session
import kotlinx.datetime.LocalDate
import com.looker.kenko.data.model.Set

@Entity(
    "sessions",
    foreignKeys = [
        ForeignKey(
            entity = Plan::class,
            parentColumns = ["id"],
            childColumns = ["planId"],
            onDelete = ForeignKey.CASCADE,
        )
    ]
)
data class SessionEntity(
    val date: LocalDate,
    val sets: List<SetEntity>,
    val planId: Long,
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
)

fun SessionEntity.toExternal(): Session = Session(
    date = date,
    sets = sets.map(SetEntity::toExternal),
)

fun Session.toEntity(): SessionEntity = SessionEntity(
    date = date,
    sets = sets.map(Set::toEntity),
    // TODO: Fix in future
    planId = 1L,
)
