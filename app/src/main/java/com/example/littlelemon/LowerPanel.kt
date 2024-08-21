package com.example.littlelemon


import android.graphics.drawable.Drawable
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.littlelemon.data.AppDatabase
import com.example.littlelemon.data.MenuItemRoom
import com.example.littlelemon.ui.theme.LittleLemonColor
import com.google.accompanist.drawablepainter.rememberDrawablePainter


@Composable
fun LowerPanel(
    navController: NavHostController? = null,
    searchPhrase: CharSequence? = "",
    selectedCategory: String ? = ""
) {
    val context = LocalContext.current
    val database by lazy {
        Room.databaseBuilder(context, AppDatabase::class.java, "database").build()
    }
    val databaseMenuItems by database.menuItemDao().getAll().observeAsState(emptyList())
    var sortedMenuList = databaseMenuItems.sortedBy{ it.title }
    Log.i("MainActivity", "menuListL: $sortedMenuList")
    if (!searchPhrase.isNullOrEmpty()) {
        sortedMenuList= databaseMenuItems.filter{
            it.title.contains(searchPhrase,ignoreCase=true)
        }
    }

    if (!selectedCategory.isNullOrEmpty()) {
        sortedMenuList= databaseMenuItems.filter{
            it.category.contains(selectedCategory,ignoreCase=true)
        }
    }

    Column () {
        WeeklySpecial()
        LazyColumn {
            itemsIndexed(sortedMenuList) { _, dish ->
                MenuDish(navController, dish)
            }
        }
    }
}


@Composable
fun WeeklySpecial() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)) {
        Text(
            text = stringResource(id = R.string.weekly_special),
            color = LittleLemonColor.charcoal,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )
    }
}


@OptIn(ExperimentalMaterialApi::class, ExperimentalGlideComposeApi::class)
@Composable
fun MenuDish(navController: NavHostController ?= null, dish: MenuItemRoom ?= null) {
    Card(onClick = {
        navController?.navigate(DishDetails.route + "/${dish?.id}")
    }) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Column {
                Text(
                    text = dish?.title ?: stringResource(id = R.string.greek_salad),
                    style = MaterialTheme.typography.h2)
                dish?.description?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.body1,
                        modifier = Modifier
                            .fillMaxWidth(0.75f)
                            .padding(top = 5.dp, bottom = 5.dp)
                    )
                }
                Text(text = "$${dish?.price}", style = MaterialTheme.typography.body2)
            }

            Surface(color = MaterialTheme.colors.background) {
                Box {
                    GlideImage(
                        model = dish?.image,
                        contentDescription = "${dish?.title} Image",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(100.dp)
                        )
                    {
                        it
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .override(70, 70)
                    }
                }
            }
        }
        Divider(
            modifier = Modifier.padding(start = 8.dp, end = 8.dp), color= LittleLemonColor.yellow,
            thickness = 1.dp,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LowerPanelPreview() {
    LowerPanel()
}
