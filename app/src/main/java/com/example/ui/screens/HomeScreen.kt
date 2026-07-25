package com.example.ui.screens

import android.content.Intent
import android.speech.RecognizerIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.animateColor
import androidx.compose.ui.draw.scale
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.BorderStroke
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.R
import kotlinx.coroutines.delay
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.Chair
import androidx.compose.material.icons.filled.FaceRetouchingNatural
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.filled.Toys
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.ui.graphics.vector.ImageVector

import androidx.compose.ui.platform.LocalConfiguration
import androidx.navigation.NavController

data class Product(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val oldPrice: Double?,
    val price: Double,
    val rating: Double,
    val soldCount: Int,
    val discount: Int? = null
)

val sampleProducts = listOf(
    Product(1, "Premium Wireless Headphones with Noise Cancellation", "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?q=80&w=600&auto=format&fit=crop", 299.99, 199.99, 4.8, 1240, 33),
    Product(2, "Minimalist Leather Watch", "https://images.unsplash.com/photo-1523275335684-37898b6baf30?q=80&w=600&auto=format&fit=crop", null, 149.00, 4.5, 850),
    Product(3, "Smart Fitness Tracker", "https://images.unsplash.com/photo-1575311373937-040b8e1fd5b6?q=80&w=600&auto=format&fit=crop", 89.99, 59.99, 4.2, 3200, 33),
    Product(4, "Ergonomic Office Chair", "https://images.unsplash.com/photo-1505843490538-5133c6c7d0e1?q=80&w=600&auto=format&fit=crop", 399.00, 249.00, 4.9, 420, 37),
    Product(5, "Professional DSLR Camera", "https://images.unsplash.com/photo-1516035069371-29a1b244cc32?q=80&w=600&auto=format&fit=crop", 1299.00, 999.00, 4.7, 150, 23),
    Product(6, "Wireless Gaming Mouse", "https://images.unsplash.com/photo-1527864550417-7fd91fc51a46?q=80&w=600&auto=format&fit=crop", 79.99, 49.99, 4.6, 5600, 37)
)

data class Category(val name: String, val icon: ImageVector)

