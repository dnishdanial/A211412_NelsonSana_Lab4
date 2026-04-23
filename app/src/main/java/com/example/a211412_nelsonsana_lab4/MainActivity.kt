package com.example.a211412_nelsonsana_lab4

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.a211412_nelsonsana_lab4.ui.theme.A211412_NelsonSana_Lab4Theme
import com.example.a211412_nelsonsana_lab4.ui.theme.primaryContainerLight
import com.example.a211412_nelsonsana_lab4.ui.theme.primaryLight


// Define colors here if they aren't in your Theme file yet



data class NearItem(
    val title: String,
    val price: String,
    val storeName: String,
    val imageRes: Int,
    val description: String
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            A211412_NelsonSana_Lab4Theme {
                KasihNavigation()
            }
        }
    }
}

@Composable
fun MainScreenContent(navController: NavController, viewModel: UserViewModel, innerPadding: PaddingValues) {
    var searchQuery by remember { mutableStateOf("") }
    var userName by remember { mutableStateOf("") } // For the name input
    var isVerified by remember { mutableStateOf(false) } // Track verification
    var greetingText by remember { mutableStateOf("Welcome to KASIH") } // Dynamic UI update
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            // rememberScrollState() allows the Column to scroll vertically
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        if (searchQuery.isEmpty()) {
            Text(
                text = greetingText,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF800000), // Maroon Theme
                modifier = Modifier.padding(bottom = 8.dp)
            )

            if (!isVerified) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = userName,
                        onValueChange = { userName = it },
                        label = { Text("Enter Name") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = {
                            if (userName.isNotEmpty()) {
                                viewModel.updateName(userName)
                                greetingText = "Welcome to KASIH, ${viewModel.userState.name}!"
                                isVerified = true // Hide the input after verification
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF800000))
                    ) {
                        Text("Verify")
                    }
                }
            } else {
                // Show recommendations once the user has entered their name
                Text(
                    "Recommended for you, $userName!",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color(0xFF800000), // Maroon Theme
                    modifier = Modifier.padding(top = 8.dp)
                )
                RecommendationItem(
                    "Warung Kak Kiah",
                    "Laksa Utara",
                    R.drawable.pic_laksa,
                    "Original Laksa Kedah with fresh mackerel and rice noodles.",
                    onClick = { navController.navigate("details/Laksa Utara") }
                )
                RecommendationItem(
                    "Dapur Nenek",
                    "Bubur Lambuk",
                    R.drawable.pic_bubur,
                    "Creamy spiced rice porridge, a Ramadan favorite from Nenek's kitchen.",
                    onClick = { navController.navigate("details/Bubur Lambuk") }
                )
            }
        }

        // 1. Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = {
                searchQuery = it // Updates the state as you type
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            placeholder = {
                Text("Search stores and food", color = Color.Gray)
            },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray)
            },
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = primaryContainerLight,
                unfocusedContainerColor = primaryContainerLight,
                focusedBorderColor = primaryLight,
                unfocusedBorderColor = Color.Transparent
            ),
            singleLine = true
        )

        if (searchQuery.isEmpty()) {
            // 2. Story Section
            StorySection()

            // 3. Banner
            Card(
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    // 1. The Background Image (Bottom Layer)
                    Image(
                        painter = painterResource(id = R.drawable.pic_hotspot), // Change to your drawable name
                        contentDescription = "Lunch Bento Banner",
                        contentScale = ContentScale.Crop, // This ensures the image fills the 160.dp height
                        modifier = Modifier.fillMaxSize()
                    )

                    // 2. Optional: A Dark Overlay (Middle Layer)
                    // This makes the text much easier to read if the image is bright
                    Surface(
                        color = Color.Black.copy(alpha = 0.3f),
                        modifier = Modifier.fillMaxSize()
                    ) {}

                    // 3. The Text (Top Layer)
                    Text(
                        text = "CLICK TO FIND HOTSPOT",
                        color = Color.White, // White text looks better on image backgrounds
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        modifier = Modifier
                            .align(Alignment.Center) // Centers the text inside the Box
                            .padding(16.dp)
                    )
                    Text(
                        text = "Free Food",
                        color = Color.White, // White text looks better on image backgrounds
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        modifier = Modifier
                            // Centers the text inside the Box
                            .padding(16.dp)
                    )
                }
            }

            // 4. Categories Section
            Text(
                "Categories",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Replace these with your actual image filenames in res/drawable
                CategoryItem("Free", R.drawable.pic_mystery)
                CategoryItem("Dine-In", R.drawable.pic_dinein)
                CategoryItem("Meals", R.drawable.pic_meals)
                CategoryItem("Desserts", R.drawable.pic_dessert)
            }
        }

        // 5. Near to You Section (The Horizontal Cards)
        Text(
            if (searchQuery.isEmpty()) "Near to you" else "Search Results",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
        )
        NearToYouSection(searchQuery, navController)
    }
}

