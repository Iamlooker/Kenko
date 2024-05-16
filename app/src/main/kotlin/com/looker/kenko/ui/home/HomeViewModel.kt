package com.looker.kenko.ui.home

import androidx.lifecycle.ViewModel
import com.looker.kenko.data.repository.PlanRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    planRepo: PlanRepo
) : ViewModel() {

}