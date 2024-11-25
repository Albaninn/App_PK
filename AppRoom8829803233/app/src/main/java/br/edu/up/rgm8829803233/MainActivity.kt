package br.edu.up.rgm8829803233

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Upsert
import kotlinx.coroutines.launch

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

@Composable
private fun ListarAfazeresScreen() {
    var titulo by remember { mutableStateOf("") }
    var descricao by remember { mutableStateOf("") }
    var afazeres by remember { mutableStateOf(listOf<Afazer>()) }
    var selectedAfazer by remember { mutableStateOf<Afazer?>(null) }

    val context = LocalContext.current
    val db = remember { abrirBanco(context) }
    val dao = db.getAfazerDao()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            afazeres = dao.listar()
        }
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        // Header
        Text(
            text = if (selectedAfazer == null) "Novo Afazer" else "Editar Afazer",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Input Fields
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

        // Save/Update Button
        Button(
            onClick = {
                coroutineScope.launch {
                    val novoAfazer = Afazer(
                        id = selectedAfazer?.id,
                        titulo = titulo,
                        descricao = descricao
                    )
                    dao.gravar(novoAfazer)
                    afazeres = dao.listar()
                    titulo = ""
                    descricao = ""
                    selectedAfazer = null
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = if (selectedAfazer == null) "Salvar" else "Atualizar")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Task List Header
        Text(
            text = "Lista de Afazeres",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Task List
        for (afazer in afazeres) {
            TaskCard(
                afazer = afazer,
                onEdit = {
                    titulo = afazer.titulo
                    descricao = afazer.descricao
                    selectedAfazer = afazer
                },
                onDelete = {
                    coroutineScope.launch {
                        dao.excluir(afazer)
                        afazeres = dao.listar()
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

//INFRAESTRUTURA DE BANCO DE DADOS
// 1) Entidade do banco de dados
@Entity(tableName = "tab_afazer")
data class Afazer(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val titulo: String,
    val descricao: String,
    val concluido: Boolean = false
)

// 2) Data Access Objects (DAOs)
@Dao
interface AfazerDao {
    //CRUD
    @Query("select * from tab_afazer")
    suspend fun listar(): List<Afazer>
    @Query("select * from tab_afazer where id = :idx")
    suspend fun buscarPorId(idx: Int): Afazer
    @Upsert
    suspend fun gravar(afazer: Afazer)
    @Delete
    suspend fun excluir(afazer: Afazer)
}

// 3) Criar o banco de dados
@Database(entities = [Afazer::class], version = 1)
abstract class AfazerDB : RoomDatabase(){
    abstract fun getAfazerDao(): AfazerDao
}

// 4) Abrir o banco de dados
fun abrirBanco(context: Context): AfazerDB{
    return Room.databaseBuilder(
        context.applicationContext,
        AfazerDB::class.java,
        name = "arquivo.db"
    ).build()
}