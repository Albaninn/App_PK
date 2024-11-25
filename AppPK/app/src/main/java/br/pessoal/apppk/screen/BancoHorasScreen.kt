package br.pessoal.apppk.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import br.pessoal.apppk.R
import java.time.LocalDate
import java.time.LocalTime
import java.time.Month
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@RequiresApi(Build.VERSION_CODES.O)
val workHours = mutableMapOf(
    "l.serenato" to mutableListOf(
        WorkDay("01/08/2024", "07:25", "18:15"),
        WorkDay("02/08/2024", "07:57", "17:33"),
        WorkDay("05/08/2024", "08:38", "17:45"),
        WorkDay("06/08/2024", "08:12", "17:53"),
        WorkDay("07/08/2024", "07:20", "18:05"),
        WorkDay("08/08/2024", "07:30", "18:15"),
        WorkDay("09/08/2024", "07:29", "15:49"),
        WorkDay("12/08/2024", "08:15", "17:47"),
        WorkDay("13/08/2024", "08:17", "17:38"),
        WorkDay("14/08/2024", "07:18", "18:10"),
        WorkDay("15/08/2024", "07:10", "18:10"),
        WorkDay("16/08/2024", "07:48", "18:02"),
        WorkDay("19/08/2024", "08:15", "17:43"),
        WorkDay("20/08/2024", "08:18", "17:41"),
        WorkDay("21/08/2024", "07:04", "18:03"),
        WorkDay("22/08/2024", "07:13", "18:03"),
        WorkDay("23/08/2024", "07:55", "17:46"),
        WorkDay("26/08/2024", "08:04", "17:47"),
        WorkDay("27/08/2024", "09:53", "17:28"),
        WorkDay("28/08/2024", "08:32", "18:16"),
        WorkDay("29/08/2024", "07:13", "18:05"),
        WorkDay("30/08/2024", "08:56", "16:23"),
        WorkDay("02/09/2024", "08:35", "17:38"),
        WorkDay("03/09/2024", "09:27", "17:33"),
        WorkDay("04/09/2024", "07:24", "17:45"),
        WorkDay("05/09/2024", "07:55", "18:06"),
        WorkDay("06/09/2024", "09:33", "16:06"),
        WorkDay("09/09/2024", "08:38", "18:00")

    ),
    "lorenna.j" to mutableListOf(
        WorkDay("16/08/2024", "08:15", "17:30"),
        WorkDay("04/09/2024", "07:24", "19:45"),
        WorkDay("05/09/2024", "07:55", "18:06"),
        WorkDay("06/09/2024", "09:33", "16:06"),
        WorkDay("09/09/2024", "08:38", "18:00")
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BancoHorasScreen(navController: NavHostController, username: String) {
    var showInReais by remember { mutableStateOf(false) }
    var selectedMonth by remember { mutableStateOf<Month?>(null) }

    // Estados para horas positivas e negativas
    val totalPositiveHours = remember { mutableStateOf(0.0) }
    val totalNegativeHours = remember { mutableStateOf(0.0) }
    val valorHora = 16.65

    // Calcular a diferença entre horas positivas e negativas
    val saldoHoras = totalPositiveHours.value - totalNegativeHours.value

    // Função para recalcular o saldo
    fun recalculateSaldo() {
        totalPositiveHours.value = calculateTotalPositiveHours(username, selectedMonth)
        totalNegativeHours.value = calculateTotalNegativeHours(username, selectedMonth)
    }

    LaunchedEffect(selectedMonth) {
        recalculateSaldo()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Consulta Banco de Horas") },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        recalculateSaldo()  // Recalcular o saldo ao clicar
                    }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,  // Ícone de refresh/atualizar
                            contentDescription = "Atualizar Cálculo"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())  // Habilita rolagem
                .padding(padding)
        ) {
            // Saldo do Banco de Horas
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = if (saldoHoras >= 0) R.drawable.ampfull else R.drawable.amp),
                        contentDescription = if (saldoHoras >= 0) "Saldo Positivo" else "Saldo Negativo",
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = if (showInReais) {
                            "R\$ ${String.format("%.2f", saldoHoras * valorHora)}"
                        } else {
                            formatHoursToHHmm(saldoHoras)
                        },
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Text(
                        text = if (saldoHoras >= 0) "Saldo Positivo" else "Saldo Negativo",
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                // Toggle entre Horas e Reais
                IconButton(onClick = { showInReais = !showInReais }) {
                    Image(
                        painter = painterResource(id = R.drawable.money),
                        contentDescription = if (showInReais) "Mostrar em Horas" else "Mostrar em Reais",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            // Filtro por tempo
            FilterByTime(selectedMonth) { month ->
                selectedMonth = month
                recalculateSaldo() // Recalcular o saldo ao mudar o mês
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Exibir a lista de registros de banco de horas do usuário logado
            WorkHoursList(
                username = username,
                selectedMonth = selectedMonth,
                onTotalsChanged = { positive, negative ->
                    totalPositiveHours.value = positive
                    totalNegativeHours.value = negative
                }
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WorkHoursList(
    username: String,
    selectedMonth: Month?,
    onTotalsChanged: (Double, Double) -> Unit
) {
    val workDays = workHours[username]?.sortedByDescending {
        LocalDate.parse(it.date, DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    }?.filter {
        selectedMonth == null || LocalDate.parse(it.date, DateTimeFormatter.ofPattern("dd/MM/yyyy")).month == selectedMonth
    } ?: emptyList()

    // Aqui armazenamos os valores totais atualizados
    var totalPositiveHours by remember { mutableStateOf(0.0) }
    var totalNegativeHours by remember { mutableStateOf(0.0) }

    // Função para recalcular o total de horas sempre que necessário
    fun recalculateTotals() {
        totalPositiveHours = 0.0
        totalNegativeHours = 0.0

        workDays.forEach { workDay ->
            val totalHours = calculateTotalAdjustedHours(workDay.entryTime, workDay.exitTime, workDay.dayOfWeek)
            val credit = calculateCredit(totalHours, workDay.dayOfWeek)
            val debit = calculateDebit(totalHours, workDay.dayOfWeek)

            totalPositiveHours += parseTimeToDouble(credit)
            totalNegativeHours += parseTimeToDouble(debit)
        }
        // Atualiza os totais na tela
        onTotalsChanged(totalPositiveHours, totalNegativeHours)
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Cabeçalho da tabela
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Data", modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium)
            Text(text = "Entrada", modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium)
            Text(text = "Saída", modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium)
            Text(text = "Total", modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium)
        }

        Divider()

        // Iterar sobre os dias de trabalho e exibir
        workDays.forEach { workDay ->
            WorkDayItem(
                workDay = workDay,
                onSave = { newEntryTime, newExitTime ->
                    // Atualizar as horas no mapa de workHours
                    workDay.entryTime = newEntryTime
                    workDay.exitTime = newExitTime

                    // Recalcular os totais após a atualização dos horários
                    recalculateTotals()
                })
            Divider()
        }
    }

    // Recalcula os totais inicialmente
    recalculateTotals()
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WorkDayItem(
    workDay: WorkDay,
    onSave: (String, String) -> Unit
) {
    var entryTime by remember { mutableStateOf(workDay.entryTime) }
    var exitTime by remember { mutableStateOf(workDay.exitTime) }
    var showDialog by remember { mutableStateOf(false) } // Controla o estado do diálogo

    // Função para validar e formatar as horas
    fun parseAndFormatTime(time: String): String {
        return try {
            val formatter = DateTimeFormatter.ofPattern("HH:mm")
            val parsedTime = LocalTime.parse(time, formatter)
            parsedTime.format(formatter)
        } catch (e: Exception) {
            "Invalid"
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { showDialog = true }, // Ao clicar, o diálogo de edição é exibido
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Coluna de Data e Dia da Semana
        Column(modifier = Modifier.weight(1f)) {
            Text(text = workDay.date, style = MaterialTheme.typography.bodyMedium)
            Text(text = workDay.dayOfWeek, style = MaterialTheme.typography.bodySmall)
        }

        // Coluna de Entrada
        Text(text = entryTime, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium)

        // Coluna de Saída
        Text(text = exitTime, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium)

        // Coluna de Total de Horas e Crédito/Débito
        Column(modifier = Modifier.weight(1f)) {
            val totalHours = calculateTotalAdjustedHours(entryTime, exitTime, workDay.dayOfWeek)
            val credit = calculateCredit(totalHours, workDay.dayOfWeek)
            val debit = calculateDebit(totalHours, workDay.dayOfWeek)

            if (totalHours == "Invalid") {
                Text(text = "Erro", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.error)
            } else {
                Text(text = totalHours, style = MaterialTheme.typography.bodyMedium)
                Text(
                    text = if (credit != "00:00") "$credit Crédito" else "$debit Débito",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (credit != "00:00") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                )
            }
        }
    }

    // Exibir o diálogo de edição quando o estado showDialog for verdadeiro
    if (showDialog) {
        EditTimeDialog(
            entryTime = entryTime,
            exitTime = exitTime,
            onDismiss = { showDialog = false }, // Fecha o diálogo sem salvar
            onSave = { newEntryTime, newExitTime ->
                // Ao salvar, os horários são atualizados e a soma só ocorre neste momento
                entryTime = newEntryTime
                exitTime = newExitTime
                onSave(newEntryTime, newExitTime) // Atualiza os horários e recalcula os valores
                showDialog = false
            }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EditTimeDialog(
    entryTime: String,
    exitTime: String,
    onDismiss: () -> Unit,
    onSave: (String, String) -> Unit
) {
    var newEntryTime by remember { mutableStateOf(entryTime) }
    var newExitTime by remember { mutableStateOf(exitTime) }

    // Função para validar os horários
    fun isValidTime(time: String): Boolean {
        return try {
            LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"))
            true
        } catch (e: Exception) {
            false
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Editar Horários") },
        text = {
            Column {
                OutlinedTextField(
                    value = newEntryTime,
                    onValueChange = { newEntryTime = it },
                    label = { Text("Horário de Entrada") },
                    isError = !isValidTime(newEntryTime)
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = newExitTime,
                    onValueChange = { newExitTime = it },
                    label = { Text("Horário de Saída") },
                    isError = !isValidTime(newExitTime)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (isValidTime(newEntryTime) && isValidTime(newExitTime)) {
                        onSave(newEntryTime, newExitTime) // Salva os horários e recalcula o saldo
                    }
                }
            ) {
                Text("Salvar")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

// Função para converter o valor de crédito/débito para Double
fun parseTimeToDouble(time: String): Double {
    return try {
        val parts = time.split(":")
        val hours = parts[0].toInt()
        val minutes = parts[1].toInt()
        hours + (minutes / 60.0)
    } catch (e: Exception) {
        0.0
    }
}

// Função para formatar as horas em "HH:mm"
@RequiresApi(Build.VERSION_CODES.O)
fun formatHoursToHHmm(hours: Double): String {
    val totalMinutes = (hours * 60).toInt()
    val hh = totalMinutes / 60
    val mm = totalMinutes % 60
    return if (hours >= 0) {
        "%02d:%02d".format(hh, mm)
    } else {
        "- %02d:%02d".format(-hh, -mm)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FilterByTime(selectedMonth: Month?, onMonthSelected: (Month?) -> Unit) {
    val months = Month.values()
    var expanded by remember { mutableStateOf(false) }

    Box {
        OutlinedButton(onClick = { expanded = true }) {
            Text(text = selectedMonth?.name ?: "Selecionar Mês")
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = null
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Todos") },
                onClick = {
                    onMonthSelected(null)
                    expanded = false
                }
            )
            months.forEach { month ->
                DropdownMenuItem(
                    text = { Text(month.name) },
                    onClick = {
                        onMonthSelected(month)
                        expanded = false
                    }
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun calculateTotalPositiveHours(username: String, selectedMonth: Month?): Double {
    val userWorkDays = workHours[username] ?: emptyList()
    return userWorkDays.sumOf { workDay ->
        val totalHours = parseTime(workDay.totalAdjustedHours)
        val standardHours = if (workDay.dayOfWeek.equals("sexta-feira", ignoreCase = true)) {
            LocalTime.of(8, 0)
        } else {
            LocalTime.of(9, 0)
        }
        val positiveMinutes = ChronoUnit.MINUTES.between(standardHours, totalHours).takeIf { it > 0 } ?: 0
        positiveMinutes / 60.0 + (positiveMinutes % 60) / 100.0
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun calculateTotalNegativeHours(username: String, selectedMonth: Month?): Double {
    val userWorkDays = workHours[username] ?: emptyList()
    return userWorkDays.sumOf { workDay ->
        val totalHours = parseTime(workDay.totalAdjustedHours)
        val standardHours = if (workDay.dayOfWeek.equals("sexta-feira", ignoreCase = true)) {
            LocalTime.of(8, 0)
        } else {
            LocalTime.of(9, 0)
        }
        val negativeMinutes = ChronoUnit.MINUTES.between(totalHours, standardHours).takeIf { it > 0 } ?: 0
        negativeMinutes / 60.0 + (negativeMinutes % 60) / 100.0
    }
}

@RequiresApi(Build.VERSION_CODES.O)
data class WorkDay(
    val date: String,
    var entryTime: String,
    var exitTime: String
) {
    val dayOfWeek: String = calculateDayOfWeek(date)

    @RequiresApi(Build.VERSION_CODES.O)
    val totalAdjustedHours: String = calculateTotalAdjustedHours(entryTime, exitTime, dayOfWeek)

    @RequiresApi(Build.VERSION_CODES.O)
    val credit: String = calculateCredit(totalAdjustedHours, dayOfWeek)
    @RequiresApi(Build.VERSION_CODES.O)
    val debit: String = calculateDebit(totalAdjustedHours, dayOfWeek)
}

@RequiresApi(Build.VERSION_CODES.O)
fun calculateTotalAdjustedHours(entryTime: String, exitTime: String, dayOfWeek: String): String {
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    val entry = LocalTime.parse(entryTime, formatter)
    val exit = if (exitTime.isBlank()) LocalTime.of(12, 0) else LocalTime.parse(exitTime, formatter)

    var totalMinutes = ChronoUnit.MINUTES.between(entry, exit)

    if (exit >= LocalTime.of(13, 0)) {
        totalMinutes -= 60
    }

    totalMinutes = if (totalMinutes < 0) 0 else totalMinutes

    val hours = totalMinutes / 60
    val minutes = totalMinutes % 60
    return "%02d:%02d".format(hours, minutes)
}

@RequiresApi(Build.VERSION_CODES.O)
fun calculateCredit(totalHours: String, dayOfWeek: String): String {
    val total = parseTime(totalHours)
    val standardHours = if (dayOfWeek.equals("sexta-feira", ignoreCase = true)) LocalTime.of(8, 0) else LocalTime.of(9, 0)
    return if (total.isAfter(standardHours)) {
        val extraMinutes = ChronoUnit.MINUTES.between(standardHours, total)

        // Aplicar o fator de 1,5
        val adjustedMinutes = (extraMinutes * 1.5).toLong()

        val hours = adjustedMinutes / 60
        val minutes = adjustedMinutes % 60
        "%02d:%02d".format(hours, minutes)
    } else {
        "00:00"
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun calculateDebit(totalHours: String, dayOfWeek: String): String {
    val total = parseTime(totalHours)
    val standardHours = if (dayOfWeek.equals("sexta-feira", ignoreCase = true)) LocalTime.of(8, 0) else LocalTime.of(9, 0)
    return if (total.isBefore(standardHours)) {
        val deficitMinutes = ChronoUnit.MINUTES.between(total, standardHours)
        val hours = deficitMinutes / 60
        val minutes = deficitMinutes % 60
        "%02d:%02d".format(hours, minutes)
    } else {
        "00:00"
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun calculateDayOfWeek(date: String): String {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val localDate = LocalDate.parse(date, formatter)
    return localDate.dayOfWeek.getDisplayName(java.time.format.TextStyle.FULL, java.util.Locale.getDefault())
}

@RequiresApi(Build.VERSION_CODES.O)
private fun parseTime(time: String): LocalTime {
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    return LocalTime.parse(time, formatter)
}