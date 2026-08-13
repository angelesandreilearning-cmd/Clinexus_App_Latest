package com.example.clinexusapp.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.clinexusapp.model.*
import com.example.clinexusapp.ui.components.*
import com.example.clinexusapp.ui.theme.*
import com.example.clinexusapp.util.Resource
import com.example.clinexusapp.viewmodel.RegisterViewModel

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel,
    onRegisterSuccess: (String) -> Unit,
    onNavigateToLogin: () -> Unit
) {
    var firstName by remember { mutableStateOf("") }
    var middleName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var dateOfBirth by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    // Address State
    var streetAddress by remember { mutableStateOf("") }
    var selectedRegion by remember { mutableStateOf<Region?>(null) }
    var selectedProvince by remember { mutableStateOf<Province?>(null) }
    var selectedCity by remember { mutableStateOf<City?>(null) }
    var selectedBarangay by remember { mutableStateOf<Barangay?>(null) }

    val regions by viewModel.regions.collectAsState()
    val provinces by viewModel.provinces.collectAsState()
    val cities by viewModel.cities.collectAsState()
    val barangays by viewModel.barangays.collectAsState()
    
    val registerState by viewModel.registerState.collectAsState()
    val validationError by viewModel.validationError.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(registerState, validationError) {
        if (registerState is Resource.Success) {
            onRegisterSuccess(email)
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
                    SectionTitle("Address Information")
                    MintTextField(value = streetAddress, onValueChange = { streetAddress = it }, label = "Street Address", icon = Icons.Default.Home)
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    AddressDropdown(
                        label = "Region",
                        options = regions.map { it.regionName },
                        selectedOption = selectedRegion?.regionName ?: "",
                        onOptionSelected = { name ->
                            val region = regions.find { it.regionName == name }
                            selectedRegion = region
                            selectedProvince = null
                            selectedCity = null
                            selectedBarangay = null
                            region?.let { viewModel.onRegionSelected(it.code) }
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    AddressDropdown(
                        label = "Province",
                        options = provinces.map { it.name },
                        selectedOption = selectedProvince?.name ?: "",
                        onOptionSelected = { name ->
                            val province = provinces.find { it.name == name }
                            selectedProvince = province
                            selectedCity = null
                            selectedBarangay = null
                            province?.let { viewModel.onProvinceSelected(it.code) }
                        },
                        enabled = selectedRegion != null
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    AddressDropdown(
                        label = "City / Municipality",
                        options = cities.map { it.name },
                        selectedOption = selectedCity?.name ?: "",
                        onOptionSelected = { name ->
                            val city = cities.find { it.name == name }
                            selectedCity = city
                            selectedBarangay = null
                            city?.let { viewModel.onCitySelected(it.code) }
                        },
                        enabled = selectedProvince != null
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    AddressDropdown(
                        label = "Barangay",
                        options = barangays.map { it.name },
                        selectedOption = selectedBarangay?.name ?: "",
                        onOptionSelected = { name ->
                            selectedBarangay = barangays.find { it.name == name }
                        },
                        enabled = selectedCity != null
                    )
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
                    onClick = {
                        viewModel.register(
                            email, password, confirmPassword, firstName, middleName, lastName, phoneNumber, dateOfBirth,
                            streetAddress, selectedProvince?.name ?: "", selectedCity?.name ?: "", selectedBarangay?.name ?: ""
                        )
                    },
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressDropdown(
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    enabled: Boolean = true
) {
    var expanded by remember { mutableStateOf(false) }
    
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            modifier = Modifier.padding(start = 6.dp, bottom = 8.dp)
        )
        ExposedDropdownMenuBox(
            expanded = expanded && enabled,
            onExpandedChange = { if (enabled) expanded = !expanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = selectedOption,
                onValueChange = {},
                readOnly = true,
                placeholder = { Text("Select $label", fontSize = 14.sp) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                enabled = enabled,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.05f),
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                    disabledContainerColor = Color.LightGray.copy(alpha = 0.1f),
                    disabledBorderColor = Color.Transparent,
                    disabledTextColor = Color.Gray
                )
            )
            ExposedDropdownMenu(
                expanded = expanded && enabled,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(MaterialTheme.colorScheme.surface)
            ) {
                options.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = { Text(selectionOption) },
                        onClick = {
                            onOptionSelected(selectionOption)
                            expanded = false
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                    )
                }
            }
        }
    }
}
