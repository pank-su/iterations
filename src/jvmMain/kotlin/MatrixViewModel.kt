import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlin.math.absoluteValue

class MatrixViewModel {
    var iterations: Int by mutableStateOf(10)
    val logs: MutableList<String> = mutableStateListOf()
    var how: Int by mutableStateOf(2)
    var minE: Double by mutableStateOf(0.01)


    var Matrixes: MutableList<MutableList<Int>> = mutableStateListOf()
    var matrixAnswerRes: MutableList<Int> = mutableStateListOf()

    @OptIn(ExperimentalStdlibApi::class)
    fun updateMatrix(newHow: Int) {
        Matrixes = MutableList(newHow) { _ ->
            val list = mutableStateListOf(0)
            for (i in 1..<newHow) {
                list.add(0)
            }
            return@MutableList list
        }
        matrixAnswerRes = mutableStateListOf(0)
        for (i in 1..<newHow) {
            matrixAnswerRes.add(0)
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    fun solve() {
        logs.clear()
        var vals = mutableListOf<Double>()

        logs.add("Начальные значения\n")
        for (i in 0..<how) {
            logs[0] += ("x_${i + 1} = ${matrixAnswerRes[i]}/${Matrixes[i][i]} = ${matrixAnswerRes[i] / Matrixes[i][i]}\n")
            vals.add(matrixAnswerRes[i].toDouble() / Matrixes[i][i].toDouble())
        }
        var last_vals = vals.toMutableList()

        loop@ while (logs.size - 1 < iterations) {


            var log = ""
            for (i in 0..<how) {
                log += "x_${i + 1} = ( ${matrixAnswerRes[i].toDouble()}"
                var value = matrixAnswerRes[i].toDouble()
                Matrixes[i].forEachIndexed { index, el ->
                    // println(index)
                    if (i != index) {
                        log += (if (-el > 0) " + " else " - ") + "${el.absoluteValue} * ${last_vals[index]}"
                        value += -el * last_vals[index]
                    }
                }
                value /= Matrixes[i][i]
                log += " ) / ${Matrixes[i][i]} = $value \n"
                vals[i] = value
            }
            logs.add(log)
            for (i in 0..<how){
                if ((last_vals[i] - vals[i]).absoluteValue < minE){
                    break@loop
                }
            }
            last_vals = vals.toMutableList()
        }


    }

    init {
        updateMatrix(how)
    }

}