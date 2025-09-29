package com.example.nms_mobile_proj

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.nms_mobile_proj.ui.theme.NmsmobileprojTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NmsmobileprojTheme {
                    Main()
            }
        }
    }
}

@Composable
fun Main() {

}

@Preview(showBackground = true)
@Composable
fun MainPreview() {
    NmsmobileprojTheme {
        Main()
    }
}