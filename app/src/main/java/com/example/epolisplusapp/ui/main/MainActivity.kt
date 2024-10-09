package com.example.epolisplusapp.ui.main

import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.example.epolisplusapp.R
import com.example.epolisplusapp.ui.cabinet.CabinetFragment
import com.example.epolisplusapp.ui.mypolis.MyPolisFragment
import com.example.epolisplusapp.ui.partners.PartnersFragment
import com.example.epolisplusapp.util.CommonUtils
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import eightbitlab.com.blurview.BlurView

class MainActivity : AppCompatActivity() {
    private lateinit var blurView: BlurView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        blurView = findViewById(R.id.progressBarBackPart)
        val rootView = findViewById<View>(android.R.id.content)
        CommonUtils.setupBlurView(this, blurView, rootView)
        val sosButton = findViewById<ImageButton>(R.id.btSos)
        sosButton.setOnClickListener{
            showBlurView()
        }
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener{item ->
            val fragment: Fragment = when(item.itemId){
                R.id.nav_item1 -> MainFragment()
                R.id.nav_item2 -> CabinetFragment()
                R.id.nav_item4 -> MyPolisFragment()
                R.id.nav_item5 -> PartnersFragment()
                else -> MainFragment()
            }
            replaceFragment(fragment)
            true
        }
        if (savedInstanceState == null) {
            bottomNavigationView.selectedItemId = R.id.nav_item1
        }



    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentTag = fragment::class.java.simpleName
        val existingFragment = supportFragmentManager.findFragmentByTag(fragmentTag)

        if (existingFragment == null) {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragmentContainerView, fragment, fragmentTag)
            fragmentTransaction.commit()
        }
    }
//   метод где можно возрвращатся назад в фрагментах
//    private fun replaceFragment(fragment: Fragment) {
//        val fragmentTag = fragment::class.java.simpleName
//        val existingFragment = supportFragmentManager.findFragmentByTag(fragmentTag)
//
//        if (existingFragment == null) {
//            val fragmentTransaction = supportFragmentManager.beginTransaction()
//            fragmentTransaction.replace(R.id.fragmentContainerView, fragment, fragmentTag)
//            fragmentTransaction.addToBackStack(null)
//            fragmentTransaction.commit()
//        }
//    }


    private fun showBlurView() {
        if (!isFinishing && !isDestroyed) {
            val blurView: BlurView = findViewById(R.id.blurForSos)
            val decorView: ViewGroup = window.decorView.findViewById(android.R.id.content)
            val radius = 5f
            val windowBackground: Drawable = window.decorView.background

            blurView.setupWith(decorView)
                .setFrameClearDrawable(windowBackground)
                .setBlurRadius(radius)
            blurView.visibility = View.VISIBLE

            val view = LayoutInflater.from(this).inflate(R.layout.main_sos_buttons, null)
            val dialog = BottomSheetDialog(this, R.style.TransparentBottomSheetDialog)
            dialog.setContentView(view)

            dialog.setOnDismissListener {
                blurView.visibility = View.GONE
            }
            val closeButton = view.findViewById<ImageButton>(R.id.ibCloseDialog)
            closeButton.setOnClickListener {
                dialog.dismiss()
            }
            val btCallCenter = view.findViewById<Button>(R.id.btGoCallCenter)
            btCallCenter.setOnClickListener {
                val callCenter = "+998909011257"
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$callCenter"))
                startActivity(intent)
            }
            val btTg = view.findViewById<Button>(R.id.btGoTg)
            btTg.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse("https://t.me/tuituz_official")
                intent.setPackage("org.telegram.messenger")
                startActivity(intent)

            }

            dialog.show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        blurView.visibility = View.GONE
    }
}
