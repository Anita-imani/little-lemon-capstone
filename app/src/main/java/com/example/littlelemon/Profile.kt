package com.example.littlelemon

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.littlelemon.ui.theme.LittleLemonColor


@Composable
fun Profile(navController: NavHostController) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    var firstname = sharedPreferences.getString("firstname", "")
    var lastname = sharedPreferences.getString("lastname", "")
    var email = sharedPreferences.getString("email", "")
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White),
    ) {
        Image(
            painter = painterResource(id = R.drawable.littlelemonimgtxt),
            contentDescription = "little lemon logo",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(0.4F)
                .padding(top = 20.dp, bottom = 10.dp)
        )

        Text(
            text = "Personal Information",
            modifier = Modifier.padding(top = 40.dp, bottom = 40.dp, start = 10.dp),
            style = MaterialTheme.typography.h6
        )
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(start = 10.dp, end = 10.dp),
        ) {
            if (firstname != null) {
                TextField(
                    value = firstname!!,
                    onValueChange = { newText -> firstname = newText },
                    label = { Text("First name") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 30.dp)
                )
            }

            if (lastname != null) {
                TextField(
                    value = lastname!!,
                    onValueChange = { newText -> lastname = newText },
                    label = { Text("Last name") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 30.dp)
                )
            }

            if (email != null) {
                TextField(
                    value = email!!,
                    onValueChange = { newText -> email = newText },
                    label = { Text("Email") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )
            }
            // Pushes the button to the bottom
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = {
                    // Delete data from SharedPreferences when the Logout button is clicked
                    with(sharedPreferences.edit()) {
                        putString("firstname", "")
                        putString("lastname", "")
                        putString("email", "")
                        apply()
                    }.apply {
                        Toast.makeText(
                            context,
                            "Log out successful!",
                            Toast.LENGTH_SHORT
                        ).show()
                        navController.navigate(Onboarding.route)
                    }
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = LittleLemonColor.yellow),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp)
            ) {
                Text(text = "Log out")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileActivityPreview() {
    // Create a mock NavHostController
    val navController = rememberNavController()
    Profile(navController)
}
