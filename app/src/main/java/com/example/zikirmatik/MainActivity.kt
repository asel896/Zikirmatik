package com.example.zikirmatik

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private var sayac = 0
    private var hedef = 33
    private var toplamSayac = 0

    private lateinit var txtZikirMetni: TextView
    private lateinit var txtToplam: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val txtSayac = findViewById<TextView>(R.id.txtSayac)
        val txtHedef = findViewById<TextView>(R.id.txtHedef)
        val txtKalan = findViewById<TextView>(R.id.txtKalan)
        txtZikirMetni = findViewById(R.id.txtZikirMetni)
        txtToplam = findViewById(R.id.txtToplamZikir)

        val anaButon = findViewById<FrameLayout>(R.id.zikirButonAlani)
        val editOzel = findViewById<EditText>(R.id.editTextText)
        val btn33 = findViewById<Button>(R.id.button2)
        val btn99 = findViewById<Button>(R.id.button3)
        val btn100 = findViewById<Button>(R.id.button4)
        val btnAyarla = findViewById<Button>(R.id.btnAyarla)
        val btnSifirla = findViewById<Button>(R.id.btnSifirla)
        val btnGeriAl = findViewById<Button>(R.id.btnGeriAl)


        val sharedPref = getSharedPreferences("ZikirmatikHafiza", MODE_PRIVATE)
        sayac = sharedPref.getInt("sayac_anahtari", 0)
        hedef = sharedPref.getInt("hedef_anahtari", 33)
        toplamSayac = sharedPref.getInt("toplam_anahtari", 0)
        val sonZikir = sharedPref.getString("son_zikir", "Zikir Seçmek İçin Dokun")


        txtSayac.text = sayac.toString()
        txtHedef.text = "/ $hedef"
        txtZikirMetni.text = sonZikir
        txtToplam.text = "Toplam: $toplamSayac"
        txtKalan.text = if (sayac >= hedef) "Zikir Tamamlandı!" else "${hedef - sayac} zikir kaldı"




        txtToplam.setOnClickListener {
            Toast.makeText(this, "Sıfırlamak için üzerine uzun basın", Toast.LENGTH_SHORT).show()
        }


        txtToplam.setOnLongClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Toplam Sayacı Sıfırla")
            builder.setMessage("Tüm zikir geçmişinizi silmek istediğinize emin misiniz?")
            builder.setPositiveButton("Evet") { _, _ ->
                toplamSayac = 0
                txtToplam.text = "Toplam: 0"
                verileriKaydet()
                Toast.makeText(this, "İstatistikler sıfırlandı", Toast.LENGTH_SHORT).show()
            }
            builder.setNegativeButton("İptal", null)
            builder.show()
            true
        }


        txtZikirMetni.setOnClickListener {
            val intent = Intent(this, ZikirListeActivity::class.java)
            startActivityForResult(intent, 100)
        }


        anaButon.setOnClickListener {
            if (sayac < hedef) {
                sayac++
                toplamSayac++

                txtSayac.text = sayac.toString()
                txtToplam.text = "Toplam: $toplamSayac"

                if (sayac == hedef) {
                    txtKalan.text = "Zikir Tamamlandı!"
                    titret(500)
                } else {
                    txtKalan.text = "${hedef - sayac} zikir kaldı"
                    titret(50)
                }
                verileriKaydet()
            }
        }


        btn33.setOnClickListener { hedefGuncelle(33, txtHedef, txtKalan, btn33, listOf(btn99, btn100), btnAyarla) }
        btn99.setOnClickListener { hedefGuncelle(99, txtHedef, txtKalan, btn99, listOf(btn33, btn100), btnAyarla) }
        btn100.setOnClickListener { hedefGuncelle(100, txtHedef, txtKalan, btn100, listOf(btn33, btn99), btnAyarla) }

        btnAyarla.setOnClickListener {
            val girilenSayi = editOzel.text.toString()
            if (girilenSayi.isNotEmpty()) {
                hedef = girilenSayi.toInt()
                txtHedef.text = "/ $hedef"
                txtKalan.text = if (sayac >= hedef) "Zikir Tamamlandı!" else "${hedef - sayac} zikir kaldı"
                btnAyarla.backgroundTintList = android.content.res.ColorStateList.valueOf(Color.parseColor("#4A7C59"))
                listOf(btn33, btn99, btn100).forEach { it.backgroundTintList = android.content.res.ColorStateList.valueOf(Color.parseColor("#262626")) }
                editOzel.clearFocus()
                verileriKaydet()
            }
        }

        
        btnSifirla.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                btnSifirla.backgroundTintList = android.content.res.ColorStateList.valueOf(Color.parseColor("#4A7C59"))
                sayac = 0
                txtSayac.text = "0"
                txtKalan.text = "$hedef zikir kaldı"
                verileriKaydet()
            } else if (event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL) {
                btnSifirla.backgroundTintList = android.content.res.ColorStateList.valueOf(Color.parseColor("#262626"))
            }
            true
        }

        btnGeriAl.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                btnGeriAl.backgroundTintList = android.content.res.ColorStateList.valueOf(Color.parseColor("#4A7C59"))
                if (sayac > 0) {
                    sayac--
                    txtSayac.text = sayac.toString()
                    txtKalan.text = "${hedef - sayac} zikir kaldı"
                    verileriKaydet()
                }
            } else if (event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL) {
                btnGeriAl.backgroundTintList = android.content.res.ColorStateList.valueOf(Color.parseColor("#262626"))
            }
            true
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK) {
            val gelenZikir = data?.getStringExtra("secilen_zikir")
            txtZikirMetni.text = gelenZikir
            val sharedPref = getSharedPreferences("ZikirmatikHafiza", MODE_PRIVATE)
            sharedPref.edit().putString("son_zikir", gelenZikir).apply()
        }
    }

    private fun hedefGuncelle(yeniHedef: Int, hView: TextView, kView: TextView, sBtn: Button, dBtns: List<Button>, aBtn: Button) {
        hedef = yeniHedef
        hView.text = "/ $hedef"
        kView.text = if (sayac >= hedef) "Zikir Tamamlandı!" else "${hedef - sayac} zikir kaldı"
        sBtn.backgroundTintList = android.content.res.ColorStateList.valueOf(Color.parseColor("#4A7C59"))
        dBtns.forEach { it.backgroundTintList = android.content.res.ColorStateList.valueOf(Color.parseColor("#262626")) }
        aBtn.backgroundTintList = android.content.res.ColorStateList.valueOf(Color.parseColor("#262626"))
        verileriKaydet()
    }

    private fun verileriKaydet() {
        val sharedPref = getSharedPreferences("ZikirmatikHafiza", MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putInt("sayac_anahtari", sayac)
        editor.putInt("hedef_anahtari", hedef)
        editor.putInt("toplam_anahtari", toplamSayac)
        editor.apply()
    }

    private fun titret(sure: Long = 50) {
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION") getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(sure, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION") vibrator.vibrate(sure)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_light_mode -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            R.id.menu_dark_mode -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            R.id.menu_system_default -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
        return super.onOptionsItemSelected(item)
    }
}