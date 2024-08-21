package com.example.littlelemon
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
fun Onboarding(navController: NavHostController) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = LittleLemonColor.green)
                .height(120.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Let's get to know you",
                style = MaterialTheme.typography.h5,
                color = Color.White
            )
        }

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
            var firstname by remember { mutableStateOf("") }
            var lastname by remember { mutableStateOf("") }
            var email by remember { mutableStateOf("") }

            TextField(
                value = firstname,
                onValueChange = { newText -> firstname = newText },
                label = { Text("First name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 30.dp)
            )

            TextField(
                value = lastname,
                onValueChange = { newText -> lastname = newText },
                label = { Text("Last name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 30.dp)
            )

            TextField(
                value = email,
                onValueChange = { newText -> email = newText },
                label = { Text("Email") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
            // Pushes the button to the bottom
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = {
                    if (firstname.isNotEmpty() && lastname.isNotEmpty() && email.isNotEmpty()) {
                        // Save data to SharedPreferences when the Register button is clicked
                        with(sharedPreferences.edit()) {
                            putString("firstname", firstname)
                            putString("lastname", lastname)
                            putString("email", email)
                            apply()
                        }.apply {
                            Toast.makeText(
                                context,
                                "Registration successful!",
                                Toast.LENGTH_SHORT
                            ).show()
                            navController.navigate(Home.route)
                        }
                    } else {
                        Toast.makeText(
                            context,
                            "Registration unsuccessful. Please enter all data.",
                            Toast.LENGTH_SHORT
                        ).show()
                        navController.navigate(Home.route)
                    }
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = LittleLemonColor.yellow),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp)
            ) {
                Text(text = "Register")
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun RegisterPreview() {
    // Create a mock NavHostController
    val navController = rememberNavController()
    Onboarding(navController)
}