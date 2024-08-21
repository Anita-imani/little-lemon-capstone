package com.example.littlelemon

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.room.Room
import com.example.littlelemon.data.AppDatabase
import com.example.littlelemon.data.MenuItems
import com.example.littlelemon.data.MenuNetworkdata
import com.example.littlelemon.ui.theme.LittleLemonColor
import com.example.littlelemon.ui.theme.LittleLemonTheme
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val httpClient = HttpClient(Android){
        install(ContentNegotiation) {
            json(contentType = ContentType("text", "plain"))
        }
    }

    private val database by lazy {
        Room.databaseBuilder(applicationContext, AppDatabase::class.java, "room-database").build()
    }
    private lateinit var menuList: List<MenuNetworkdata>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch(Dispatchers.IO) {
            if (database.menuItemDao().isEmpty()) {
                menuList = fetchData()
                saveMenuToDatabase(menuList)
            }
        }

        setContent {
            AppScreen()
        }
    }

    private suspend fun fetchData(): List<MenuNetworkdata>{
        val menuList: MenuItems = httpClient.get(
            "https://raw.githubusercontent.com/Meta-Mobile-Developer-PC/Working-With-Data-API/main/menu.json"
        ).body()
        Log.i("MainActivity", "menuList: $menuList")

        return menuList.menu
    }

    private fun saveMenuToDatabase(menuItems: List<MenuNetworkdata>){
        val menuItemsRoom = menuItems.map { it.toMenuItemRoom() }
        database.menuItemDao().insertAll(*menuItemsRoom.toTypedArray())
    }
}


@Composable
fun AppScreen(){
    var count by rememberSaveable() {
        mutableIntStateOf(0)
    }
    LittleLemonTheme {
        Column {
            Row {
                MyNavigation()
            }
            Row {
                ItemOrder(count, { count++ }, { count-- })
            }
        }
    }
}

@Composable
fun ItemOrder(count: Int, onIncrement: () -> Int, onDecrement: () -> Int) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card() {
            Column(modifier = Modifier.padding(20.dp)) {

                Text(
                    text = stringResource(id = R.string.greek_salad),
                    fontSize = 30.sp,
                    fontWeight = FontWeight.W700
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { if (count != 0) onDecrement() }) {
                        Icon(imageVector = Icons.Default.Clear, contentDescription = "Remove")
                    }

                    Text(
                        text = "$count",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(8.dp)
                    )
                    IconButton(onClick = { onIncrement() }) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
                    }
                }
                Row ( modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically) {

                    Button(onClick = { /*TODO*/ },
                        modifier = Modifier.fillMaxWidth(0.75F)) {
                        Text(text = "Add")
                    }
                }
            }
        }
    }
}

@Composable
fun MenuCategory(category: String){
    Button(onClick = { /*TODO*/ },
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.LightGray),
        shape = RoundedCornerShape(40),
        modifier = Modifier.padding(5.dp)
    ) {
        Text(text = category)
    }
}

@Preview(showBackground = true)
@Composable
fun MenuCategoryPreview(){
    MenuCategory(category = "Category")
}

@Composable
fun MyNavigation(){
    val context = LocalContext.current
    val navController = rememberNavController()
    val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    val firstname = sharedPreferences.getString("firstname", "")
    NavHost(navController, startDestination = if(firstname.isNullOrEmpty()) Onboarding.route else Home.route){
        composable(Home.route){
            HomeScreen(navController)
        }

        composable(
            DishDetails.route + "/{${DishDetails.argDishId}}",
            arguments = listOf(navArgument(DishDetails.argDishId) { type = NavType.IntType })
        ) {
            val id = requireNotNull(it.arguments?.getInt(DishDetails.argDishId)) { "Dish id is null" }
            DishDetails(id)
        }

        composable(Onboarding.route){
            Onboarding(navController)
        }
        composable(Profile.route){
            Profile(navController)
        }
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeScreen(navController: NavHostController) {
    val context = LocalContext.current
    val scaffoldState = rememberScaffoldState()
    var searchPhrase: CharSequence by remember {
        mutableStateOf("")
    }
    var selectedCategory by remember {
        mutableStateOf("")
    }

    val database by lazy {
        Room.databaseBuilder(context, AppDatabase::class.java, "database").build()
    }
    val menuList = database.menuItemDao().getAll().observeAsState().value
    Scaffold(
        modifier = Modifier.padding(5.dp),
        scaffoldState = scaffoldState,
        topBar = { TopAppBar(navController) }){
        Column {
            UpperPanel()
            Card(modifier = Modifier
                .background(color = LittleLemonColor.green)
                .padding(10.dp)) {
                TextField(
                    value = searchPhrase.toString(),
                    onValueChange = { value: CharSequence -> searchPhrase = value
                        selectedCategory =""},
                    placeholder = {
                        Text(
                            text = "Enter search phrase",
                            fontSize = 16.sp
                        )
                    },
                    leadingIcon = { Icon( imageVector = Icons.Default.Search, contentDescription = "") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                )
            }
            LazyRow (modifier = Modifier.fillMaxWidth()){

                if(!menuList.isNullOrEmpty()) {
                    val uniqueCategories = menuList.distinctBy { it.category }
                    items(uniqueCategories) {
                        //  MenuCategory(category = it.category)
                        Button(onClick = { selectedCategory = it.category
                            searchPhrase = "" },
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.LightGray),
                            shape = RoundedCornerShape(40),
                            modifier = Modifier.padding(5.dp)
                        ) {
                            Text(text = it.category)
                        }
                    }
                }
            }
            LowerPanel(navController, searchPhrase, selectedCategory)
        }
    }
}
