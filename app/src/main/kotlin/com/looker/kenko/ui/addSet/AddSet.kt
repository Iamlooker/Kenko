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

package com.looker.kenko.ui.addSet

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonGroupDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedToggleButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.ToggleButtonDefaults
import androidx.compose.material3.toPath
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.graphics.shapes.Morph
import androidx.graphics.shapes.RoundedPolygon
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.looker.kenko.R
import com.looker.kenko.data.local.model.SetType
import com.looker.kenko.data.model.Exercise
import com.looker.kenko.data.model.repDurationStringRes
import com.looker.kenko.ui.addSet.AddSetViewModel.FloatTransformation
import com.looker.kenko.ui.addSet.AddSetViewModel.IntTransformation
import com.looker.kenko.ui.addSet.components.DraggableTextField
import com.looker.kenko.ui.addSet.components.rememberDraggableTextFieldState
import com.looker.kenko.ui.theme.KenkoIcons
import kotlinx.coroutines.launch

private val incrementButtonModifier = Modifier
    .height(48.dp)
    .zIndex(0f)

private val zIndexModifier = Modifier.zIndex(1F)

@Composable
fun AddSet(exercise: Exercise, onDone: () -> Unit) {
    val viewModel: AddSetViewModel =
        hiltViewModel<AddSetViewModel, AddSetViewModel.AddSetViewModelFactory>(key = exercise.name) {
            it.create(exercise.id!!)
        }
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .wrapContentHeight(),
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        AddSetHeader(
            modifier = Modifier.fillMaxWidth(),
            exerciseName = exercise.name,
            onClick = {
                viewModel.addSet()
                onDone()
            },
        )

        Spacer(modifier = Modifier.height(16.dp))

        SetTypeSelector(
            modifier = Modifier.align(CenterHorizontally),
            selected = viewModel.selectedSetType,
            onSelect = viewModel::setSetType,
        )

        Spacer(modifier = Modifier.height(16.dp))

        SwipeableTextField(
            modifier = Modifier.align(CenterHorizontally),
        ) {
            TextButton(
                modifier = incrementButtonModifier,
                onClick = { viewModel.addRep(-1) },
            ) {
                Text(text = stringResource(R.string.label_minus_int, 1))
            }
            val reps = rememberDraggableTextFieldState(viewModel.repsBoundReached)
            DraggableTextField(
                dragState = reps,
                textFieldState = viewModel.reps,
                inputTransformation = IntTransformation,
                supportingText = stringResource(exercise.repDurationStringRes),
                modifier = zIndexModifier,
            )
            TextButton(
                modifier = incrementButtonModifier,
                onClick = { viewModel.addRep(1) },
            ) {
                Text(text = stringResource(R.string.label_plus_int, 1))
            }
            TextButton(
                modifier = incrementButtonModifier,
                onClick = { viewModel.addRep(5) },
            ) {
                Text(text = stringResource(R.string.label_plus_int, 5))
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        SwipeableTextField(
            modifier = Modifier.align(CenterHorizontally),
        ) {
            TextButton(
                modifier = incrementButtonModifier,
                onClick = { viewModel.addWeight(-1F) },
            ) {
                Text(text = stringResource(R.string.label_minus_int, 1F))
            }
            val weights = rememberDraggableTextFieldState(viewModel.weightsBoundReached)
            DraggableTextField(
                dragState = weights,
                textFieldState = viewModel.weights,
                supportingText = stringResource(R.string.label_weight),
                inputTransformation = FloatTransformation,
                modifier = zIndexModifier,
            )
            TextButton(
                modifier = incrementButtonModifier,
                onClick = { viewModel.addWeight(1F) },
            ) {
                Text(text = stringResource(R.string.label_plus_int, 1F))
            }
            TextButton(
                modifier = incrementButtonModifier,
                onClick = { viewModel.addWeight(5F) },
            ) {
                Text(text = stringResource(R.string.label_plus_int, 5F))
            }
        }
        Spacer(modifier = Modifier.height(36.dp))
    }
}

@Composable
private fun AddSetHeader(
    exerciseName: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1F)) {
            Text(
                text = stringResource(R.string.label_add_set_for).uppercase(),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.outline,
            )
            Text(
                text = exerciseName,
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.tertiary,
            )
        }
        FilledTonalIconButton(onClick = onClick) {
            Icon(
                painter = KenkoIcons.Done,
                contentDescription = "",
            )
        }
    }
}

