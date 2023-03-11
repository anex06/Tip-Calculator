package com.myjetpack.tipcalculator

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.myjetpack.tipcalculator.components.InputField
import com.myjetpack.tipcalculator.ui.theme.TipCalculatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp{
                Column(modifier = Modifier.padding(20.dp)) {
                    CreateTopHeader()
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

@Preview
@Composable
fun CreateTopHeader(totalPerPerson: Double=134.0){

    Card(modifier = Modifier
        .height(height = 150.dp)
        .fillMaxSize()
        , shape = RoundedCornerShape(size = 10.dp)
        , backgroundColor = Color(0xFFe8d2f2)
        ) {
          Column(verticalArrangement = Arrangement.Center,
              horizontalAlignment = Alignment.CenterHorizontally) {
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

    val keyboardController = LocalSoftwareKeyboardController.current

    Surface(modifier = Modifier
        .height(height = 200.dp)
        .fillMaxSize()
        , border = BorderStroke(width = 1.dp, color = Color.LightGray)
        , shape = RoundedCornerShape(size = 10.dp)
    ){
        Column(modifier = Modifier.padding(start = 10.dp, end = 10.dp, top = 14.dp)) {
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

            Spacer(Modifier.height(20.dp))

            Row(horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Spilt")
                OutlinedButton(onClick = {

                },shape = CircleShape,
                    modifier = Modifier.size(40.dp),
                    border = BorderStroke(width = 1.2.dp,
                        color=Color.LightGray),
                    contentPadding = PaddingValues(0.dp),
                    elevation = ButtonDefaults.elevation(defaultElevation = 5.dp)
                ){
                    Icon(painter = painterResource(id = R.drawable.ic_baseline_substract_24),
                        tint = Color.Black,
                        contentDescription = "Decrease amount")
                }

                Text(text = "1", modifier = Modifier.padding(5.dp))

                OutlinedButton(onClick = {

                },shape = CircleShape,
                    modifier = Modifier.size(40.dp),
                    border = BorderStroke(width = 1.2.dp,
                        color=Color.LightGray),
                    contentPadding = PaddingValues(0.dp),
                    elevation = ButtonDefaults.elevation(defaultElevation = 5.dp,)

                ){
                    Icon(Icons.Default.Add,
                        contentDescription = "Add amount",
                        tint = Color.Black)
                }

            }
            Spacer(modifier = Modifier.height(10.dp))

            Row(horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()) {
                Text(text = "Tip")
                Text(text = "$22")
                Text(text = "")

            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()) {
                Text(text = "")
                Text(text = "12%")

            }

        }
    }
}