val sampleCategories = listOf(
    Category("All", Icons.Default.Dashboard),
    Category("Electronics", Icons.Default.PhoneAndroid),
    Category("Fashion", Icons.Default.Checkroom),
    Category("Home", Icons.Default.Chair),
    Category("Beauty", Icons.Default.FaceRetouchingNatural),
    Category("Sports", Icons.Default.SportsSoccer),
    Category("Toys", Icons.Default.Toys)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }
    val lazyListState = rememberLazyListState()
    
    val showStickyHeader by remember {
        derivedStateOf { lazyListState.firstVisibleItemIndex > 0 || lazyListState.firstVisibleItemScrollOffset > 50 }
    }

    val sampleBanners = listOf(
        R.drawable.img_flash_sale_banner_1784886111339,
        R.drawable.img_flash_sale_banner_1784886111339,
        R.drawable.img_flash_sale_banner_1784886111339,
        R.drawable.img_flash_sale_banner_1784886111339,
        R.drawable.img_flash_sale_banner_1784886111339
    )

    val pagerState = rememberPagerState(pageCount = { sampleBanners.size })
    
    LaunchedEffect(pagerState) {
        while (true) {
            delay(3000)
            val nextPage = (pagerState.currentPage + 1) % sampleBanners.size
            pagerState.animateScrollToPage(nextPage)
        }
    }

    val searchHints = listOf("Laptop", "Smartphone", "Headphones", "Sneakers", "Watch", "Camera")
    var currentHintIndex by remember { mutableStateOf(0) }
    
    LaunchedEffect(Unit) {
        while (true) {
            delay(2000)
            currentHintIndex = (currentHintIndex + 1) % searchHints.size
        }
    }
    
    val dynamicHint = "Search for ${searchHints[currentHintIndex]}"

    val speechRecognizerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            val matches = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            val spokenText = matches?.firstOrNull()
            if (!spokenText.isNullOrEmpty()) {
                navController.navigate("search?hint=$dynamicHint&query=$spokenText")
            }
        }
    }

    val circleDealsProducts = remember { sampleProducts.shuffled().take(4) }
    val forYouProducts = remember { sampleProducts.shuffled() }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            state = lazyListState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            item {
                Box(modifier = Modifier.fillMaxWidth()) {
                    // Flash Sale Banner Pager
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.fillMaxWidth()
                    ) { page ->
                        Image(
                            painter = painterResource(id = sampleBanners[page]),
                            contentDescription = "Flash Sale Banner",
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(4f/3f),
                            contentScale = ContentScale.Crop
                        )
                    }
                    
                    // Search Bar overlay on the banner
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .windowInsetsPadding(WindowInsets.statusBars)
                            .padding(start = 16.dp, end = 16.dp, top = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp)
                                .background(Color.White, androidx.compose.foundation.shape.RoundedCornerShape(50))
                                .padding(horizontal = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                                    .clickable(
                                        interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
                                        indication = null
                                    ) { navController.navigate("search?hint=$dynamicHint") },
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(start = 12.dp)
                                ) {
                                    Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.Gray, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(dynamicHint, color = Color.Gray, fontSize = 13.sp)
                                }
                            }
                            
                            IconButton(onClick = { navController.navigate("lens") }, modifier = Modifier.size(32.dp)) {
                                Icon(Icons.Default.CameraAlt, contentDescription = "Lens", tint = Color.Gray, modifier = Modifier.size(20.dp))
                            }
                            IconButton(
                                onClick = { 
                                    val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                                        putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                                    }
                                    try {
                                        speechRecognizerLauncher.launch(intent)
                                    } catch (e: Exception) {
                                        // Fallback if no speech recognizer exists
                                        navController.navigate("search?hint=$dynamicHint")
                                    }
                                },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(Icons.Default.Mic, contentDescription = "Mic", tint = Color.Gray, modifier = Modifier.size(20.dp))
                            }
                            Spacer(modifier = Modifier.width(4.dp))
                        }
                    }
                }
            }
        
        item {
            SectionTitle("Categories")
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(sampleCategories) { category ->
                    CategoryItem(category)
                }
            }
        }
        
        item {
            AnimatedSectionTitle(
                title = "Circle Deals",
                actionText = "Shop More",
                onActionClick = { navController.navigate("circle_deals?productId=") }
            )
            LazyRow(
                contentPadding = PaddingValues(horizontal = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(circleDealsProducts) { product ->
                    CircleDealCard(
                        product = product,
                        modifier = Modifier.width(140.dp),
                        onClick = { navController.navigate("circle_deals?productId=${product.id}") }
                    )
                }
            }
        }
        
        item {
            SectionTitle("Just For You")
        }
        
        // Grid implementation using pairs for LazyColumn
        items(forYouProducts.chunked(2)) { rowProducts ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                for (product in rowProducts) {
                    ProductCard(
                        product = product,
                        modifier = Modifier.weight(1f)
                    )
                }
                // Fill empty space if odd number
                if (rowProducts.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    } // Close LazyColumn
        
    // Sticky Header Overlay
    AnimatedVisibility(
        visible = showStickyHeader,
        enter = fadeIn() + slideInVertically(initialOffsetY = { -it }),
        exit = fadeOut() + slideOutVertically(targetOffsetY = { -it })
    ) {
        Surface(
            color = Color.White,
            shadowElevation = 4.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.statusBars)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Logo
                Icon(
                    painter = painterResource(id = R.drawable.img_circle_bazar_icon_1784886096683),
                    contentDescription = "Logo",
                    modifier = Modifier.size(24.dp).clip(CircleShape),
                    tint = Color.Unspecified
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Circle",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2E7D32)
                )
                
                Spacer(modifier = Modifier.weight(1f))
                
                // Search Icon
                Icon(
                    Icons.Default.Search,
                    contentDescription = "Search",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { navController.navigate("search?hint=$dynamicHint") },
                    tint = Color.Black
                )
                
                Spacer(modifier = Modifier.width(16.dp))
                
                // Notification Icon
                Icon(
                    Icons.Outlined.Notifications,
                    contentDescription = "Notifications",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { },
                    tint = Color.Black
                )
            }
        }
    }
    } // Close Box
}

@Composable
fun SectionTitle(title: String, actionText: String = "See All", onActionClick: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = actionText,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.clickable { onActionClick() }
        )
    }
}

