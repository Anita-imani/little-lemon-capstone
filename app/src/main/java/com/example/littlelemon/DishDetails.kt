package com.example.littlelemon


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.room.Room
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.littlelemon.data.AppDatabase
import com.example.littlelemon.ui.theme.LittleLemonColor

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun DishDetails(id: Int) {
    val context = LocalContext.current
    val database by lazy {
        Room.databaseBuilder(context, AppDatabase::class.java, "database").build()
    }
    val menuList = database.menuItemDao().getAll().observeAsState().value
    val dish = menuList?.find { it.id == id }
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        TopAppBar()
        Surface(color = MaterialTheme.colors.background) {
            Box {
                GlideImage(
                    model = dish?.image,
                    contentDescription = "${dish?.title} Image",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(400.dp)
                ){
                    it.override(400,600)
                }
            }
        }
        Column( verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.padding(start = 10.dp, end = 10.dp)) {
            Text(text = dish?.title ?: "" , style = MaterialTheme.typography.h1)
            Text(text = dish?.description ?: "", style = MaterialTheme.typography.body1 )
            Counter()
            Button(onClick = { /*TODO*/ },
                colors = ButtonDefaults.buttonColors(backgroundColor = LittleLemonColor.yellow),
            ) {
                Text(text = stringResource(id = R.string.add_for) + " $${dish?.price}",
                    color = LittleLemonColor.green,
                    textAlign = TextAlign.Center
                    , modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterVertically))
            }
        }
    }
}

@Composable
fun Counter() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth(),
    ) {
        var counter by remember {
            mutableStateOf(1)
        }
        TextButton(
            onClick = {
                counter--
            }
        ) {
            Text(
                text = "-",
                style = MaterialTheme.typography.h2
            )
        }
        Text(
            text = counter.toString(),
            style = MaterialTheme.typography.h2,
            modifier = Modifier.padding(16.dp)
        )
        TextButton(
            onClick = {
                counter++
            }
        ) {
            Text(
                text = "+",
                style = MaterialTheme.typography.h2
            )
        }
    }
}

@Preview
@Composable
fun DishDetailsPreview() {
    DishDetails(1)
}
