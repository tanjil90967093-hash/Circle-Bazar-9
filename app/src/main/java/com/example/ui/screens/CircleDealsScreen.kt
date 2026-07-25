package com.example.ui.screens

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Search
import androidx.compose.foundation.clickable
import androidx.compose.ui.draw.clip
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.CircleShape

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CircleDealsScreen(navController: NavController, productId: String? = null) {
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }
    
    // Sample specific to Circle Deals
    val initialProducts = sampleProducts.map { 
        it.copy(discount = it.discount ?: 10, oldPrice = it.oldPrice ?: (it.price * 1.1)) 
    }
    
    Scaffold(
        topBar = {
            Surface(
                color = Color.White,
                shadowElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.Black)
                    }
                    
                    Text(
                        text = "Circle Deals",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4CAF50),
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    
                    // Small Search Box
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .height(36.dp)
                            .background(Color(0xFFF5F5F5), RoundedCornerShape(18.dp))
                            .padding(horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.Gray, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        androidx.compose.foundation.text.BasicTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            textStyle = androidx.compose.material3.LocalTextStyle.current.copy(fontSize = 12.sp, color = Color.Black),
                            decorationBox = { innerTextField ->
                                if (searchQuery.isEmpty()) {
                                    Text("Search deals...", color = Color.Gray, fontSize = 12.sp)
                                }
                                innerTextField()
                            }
                        )
                    }
                    
                    Spacer(modifier = Modifier.width(4.dp))
                    
                    // Cart Icon
                    IconButton(onClick = { }) {
                        Icon(Icons.Outlined.ShoppingCart, contentDescription = "Cart", tint = Color.Black)
                    }
                    
                    // Share Icon
                    IconButton(onClick = { 
                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_SUBJECT, "Check out Circle Deals!")
                            putExtra(Intent.EXTRA_TEXT, "Hey! Check out these amazing Circle Deals on the Circle Bazar app: https://circlebazar.com/circle_deals")
                        }
                        context.startActivity(Intent.createChooser(shareIntent, "Share Circle Deals via"))
                    }) {
                        Icon(Icons.Default.Share, contentDescription = "Share", tint = Color.Black)
                    }
                }
            }
        },
        containerColor = Color(0xFFF5F5F5)
    ) { paddingValues ->
        val categories = listOf("For You", "৳99 Offer", "৳200 Offer", "Electronics", "Fashion", "Home")
        var selectedCategory by remember { mutableStateOf(categories.first()) }
        
        val filteredProducts = if (searchQuery.isEmpty()) {
            initialProducts.filter { 
                when (selectedCategory) {
                    "For You" -> true
                    "৳99 Offer" -> it.price <= 99.99
                    "৳200 Offer" -> it.price <= 200.0 && it.price > 99.99
                    "Electronics" -> it.id % 2 != 0
                    "Fashion" -> it.id % 2 == 0
                    else -> true
                }
            }
        } else {
            initialProducts.filter { it.name.contains(searchQuery, ignoreCase = true) }
        }
        
        androidx.compose.foundation.lazy.LazyColumn(
            contentPadding = PaddingValues(bottom = 12.dp),
            modifier = Modifier.padding(paddingValues).fillMaxSize()
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp) // Space for the overlapping card
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFE8F5E9))
                            .padding(top = 12.dp, bottom = 60.dp) // Extra bottom padding for overlap effect
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Mega Deals",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2E7D32)
                            )
                            Text(
                                text = "See All",
                                fontSize = 14.sp,
                                color = Color(0xFF2E7D32),
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.clickable { navController.navigate("mega_deals") }
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        androidx.compose.foundation.lazy.LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(sampleProducts.take(10), key = { it.id }) { product ->
                                CircleDealsMegaDealCard(product = product, modifier = Modifier.width(170.dp))
                            }
                        }
                    }
                    
                    // Categories Card Overlapping
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter)
                            .offset(y = 20.dp), // Shift down to overlap
                        shape = RoundedCornerShape(0.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        androidx.compose.foundation.lazy.LazyRow(
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 12.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(categories, key = { it }) { category ->
                                val isSelected = category == selectedCategory
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(20.dp))
                                        .background(if (isSelected) Color(0xFF2E7D32) else Color(0xFFF5F5F5))
                                        .clickable { selectedCategory = category }
                                        .padding(horizontal = 16.dp, vertical = 8.dp)
                                ) {
                                    Text(
                                        text = category,
                                        color = if (isSelected) Color.White else Color.Black,
                                        fontSize = 14.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }
            }
            
            items(filteredProducts.chunked(2), key = { it.first().id }) { rowProducts ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    rowProducts.forEach { product ->
                        FullCircleDealCard(
                            product = product,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    if (rowProducts.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
fun FullCircleDealCard(product: Product, modifier: Modifier = Modifier, onClick: () -> Unit = {}) {
    var localStock by remember { mutableStateOf(product.stockQuantity) }
    var isFavorite by remember { mutableStateOf(false) }
    
    Card(
        modifier = modifier
            .height(310.dp)
            .clickable { 
                onClick()
                localStock?.let { if (it > 0) localStock = it - 1 }
            },
        shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF4CAF50)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(Color.White)
            ) {
                coil.compose.AsyncImage(
                    model = product.imageUrl,
                    contentDescription = product.name,
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                
                if (product.discount != null) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .clip(androidx.compose.foundation.shape.RoundedCornerShape(bottomEnd = 12.dp))
                            .background(Color(0xFF2E7D32).copy(alpha = 0.9f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "-${product.discount}%",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 10.dp, end = 10.dp)
                        .size(26.dp)
                        .clip(androidx.compose.foundation.shape.CircleShape)
                        .background(if (isFavorite) Color.Red else Color.White.copy(alpha = 0.8f))
                        .clickable { isFavorite = !isFavorite },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (isFavorite) Color.White else Color.Black.copy(alpha = 0.7f),
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
            
            Column(modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(10.dp)
            ) {
                Text(
                    text = product.name,
                    fontSize = 12.sp,
                    maxLines = 2,
                    minLines = 2,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                    lineHeight = 15.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Medium
                )
                
                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = "৳ ${product.price.toInt()}",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2E7D32)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    if (product.oldPrice != null) {
                        Text(
                            text = "৳ ${product.oldPrice.toInt()}",
                            fontSize = 11.sp,
                            color = Color.Gray,
                            textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough,
                            modifier = Modifier.padding(bottom = 1.dp)
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Filled.Star,
                            contentDescription = "Rating",
                            tint = Color(0xFFFFB300),
                            modifier = Modifier.size(13.dp)
                        )
                        Text(
                            text = " ${product.rating} ",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Text(
                            text = "(${product.soldCount})",
                            fontSize = 10.sp,
                            color = Color.Gray
                        )
                    }
                    
                    Box(
                        modifier = Modifier
                            .size(26.dp)
                            .clip(androidx.compose.foundation.shape.RoundedCornerShape(6.dp))
                            .background(Color(0xFF2E7D32))
                            .clickable { },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Outlined.ShoppingCart,
                            contentDescription = "Add to cart",
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))
                
                if (localStock != null) {
                    androidx.compose.material3.LinearProgressIndicator(
                        progress = { (localStock ?: 0).toFloat() / 50f },
                        modifier = Modifier.fillMaxWidth().height(4.dp).clip(androidx.compose.foundation.shape.CircleShape),
                        color = Color(0xFF4CAF50),
                        trackColor = Color(0xFFE0E0E0),
                        strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Text(
                        text = "Only $localStock Left",
                        fontSize = 10.sp,
                        color = Color(0xFF4CAF50),
                        fontWeight = FontWeight.Normal
                    )
                } else {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun CircleDealsMegaDealCard(product: Product, modifier: Modifier = Modifier, onClick: () -> Unit = {}) {
    var localStock by remember { mutableStateOf(product.stockQuantity) }
    var isFavorite by remember { mutableStateOf(false) }
    
    // Timer state
    var timeLeft by remember { mutableLongStateOf(24 * 60 * 60 * 1000L) } // 24 hours in ms
    LaunchedEffect(Unit) {
        while(timeLeft > 0) {
            delay(1000)
            timeLeft -= 1000
        }
    }
    
    val hours = (timeLeft / (1000 * 60 * 60)).toInt()
    val minutes = ((timeLeft / (1000 * 60)) % 60).toInt()
    val seconds = ((timeLeft / 1000) % 60).toInt()
    val timeString = String.format("%02d : %02d : %02d", hours, minutes, seconds)
    
    Card(
        modifier = modifier
            .height(310.dp)
            .clickable { 
                onClick()
                localStock?.let { if (it > 0) localStock = it - 1 }
            },
        shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF4CAF50)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(Color.White)
            ) {
                coil.compose.AsyncImage(
                    model = product.imageUrl,
                    contentDescription = product.name,
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                
                // Timer Chip on Top
                Box(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 8.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFE53935).copy(alpha = 0.9f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = timeString,
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                if (product.discount != null) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .clip(androidx.compose.foundation.shape.RoundedCornerShape(bottomEnd = 12.dp))
                            .background(Color(0xFF2E7D32).copy(alpha = 0.9f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "-${product.discount}%",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 10.dp, end = 10.dp)
                        .size(26.dp)
                        .clip(androidx.compose.foundation.shape.CircleShape)
                        .background(if (isFavorite) Color.Red else Color.White.copy(alpha = 0.8f))
                        .clickable { isFavorite = !isFavorite },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (isFavorite) Color.White else Color.Black.copy(alpha = 0.7f),
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
            
            Column(modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(10.dp)
            ) {
                Text(
                    text = product.name,
                    fontSize = 12.sp,
                    maxLines = 2,
                    minLines = 2,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                    lineHeight = 15.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Medium
                )
                
                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = "৳ ${product.price.toInt()}",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2E7D32)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    if (product.oldPrice != null) {
                        Text(
                            text = "৳ ${product.oldPrice.toInt()}",
                            fontSize = 11.sp,
                            color = Color.Gray,
                            textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough,
                            modifier = Modifier.padding(bottom = 1.dp)
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Filled.Star,
                            contentDescription = "Rating",
                            tint = Color(0xFFFFB300),
                            modifier = Modifier.size(13.dp)
                        )
                        Text(
                            text = " ${product.rating} ",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Text(
                            text = "(${product.soldCount})",
                            fontSize = 10.sp,
                            color = Color.Gray
                        )
                    }
                    
                    Box(
                        modifier = Modifier
                            .size(26.dp)
                            .clip(androidx.compose.foundation.shape.RoundedCornerShape(6.dp))
                            .background(Color(0xFF2E7D32))
                            .clickable { },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Outlined.ShoppingCart,
                            contentDescription = "Add to cart",
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))
                
                if (localStock != null) {
                    androidx.compose.material3.LinearProgressIndicator(
                        progress = { (localStock ?: 0).toFloat() / 50f },
                        modifier = Modifier.fillMaxWidth().height(4.dp).clip(androidx.compose.foundation.shape.CircleShape),
                        color = Color(0xFF4CAF50),
                        trackColor = Color(0xFFE0E0E0),
                        strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Text(
                        text = "Only $localStock Left",
                        fontSize = 10.sp,
                        color = Color(0xFF4CAF50),
                        fontWeight = FontWeight.Normal
                    )
                } else {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}
