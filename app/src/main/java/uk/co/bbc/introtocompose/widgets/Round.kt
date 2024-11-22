package uk.co.bbc.introtocompose.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


// OnClick btn we want to animate the size and make it bigger
val IconButtonModifier: Modifier = Modifier.size(4.dp)

@Composable
fun RoundIconButton(
    modifier: Modifier,
    imageVector: ImageVector,
    onClick: () -> Unit,
    tint: Color = Color.Black.copy(alpha = 0.8F),
    backgroundColor: Color = MaterialTheme.colorScheme.background,
    elevation: Dp = 4.dp) {

    Card(modifier = modifier.padding(all = 4.dp).size(20.dp)
        .clickable { onClick.invoke() }.then(IconButtonModifier),
        shape = CircleShape,
        colors = CardDefaults.cardColors(backgroundColor),
        elevation = CardDefaults.cardElevation(elevation)
    ) {
        Icon(imageVector = imageVector,
            contentDescription = "Plus or Minus icon",
            tint = tint)
    }

}



