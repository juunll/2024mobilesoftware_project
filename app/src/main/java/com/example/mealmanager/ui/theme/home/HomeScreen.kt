package com.example.mealmanager.ui.theme.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mealmanager.R

@Composable
fun HomeScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.app_logo),
            contentDescription = "앱 로고",
            modifier = Modifier
                .height(150.dp)
        )

        Spacer(modifier = Modifier.height(32.dp)) //이미지와 버튼 간의 간격

        Button(onClick = { navController.navigate("meal_input") }) {
            Text("식사를 입력하세요!!")
        }
        Button(onClick = { navController.navigate("meal_list") }) {
            Text("이 날 뭐 먹었지?")
        }
        Button(onClick = { navController.navigate("meal_analysis") }) {
            Text("이번달 얼마나 먹었지?")
        }
    }
}
