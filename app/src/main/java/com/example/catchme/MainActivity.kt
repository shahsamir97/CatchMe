package com.example.catchme

import android.content.res.Resources
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.catchme.model.GameData
import com.example.catchme.ui.theme.CatchMeTheme
import com.example.catchme.ui.theme.Typography
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CatchMeTheme {
                GameScreen()
            }
        }
    }
}

@Composable
fun GameScreen() {
    val gameData = GameData()
    val configuration = Resources.getSystem().displayMetrics
    var position by remember { mutableStateOf(Offset(0F, 0F)) }
    val offsetAnim = animateOffsetAsState(targetValue = position)
    var score by remember { mutableStateOf(gameData.score) }
    var lives by remember { mutableStateOf(gameData.lives) }

    val runnable: Runnable by lazy {
        Runnable {
            val x = generateRandomFloat(configuration.widthPixels)
            val y = generateRandomFloat(configuration.widthPixels)
            position = Offset(x, y)
        }
        }

    val handler = Handler(Looper.getMainLooper())
    if (lives > 0) handler.postDelayed(runnable, 2000)

    Scaffold(modifier = Modifier.background(color = Color.Red),
        topBar = { TopAppBar(score, lives) }) {
        Surface(modifier = Modifier.clickable {
            if (lives < 1)
                handler.removeCallbacks(runnable)
            lives--
        }.fillMaxSize()) {
            Greeting(paddingValues = it, offsetAnim) {
                score++
            }
        }
    }
}

@Composable
fun Greeting(paddingValues: PaddingValues, offsetAnim: State<Offset>, onClick: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize().background(color = Color.Blue)) {
        Image(
            painter = painterResource(id = R.drawable.jerry),
            contentDescription = "",
            modifier = Modifier
                .offset(offsetAnim.value.x.dp, offsetAnim.value.y.dp)
                .height(25.dp)
                .width(25.dp)
                .clickable { onClick() }
                .background(color = Color.Red)
                .constrainIn
        )
    }
}

@Composable
fun TopAppBar(score: Int, lives: Int) {
    TopAppBar() {
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(text = "Score:$score", style = Typography.h5)
            Text(text = "Lives:$lives", style = Typography.h5)
        }
    }
}

fun generateRandomFloat(max: Int): Float {
    val value = Random.nextInt(max +1).toFloat()
    Log.i("Position :::",value.toString())
    return value
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CatchMeTheme {
        GameScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun TopAppBar() {
    CatchMeTheme {
        TopAppBar(score = 1, lives = 3)
    }
}