@Composable
private fun SwipeableTextField(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    Surface(
        modifier = modifier.requiredHeight(48.dp),
        shape = CircleShape,
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            content = content,
        )
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun SetTypeSelector(
    selected: SetType,
    onSelect: (SetType) -> Unit,
    modifier: Modifier = Modifier,
) {
    val options = listOf(SetType.Standard, SetType.Drop, SetType.RestPause)
    Row(
        modifier.padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(ButtonGroupDefaults.ConnectedSpaceBetween),
    ) {
        options.forEachIndexed { index, type ->
            val interactionSource = remember { MutableInteractionSource() }
            val checked = selected == type
            OutlinedToggleButton(
                checked = checked,
                onCheckedChange = { onSelect(type) },
                interactionSource = interactionSource,
                modifier = Modifier.semantics { role = Role.RadioButton },
                border = if (checked) ButtonDefaults.outlinedButtonBorder(true) else null,
                colors = ToggleButtonDefaults.outlinedToggleButtonColors(
                    checkedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                    checkedContentColor = MaterialTheme.colorScheme.onSurface,
                ),
                shapes =
                    when (index) {
                        0 -> ButtonGroupDefaults.connectedLeadingButtonShapes()
                        options.lastIndex -> ButtonGroupDefaults.connectedTrailingButtonShapes()
                        else -> ButtonGroupDefaults.connectedMiddleButtonShapes()
                    },
            ) {
                SetTypeIndicator(
                    selected = checked,
                    type = type,
                    interactionSource = interactionSource,
                    modifier = Modifier.size(12.dp),
                )
                Spacer(Modifier.size(ToggleButtonDefaults.IconSpacing))
                Text(text = setTypeLabel(type))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun SetTypeIndicator(
    selected: Boolean,
    type: SetType,
    interactionSource: MutableInteractionSource,
    modifier: Modifier = Modifier,
) {
    val isPressed by interactionSource.collectIsPressedAsState()
    val morphAnimatable = remember { Animatable(0F) }
    val morph = remember { Morph(MaterialShapes.Circle, setTypeShape(type)) }
    val path = remember { Path() }

    LaunchedEffect(isPressed || selected) {
        launch {
            if (isPressed || selected) {
                morphAnimatable.animateTo(1F)
            } else {
                morphAnimatable.animateTo(0F)
            }
        }
    }

    Canvas(modifier) {
        drawPath(
            color = setTypeColor(type),
            path = processPath(
                path = morph.toPath(progress = morphAnimatable.value, path = path),
                size = size,
                scaleFactor = 1F,
            ),
        )
    }
}

private fun processPath(
    path: Path,
    size: Size,
    scaleFactor: Float,
    scaleMatrix: Matrix = Matrix(),
): Path {
    scaleMatrix.reset()

    scaleMatrix.apply { scale(x = size.width * scaleFactor, y = size.height * scaleFactor) }

    // Scale to the desired size.
    path.transform(scaleMatrix)

    // Translate the path to align its center with the available size center.
    path.translate(size.center - path.getBounds().center)
    return path
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
private fun setTypeShape(type: SetType): RoundedPolygon = when (type) {
    SetType.Standard -> MaterialShapes.Ghostish
    SetType.Drop -> MaterialShapes.Arrow
    SetType.RestPause -> MaterialShapes.Bun
}

private fun setTypeColor(type: SetType): Color = when (type) {
    SetType.Standard -> Color(0xFF2196F3) // Blue
    SetType.Drop -> Color(0xFFFFC107) // Amber/Yellow
    SetType.RestPause -> Color(0xFFFF7043) // Red/Orange (Deep Orange)
}

private fun setTypeLabel(type: SetType): String = when (type) {
    SetType.Standard -> "Standard"
    SetType.Drop -> "Drop"
    SetType.RestPause -> "Rest-Pause"
}