@Composable
fun KasihNavigation() {
    val navController = rememberNavController()
    val viewModel: UserViewModel = viewModel()

    Scaffold(
        bottomBar = { AppBottomNavigation(navController) }
    ) { innerPadding ->
        NavHost(navController = navController, startDestination = "home", modifier = Modifier.padding(innerPadding)) {
            composable("home") {
                MainScreenContent(navController, viewModel, PaddingValues(0.dp))
            }
            composable("add") {
                AddScreen(navController)
            }
            composable("profile") {
                ProfileScreen(navController, viewModel)
            }
            composable("details/{foodName}") { backStackEntry ->
                val foodName = backStackEntry.arguments?.getString("foodName") ?: ""
                FoodDetailsScreen(navController, foodName)
            }
        }
    }
}

@Composable
fun ProfileScreen(navController: NavController, viewModel: UserViewModel) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = viewModel.userState.profileImage),
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .border(2.dp, primaryLight, CircleShape),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text("Profile Screen", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Name: ${viewModel.userState.name}")
        Text("Rescued Meals: ${viewModel.userState.rescuedMeals}")
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { navController.popBackStack() },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF800000))
        ) {
            Text("Back")
        }
    }
}

@Composable
fun AddScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Default.AddCircle,
            contentDescription = null,
            modifier = Modifier.size(100.dp),
            tint = Color(0xFF800000)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text("Add New Item", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Share your food with others!", fontSize = 16.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = { navController.popBackStack() },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF800000))
        ) {
            Text("Back")
        }
    }
}

@Composable
fun FoodDetailsScreen(navController: NavController, foodName: String) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Food Details: $foodName", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.popBackStack() }) {
            Text("Back")
        }
    }
}

@Composable
fun RecommendationItem(shopName: String, foodName: String, imageRes: Int, description: String, onClick: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() }
            .animateContentSize(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = primaryLight)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(text = foodName, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(text = "from $shopName", fontSize = 14.sp, color = Color.Gray)
                    Text(
                        text = if (expanded) "Show less" else "Show more",
                        fontSize = 12.sp,
                        color = primaryLight,
                        modifier = Modifier.clickable { expanded = !expanded }
                    )
                }
            }
            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
fun StoreCard(item: NearItem, modifier: Modifier = Modifier, onClick: () -> Unit) {
    // 1. State to track if the card is expanded
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .padding(bottom = 16.dp)
            .clickable { onClick() } // Toggle state on click
            .animateContentSize(), // 2. Subtle "Stretch" animation
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column {
            Image(
                painter = painterResource(id = item.imageRes),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth().height(120.dp)
            )

            Column(modifier = Modifier.padding(12.dp)) {
                Text(text = item.title, fontWeight = FontWeight.Bold)

                Text(
                    text = if (expanded) "Show less" else "Show more",
                    fontSize = 12.sp,
                    color = primaryLight,
                    modifier = Modifier.clickable { expanded = !expanded }
                )

                // 3. Conditional content that appears when clicked
                if (expanded) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = item.description,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Text(text = item.price, color = primaryLight, fontWeight = FontWeight.Bold)
                Text(text = item.storeName, fontSize = 12.sp, color = Color.Gray)
            }
        }
    }
}

