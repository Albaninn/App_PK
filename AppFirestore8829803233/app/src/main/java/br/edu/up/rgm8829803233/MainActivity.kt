package br.edu.up.rgm8829803233

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApp {
                ListarAfazeresScreen()
            }
        }
    }
}

// Tema personalizado
@Composable
fun MyApp(content: @Composable () -> Unit) {
    val darkTheme = isSystemInDarkTheme()
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        content = content
    )
}

// Paletas de cores
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFBB86FC),
    secondary = Color(0xFF03DAC6),
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onBackground = Color.White,
    onSurface = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF6200EE),
    secondary = Color(0xFF03DAC6),
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black
)

// Tela principal
@Composable
fun ListarAfazeresScreen() {
    var titulo by remember { mutableStateOf("") }
    var descricao by remember { mutableStateOf("") }
    var afazeres by remember { mutableStateOf(listOf<Afazer>()) }
    var selectedAfazer by remember { mutableStateOf<Afazer?>(null) }

    val db = Firebase.firestore
    val collectionRef = db.collection("afazeres")

    // Carregar dados
    LaunchedEffect(Unit) {
        afazeres = collectionRef.get().await().toObjects(Afazer::class.java)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Text(
            text = if (selectedAfazer == null) "Nova Tarefa" else "Editar Tarefa",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Campos de entrada
        OutlinedTextField(
            value = titulo,
            onValueChange = { titulo = it },
            label = { Text("Título") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = descricao,
            onValueChange = { descricao = it },
            label = { Text("Descrição") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        // Botão de salvar/atualizar
        Button(
            onClick = {
                val novoAfazer = Afazer(
                    id = selectedAfazer?.id ?: collectionRef.document().id,
                    titulo = titulo,
                    descricao = descricao
                )
                collectionRef.document(novoAfazer.id).set(novoAfazer)
                    .addOnSuccessListener {
                        titulo = ""
                        descricao = ""
                        selectedAfazer = null
                        collectionRef.get().addOnSuccessListener { snapshot ->
                            afazeres = snapshot.toObjects(Afazer::class.java)
                        }
                    }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = if (selectedAfazer == null) "Salvar" else "Atualizar")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Lista de afazeres
        Text(
            text = "Lista de Tarefas",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        for (afazer in afazeres) {
            TaskCard(
                afazer = afazer,
                onEdit = {
                    titulo = afazer.titulo
                    descricao = afazer.descricao
                    selectedAfazer = afazer
                },
                onDelete = {
                    collectionRef.document(afazer.id).delete().addOnSuccessListener {
                        collectionRef.get().addOnSuccessListener { snapshot ->
                            afazeres = snapshot.toObjects(Afazer::class.java)
                        }
                    }
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun TaskCard(
    afazer: Afazer,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = afazer.titulo,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = afazer.descricao,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(top = 4.dp)
            )
            Row(
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Button(
                    onClick = onEdit,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Editar")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = onDelete,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red,
                        contentColor = Color.White
                    )
                ) {
                    Text("Excluir")
                }
            }
        }
    }
}

// Classe de dados
data class Afazer(
    val id: String = "",
    val titulo: String = "",
    val descricao: String = "",
    val concluido: Boolean = false
)