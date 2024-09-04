package com.looker.kenko.data.local.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.looker.kenko.data.model.Session
import com.looker.kenko.data.model.Set
import kotlinx.datetime.LocalDate

@Entity(
    "sessions",
    foreignKeys = [
        ForeignKey(
            entity = PlanEntity::class,
            parentColumns = ["id"],
            childColumns = ["planId"],
            onDelete = ForeignKey.CASCADE,
        )
    ]
)
data class SessionEntity(
    val date: LocalDate,
    val sets: List<SetEntity>,
    val planId: Int,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)

fun SessionEntity.toExternal(): Session = Session(
    date = date,
    sets = sets.map(SetEntity::toExternal),
    id = id,
)

fun Session.toEntity(planId: Int): SessionEntity = SessionEntity(
    date = date,
    sets = sets.map(Set::toEntity),
    planId = planId,
    id = id ?: 0,
)
