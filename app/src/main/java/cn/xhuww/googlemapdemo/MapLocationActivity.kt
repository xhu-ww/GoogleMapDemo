package cn.xhuww.googlemapdemo

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import cn.xhuww.googlemapdemo.utils.*
import com.google.android.gms.location.*

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.JointType
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.PolyUtil
import kotlinx.android.synthetic.main.activity_maps.*

class MapLocationActivity : AppCompatActivity() {
    private val locationPermission = Manifest.permission.ACCESS_FINE_LOCATION

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private var googleMap: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationRequest = LocationRequest().apply {
            interval = 10000  //请求时间间隔
            fastestInterval = 5000 //最快时间间隔
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                handleLocation(locationResult.lastLocation)
            }
        }

        startOtherMap.setOnClickListener {
            val url = Uri.parse(
                "https://www.google.com/maps/dir/?api=1&origin=" +
                        "30.5702000000,104.0647600000&destination=22.5428600000,114.0595600000"
            )
            val intent = Intent().apply {
                action = "android.intent.action.VIEW"
                data = url
            }
            startActivity(intent)
        }

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this::onMapReady)
    }

    private fun onMapReady(map: GoogleMap) {
        googleMap = map
        drawRoutes(map)
        requestLocationPermission()
    }

    private fun drawRoutes(map: GoogleMap) {
        val line = "agryDundzRnXibE~yM{lBlyXaM|}O{y@`_SgrIb_OamDnpOmkA`bSseJ" +
                "jyTe|MlqHsgKpfBw|If`F}kBpiFefCp}EgeDdpO{uHjsIe`QnnHcy_@nmKk}Et`NsaMlsQisVbqJq" +
                "iTreNciY~eKcmSxkI{vOlnKonHtmZqwNr{PwtJ~pK_vDlfI}sQvwB}sLzeHodLvhEboBdbCcUzmApl" +
                "CniDaUr}CoyDzyCtDxmFkxDzi@kpJfgC_~EtcApt@hbA}fDuy@ycEpnAwgDkcC}lBjqDgsKpdDmqIx" +
                "d@_iIlbCawB`}AqoD|nBw`DjcGebAxnGjyA~jOv|@vpCyoB~oJcJf_InaAfcCl~BriInj@flAklCtk" +
                "GuxD`lHsqBxhDu~D~~Em{Nx_@gtH_`BkzVll@}eIomBy`GzaDemDdsD~rBnmOd}HxrMvjD~wGbVtuEe" +
                "hBjkLx`DhkKflDlwE`A`zBtfCd_KuWz_Jrj@ptDnqA|mMkVnsL}iAvkL`|AnyJ{LvdBd_@nyB{rHjzG" +
                "ijC~tDsm@dkFwzAbrHi_D~uAjqC||C{pFxkEg|SdpDgoZjmIikPleFo~LlmEqjN~eBm~VxnJk~OvcFo" +
                "dJjWwdKxoFae`@~yEe~Hfo@ckMxyG_nSnrLii_@r_CmxOtzGaoRlxBauJ`LifN~`D}uE_ZuyHmr@o|I" +
                "mfCsh\\giEmuIkeAkyLhmC_u\nwAkd\ti@ugSxqG{uf@npD_jFsvAkhMsvCkuIlyAehFqt@wlMzvEs_G" +
                "h`EiePr`DgcLbiCmfExgJ|lAtlk@upH~gRo`C`jGuoCnmFolCnvEhpExpFd{@fgDyt@xqJ{rRjqE}xD" +
                "`xIosB|hQktGzbQa{EnxG_|FfuJwdG|qFsvBye@}fFhkBqaM~fGulPwZe_\\dt@g`IzfDsrFx`FmpCf|A" +
                "qyEhxE{eJ`_BwjGpbIuzF`cHm`Ixz@yfEbwCarDcnBuzNbgFolk@uIooW`pLouAd`L{`B`dP}rEfyK}x" +
                "Bx~FinFx{D{cKxc@amGfzA_qHnbBghSlk@_gN`gHyfFbbN{jJnvKafKv_OkgA~bFefBlrDgvGbdHipFr}" +
                "CuiH~pIs~Ip~JqtKndIidR~`@kgKrcDgqHpfRcnMrrCeoDlnG{|@|eD_`K|rBoeK|uAom\\wpFwlO``@wsO" +
                "ocAyeG~pBw|G_k@cvJbcFw|UfnNesNblGorHtwEam@vsLpuDppLqnErkBeaLpzCkuD~fImkHz|KnqBn" +
                "`Dyw@ryBcnDvoHmdI~gAywG~wBadOuRgp@"

        val decodedPath = PolyUtil.decode(line)  //来源Google Map Util
        val lineOptions = PolylineOptions().apply {
            addAll(decodedPath) //添加路线
            color(Color.GREEN)  //线条设置
            jointType(JointType.ROUND)
            width(15f)
        }
        map.addPolyline(lineOptions)
    }

    @SuppressLint("MissingPermission")
    private fun requestLocationUpdate() {
        fusedLocationClient.requestLocationUpdates(
            locationRequest, locationCallback, Looper.myLooper()
        )
    }

    /**
     * 停止获取位置更新
     */
    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    @SuppressLint("MissingPermission")
    private fun handleLocation(location: Location) {
        val map = googleMap ?: return
        map.isMyLocationEnabled = true
        map.uiSettings.isMyLocationButtonEnabled = true
        val latLng = LatLng(location.latitude, location.longitude)
        //地图中心位置移动到定位位置，并设置地图缩放等级15
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
        //定位成功后可停止获取位置更新
        stopLocationUpdates()
    }

    private fun requestLocationPermission() {
        when {
            hasPermissions(locationPermission) -> requestLocationService()
            else -> requestPermissions(locationPermission, requestCode = 1)
        }
    }

    private fun requestLocationService() {
        if (LocationServiceEnable()) requestLocationUpdate()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            requestLocationUpdate()
        }
    }
}
