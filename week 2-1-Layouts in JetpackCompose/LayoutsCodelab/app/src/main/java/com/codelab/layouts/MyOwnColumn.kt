package com.codelab.layouts

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.codelab.layouts.ui.LayoutsCodelabTheme

/*
전형적인 커스텀 레이아웃의 구조.

@Composable
fun CustomLayout(
    modifier: Modifier = Modifier,
    // custom layout attributes
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->
        // measure and position children given constraints logic here
    }
}
*/

@Composable
fun MyOwnColumn(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->

        val placeables = measurables.map { measurable ->
            measurable.measure(constraints)
        }

        var yPosition = 0

        layout(constraints.maxWidth, constraints.maxHeight) {
            placeables.forEach { placeable ->
                placeable.placeRelative(x = 0, y = yPosition)
                yPosition += placeable.height
            }
        }
    }
}

@Preview
@Composable
fun MyOwnColumnPreview() {
    LayoutsCodelabTheme {
        Surface(Modifier.padding(8.dp)) {
            MyOwnColumn {
                Text("MyOwnColumn")
                Text("places items test")
                Text("vertically.")
                Text("We've done it by hand!")
            }
        }
    }
}
