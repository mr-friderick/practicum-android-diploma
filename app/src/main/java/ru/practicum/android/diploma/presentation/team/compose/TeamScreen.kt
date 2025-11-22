package ru.practicum.android.diploma.presentation.team.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.practicum.android.diploma.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamScreen(

) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.team))
                },

                )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            Box(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center,

                ) {
                Text(
                    text = stringResource(R.string.team_screen_head),
                    color = Color.Black,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start

                )
            }




            Box(
                modifier = Modifier
                    .padding(top = 32.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.CenterStart,

                ) {
                Text(
                    text = stringResource(R.string.name1),
                    color = Color.Black,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start
                )
            }
            Box(
                modifier = Modifier
                    .padding(top = 32.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.CenterStart,

                ) {
                Text(
                    text = stringResource(R.string.name2),
                    color = Color.Black,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start
                )
            }
            Box(
                modifier = Modifier
                    .padding(top = 32.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.CenterStart,

                ) {
                Text(
                    text = stringResource(R.string.name3),
                    color = Color.Black,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start
                )
            }
            Box(
                modifier = Modifier
                    .padding(top = 32.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.CenterStart,

                ) {
                Text(
                    text = stringResource(R.string.name4),
                    color = Color.Black,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start
                )
            }


        }
    }
}


