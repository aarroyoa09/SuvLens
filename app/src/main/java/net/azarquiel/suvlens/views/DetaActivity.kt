package net.azarquiel.suvlens.views

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.models.SlideModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.content_deta.*
import net.azarquiel.suvlens.R
import net.azarquiel.suvlens.model.Camera


class DetaActivity : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore
    private lateinit var cam: Camera
    private var user1 = FirebaseAuth.getInstance().currentUser
    private lateinit var camSlider: ImageSlider
    private lateinit var imageSlider: ImageSlider
    private lateinit var foto1: String
    private lateinit var foto2: String
    private lateinit var foto3: String
    private lateinit var pic1: String
    private lateinit var pic2: String
    private lateinit var pic3: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deta)


        cam = intent.getSerializableExtra("cam") as Camera
        foto1 = cam.photo1
        foto2 = cam.photo2
        foto3 = cam.photo3
        pic1 = cam.pic1
        pic2 = cam.pic2
        pic3 = cam.pic3

        camSlider = this.findViewById(R.id.ivcamera) as ImageSlider
        imageSlider = this.findViewById(R.id.viewp2) as ImageSlider

        val slideModels: ArrayList<SlideModel> = ArrayList()
        val camModels: ArrayList<SlideModel> = ArrayList()

        camModels.add(SlideModel(foto1, ""))
        camModels.add(SlideModel(foto2, ""))
        camModels.add(SlideModel(foto3, ""))
        camSlider.setImageList(camModels, true)

        slideModels.add(SlideModel(pic1, "Shot on ${cam.name}"))
        slideModels.add(SlideModel(pic2, "Shot on ${cam.name}"))
        slideModels.add(SlideModel(pic3, "Shot on ${cam.name}"))
        imageSlider.setImageList(slideModels, true)

        title = "Detalles"

        db = FirebaseFirestore.getInstance()

        pintar()
        btnFav.setOnClickListener {
            if (user1 == null) {
                Toast.makeText(
                    this,
                    "Por favor, inicie sesión para utilizar ésta función",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                addDel()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun pintar() {
        tvtitledetail.text = cam.name
        tvvotosdetail.text = cam.price.toString() + "€"
        rbEstrellas2.rating = cam.rate.toFloat()
        sensor.text = cam.sensor
        maxres.text = cam.maxres
        brand.text = cam.brand
        tipo.text = cam.type
        iso.text = cam.iso
        film.text = cam.film
    }

    private fun addDel() {
        val q1 = db.collection("products")
        q1.addSnapshotListener { snapshot, e ->
            snapshot?.documents?.forEach { d ->
                val n = d.data?.getValue("name").toString()
                val sel = d.data?.getValue("selected").toString()
                val id = d.id

                if (n == cam.name && sel == cam.selected.toString()) {
                    Toast.makeText(this, "Item añadido", Toast.LENGTH_SHORT).show()
                    btnFav.setBackgroundResource(R.drawable.fav)
                    Log.d("camname", cam.name)

                    val came = hashMapOf(
                        "name" to cam.name,
                        "brand" to cam.brand,
                        "film" to cam.film,
                        "iso" to cam.iso,
                        "maxfps" to cam.maxfps,
                        "maxres" to cam.maxres,
                        "photo1" to cam.photo1,
                        "photo2" to cam.photo2,
                        "photo3" to cam.photo3,
                        "pic1" to cam.pic1,
                        "pic2" to cam.pic2,
                        "pic3" to cam.pic3,
                        "rank" to cam.rank,
                        "rate" to cam.rate,
                        "sensor" to cam.sensor,
                        "type" to cam.type,
                        "price" to cam.price,
                        "selected" to true
                    )

                    db.collection("favs").document(id).set(came)
                    cam.selected = true

                } else if (n == cam.name && cam.selected) {
                    btnFav.setBackgroundResource(R.drawable.ic_favorite_border_black_24dp)
                    db.collection("favs").document(id).delete()
                    cam.selected = false
                }
            }
        }
    }
}


