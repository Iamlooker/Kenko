package com.looker.kenko.data.local.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.looker.kenko.data.model.Session
import com.looker.kenko.data.model.Set
import kotlinx.datetime.LocalDate

data class SessionEntity(
    @Embedded
    val data: SessionDataEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "sessionId",
    )
    val sets: List<SetEntity>,
)

@Entity(
    "sessions",
    foreignKeys = [
        ForeignKey(
            entity = PlanEntity::class,
            parentColumns = ["id"],
            childColumns = ["planId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
)
data class SessionDataEntity(
    val date: LocalDate,
    val planId: Int,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
)

fun Session.data(planId: Int): SessionDataEntity = SessionDataEntity(
    date = date,
    planId = planId,
    id = id ?: 0,
)

fun Session.sets(): List<SetEntity> = sets.map { it.toEntity(id!!, sets.indexOf(it)) }

fun SessionEntity.toExternal(
    setsMap: List<Set>
): Session = Session(
    date = data.date,
    sets = setsMap,
    id = data.id,
)
