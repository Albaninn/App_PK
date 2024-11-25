package br.edu.up.rgm8829803233

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ListarAfazeresScreen()
        }
    }
}

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
        modifier = Modifier.padding(
            top = 90.dp,
            start = 20.dp,
            end = 20.dp,
            bottom = 20.dp
        )
    ) {
        Text(
            text = "Novo ou Editar Afazer",
            fontWeight = FontWeight.ExtraBold,
            fontSize = 30.sp
        )
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = titulo,
            onValueChange = { titulo = it },
            textStyle = TextStyle(fontSize = 25.sp, fontWeight = FontWeight.Normal),
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Título") }
        )
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = descricao,
            onValueChange = { descricao = it },
            textStyle = TextStyle(fontSize = 25.sp, fontWeight = FontWeight.Normal),
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Descrição") }
        )
        Spacer(modifier = Modifier.height(10.dp))
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
            }
        ) {
            Text(
                text = if (selectedAfazer == null) "Salvar" else "Atualizar",
                fontSize = 30.sp
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Lista de afazeres",
            fontWeight = FontWeight.ExtraBold,
            fontSize = 30.sp
        )
        Spacer(modifier = Modifier.height(10.dp))
        for (afazer in afazeres) {
            Column(modifier = Modifier.padding(vertical = 5.dp)) {
                Text(text = afazer.titulo, fontSize = 25.sp)
                Text(text = afazer.descricao, fontSize = 20.sp)
                Row {
                    Button(
                        onClick = {
                            titulo = afazer.titulo
                            descricao = afazer.descricao
                            selectedAfazer = afazer
                        },
                        modifier = Modifier.padding(end = 10.dp)
                    ) {
                        Text("Editar")
                    }
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                dao.excluir(afazer)
                                afazeres = dao.listar()
                            }
                        }
                    ) {
                        Text("Excluir")
                    }
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
    //@Update @Insert
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