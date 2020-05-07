package net.azarquiel.suvlens.views

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.content_deta.*
import net.azarquiel.suvlens.R
import net.azarquiel.suvlens.adapters.FavAdapter
import net.azarquiel.suvlens.adapters.SliderAdapter
import net.azarquiel.suvlens.model.Camera
import net.azarquiel.suvlens.model.Slide
import kotlin.concurrent.fixedRateTimer

class DetaActivity : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore
    private lateinit var cam: Camera
    private lateinit var cam2: Camera


    var lstSlides: MutableList<Slide> = ArrayList()
    var lstSlides2: MutableList<Slide> = ArrayList()
    var sliderPage: ViewPager? = null
    var sliderPage2: ViewPager? = null
    private lateinit var adapter: SliderAdapter
    private lateinit var fav_adapter: FavAdapter
    private var favs: ArrayList<Camera> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deta)
//        setSupportActionBar(toolbar)
        cam2 = Camera()

        sliderPage = this.findViewById(R.id.view_pager_glide)
        sliderPage2 = this.findViewById(R.id.viewp2)


        lstSlides.add(Slide(R.drawable.face))
        lstSlides.add(Slide(R.drawable.google))
        lstSlides.add(Slide(R.drawable.face))

        lstSlides2.add(Slide(R.drawable.face))
        lstSlides2.add(Slide(R.drawable.google))
        lstSlides2.add(Slide(R.drawable.face))

        adapter = SliderAdapter(this, lstSlides)
        sliderPage!!.adapter = adapter
        adapter = SliderAdapter(this, lstSlides2)
        sliderPage2!!.adapter = adapter

        fixedRateTimer(
            name = "timer",
            initialDelay = 2000, period = 3000
        ) {
            runOnUiThread {
                if (sliderPage!!.currentItem < lstSlides.count() - 1) {
                    sliderPage!!.currentItem = sliderPage!!.currentItem + 1
                } else {
                    sliderPage!!.currentItem = 0
                }
            }
        }

        exindicator.setupWithViewPager(sliderPage, true)
        exindicator2.setupWithViewPager(sliderPage2, true)

        db = FirebaseFirestore.getInstance()
        cam = intent.getSerializableExtra("cam") as Camera

        setOf(db.collection("cameras").whereEqualTo("selected", true))
        pintar()
        btnFav.setOnClickListener {
            if (cam.selected == false) {
                Toast.makeText(this, "Item añadido", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Item eliminado", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun pintar() {
        tvtitledetail.text = cam.name
        tvvotosdetail.text = cam.price.toString()
        tvdatedetail.text = cam.type

        // Pintamos la foto mediante la libreria picasso
        var foto = cam.photo
        Picasso.get().load(foto).into(ivcamera)
    }

    fun addData() {
        Toast.makeText(this, "Item añadido", Toast.LENGTH_SHORT).show()
        cam.selected = true
        setOf(db.collection("cameras").whereEqualTo("seledted", true))

        db.collection("favs")
            .add(cam)
    }


    fun getAll() {
        db.collection("favs").whereEqualTo("name", cam.name).get().addOnSuccessListener { result ->
            for (documento in result) {
                var pulp = documento.data.getValue("name")
                if (pulp == cam.name) {
                    documento.reference.delete()
                    val p = hashMapOf("selected" to false)
                    db.collection("cameras").document(documento.id).set(p, SetOptions.merge())
                    cam.selected = false
                    Toast.makeText(
                        this, "Item Deleted",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.d("dato", pulp.toString())
                    Log.d("camname", cam.name)
                }
            }
        }
    }


    fun aux2() {
        db.collection("cameras").whereEqualTo("selected", false).get()
            .addOnSuccessListener { result ->
                for (documento in result) {
                    if ((documento.data.getValue("selected") == false) && (documento.data.getValue("name") == cam.name)) {
                        val p = hashMapOf("selected" to true)
                        db.collection("cameras").document(documento.id).set(p, SetOptions.merge())
                        Log.d("doc", documento.id)

                        Toast.makeText(this, "No se pudo añadir a favoritos", Toast.LENGTH_SHORT).show()
                        cam.selected = false

                        db.collection("favs")
                            .add(cam)
                    }else{
                        Log.d("doc", documento.id)
                        Toast.makeText(this, "Añadido a favoritos", Toast.LENGTH_SHORT).show()

                    }


                }
            }
    }
}

