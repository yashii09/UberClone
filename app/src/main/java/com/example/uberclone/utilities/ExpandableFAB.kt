package com.example.uberclone.utilities

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.FloatingActionButton

@Composable
fun ExpandableFAB(modifier: Modifier){

    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier.wrapContentSize()){

        //Expanded Buttons(appear when FAB is clicked)
        AnimatedVisibility(
            visible = expanded,
            enter = fadeIn() + expandVertically(expandFrom = Alignment.Bottom),
            exit = fadeOut() + shrinkVertically(shrinkTowards = Alignment.Bottom),
            modifier = Modifier.align(Alignment.BottomStart)
        ) {

            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.align(Alignment.BottomEnd).padding(bottom = 70.dp, end = 16.dp)
            ) {
                // the content of the expanded buttons

                ExtendedFloatingActionButton(
                    onClick = { /* Draw route */
                        expanded = false
                              },
                    icon = { Icon(Icons.Default.Edit, contentDescription = null) },
                    text = { Text(text = "Draw route")}
                )

                ExtendedFloatingActionButton(
                    onClick = {/* clear markers */
                        expanded = false
                              },
                    icon = { Icon(Icons.Default.Edit, contentDescription = null) },
                    text = { Text(text = "Clear Route") }
                )

            }
        }

        //FAB
        FloatingActionButton(
            onClick = { expanded = !expanded },
            modifier = Modifier.align(Alignment.BottomStart).padding(16.dp)
        ) {
            Icon(
                imageVector = if(expanded) Icons.Default.Close else Icons.Default.Menu,
                contentDescription = null)

        }

    }

}