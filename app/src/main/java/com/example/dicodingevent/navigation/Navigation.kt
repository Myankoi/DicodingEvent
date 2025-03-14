package com.example.dicodingevent.navigation

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.dicodingevent.data.EventRepository
import com.example.dicodingevent.data.FavoriteEventRepository
import com.example.dicodingevent.data.Injection
import com.example.dicodingevent.ui.component.bottomBarItem
import com.example.dicodingevent.ui.screen.detail.DetailScreen
import com.example.dicodingevent.ui.screen.favorite.FavoriteScreen
import com.example.dicodingevent.ui.screen.finished.FinishedScreen
import com.example.dicodingevent.ui.screen.upcoming.UpcomingScreen
import com.example.dicodingevent.ui.screen.home.HomeScreen
import com.example.dicodingevent.ui.screen.setting.SettingScreen

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    context: Context = LocalContext.current
) {
    var navIndex by remember { mutableIntStateOf(0) }
    var barVisible by remember { mutableStateOf(true) }
    var topBarText by remember { mutableStateOf("") }

    val eventRepository: EventRepository = Injection.provideRepository()
    val favEventRepository: FavoriteEventRepository = Injection.provideFavoriteRepository(context)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.primary)
                    .padding(
                        WindowInsets.systemBars
                            .only(WindowInsetsSides.Top)
                            .asPaddingValues()
                    ),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (!barVisible) {
                        IconButton(
                            content = {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                            },
                            onClick = {
                                navController.popBackStack()
                            }
                        )
                    }
                    Text(
                        text = topBarText,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 20.sp,
                    )
                }
            }
        },
        bottomBar = {
            if (barVisible) {
                NavigationBar(
                    modifier = Modifier,
                    containerColor = MaterialTheme.colorScheme.background
                ) {
                    bottomBarItem().forEachIndexed { index, bar ->
                        NavigationBarItem(
//                            alwaysShowLabel = index == navIndex,
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = MaterialTheme.colorScheme.secondary,
                                selectedIconColor = MaterialTheme.colorScheme.onSecondary,
                                unselectedIconColor = MaterialTheme.colorScheme.onBackground,
                                selectedTextColor = MaterialTheme.colorScheme.secondary,
                                unselectedTextColor = MaterialTheme.colorScheme.onBackground
                            ),
                            selected = index == navIndex,
                            onClick = {
                                navIndex = index
                                navController.navigate(bar.title)
                            },
                            icon = {
                                Icon(
                                    modifier = Modifier.size(20.dp),
                                    imageVector = bar.icon,
                                    contentDescription = ""
                                )
                            },
                            label = {
                                Text(
                                    text = bar.title,
                                    fontSize = 10.sp
                                )
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(navController = navController, startDestination = "Home") {
            composable("Home") {
                barVisible = true
                navIndex = 0
                topBarText = "Dicoding Event"
                HomeScreen(
                    modifier = Modifier.padding(innerPadding),
                    onClickEvent = { id ->
                        navController.navigate("Detail/$id")
                    },
                    eventRepository = eventRepository
                )
            }
            composable("Upcoming") {
                barVisible = true
                navIndex = 1
                topBarText = "Upcoming Event"
                UpcomingScreen(
                    modifier = Modifier.padding(innerPadding),
                    onClickEvent = { id ->
                        navController.navigate("Detail/$id")
                    },
                    eventRepository = eventRepository
                )
            }
            composable("Finished") {
                barVisible = true
                navIndex = 2
                topBarText = "Finished Event"
                FinishedScreen(
                    modifier = Modifier.padding(innerPadding),
                    onClickEvent = { id ->
                        navController.navigate("Detail/$id")
                    },
                    eventRepository = eventRepository
                )
            }
            composable("Detail/{id}") { backStackEntry ->
                barVisible = false
                val eventId = backStackEntry.arguments?.getString("id").toString()
                DetailScreen(
                    modifier = Modifier.padding(innerPadding),
                    eventId = eventId.toInt(),
                    onLoad = { name ->
                        topBarText = name
                    },
                    eventRepository = eventRepository,
                    favEventRepository = favEventRepository
                )
            }
            composable("Favorite") {
                barVisible = true
                navIndex = 3
                topBarText = "Favorite Event"
                FavoriteScreen(
                    modifier = Modifier.padding(innerPadding),
                    onClickEvent = { id ->
                        navController.navigate("Detail/$id")
                    },
                    favEventRepository = favEventRepository
                )
            }
            composable("Setting") {
                barVisible = true
                navIndex = 4
                topBarText = "Setting"
                SettingScreen(
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}