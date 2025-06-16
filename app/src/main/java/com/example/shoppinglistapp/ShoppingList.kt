package com.example.shoppinglistapp

import android.app.AlertDialog
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

data class ShoppingList(
    val id : Int,
    val name : String,
    val quantity : Int,
    val isEditing : Boolean = false
)


@Composable
fun ShoppingListApp(modifier: Modifier = Modifier)
{
    var sItems by remember { mutableStateOf(listOf<ShoppingList>()) }
    var sdailog by remember { mutableStateOf(false) }
    var inputitem by remember {mutableStateOf("")}
    var inputquantity by remember { mutableStateOf("") }


    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp)
        //horizontalAlignment = Alignment.CenterHorizontally

    ) {
        //Text("Shopping List")
        Button(
            onClick = {sdailog = true},
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Add Item")
        }
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(16.dp)
        ) {
            // Content
            items(sItems) {
                ShoppingListItem(it, {}, {})
            }
        }
    }

    if(sdailog)
    {
        AlertDialog(
            onDismissRequest = {sdailog = false},
            confirmButton = {
                Row (modifier = Modifier.padding(8.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween){
                    Button(onClick = {
                        if (inputitem.isNotBlank())
                        {
                            val newItem = ShoppingList(
                                id = sItems.size + 1,
                                name = inputitem,
                                quantity = inputquantity.toInt()
                            )
                            sItems = sItems + newItem
                            inputitem = ""
                            sdailog = false
                        }
                    }) {
                        Text("Add")
                    }
                    Button(onClick = {sdailog = false}){
                        Text("Cancel")
                    }

                }
            },
            title = {Text("Add Shopping Item")},
            text = {
                Column {
                    OutlinedTextField(
                        value = inputitem,
                        onValueChange = {inputitem = it},
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().padding(8.dp)
                    )
                    OutlinedTextField(
                        value = inputquantity,
                        onValueChange = {inputquantity = it},
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().padding(8.dp)
                    )
                }
            }
        )
    }

}

@Composable
fun ShoppingListItem(
    item : ShoppingList,
    onEdit: () -> Unit,
    onDelete: () -> Unit
)
{
    Row(
        modifier = Modifier.fillMaxWidth().padding(8.dp).border(
            border = BorderStroke(2.dp, color = Color(0XFF018786)),
            shape = RoundedCornerShape(20)
        )
    ) {
        Text(text = item.name, modifier = Modifier.padding(8.dp))

    }
}

@Preview
@Composable
fun ShoppingListAppPreview()
{
    ShoppingListApp()
}

