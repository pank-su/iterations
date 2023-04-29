import androidx.compose.material.MaterialTheme
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import theme.MatrixTheme
import java.lang.NumberFormatException
import kotlin.math.absoluteValue

val matrixViewModel = MatrixViewModel()

@Composable
fun matrixEnter() {

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun App() {
    MatrixTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            val scrollState = rememberScrollState()

            Column(
                modifier = Modifier.fillMaxWidth(),//.verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    RadioButton(
                        matrixViewModel.mode == Mode.`Метод итераций`,
                        onClick = { matrixViewModel.mode = Mode.`Метод итераций` })
                    Text("Метод итераций")
                    RadioButton(
                        matrixViewModel.mode == Mode.`Метод зейделя`,
                        onClick = { matrixViewModel.mode = Mode.`Метод зейделя` })
                    Text("Метод зейделя")
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        matrixViewModel.iterations.toString(), modifier = Modifier.width(150.dp),
                        onValueChange = { s ->
                            try {
                                matrixViewModel.iterations = s.toInt().absoluteValue
                            } catch (_: Exception) {

                            }

                        }, label = { Text("Количество итераций") })
                    OutlinedTextField(
                        "${matrixViewModel.minE}",
                        onValueChange = { s ->
                            try {
                                matrixViewModel.minE = s.toDouble().absoluteValue
                            } catch (_: Exception) {

                            }

                        }, label = { Text("Мин. разница") }, modifier = Modifier.width(150.dp)
                    )
                }
                Text("Количество столбцов&колонок")
                Row(verticalAlignment = Alignment.CenterVertically) {
                    FilledIconButton(onClick = {
                        if (matrixViewModel.how > 2) {
                            matrixViewModel.updateMatrix(matrixViewModel.how - 1)
                            matrixViewModel.how--
                        }
                    }) {
                        Icon(painterResource("remove.svg"), null, modifier = Modifier.size(24.dp))
                    }
                    TextField(
                        matrixViewModel.how.toString(),
                        onValueChange = {},
                        modifier = Modifier.width(100.dp),
                        readOnly = true,
                        textStyle = TextStyle(textAlign = TextAlign.Center, fontSize = 18.sp)
                    )
                    FilledIconButton(onClick = {
                        matrixViewModel.updateMatrix(matrixViewModel.how + 1)
                        matrixViewModel.how++
                    }) {
                        Icon(painterResource("add.svg"), null, modifier = Modifier.size(24.dp))
                    }
                }

                LazyVerticalGrid(
                    columns = GridCells.Fixed(matrixViewModel.how + 1),
                    horizontalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterHorizontally),
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                    modifier = Modifier.wrapContentHeight().fillMaxWidth().padding(20.dp),
                    userScrollEnabled = false
                ) {
                    items(matrixViewModel.how + 1) {
                        if (it != matrixViewModel.how) Text("a_x_${it + 1}", textAlign = TextAlign.Center)
                        else Text("b", textAlign = TextAlign.Center, fontWeight = FontWeight.Bold)
                    }

                    items((matrixViewModel.how + 1) * matrixViewModel.how) {
                        if (it % (matrixViewModel.how + 1) != matrixViewModel.how) TextField(
                            if (matrixViewModel.Matrixes[it / (matrixViewModel.how + 1)][it % (matrixViewModel.how + 1)] != 0) matrixViewModel.Matrixes[it / (matrixViewModel.how + 1)][it % (matrixViewModel.how + 1)].toString() else "",
                            onValueChange = { s ->
                                try {
                                    if (s.isBlank()) matrixViewModel.Matrixes[it / (matrixViewModel.how + 1)][it % (matrixViewModel.how + 1)] =
                                        0
                                    else matrixViewModel.Matrixes[it / (matrixViewModel.how + 1)][it % (matrixViewModel.how + 1)] =
                                        s.toInt()
                                } catch (_: Exception) {

                                }
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            textStyle = TextStyle(textAlign = TextAlign.Center, fontSize = 16.sp)
                        )
                        else TextField(
                            if (matrixViewModel.matrixAnswerRes[it / (matrixViewModel.how + 1)] != 0) matrixViewModel.matrixAnswerRes[it / (matrixViewModel.how + 1)].toString() else "",
                            onValueChange = { s ->
                                try {
                                    if (s.isBlank()) {
                                        matrixViewModel.matrixAnswerRes[it / (matrixViewModel.how + 1)] = 0
                                    } else matrixViewModel.matrixAnswerRes[it / (matrixViewModel.how + 1)] =
                                        s.toInt()
                                } catch (_: Exception) {

                                }
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            textStyle = TextStyle(textAlign = TextAlign.Center, fontSize = 16.sp)
                        )
                    }
                }

                Button(onClick = { matrixViewModel.solve() }) {
                    Text("Решить")
                }
                if (matrixViewModel.logs.size > 0) {
                    Divider(modifier = Modifier.padding(0.dp, 20.dp, 0.dp, 0.dp))

                    Text("Этапы решения", fontSize = 24.sp)

                    LazyColumn {
                        items(matrixViewModel.logs.size) {
                            val log = matrixViewModel.logs[it]
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("${it}. ", fontWeight = FontWeight.Bold)
                                Text(log, modifier = Modifier.padding(20.dp, 0.dp, 0.dp, 0.dp))
                            }
                        }
                    }
                }
            }

        }
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "Метод итераций") {
        App()
    }
}
