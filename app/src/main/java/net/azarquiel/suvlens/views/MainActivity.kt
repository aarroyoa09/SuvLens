package net.azarquiel.suvlens.views

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.viewpager.widget.ViewPager
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.nav_header_main.view.*
import net.azarquiel.suvlens.R
import net.azarquiel.suvlens.adapters.ViewPageAdapter
import net.azarquiel.suvlens.fragments.BlankFragmentMarcas
import net.azarquiel.suvlens.fragments.BlankFragmentOfertas
import net.azarquiel.suvlens.fragments.BlankFragmentRangos
import net.azarquiel.suvlens.fragments.BlankFragmentTipos
import net.azarquiel.suvlens.model.Camera

enum class ProviderType {
    BASIC,
    GOOGLE
}

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    companion object {
        const val TAG = "SuvLens"
    }

    private lateinit var db: FirebaseFirestore
    private var user1 = FirebaseAuth.getInstance().currentUser

    override fun onCreate(savedInstanceState: Bundle?) {

        //Splash Screen
        Thread.sleep(500)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupViewPager(view_pager)
        setupTabs()

        title="Home"

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        nav_view.setNavigationItemSelectedListener(this)
        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        updateHeader()
        db = FirebaseFirestore.getInstance()

        //SetUp
        val bundle = intent.extras
        val email = bundle?.getString("email")
        val provider = bundle?.getString("provider")
        setUp(email ?: "", provider ?: "")

        //Guardado de datos de usuario
        val prefs =
            getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
        prefs.putString("email", email)
        prefs.putString("provider", provider)
        prefs.apply()

    }

    private fun setUp(email: String, provider: String) {

    }

    @SuppressLint("SetTextI18n")
    private fun updateHeader() {
        val miivavatar = nav_view.getHeaderView(0).ivavatar
        miivavatar.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
        val mitvavatar = nav_view.getHeaderView(0).tvavatar
        miivavatar.setImageResource(R.drawable.minicon)
        mitvavatar.text = "SuvLenS"
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_join -> {
                    if (user1 != null){
                        Toast.makeText(this, "Ya estás logeado", Toast.LENGTH_LONG).show()
                    }else{
                        val intent = Intent(this, RegisterActivity::class.java)
                        startActivity(intent)
                }
            }
            R.id.nav_home -> {
                Toast.makeText(this, "Home", Toast.LENGTH_LONG).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_settings -> {
                Toast.makeText(this, "Ajustes", Toast.LENGTH_LONG).show()
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_info -> {
                Toast.makeText(this, "Info", Toast.LENGTH_LONG).show()
                val intent = Intent(this, InfoActivity::class.java)
                startActivity(intent)
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.fav) {
            if (user1 == null){
                Toast.makeText(this, "Por favor, inicie sesión para utilizar ésta función", Toast.LENGTH_LONG).show()
                val intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)
            }else{
                val intent = Intent(this, FavActivity::class.java)
                startActivity(intent)
                return true
            }

        }
        return super.onOptionsItemSelected(item)
    }


    fun onClickDetail(v: View) {
        val cam = v.tag as Camera
        val intent = Intent(this, DetaActivity::class.java)
        intent.putExtra("cam", cam)
        startActivity(intent)
    }

    fun onClickTipo(v: View) {
        val tipopulsado = v.tag as Camera
        val intent = Intent(this, TiposActivity::class.java)
        intent.putExtra("tipopulsado", tipopulsado)
        startActivityForResult(intent, 1)
    }

    fun onClickMarca(v: View) {
        val marcapulsada = v.tag as Camera
        val intent = Intent(this, MarcasActivity::class.java)
        intent.putExtra("marcapulsada", marcapulsada)
        startActivityForResult(intent, 1)
    }

    fun onClickPrecio(v: View) {
        val rango = v.tag as Camera
        val intent = Intent(this, RangosActivity::class.java)
        intent.putExtra("rango", rango)
        startActivityForResult(intent, 1)
    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = ViewPageAdapter(this, supportFragmentManager)
        adapter.addFragment(BlankFragmentOfertas(), "Ofertas")
        adapter.addFragment(BlankFragmentTipos(), "Tipos")
        adapter.addFragment(BlankFragmentMarcas(), "Marcas")
        adapter.addFragment(BlankFragmentRangos(), "Rangos")

        viewPager.adapter = adapter
    }

    private fun setupTabs() {
        tabs.setupWithViewPager(view_pager)
    }
}

