package com.example.clinexusapp.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.clinexusapp.ui.components.*
import com.example.clinexusapp.ui.theme.*
import com.example.clinexusapp.util.Resource
import com.example.clinexusapp.viewmodel.RegisterViewModel

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel,
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    var firstName by remember { mutableStateOf("") }
    var middleName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var sex by remember { mutableStateOf("Male") }
    var dateOfBirth by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    val registerState by viewModel.registerState.collectAsState()
    val validationError by viewModel.validationError.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(registerState, validationError) {
        if (registerState is Resource.Success) {
            onRegisterSuccess()
            viewModel.resetState()
        } else if (registerState is Resource.Error) {
            snackbarHostState.showSnackbar(registerState?.message ?: "Registration failed")
        }
        
        validationError?.let {
            snackbarHostState.showSnackbar(it)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp),
            contentPadding = PaddingValues(top = 40.dp, bottom = 40.dp)
        ) {
            item {
                Text(
                    text = "Registration",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Join our network of elite care",
                    fontSize = 15.sp,
                    color = SlateGray
                )
            }

            item {
                SectionTitle("Identity Details")
                NeumorphicCard {
                    MintTextField(value = firstName, onValueChange = { firstName = it }, label = "First Name", icon = Icons.Default.Person)
                    Spacer(modifier = Modifier.height(16.dp))
                    MintTextField(value = middleName, onValueChange = { middleName = it }, label = "Middle Name", icon = Icons.Default.Badge)
                    Spacer(modifier = Modifier.height(16.dp))
                    MintTextField(value = lastName, onValueChange = { lastName = it }, label = "Last Name", icon = Icons.Default.Person)
                    
                    Spacer(modifier = Modifier.height(20.dp))
                    Text("Select Gender", style = MaterialTheme.typography.labelMedium, color = RoyalNavy, fontWeight = FontWeight.Bold)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(selected = sex == "Male", onClick = { sex = "Male" }, colors = RadioButtonDefaults.colors(selectedColor = DeepTeal))
                        Text("Male", fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(16.dp))
                        RadioButton(selected = sex == "Female", onClick = { sex = "Female" }, colors = RadioButtonDefaults.colors(selectedColor = DeepTeal))
                        Text("Female", fontSize = 14.sp)
                    }
                }
            }

            item {
                SectionTitle("Network Details")
                NeumorphicCard {
                    MintTextField(value = email, onValueChange = { email = it }, label = "Email Address", icon = Icons.Default.Email)
                    Spacer(modifier = Modifier.height(16.dp))
                    MintTextField(value = phoneNumber, onValueChange = { phoneNumber = it }, label = "Mobile Number", icon = Icons.Default.Phone)
                    Spacer(modifier = Modifier.height(16.dp))
                    MintTextField(value = dateOfBirth, onValueChange = { dateOfBirth = it }, label = "Birthday (YYYY-MM-DD)", icon = Icons.Default.CalendarToday)
                }
            }

            item {
                SectionTitle("Security")
                NeumorphicCard {
                    MintTextField(value = password, onValueChange = { password = it }, label = "Account Password", icon = Icons.Default.Lock, isPassword = true)
                    Spacer(modifier = Modifier.height(16.dp))
                    MintTextField(value = confirmPassword, onValueChange = { confirmPassword = it }, label = "Verify Password", icon = Icons.Default.LockReset, isPassword = true)
                }
            }

            item {
                VibrantButton(
                    text = if (registerState is Resource.Loading) "Processing..." else "Create Account",
                    onClick = { viewModel.register(email, password, confirmPassword, firstName, middleName, lastName, phoneNumber, dateOfBirth, sex) },
                    enabled = registerState !is Resource.Loading
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                    Text("Have an account? ", color = SlateGray)
                    TextButton(onClick = onNavigateToLogin) {
                        Text("Sign In", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }
    }
}
