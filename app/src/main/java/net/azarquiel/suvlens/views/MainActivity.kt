package net.azarquiel.suvlens.views

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.nav_header_main.view.*
import net.azarquiel.suvlens.R
import net.azarquiel.suvlens.adapters.CustomAdapter
import net.azarquiel.suvlens.adapters.ViewPageAdapter
import net.azarquiel.suvlens.fragments.BlankFragmentMarcas
import net.azarquiel.suvlens.fragments.BlankFragmentOfertas
import net.azarquiel.suvlens.fragments.BlankFragmentRangos
import net.azarquiel.suvlens.fragments.BlankFragmentTipos
import net.azarquiel.suvlens.model.Camera
import net.azarquiel.suvlens.model.Marca

enum class ProviderType {
    BASIC,
    GOOGLE
}

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private val icons = arrayOf(
        android.R.drawable.ic_input_add,
        android.R.drawable.ic_input_delete,
        android.R.drawable.ic_input_get
    )

    companion object {
        const val TAG = "SuvLens"
    }

    private lateinit var searchView: SearchView
    private lateinit var adapter: CustomAdapter
    private lateinit var db: FirebaseFirestore
    private lateinit var camera: Camera
    private lateinit var marca: Marca
    private var cams: ArrayList<Camera> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {

        //Splash Screen
        Thread.sleep(1000)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupViewPager(view_pager)
        view_pager.adapter!!.notifyDataSetChanged()
        setupTabs()

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
//        initRV()
//        setListener()

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

    private fun updateHeader() {
        val miivavatar = nav_view.getHeaderView(0).ivavatar
        miivavatar.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
        val mitvavatar = nav_view.getHeaderView(0).tvavatar
        miivavatar.setImageResource(R.drawable.ic_account_circle_black_24dp)
        mitvavatar.text = "El presi"
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        var fragment: Fragment? = null
        when (item.itemId) {
            R.id.nav_join -> {
                Toast.makeText(this, "Login", Toast.LENGTH_LONG).show()
                val intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)
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

    private fun initRV() {
        adapter = CustomAdapter(this, R.layout.row)
        rvBares.adapter = adapter
        rvBares.layoutManager = LinearLayoutManager(this)
    }

    private fun setListener() {
        val docRef = db.collection("cameras")
        docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null && !snapshot.isEmpty) {
                documentToList(snapshot.documents)
                adapter.setCameras(cams)
            } else {
                Log.d(TAG, "Current data: null")
            }
        }
    }

    private fun documentToList(documents: List<DocumentSnapshot>) {
        cams.clear()
        documents.forEach { d ->
            val name = d["name"] as String
            val price = d["price"] as Double
            val photo = d["photo"] as String
            val brand = d["brand"] as String
            val type = d["type"] as String
            cams.add(Camera(name = name, price = price, photo = photo, brand = brand, type = type))
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        // ************* <Filtro> ************
        val searchItem = menu.findItem(R.id.search)
        searchView = searchItem.actionView as SearchView
        searchView.setQueryHint("Search...")
//        searchView.setOnQueryTextListener(this)
        // ************* </Filtro> ************

        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.fav) {
            val intent = Intent(this, FavActivity::class.java)
            startActivity(intent)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    // ************* <Filtro> ************
//    override fun onQueryTextChange(query: String): Boolean {
//        val original = ArrayList<Camera>(cams)
//        adapter.setCameras(original.filter { camera -> camera.name.startsWith(query, ignoreCase = true)})
//        return false
//    }
//
//    override fun onQueryTextSubmit(text: String): Boolean {
//        return false
//    }

    // ************* </Filtro> ************

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
        adapter.addFragment(BlankFragmentRangos(), "Rango")
        viewPager.adapter = adapter
    }

    private fun setupTabs() {
        tabs.setupWithViewPager(view_pager)
        // Ponerles icono, opcional
//        for ((i, icon) in icons.withIndex()) {
//            tabs.getTabAt(i)!!.icon = ContextCompat.getDrawable(this, icon)
//        }
    }
}