@Composable
fun CategoryItem(category: Category) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(4.dp)
    ) {
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .clickable { }
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = category.icon,
                    contentDescription = category.name,
                    modifier = Modifier.size(28.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = category.name,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
fun ProductCard(product: Product, modifier: Modifier = Modifier, onClick: () -> Unit = {}) {
    var isFavorite by remember { mutableStateOf(false) }

    Card(
        modifier = modifier.clickable { onClick() }.height(270.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFF4CAF50)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(Color.White)
            ) {
                AsyncImage(
                    model = product.imageUrl,
                    contentDescription = product.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                )
                
                if (product.discount != null) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .clip(RoundedCornerShape(bottomEnd = 12.dp))
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
                        .clip(CircleShape)
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
                .padding(12.dp)
            ) {
                Text(
                    text = product.name,
                    fontSize = 13.sp,
                    maxLines = 2,
                    minLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 16.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Medium
                )
                
                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = "৳ ${product.price.toInt()}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2E7D32)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    if (product.oldPrice != null) {
                        Text(
                            text = "৳ ${product.oldPrice.toInt()}",
                            fontSize = 12.sp,
                            color = Color.Gray,
                            textDecoration = TextDecoration.LineThrough,
                            modifier = Modifier.padding(bottom = 2.dp)
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(6.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Filled.Star,
                            contentDescription = "Rating",
                            tint = Color(0xFFFFB300),
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = " ${product.rating} ",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Text(
                            text = "(${product.soldCount}) | Sold ${if (product.soldCount > 1000) "${product.soldCount / 1000}k+" else "${product.soldCount}+"}",
                            fontSize = 10.sp,
                            color = Color.Gray
                        )
                    }
                    
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF2E7D32))
                            .clickable { },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Outlined.ShoppingCart,
                            contentDescription = "Add to cart",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AnimatedSectionTitle(title: String, actionText: String = "See All", onActionClick: () -> Unit = {}) {
    val infiniteTransition = androidx.compose.animation.core.rememberInfiniteTransition()
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = androidx.compose.animation.core.infiniteRepeatable(
            animation = androidx.compose.animation.core.tween(1000, easing = androidx.compose.animation.core.FastOutSlowInEasing),
            repeatMode = androidx.compose.animation.core.RepeatMode.Reverse
        )
    )
    val color by infiniteTransition.animateColor(
        initialValue = Color(0xFF2E7D32),
        targetValue = Color(0xFF4CAF50),
        animationSpec = androidx.compose.animation.core.infiniteRepeatable(
            animation = androidx.compose.animation.core.tween(2000, easing = androidx.compose.animation.core.LinearEasing),
            repeatMode = androidx.compose.animation.core.RepeatMode.Reverse
        )
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.ExtraBold,
            color = color,
            modifier = Modifier.scale(scale)
        )
        Text(
            text = actionText,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.clickable { onActionClick() }
        )
    }
}

@Composable
fun CircleDealCard(product: Product, modifier: Modifier = Modifier, onClick: () -> Unit = {}) {
    var quantityLeft by remember { mutableIntStateOf(10) }
    var isFavorite by remember { mutableStateOf(false) }
    
    androidx.compose.animation.AnimatedVisibility(
        visible = quantityLeft > 0,
        exit = androidx.compose.animation.shrinkOut() + androidx.compose.animation.fadeOut()
    ) {
        Card(
            modifier = modifier
                .height(240.dp)
                .clickable { 
                    onClick()
                    if (quantityLeft > 0) {
                        quantityLeft--
                    }
                },
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, Color(0xFF4CAF50)),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .background(Color.White)
                ) {
                    AsyncImage(
                        model = product.imageUrl,
                        contentDescription = product.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    
                    if (product.discount != null) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .clip(RoundedCornerShape(bottomEnd = 12.dp))
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
                            .clip(CircleShape)
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
                        overflow = TextOverflow.Ellipsis,
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
                                textDecoration = TextDecoration.LineThrough,
                                modifier = Modifier.padding(bottom = 1.dp)
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(6.dp))
                    
                    androidx.compose.material3.LinearProgressIndicator(
                        progress = { quantityLeft / 10f },
                        modifier = Modifier.fillMaxWidth().height(4.dp).clip(CircleShape),
                        color = Color(0xFF4CAF50),
                        trackColor = Color(0xFFE0E0E0),
                        strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Text(
                        text = "Only $quantityLeft Left",
                        fontSize = 10.sp,
                        color = Color.Gray,
                        fontWeight = FontWeight.Normal
                    )
                }
            }
        }
    }
}
