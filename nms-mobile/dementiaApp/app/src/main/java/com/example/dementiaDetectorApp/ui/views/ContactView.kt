package com.example.dementiaDetectorApp.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.example.dementiaDetectorApp.R
import com.example.dementiaDetectorApp.ui.composables.NavMenu
import com.example.dementiaDetectorApp.ui.theme.DarkPurple
import com.example.dementiaDetectorApp.ui.theme.LightPurple
import com.example.dementiaDetectorApp.ui.theme.MidPurple
import com.example.dementiaDetectorApp.viewModels.ContactVM
import com.example.dementiaDetectorApp.viewModels.SharedVM
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@Composable
fun ContactScreen(cVM: ContactVM, sharedVM: SharedVM, nc: NavController){
    cVM.initClinic(sharedVM.id.value)
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)
    ){
        Column{
            Spacer(Modifier.height(35.dp))
            HeaderSection()
            InfoSection(cVM)
            Spacer(Modifier.height(85.dp))
            ClinicMap(cVM)
            Spacer(Modifier.height(35.dp))
        }
        NavMenu(
            sharedVM,
            nc,
            Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun HeaderSection(){
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(15.dp),
        horizontalArrangement = Arrangement.spacedBy(35.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        Icon(
            painter = painterResource(R.drawable.logo),
            contentDescription = "Logo",
            tint = Color.Unspecified,
            modifier = Modifier.height(45.dp)
        )
        Column {
            Text("Contact", color = DarkPurple, fontSize = 25.sp)
            Text("Information on your clinic", color = MidPurple, fontSize = 18.sp)
        }
    }
}

@Composable
fun InfoSection(cVM: ContactVM) {
    val clinicState = cVM.clinic.collectAsState()

    clinicState.value?.let { clinic ->
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = LightPurple)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = clinic.name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkPurple
                )
                Spacer(modifier = Modifier.height(8.dp))
                InfoRow(label = "Phone:", value = clinic.phone)
                InfoRow(label = "County:", value = clinic.county)
                InfoRow(label = "Eircode:", value = clinic.eircode)
            }
        }
    }
}

@Composable
fun ClinicMap(cVM: ContactVM) {
    val coords = cVM.coords.collectAsState()
    val clinic = cVM.clinic.collectAsState().value
    val context = LocalContext.current

    coords.value?.let { (lat, lon) -> //If coordinates are available (not null)
        AndroidView(factory = { ctx -> //Use traditional AndroidView to embed an AndroidMapView
            Configuration.getInstance().load(ctx, ctx.getSharedPreferences("osmdroid", 0)) //Load osmdroid config
            MapView(ctx).apply { //The map is created
                setTileSource(TileSourceFactory.MAPNIK) //Use OpenStreetMap standard tiles
                setMultiTouchControls(true) //Allow for pinch zoom and etc
                controller.setZoom(16.0) //Default zoom
                controller.setCenter(GeoPoint(lat, lon)) //Centre on the clinic's coordinates

                val marker = Marker(this)
                marker.position = GeoPoint(lat, lon) //Add marker above clinic
                marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                marker.title = clinic.name
                overlays.add(marker)
            }
        },
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .padding(horizontal = 16.dp)
        )
    } ?: Text(
        text = "Map loading...", //Fallback if waiting for response
        modifier = Modifier.padding(16.dp),
        color = DarkPurple
    )
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontWeight = FontWeight.SemiBold,
            color = DarkPurple,
            modifier = Modifier.width(100.dp)
        )
        Text(
            text = value,
            fontWeight = FontWeight.Normal,
            color = Color.White
        )
    }
}
