package com.looker.kenko.ui.sessionDetail.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.looker.kenko.ui.sessionDetail.SessionDetails
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class SessionDetailRoute(
    val epochDays: Int,
)

fun NavController.navigateToSessionDetail(date: LocalDate?, navOptions: NavOptions? = null) {
    navigate(SessionDetailRoute(date?.toEpochDays() ?: -1), navOptions)
}

fun NavGraphBuilder.sessionDetail(
    onBackPress: () -> Unit,
) {
    composable<SessionDetailRoute> {
        SessionDetails(
            onBackPress = onBackPress,
            viewModel = hiltViewModel(),
        )
    }
}