@Composable
fun StorySection() {
    // 1. Define the list of your 5 different images
    val storyImages = listOf(
        R.drawable.pic_1, // Replace these with your actual drawable names
        R.drawable.pic_2,
        R.drawable.pic_3,
        R.drawable.pic_4,
        R.drawable.pic_5
    )

    LazyRow(
        modifier = Modifier.padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 2. Use the size of your list
        items(storyImages.size) { index ->
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .border(2.dp, primaryLight, CircleShape)
                    .padding(3.dp)
            ) {
                Image(
                    // 3. Use the index to grab a different image for each circle
                    painter = painterResource(id = storyImages[index]),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                )
            }
        }
    }
}

@Composable
fun CategoryItem(name: String, iconRes: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Surface(
            shape = CircleShape,
            color = primaryLight,
            modifier = Modifier.size(60.dp)
        ) {
            // Use Image instead of Icon for full-color pictures
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = name,
                contentScale = ContentScale.Crop, // This makes the picture fill the circle
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape) // Ensures the image stays inside the circle
            )
        }
        Text(text = name, fontSize = 11.sp, modifier = Modifier.padding(top = 4.dp))
    }
}



@Composable
fun NearToYouSection(searchQuery: String, navController: NavController) {
    // 1. Create your list of different items
    val nearItems = listOf(
        NearItem("Nasi Lemak Ayam", "25 left", "Warung leha", R.drawable.pic_nl, "Delicious Malaysian coconut rice served with fried chicken and sambal."),
        NearItem("Nasi Kerabu Ayam", "10 left", "Moknab", R.drawable.pic_nk, "Authentic blue rice from Kelantan with flavorful herbs and chicken."),
        NearItem("Nasi Minyak", "15 left", "Nasi Ahmed", R.drawable.pic_nm, "Traditional fragrant rice cooked with ghee and aromatic spices."),
        NearItem("Chicken Chop", "5 left", "Western Corner", R.drawable.pic_1, "Golden crispy chicken chop with savory black pepper gravy."),
        NearItem("Burger Bakar", "20 left", "Abang Burn", R.drawable.pic_2, "Thick, juicy grilled patty with melted cheese and fresh greens.")
    )

    // Filter items based on searchQuery
    val filteredItems = nearItems.filter {
        it.title.contains(searchQuery, ignoreCase = true) ||
                it.storeName.contains(searchQuery, ignoreCase = true)
    }

    if (searchQuery.isEmpty()) {
        LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            items(filteredItems) { item ->
                StoreCard(item, modifier = Modifier.width(250.dp), onClick = { navController.navigate("details/${item.title}") })
            }
        }
    } else {
        // Show as a vertical list when searching
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            filteredItems.forEach { item ->
                StoreCard(item, modifier = Modifier.fillMaxWidth(), onClick = { navController.navigate("details/${item.title}") })
            }
        }
    }
}

@Composable
fun AppBottomNavigation(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.primary, // Dark blue from your image
        contentColor = Color.White
    ) {
        NavigationBarItem(
            selected = currentRoute == "home",
            onClick = {
                if (currentRoute != "home") {
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            },
            icon = { Icon(Icons.Default.Home, contentDescription = null, tint = Color.White) },
            label = { Text("Home", color = Color.White) }
        )
        NavigationBarItem(
            selected = currentRoute == "add",
            onClick = {
                if (currentRoute != "add") {
                    navController.navigate("add")
                }
            },
            icon = {
                Icon(
                    Icons.Default.AddCircle,
                    contentDescription = "Add",
                    tint = Color.White,
                    modifier = Modifier.size(40.dp) // Making the middle icon larger
                )
            },
            label = { Text("Add", color = Color.White) }
        )
        NavigationBarItem(
            selected = currentRoute == "profile",
            onClick = {
                if (currentRoute != "profile") {
                    navController.navigate("profile")
                }
            },
            icon = { Icon(Icons.Default.Person, contentDescription = null, tint = Color.White) },
            label = { Text("Profile", color = Color.White) }
        )
    }
}

@Preview()
@Composable
fun MainPreview() {
    val navController = rememberNavController()
    val viewModel: UserViewModel = viewModel()
    A211412_NelsonSana_Lab4Theme {
        Scaffold(
            // ADD THIS LINE TO THE PREVIEW AS WELL
            bottomBar = { AppBottomNavigation(navController) }
        ) { innerPadding ->
            MainScreenContent(navController, viewModel, innerPadding)
        }
    }
}
