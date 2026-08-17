package com.example.clinexusapp.ui.screens.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.clinexusapp.model.UpdateProfileRequest
import com.example.clinexusapp.ui.components.*
import com.example.clinexusapp.ui.screens.auth.AddressDropdown
import com.example.clinexusapp.util.Resource
import com.example.clinexusapp.util.SessionManager
import com.example.clinexusapp.viewmodel.ProfileViewModel

@Composable
fun PersonalInformationScreen(onBack: () -> Unit, viewModel: ProfileViewModel) {
    val user by SessionManager.currentUser.collectAsState()
    val updateState by viewModel.updateState.collectAsState()
    
    var firstName by remember { mutableStateOf(user?.firstName ?: "") }
    var middleName by remember { mutableStateOf(user?.middleName ?: "") }
    var lastName by remember { mutableStateOf(user?.lastName ?: "") }
    var phoneNumber by remember { mutableStateOf(user?.phoneNumber ?: "") }
    var dateOfBirth by remember { mutableStateOf(user?.dateOfBirth ?: "") }
    var streetAddress by remember { mutableStateOf(user?.streetAddress ?: "") }
    var province by remember { mutableStateOf(user?.province ?: "") }
    var city by remember { mutableStateOf(user?.city ?: "") }
    var barangay by remember { mutableStateOf(user?.barangay ?: "") }

    val regions by viewModel.regions.collectAsState()
    val provinces by viewModel.provinces.collectAsState()
    val cities by viewModel.cities.collectAsState()
    val barangays by viewModel.barangays.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(user) {
        user?.let {
            firstName = it.firstName ?: ""
            middleName = it.middleName ?: ""
            lastName = it.lastName ?: ""
            phoneNumber = it.phoneNumber ?: ""
            dateOfBirth = it.dateOfBirth ?: ""
            streetAddress = it.streetAddress ?: ""
            province = it.province ?: ""
            city = it.city ?: ""
            barangay = it.barangay ?: ""
        }
    }

    LaunchedEffect(updateState) {
        if (updateState is Resource.Success) {
            snackbarHostState.showSnackbar("Profile updated successfully")
            viewModel.resetState()
        } else if (updateState is Resource.Error) {
            snackbarHostState.showSnackbar(updateState?.message ?: "Update failed")
        }
    }

    Scaffold(
        topBar = { ElegantTopAppBar(title = "Personal Information", onBack = onBack) },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 24.dp)
        ) {
            item {
                SectionTitle("Personal Details")
                NeumorphicCard {
                    MintTextField(value = firstName, onValueChange = { firstName = it }, label = "First Name", icon = Icons.Default.Person)
                    Spacer(modifier = Modifier.height(12.dp))
                    MintTextField(value = middleName, onValueChange = { middleName = it }, label = "Middle Name", icon = Icons.Default.Badge)
                    Spacer(modifier = Modifier.height(12.dp))
                    MintTextField(value = lastName, onValueChange = { lastName = it }, label = "Last Name", icon = Icons.Default.Person)
                    Spacer(modifier = Modifier.height(12.dp))
                    MintTextField(value = phoneNumber, onValueChange = { phoneNumber = it }, label = "Phone Number", icon = Icons.Default.Phone)
                    Spacer(modifier = Modifier.height(12.dp))
                    MintTextField(value = dateOfBirth, onValueChange = { dateOfBirth = it }, label = "Date of Birth (YYYY-MM-DD)", icon = Icons.Default.CalendarToday)
                }
            }
            item {
                SectionTitle("Address")
                NeumorphicCard {
                    MintTextField(value = streetAddress, onValueChange = { streetAddress = it }, label = "Street Address", icon = Icons.Default.Home)
                    Spacer(modifier = Modifier.height(12.dp))
                    AddressDropdown(
                        label = "Province",
                        options = provinces.map { it.name },
                        selectedOption = province,
                        onOptionSelected = { name ->
                            province = name
                            val prov = provinces.find { it.name == name }
                            prov?.let { viewModel.onProvinceSelected(it.code) }
                        }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    AddressDropdown(
                        label = "City",
                        options = cities.map { it.name },
                        selectedOption = city,
                        onOptionSelected = { name ->
                            city = name
                            val c = cities.find { it.name == name }
                            c?.let { viewModel.onCitySelected(it.code) }
                        }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    AddressDropdown(
                        label = "Barangay",
                        options = barangays.map { it.name },
                        selectedOption = barangay,
                        onOptionSelected = { barangay = it }
                    )
                }
            }
            item {
                VibrantButton(
                    text = if (updateState is Resource.Loading) "Updating..." else "Save Changes",
                    onClick = {
                        viewModel.updateFullProfile(
                            UpdateProfileRequest(
                                firstName, middleName, lastName, phoneNumber, dateOfBirth,
                                streetAddress, province, city, barangay
                            )
                        )
                    },
                    enabled = updateState !is Resource.Loading
                )
            }
        }
    }
}
