package com.myjetpack.tipcalculator

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.myjetpack.tipcalculator.components.InputField
import com.myjetpack.tipcalculator.ui.theme.TipCalculatorTheme
import com.myjetpack.tipcalculator.util.calculateTotalPerPerson
import com.myjetpack.tipcalculator.util.calculateTotalTip
import com.myjetpack.tipcalculator.widgets.RoundIconButton

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp{
                Column(modifier = Modifier.padding(20.dp)) {
                    //CreateTopHeader()
                    Spacer(Modifier.height(10.dp))
                    CreateMainContent()
                }
            }
        }
    }


}

@Composable
 fun MyApp(content: @Composable ()->Unit) {
    TipCalculatorTheme {
        // A surface container using the 'background' color from the theme
        Surface(modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background) {
            content()
        }

    }

}

@Composable
fun CreateTopHeader(totalPerPerson: Double=134.0){

    Card(modifier = Modifier
        .height(height = 150.dp)
        .fillMaxSize()
        .padding(15.dp)
        , shape = RoundedCornerShape(size = 10.dp)
        , backgroundColor = Color(0xFFe8d2f2)
        ) {
          Column(verticalArrangement = Arrangement.Center,
              horizontalAlignment = CenterHorizontally) {
              val total = "%.2f".format(totalPerPerson)
              Text(text = "Total Per Person",
                  style = TextStyle(fontWeight = FontWeight.Bold))
              Text(text = "$${total}",
                  style = TextStyle(fontWeight = FontWeight.Bold,
                  fontSize = MaterialTheme.typography.h3.fontSize))

          }
    }
}

@Preview
@Composable
fun CreateMainContent(){
  BillForm { billAmount->
      Log.d("Amount", billAmount)


  }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BillForm(modifier: Modifier=Modifier,
onValueChange: (String)->Unit={}){

    val totalBillState = remember {
        mutableStateOf("")
    }

    val validState = remember(totalBillState.value) {
        totalBillState.value.trim().isNotEmpty()
    }

    val range = IntRange(1, 100)
    val spiltByState = remember {
        mutableStateOf(1)
    }


    val sliderPositionState  = remember {
        mutableStateOf(0f)
    }

    val tipPercentage = (sliderPositionState.value*100).toInt()

    val tipAmountState = remember {
        mutableStateOf(0.0)
    }

    val totalPerPersonState = remember {
        mutableStateOf(0.0)
    }
    val keyboardController = LocalSoftwareKeyboardController.current

    CreateTopHeader(totalPerPerson = totalPerPersonState.value)

    Surface(modifier = modifier
        ,border = BorderStroke(width = 1.dp, color = Color.LightGray)
        , shape = RoundedCornerShape(size = 10.dp)
    ){
        Column(modifier = modifier.padding(start = 10.dp, end = 10.dp, top = 14.dp, bottom = 14.dp)) {
            InputField(valueState = totalBillState,
                labelId = "Enter Bill",
                enabled = true,
                isSingleLine = true,
                onAction = KeyboardActions {
                    if (!validState) return@KeyboardActions
                    onValueChange(totalBillState.value.trim())
                    keyboardController?.hide()
                }
            )
           if (validState){

               // Add & remove button
                Row(modifier = modifier.padding(horizontal = 3.dp),
                    horizontalArrangement = Arrangement.Start,
                verticalAlignment = CenterVertically
                ) {
                    Text(text = "Spilt")
                    Spacer(modifier = modifier.width(120.dp))
                    Row(modifier = modifier.padding(horizontal = 3.dp),
                        horizontalArrangement = Arrangement.End) {
                        RoundIconButton(modifier = modifier,
                            imageVector = Icons.Default.Remove, onClick = {
                                if (spiltByState.value!=1){
                                    spiltByState.value=spiltByState.value-1
                                    totalPerPersonState.value= calculateTotalPerPerson(totalBillState.value.toDouble(),
                                    spiltByState.value, tipPercentage)
                                }
                            })
                        Text(text = spiltByState.value.toString(),
                            modifier = modifier
                                .align(alignment = CenterVertically)
                                .padding(9.dp))
                        RoundIconButton(modifier = modifier,
                            imageVector = Icons.Default.Add, onClick = {
                                if (spiltByState.value < range.last){
                                    spiltByState.value = spiltByState.value+1
                                    totalPerPersonState.value= calculateTotalPerPerson(totalBillState.value.toDouble(),
                                        spiltByState.value, tipPercentage)
                                }
                            })
                    }


                }
            //Tip row
                Row(modifier = modifier.padding(horizontal = 3.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = CenterVertically
                ) {
                    Text(text = "Tip", modifier.align(alignment = CenterVertically))
                    Spacer(modifier = modifier.width(200.dp))
                    Text(text = "$ ${tipAmountState.value}",modifier.align(alignment = CenterVertically))
                }

            Column(horizontalAlignment = CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
                ) {
                Text("$tipPercentage %")
                Spacer(modifier = modifier.height(14.dp))

                //Slider
                Slider(value = sliderPositionState.value,
                    onValueChange = { newVal->
                    sliderPositionState.value=newVal

                        tipAmountState.value = calculateTotalTip(totalBillState.value.toDouble(),
                            tipPercentage)

                        totalPerPersonState.value = calculateTotalPerPerson(totalBillState.value.toDouble(),
                        spiltByState.value,
                        tipPercentage)

                }, modifier = modifier.padding(start = 16.dp, end = 16.dp),
                steps = 5,
                    onValueChangeFinished={
                        //Log.d()
                    })
            }
            }else Box() {
               
           }
        }
    }
}


