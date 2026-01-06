/*
 * Copyright (C) 2025 LooKeR & Contributors
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.looker.kenko.data.model.settings

import androidx.annotation.StringRes
import com.looker.kenko.R

enum class BackupInterval(
    val hours: Long,
    @param:StringRes val nameRes: Int,
) {
    Off(0, R.string.label_backup_off),
    Daily(24, R.string.label_backup_daily),
    Weekly(168, R.string.label_backup_weekly),
    Monthly(720, R.string.label_backup_monthly),
}
