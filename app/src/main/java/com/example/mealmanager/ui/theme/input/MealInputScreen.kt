package com.example.mealmanager.ui.theme.input

import android.app.DatePickerDialog
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.example.mealmanager.data.MealRepository
import java.util.Calendar

@Composable
fun MealInputScreen(repository: MealRepository) {
    val viewModel: MealInputViewModel = viewModel(factory = MealInputViewModelFactory(repository))
    val mealState by viewModel.mealState.collectAsState()
    val context = LocalContext.current
    var mealTypeExpanded by remember { mutableStateOf(false) }
    val mealTypes = listOf("조식", "중식", "석식", "간식/음료")
    var expanded by remember { mutableStateOf(false) }
    val options = listOf("상록원 2층", "상록원 3층", "기숙사 식당", "혜화관 카페", "그루터기")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
    ) {
        Button(onClick = { expanded = true }) {
            Text(text = mealState.location.ifEmpty { "장소 선택" })
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        viewModel.updateLocation(option)
                        expanded = false
                    }
                )
            }
        }

        DatePicker { selectedDate ->
            viewModel.updateDate(selectedDate)
        }
        Text(text = "선택된 날짜: ${mealState.date}", style = MaterialTheme.typography.bodyLarge)

        ImagePicker { uri ->
            viewModel.updateImageUri(uri.toString())
        }
        mealState.imageUri?.let { imageUri ->
            Image(
                painter = rememberImagePainter(data = imageUri),
                contentDescription = "업로드된 이미지",
                modifier = Modifier.fillMaxWidth().height(130.dp)
            )
        }

        Button(onClick = { mealTypeExpanded = true }) {
            Text(text = mealState.mealType.ifEmpty { "언제먹었어요?" })
        }

        DropdownMenu(
            expanded = mealTypeExpanded,
            onDismissRequest = { mealTypeExpanded = false }
        ) {
            mealTypes.forEach { type ->
                DropdownMenuItem(
                    text = { Text(type) },
                    onClick = {
                        viewModel.updateMealType(type)
                        mealTypeExpanded = false
                    }
                )
            }
        }

        TextField(
            value = mealState.foodName,
            onValueChange = { viewModel.updateFoodName(it) },
            label = { Text("메인 메뉴") }
        )

        TextField(
            value = mealState.sideDishes.joinToString(", "), // 리스트를 쉼표로 구분된 문자열로 변환
            onValueChange = { viewModel.updateSideDishes(it) }, // 변경 사항을 ViewModel로 전달
            label = { Text("반찬은!?") }
        )


        TextField(
            value = mealState.cost.toString(),
            onValueChange = { cost ->
                viewModel.updateCost(cost.toIntOrNull() ?: 0)
            },
            label = { Text("금액") }
        )

        TextField(
            value = mealState.review,
            onValueChange = { viewModel.updateReview(it) },
            label = { Text("어땠나요?") }
        )

        Button(onClick = {
            viewModel.saveMeal(mealState)
            println("Debug: Saved mealState = $mealState")
            Toast.makeText(context, "저장되었습니다!", Toast.LENGTH_SHORT).show()
        }) {
            Text("저장하기")
        }

    }
}

@Composable
fun ImagePicker(onImageSelected: (Uri) -> Unit) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let { onImageSelected(it) }
        }
    )

    Button(onClick = { launcher.launch("image/*") }) {
        Text("사진 업로드")
    }
}

@Composable
fun DatePicker(onDateSelected: (String) -> Unit) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val date = "$year-${month + 1}-$dayOfMonth"
            onDateSelected(date)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    Button(onClick = { datePickerDialog.show() }) {
        Text("날짜 선택")
    }
}
