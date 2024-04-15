package com.looker.kenko.ui.sessionDetail.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.looker.kenko.ui.sessionDetail.SessionDetails
import kotlinx.datetime.LocalDate

const val SESSION_DETAIL_ROUTE = "session_detail"

const val ARG_SESSION_ID = "epoch_days"

fun NavController.navigateToSessionDetail(date: LocalDate?, navOptions: NavOptions? = null) {
    navigate("$SESSION_DETAIL_ROUTE?noteId=${date?.toEpochDays() ?: -1}", navOptions)
}

fun NavGraphBuilder.sessionDetail(
    onBackPress: () -> Unit,
) {
    composable(
        route = "$SESSION_DETAIL_ROUTE?noteId={$ARG_SESSION_ID}",
        arguments = listOf(
            navArgument(name = ARG_SESSION_ID) {
                type = NavType.IntType
                defaultValue = -1
            }
        )
    ) {
        SessionDetails(onBackPress)
    }
}