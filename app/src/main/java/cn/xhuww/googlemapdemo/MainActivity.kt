package cn.xhuww.googlemapdemo

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.main_activity.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        locationActivity.setOnClickListener {
            startActivity(Intent(this, LocationActivity::class.java))
        }
        mapLocationActivity.setOnClickListener {
            startActivity(Intent(this, MapLocationActivity::class.java))
        }
    }
}
