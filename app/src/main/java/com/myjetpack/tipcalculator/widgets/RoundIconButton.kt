package com.myjetpack.tipcalculator.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

val IconButtonSizeModifier = Modifier.size(40.dp)
@Composable
fun RoundIconButton(
    modifier: Modifier,
    imageVector: ImageVector,
    onClick:()->Unit,
    tint: Color=Color.Black.copy(alpha = 0.8f),
    backGroundColor: Color = MaterialTheme.colors.background,
    elevation: Dp = 4.dp,
){
    Card(modifier = modifier
        .padding(4.dp)
        .clickable {
            onClick.invoke()
        }
        .then(IconButtonSizeModifier),
    shape = CircleShape,
    backgroundColor = backGroundColor,
    elevation = elevation,
    ) {
        Icon(imageVector = imageVector,
            contentDescription = "Plus or minus",
            tint = tint)
    }
}