package uk.co.bbc.introtocompose

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uk.co.bbc.introtocompose.components.InputField
import uk.co.bbc.introtocompose.ui.theme.IntroToComposeTheme
import uk.co.bbc.introtocompose.util.calculateTotalPerPerson
import uk.co.bbc.introtocompose.util.calculateTotalTip
import uk.co.bbc.introtocompose.widgets.RoundIconButton
import kotlin.math.absoluteValue

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            IntroToComposeTheme {
                SplitBillApp {

                }
            }
        }
    }
}

@Composable
fun SplitBillApp(content: @Composable () -> Unit) {

    IntroToComposeTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                MainContent()
            }
        }
    }

}


@Composable
fun MainContent() {

    val tipAmount = remember {
        mutableDoubleStateOf(0.0)
    }

    val totalPerPersonValueState = remember {
        mutableDoubleStateOf(0.0)
    }

    val numberOfPeople = remember {
        mutableIntStateOf(1)
    }
    Column(modifier = Modifier.padding(12.dp)) {
        BillForm(
            splitByState = numberOfPeople,
            tipAmountState = tipAmount,
            totalPerPerson = totalPerPersonValueState
        )
    }

}

@Composable
fun BillForm(
    modifier: Modifier = Modifier,
    range: IntRange = 1..100,
    splitByState: MutableIntState,
    tipAmountState: MutableState<Double>,
    totalPerPerson: MutableState<Double>,
    onValueChange: (String) -> Unit = {},
    onPeopleChange: (Int) -> Int = { it }
) {

    val totalBillState = remember {
        mutableStateOf("")
    }
    val validState = remember(totalBillState.value) {
        totalBillState.value.trim().isNotEmpty()
    }

    val keyboardController = LocalSoftwareKeyboardController.current

    val sliderPosition = remember {
        mutableFloatStateOf(0f)
    }
    val tipPercentage =  (sliderPosition.floatValue * 100).toInt()



    TopHeader(totalPerPerson = totalPerPerson.value)
    Surface(
        modifier = modifier
            .padding(top = 40.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(corner = CornerSize(8.dp)),
        border = BorderStroke(width = 1.dp, color = Color.LightGray)
    ) {
        Column(
            modifier = Modifier.padding(6.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            InputField(valueState = totalBillState,
                labelId = "Enter Bill",
                enabled = true,
                isSingleLine = true,
                onAction = KeyboardActions {
                    if (!validState) return@KeyboardActions
                    onValueChange(totalBillState.value.trim())
                    keyboardController?.show()
                })
//            if (validState) {
            Row(
                modifier = modifier.padding(3.dp), horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    "Split", modifier = modifier.align(
                        alignment = Alignment.CenterVertically
                    )
                )
                Spacer(modifier = Modifier.width(120.dp))
                Row(
                    modifier = Modifier.padding(horizontal = 3.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    RoundIconButton(modifier = modifier,
                        imageVector = Icons.Default.Delete,
                        onClick = {
                            onPeopleChange(minNumberOfPeople(splitByState))
                            totalPerPerson.value = calculateTotalPerPerson(totalBill = totalBillState.value.toDouble(), splitBillBy = splitByState.intValue, tipPercentage = tipPercentage)
                        }
                    )

                    (Text(
                        splitByState.value .toString(), modifier = Modifier
                            .padding(start = 9.dp, end = 9.dp)
                            .align(Alignment.CenterVertically)
                    ))

                    RoundIconButton(
                        modifier = modifier,
                        imageVector = Icons.Default.Add,
                        onClick = {
                            onPeopleChange(splitByState.intValue++)
                            totalPerPerson.value = calculateTotalPerPerson(totalBill = totalBillState.value.toDouble(), splitBillBy = splitByState.intValue, tipPercentage = tipPercentage)
                        }
                    )

                }
            }
            // Tip Row
            Row(
                modifier = Modifier.padding(horizontal = 3.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    "Tip", modifier = Modifier.align(
                        alignment = Alignment.CenterVertically
                    )
                )
                Spacer(modifier = Modifier.width(200.dp))
                Text("£ ${tipAmountState.value}", modifier = Modifier.align(
                        alignment = Alignment.CenterVertically
                    )
                )
            }
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("$tipPercentage%")
                Spacer(modifier = Modifier.height(10.dp))

                //Slider
                Slider(
                    value = sliderPosition.floatValue,
                    steps = 50,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp),
                    onValueChange = { newValue ->
                        sliderPosition.floatValue = newValue
                        Log.i("SLIDER", "Slider Value: ${sliderPosition.floatValue}")
                        Log.i("SLIDER", "Total Bill: ${totalBillState.value}")
                        Log.i("SLIDER", "TiP Percentage: $tipPercentage")
                        Log.i("SLIDER", "TiP Amount: ${tipAmountState.value}")

                        tipAmountState.value =
                            calculateTotalTip(totalBill = totalBillState.value.toDouble(), tipPercentage = tipPercentage)

                        totalPerPerson.value = calculateTotalPerPerson(totalBill = totalBillState.value.toDouble(), splitBillBy = splitByState.intValue, tipPercentage = tipPercentage)
                    },


                    )
            }


//            } else {
//                Box { }
//            }
        }

    }

}

@Composable
fun TopHeader(totalPerPerson: Double = 122.0) {
    Surface(
        modifier = Modifier
            .padding(top = 60.dp)
            .fillMaxWidth()
            .height(150.dp)
            .clip(shape = RoundedCornerShape(corner = CornerSize(33.dp))),
        color = Color(0x3C00BCD4)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val total = "%.2f".format(totalPerPerson)
            Text(
                text = "Total Per Person",
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = "£$total",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}


@Composable
fun MyApp() {
    val age = remember {
        mutableIntStateOf(value = 0)
    }

    Scaffold(
        containerColor = Color.Yellow,
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) { innerPadding ->
        Column(

            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
        ) {
            TopHeader()
            Greeting(
                name = "Android"
            )
            ShowAge(age = age.intValue)
            CreateCircle(age = age.intValue) { newAge ->
                age.intValue = newAge
            }
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun CreateCircle(age: Int = 0, updateAge: (Int) -> Unit) {
    Card(
        border = BorderStroke(5.dp, Color.Yellow),
        elevation = CardDefaults.elevatedCardElevation(),
        shape = CircleShape,
        modifier = Modifier
            .padding(5.dp)
            .size(100.dp)
            .clickable {
                updateAge(age + 1)
                Log.d("TAG", "Add age: ${age.absoluteValue}")
            }
    ) {
        Box(
            contentAlignment = Alignment.CenterStart,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = "Tap",
                textAlign = TextAlign.Center,
                fontStyle = FontStyle.Normal,
                fontSize = 33.sp,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

fun minNumberOfPeople(people: MutableIntState): Int {
    if (people.intValue > 1) people.intValue--
    return people.intValue
}

@Composable
fun ShowAge(age: Int = 12) {
    Text(
        text = age.toString(),
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(5.dp),
        fontWeight = FontWeight.Bold,
        fontSize = 36.sp
    )
}

@Composable
fun Greeting(name: String = "Gregorio") {
    Text(
        text = "Hello $name!",
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(5.dp)
    )
}

@Composable
fun BillPreview() {
    SplitBillApp {}
}

@Composable
fun GreetingPreview() {
    MyApp()
}