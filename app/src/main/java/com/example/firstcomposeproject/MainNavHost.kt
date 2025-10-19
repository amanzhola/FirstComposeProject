package com.example.firstcomposeproject

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@Composable
fun MainNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "list"
    ) {
        composable("list") {
            ContactListScreen(
                onContactClick = { index ->
                    navController.navigate("details/$index")
                }
            )
        }

        composable(
            route = "details/{index}",
            arguments = listOf(navArgument("index") { type = NavType.IntType })
        ) { backStackEntry ->
            val index = backStackEntry.arguments?.getInt("index") ?: 0
            val contact = SampleContacts.all.getOrNull(index)
            if (contact != null) {
                ContactScreen(
                    contact = contact,
                    onBackClick = { navController.popBackStack() }
                )
            }
        }
    }
}
