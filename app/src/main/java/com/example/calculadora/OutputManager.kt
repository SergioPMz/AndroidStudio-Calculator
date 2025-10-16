package com.example.calculadora

import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton

class OutputManager(activity: MainActivity) {
    val calcButtons = ArrayList<AppCompatButton>()

    private lateinit var mainActivity: AppCompatActivity
    private lateinit var output: TextView
    private lateinit var operation: TextView

    private var operationNumber = ""
    private var operationMode = ""

    var overwriteNumberAfterOperation: Boolean = false
    var currentOutput = "0"
    private fun findButton(id: Int): AppCompatButton {
        return this.mainActivity.findViewById(id)
    }

    init {
        this.mainActivity = activity
        calcButtons.add(findButton(R.id.bt0))
        calcButtons.add(findButton(R.id.bt1))
        calcButtons.add(findButton(R.id.bt2))
        calcButtons.add(findButton(R.id.bt3))
        calcButtons.add(findButton(R.id.bt4))
        calcButtons.add(findButton(R.id.bt5))
        calcButtons.add(findButton(R.id.bt6))
        calcButtons.add(findButton(R.id.bt7))
        calcButtons.add(findButton(R.id.bt8))
        calcButtons.add(findButton(R.id.bt9))
        calcButtons.add(findButton(R.id.btPunto))
        calcButtons.add(findButton(R.id.btIgual))
        calcButtons.add(findButton(R.id.btSuma))
        calcButtons.add(findButton(R.id.btResta))
        calcButtons.add(findButton(R.id.btMultiplicar))
        calcButtons.add(findButton(R.id.btDividir))
        calcButtons.add(findButton(R.id.btC))
        calcButtons.add(findButton(R.id.btCe))
        calcButtons.add(findButton(R.id.btPercentage))

        this.output = mainActivity.findViewById(R.id.output)
        this.operation = mainActivity.findViewById(R.id.operation)

        for (button in calcButtons) {
            button.setOnClickListener { getInput((button.getText() as String)) }
        }
    }
    private fun updateOutput(newOutput: String) {
        this.output.setText(newOutput)
        this.currentOutput = newOutput
    }

    private fun getInput(input: String) {
        when (input) {
            "0" -> if (currentOutput != "0") {
                if (currentOutput != "Err") {
                    updateOutput(currentOutput + input)
                }
                if (overwriteNumberAfterOperation) {
                    overwriteNumberAfterOperation = false
                    updateOutput("0")
                }
            }

            "." -> if (!currentOutput.contains(".") && !overwriteNumberAfterOperation) {
                updateOutput(currentOutput + input)
            }

            "+", "-", "x", "/" -> {
                if (this.currentOutput == "Err") return
                if (operationMode.isEmpty()) {
                    this.operationMode = input
                    if (operationNumber.isEmpty()) {
                        if (currentOutput.get(currentOutput.length - 1) == '.') {
                            currentOutput = currentOutput.substring(0, currentOutput.length - 1)
                        }
                        this.operationNumber = currentOutput
                        getInput("ce")
                    }
                    this.operation.setText(("$operationNumber $operationMode"))
                } else {
                    this.operationMode = input
                    this.operation.setText(("$operationNumber $operationMode"))
                }
            }

            "%" -> try {
                val result = (currentOutput.toDouble() / 100).toString()
                getInput("c")
                updateOutput(result)
                overwriteNumberAfterOperation = true
            } catch (e: NumberFormatException) {
                println("Error inesperado")
                e.printStackTrace()
            }

            "=" -> try {
                if (!operationMode.isEmpty()) {
                    var result = ""
                    val operateBy = currentOutput.toDouble()
                    when (operationMode) {
                        "+" -> result = (operationNumber.toDouble() + operateBy).toString()
                        "-" -> result = (operationNumber.toDouble() - operateBy).toString()
                        "x" -> result = (operationNumber.toDouble() * operateBy).toString()
                        "/" -> if (operateBy != 0.0) {
                            result = (operationNumber.toDouble() / operateBy).toString()
                        } else {
                            result = "Err"
                        }
                    }
                    getInput("c")
                    updateOutput(result)
                    overwriteNumberAfterOperation = true
                }
            } catch (e: NumberFormatException) {
                println("Error inesperado")
                e.printStackTrace()
            }

            "ce" -> {
                updateOutput("0")
                overwriteNumberAfterOperation = false
            }

            "c" -> {
                operation.setText("")
                operationNumber = ""
                operationMode = ""
                getInput("ce")
            }

            else -> if (overwriteNumberAfterOperation || currentOutput == "0" || currentOutput == "Err") {
                overwriteNumberAfterOperation = false
                updateOutput(input)
            } else {
                updateOutput(currentOutput + input)
            }
        }
    }
}