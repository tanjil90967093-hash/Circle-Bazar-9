package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ui.theme.CircleBazarTheme
import com.example.ui.screens.MainScreen
import com.example.ui.screens.AuthScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.screens.SearchScreen
import com.example.ui.screens.LensScreen
import androidx.navigation.NavType
import androidx.navigation.navArgument

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CircleBazarTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = "splash",
                        enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(300)) },
                        exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(300)) },
                        popEnterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(300)) },
                        popExitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(300)) }
                    ) {
                        composable("splash") { SplashScreen(navController) }
                        composable("auth") { AuthScreen(navController) }
                        composable("main") { MainScreen(navController) }
                        composable(
                            "search?hint={hint}&query={query}",
                            arguments = listOf(
                                navArgument("hint") { type = NavType.StringType; defaultValue = "Search in Circle Bazaar" },
                                navArgument("query") { type = NavType.StringType; defaultValue = "" }
                            )
                        ) { backStackEntry ->
                            val hint = backStackEntry.arguments?.getString("hint") ?: "Search in Circle Bazaar"
                            val query = backStackEntry.arguments?.getString("query") ?: ""
                            SearchScreen(navController, hint, query)
                        }
                        composable("lens") { LensScreen(navController) }
                        composable(
                            "circle_deals?productId={productId}",
                            arguments = listOf(
                                navArgument("productId") { type = NavType.StringType; nullable = true }
                            ),
                            deepLinks = listOf(androidx.navigation.navDeepLink { uriPattern = "https://circlebazar.com/circle_deals" })
                        ) { backStackEntry ->
                            val productId = backStackEntry.arguments?.getString("productId")
                            com.example.ui.screens.CircleDealsScreen(navController, productId) 
                        }
                        composable("mega_deals") {
                            com.example.ui.screens.MegaDealsScreen(navController)
                        }
                        composable("product_detail") {
                            // Placeholder for product detail screen
                            androidx.compose.material3.Text("Product Detail Placeholder")
                        }
                    }
                }
            }
        }
    }
}
