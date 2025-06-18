package com.example.shoppinglistapp

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

// Data class for shopping items
data class ShoppingList(
    val id: Int,
    var name: String,
    var quantity: Int,
    val isEditing: Boolean = false
)

@Composable
fun ShoppingListApp(modifier: Modifier = Modifier) {
    var sItems by remember { mutableStateOf(listOf<ShoppingList>()) }
    var dialogOpen by remember { mutableStateOf(false) }
    var inputItem by remember { mutableStateOf("") }
    var inputQuantity by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp)
    ) {
        Text(text = "Shopping List",modifier = Modifier.align(Alignment.CenterHorizontally), style = MaterialTheme.typography.headlineLarge)
        Button(
            onClick = { dialogOpen = true },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Add Item")
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(sItems) {
                item ->
                if(item.isEditing){
                    ShoppingListEdited(item = item, onEditComplete = {
                        editedName, editedQuantity ->
                        sItems = sItems.map{it.copy(isEditing = false)}
                        val editedItem = sItems.find { it.id == item.id }
                        editedItem?.let{
                            it.name = editedName
                            it.quantity = editedQuantity
                        }
                    })
                }else{
                    ShoppingListItem(item = item, onEdit = {
                        // finding out which item are editing and changing its "isEdit boolean" to true
                        sItems = sItems.map{it.copy(isEditing = it.id == item.id)}
                        },
                        onDelete = {
                            sItems = sItems - item
                        }
                    )
                }

            }
        }
    }

    if (dialogOpen) {
        AlertDialog(
            onDismissRequest = { dialogOpen = false },
            confirmButton = {
                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(onClick = {
                        if (inputItem.isNotBlank() && inputQuantity.isNotBlank()) {
                            val newItem = ShoppingList(
                                id = (sItems.maxOfOrNull { it.id } ?: 0) + 1,
                                name = inputItem,
                                quantity = inputQuantity.toInt()
                            )
                            sItems = sItems + newItem
                            inputItem = ""
                            inputQuantity = ""
                            dialogOpen = false
                        }
                    }) {
                        Text("Add")
                    }

                    Button(onClick = { dialogOpen = false }) {
                        Text("Cancel")
                    }
                }
            },
            title = { Text("Add Shopping Item") },
            text = {
                Column {
                    OutlinedTextField(
                        value = inputItem,
                        onValueChange = { inputItem = it },
                        singleLine = true,
                        label = { Text("Item Name") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                    OutlinedTextField(
                        value = inputQuantity,
                        onValueChange = { inputQuantity = it },
                        singleLine = true,
                        label = { Text("Quantity") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                }
            }
        )
    }
}

@Composable
fun ShoppingListEdited(item: ShoppingList, onEditComplete: (String, Int) -> Unit) {
    var editedName by remember { mutableStateOf(item.name) }
    var editedQuantity by remember { mutableStateOf(item.quantity.toString()) }
    var isEditing by remember { mutableStateOf(item.isEditing)}

    Row(
        modifier = Modifier
            .padding(8.dp)
            .background(Color(0xFFE0F7FA))
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Column {
            BasicTextField(
                value = editedName,
                onValueChange = {editedName = it},
                singleLine = true,
                modifier = Modifier.wrapContentSize().padding(8.dp)
            )

            BasicTextField(
                value = editedQuantity,
                onValueChange = {editedQuantity = it},
                singleLine = true,
                modifier = Modifier.wrapContentSize().padding(8.dp)
            )
        }

        Button(
            onClick = {
                isEditing = false
                onEditComplete (editedName, editedQuantity.toIntOrNull() ?: 1)
            },
            modifier = Modifier.align(Alignment.CenterVertically)
        ) {
            Text("Save")
        }
    }
}

@Composable
fun ShoppingListItem(
    item: ShoppingList,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(
                border = BorderStroke(2.dp, color = Color(0XFF018786)),
                shape = RoundedCornerShape(20)
            )
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = item.name, modifier = Modifier.padding(4.dp))
            Text(text = "Qty: ${item.quantity}", modifier = Modifier.padding(4.dp))
        }

        Row {
            IconButton(onClick = onEdit) {
                Icon(Icons.Default.Edit, contentDescription = "Edit Button")
            }

            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete Button")
            }
        }
    }
}

