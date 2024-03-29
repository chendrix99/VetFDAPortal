package ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Chip
import androidx.compose.material.ChipDefaults
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconToggleButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import moe.tlaster.precompose.navigation.Navigator
import ui.screens.composables.GeneralSearchBar
import ui.screens.composables.GoBackTextTopAppBar
import ui.screens.composables.SimpleTextFAB
import ui.viewmodels.AdvancedSearchViewModel
import ui.viewmodels.AnimalGender
import ui.viewmodels.MatchTerms

//----------------------------------------------------------------------------------------
@Composable
fun AdvancedSearchScreen(
    navigator: Navigator,
    viewModel: AdvancedSearchViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    var openFilterDialog by remember { mutableStateOf(false) }
    var contentList: List<@Composable () -> Unit> = emptyList()

    Scaffold(
        topBar = { AdvancedSearchTopBar(
            onBack = {navigator.goBack()}
        ) },
        floatingActionButton = {
            SimpleTextFAB(
                onClick = {
                    openFilterDialog = !openFilterDialog
                },
                title = "Filters"
            )
        },
        modifier = Modifier.fillMaxSize()
    ) {
        if (openFilterDialog) {
            FilterDialog(
                viewModel = viewModel,
                onDismiss = {
                    openFilterDialog = !openFilterDialog
                }
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row (
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextChip("Match All Terms", state.termMatch.value) {
                    viewModel.updateTermMatch(
                        when (state.termMatch.value) {
                            true -> MatchTerms.MatchAnyTerm
                            false -> MatchTerms.MatchAllTerms
                        }
                    )
                }
                TextChip("Match Any Term", !state.termMatch.value) {
                    viewModel.updateTermMatch(
                        when (state.termMatch.value) {
                            true -> MatchTerms.MatchAnyTerm
                            false -> MatchTerms.MatchAllTerms
                        }
                    )
                }
            }

            // Search bar
            GeneralSearchBar(
                value = state.searchString,
                title = "Advanced Search Terms...",
                onValueChange = {
                    viewModel.updateSearchString(it)
                },
                onSearch = {},
                modifier = Modifier.fillMaxWidth()
            )

            Divider()
            Spacer(modifier = Modifier.size(8.dp))

            contentList = emptyList()

            contentList = contentList + {
                Row (
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                ) {
                    Text("Search By Animal:")
                    Switch(
                        checked = state.searchByAnimal,
                        onCheckedChange = {
                            viewModel.updateSearchByAnimal(it)
                        }
                    )
                }
            }

            if (state.searchByAnimal) {
                contentList = contentList + {
                    SearchByAnimalCard(viewModel)
                }
            }

            contentList = contentList + {
                Row (
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                ) {
                    Text("Search by Drug:")
                    Switch(
                        checked = state.searchByDrug,
                        onCheckedChange = {
                            viewModel.updateSearchByDrug(it)
                        }
                    )
                }
            }

            if (state.searchByDrug) {
                contentList = contentList + {
                    SearchByDrugCard(viewModel)
                }
            }

            contentList = contentList + {
                Spacer(modifier = Modifier.size(64.dp))
            }

            // Other sections for the options
            LazyColumn(
                contentPadding = PaddingValues(4.dp),
            ) {
                items(items = contentList) {item ->
                    item()
                }
            }
        }
    }
}

//----------------------------------------------------------------------------------------
@Composable
fun AdvancedSearchTopBar(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    GoBackTextTopAppBar(
        title = "Advanced Search",
        onBackClick = onBack
    )
}

//----------------------------------------------------------------------------------------
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TextChip(
    text: String,
    checked: Boolean,
    onClicked: () -> Unit
) {
    Chip(
        onClick = onClicked,
        leadingIcon = { if (checked) {
            Icon(Icons.Filled.Check, contentDescription = null)
        } },
        colors = when (checked) {
            true -> ChipDefaults.chipColors(backgroundColor = MaterialTheme.colors.secondary)
            false -> ChipDefaults.chipColors()
        },
        modifier = Modifier.padding(top = 4.dp)
    ) {
        Text(text)
    }
}

//----------------------------------------------------------------------------------------
@Composable
fun SearchByAnimalCard(
    viewModel: AdvancedSearchViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()

    Card (
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = 4.dp
    ) {
        Column (
            modifier = Modifier.fillMaxWidth(),
        ) {
            Row (
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
            ) {
                Text("Age: ")
                TextField(
                    value = state.ageMin?.toString() ?: "",
                    onValueChange = {
                        when (it.toIntOrNull() == null) {
                            true -> viewModel.updateAgeMin(null)
                            false -> viewModel.updateAgeMin(it.toInt())
                        }
                    },
                    label = {Text("Min:")},
                    modifier = Modifier
                        .weight(.5f)
                        .padding(start = 2.dp)
                )
                TextField(
                    value = state.ageMax?.toString() ?: "",
                    onValueChange = {
                        when (it.toIntOrNull() == null) {
                            true -> viewModel.updateAgeMax(null)
                            false -> viewModel.updateAgeMax(it.toInt())
                        }
                    },
                    label = {Text("Max:")},
                    modifier = Modifier
                        .weight(.5f)
                        .padding(start = 2.dp)
                )
            }
            TextField(
                value = state.animalSpecies,
                onValueChange = {
                    viewModel.updateAnimalSpecies(it)
                },
                label = {Text("Species:")},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
            )
            TextField(
                value = state.animalBreed,
                onValueChange = {
                    viewModel.updateAnimalBreed(it)
                },
                label = {Text("Breed:")},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
            )
            Row (
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
            ) {
                Text("Gender:")
                TextChip(
                    text = "Male",
                    checked = state.animalGender.value,
                    onClicked = {
                        viewModel.updateAnimalGender(
                            when (state.animalGender.value) {
                                true -> AnimalGender.Female
                                false -> AnimalGender.Male
                            }
                        )
                    }
                )
                TextChip(
                    text = "Female",
                    checked = !state.animalGender.value,
                    onClicked = {
                        viewModel.updateAnimalGender(
                            when (state.animalGender.value) {
                                true -> AnimalGender.Female
                                false -> AnimalGender.Male
                            }
                        )
                    }
                )
            }
        }
    }
}

//----------------------------------------------------------------------------------------
@Composable
fun SearchByDrugCard(
    viewModel: AdvancedSearchViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()

    Card (
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = 4.dp
    ) {
        Column (
            modifier = Modifier.fillMaxWidth(),
        ) {
            TextField(
                value = state.activeIngredients,
                onValueChange = {
                    viewModel.updateActiveIngredients(it)
                },
                label = {Text("Active Ingredients:")},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
            )
            Row (
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
            ) {
                Text("Previous Exposure:")
                Switch(
                    checked = state.previousExposure,
                    onCheckedChange = {
                        viewModel.updatePreviousExposure(it)
                    }
                )
            }
            TextField(
                value = state.administrationRoute,
                onValueChange = {
                    viewModel.updateAdministrationRoute(it)
                },
                label = {Text("Administration Route:")},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
            )
            Row (
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
            ) {
                Text("Used According to Label:")
                Switch(
                    checked = state.usedAccordingToLabel,
                    onCheckedChange = {
                        viewModel.updateUsedAccordingToLabel(it)
                    }
                )
            }
        }
    }
}

//----------------------------------------------------------------------------------------
@Composable
fun FilterDialog(
    viewModel: AdvancedSearchViewModel,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()

    Dialog (
        onDismissRequest = onDismiss,
        properties = DialogProperties()
    ) {
        Card {
            Column {
                Column (
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Event Date in Years:")
                    Row (
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp),
                    ) {
                        TextField(
                            value = state.yearStart?.toString() ?: "",
                            onValueChange = {
                                when (it.toIntOrNull() == null) {
                                    true -> viewModel.updateYearStart(null)
                                    false -> viewModel.updateYearStart(it.toInt())
                                }
                            },
                            label = {Text("Start:")},
                            modifier = Modifier
                                .weight(.5f)
                        )
                        TextField(
                            value = state.yearEnd?.toString() ?: "",
                            onValueChange = {
                                when (it.toIntOrNull() == null) {
                                    true -> viewModel.updateYearEnd(null)
                                    false -> viewModel.updateYearEnd(it.toInt())
                                }
                            },
                            label = {Text("End:")},
                            modifier = Modifier
                                .weight(.5f)
                                .padding(start = 2.dp)
                        )
                    }
                }
                Row (
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                ) {
                    Text("Serious Adverse Event:")
                    Switch(
                        checked = state.seriousAdverseEvent,
                        onCheckedChange = {
                            viewModel.updateSeriousAdverseEvent(it)
                        }
                    )
                }
                Row (
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                ) {
                    Text("Treated for Adverse Event:")
                    Switch(
                        checked = state.treatedForAdverseEvent,
                        onCheckedChange = {
                            viewModel.updateTreatedForAdverseEvent(it)
                        }
                    )
                }
            }
        }
    }
}