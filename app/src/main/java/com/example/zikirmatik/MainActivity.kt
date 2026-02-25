package com.example.zikirmatik

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var toggle: ActionBarDrawerToggle
    private var sayac = 0
    private var hedef = 33
    private var toplamZikir = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // --- NAVIGATION DRAWER (YAN MENÜ) AYARLARI ---
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
        val navView = findViewById<NavigationView>(R.id.navView)

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_ilham -> {
                    val intent = Intent(this, TakvimActivity::class.java)
                    startActivity(intent)
                }

                R.id.menu_sadaka_gunlugu -> {
                    val intent = Intent(this, SadakaActivity::class.java)
                    startActivity(intent)
                }

                // --- GÜNÜN DUASI BURAYA EKLENDİ ---
                R.id.nav_dua -> {
                    val intent = Intent(this, DuaActivity::class.java)
                    startActivity(intent)
                }

                R.id.menu_light_mode -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }

                R.id.menu_dark_mode -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }

                R.id.menu_system_default -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                }

                // ... diğerlerinin altına ekle
                R.id.menu_kuran -> {
                    val intent = Intent(this, KuranActivity::class.java)
                    startActivity(intent)
                }

            }
            drawerLayout.closeDrawers()
            true
        }

        // --- ZİKİRMATİK BUTON VE GÖRÜNÜM TANIMLAMALARI ---
        val txtSayac = findViewById<TextView>(R.id.txtSayac)
        val txtHedef = findViewById<TextView>(R.id.txtHedef)
        val txtKalan = findViewById<TextView>(R.id.txtKalan)
        val txtToplam = findViewById<TextView>(R.id.txtToplamZikir)
        val zikirAlani = findViewById<FrameLayout>(R.id.zikirButonAlani)

        val btn33 = findViewById<Button>(R.id.button2)
        val btn99 = findViewById<Button>(R.id.button3)
        val btn100 = findViewById<Button>(R.id.button4)
        val btnSifirla = findViewById<Button>(R.id.btnSifirla)
        val btnGeriAl = findViewById<Button>(R.id.btnGeriAl)
        val btnAyarla = findViewById<Button>(R.id.btnAyarla)
        val editOzelSayı = findViewById<EditText>(R.id.editTextText)

        // --- ZİKİRMATİK MANTIK İŞLEMLERİ ---

        zikirAlani.setOnClickListener {
            sayac++
            toplamZikir++
            guncelle(txtSayac, txtHedef, txtKalan, txtToplam)
            titresimVer(50)

            if (sayac == hedef) {
                titresimVer(500)
                Toast.makeText(this, "Hedefe ulaşıldı!", Toast.LENGTH_SHORT).show()
            }
        }

        btn33.setOnClickListener {
            hedef = 33; sayac = 0; guncelle(txtSayac, txtHedef, txtKalan, txtToplam)
        }
        btn99.setOnClickListener {
            hedef = 99; sayac = 0; guncelle(txtSayac, txtHedef, txtKalan, txtToplam)
        }
        btn100.setOnClickListener {
            hedef = 100; sayac = 0; guncelle(txtSayac, txtHedef, txtKalan, txtToplam)
        }

        btnSifirla.setOnClickListener {
            sayac = 0
            guncelle(txtSayac, txtHedef, txtKalan, txtToplam)
        }

        btnGeriAl.setOnClickListener {
            if (sayac > 0) {
                sayac--
                if (toplamZikir > 0) toplamZikir--
                guncelle(txtSayac, txtHedef, txtKalan, txtToplam)
            }
        }

        btnAyarla.setOnClickListener {
            val ozel = editOzelSayı.text.toString()
            if (ozel.isNotEmpty()) {
                hedef = ozel.toInt()
                sayac = 0
                guncelle(txtSayac, txtHedef, txtKalan, txtToplam)
                editOzelSayı.text.clear()
            }
        }
    }

    private fun guncelle(s: TextView, h: TextView, k: TextView, t: TextView) {
        s.text = sayac.toString()
        h.text = "/ $hedef"
        val kalan = if (hedef - sayac > 0) hedef - sayac else 0
        k.text = "$kalan zikir kaldı"
        t.text = "Toplam: $toplamZikir"
    }

    private fun titresimVer(sure: Long) {
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(sure, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(sure)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}