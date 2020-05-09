package net.azarquiel.suvlens.views

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.models.SlideModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.android.synthetic.main.content_deta.*
import net.azarquiel.suvlens.R
import net.azarquiel.suvlens.model.Camera

class DetaActivity : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore
    private lateinit var cam: Camera
    private lateinit var camSlider: ImageSlider
    private lateinit var imageSlider: ImageSlider
    private lateinit var foto : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deta)

        cam = intent.getSerializableExtra("cam") as Camera
        foto = cam.photo
        camSlider = this.findViewById(R.id.ivcamera) as ImageSlider
        imageSlider = this.findViewById(R.id.viewp2) as ImageSlider

        val slideModels: ArrayList<SlideModel> = ArrayList()
        val camModels: ArrayList<SlideModel> = ArrayList()

        camModels.add(SlideModel("https://images.unsplash.com/photo-1476418862791-4a38e3af5f87?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=500&q=60", "Shot on ${cam.name}"))
        camModels.add(SlideModel("https://images.unsplash.com/photo-1476418862791-4a38e3af5f87?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=500&q=60", "Shot on ${cam.name}"))
        camModels.add(SlideModel(foto, ""))
        camSlider.setImageList(camModels, true)

        slideModels.add(SlideModel("https://images.unsplash.com/photo-1476418862791-4a38e3af5f87?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=500&q=60", "Shot on ${cam.name}"))
        slideModels.add(SlideModel("https://images.unsplash.com/photo-1476418862791-4a38e3af5f87?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=500&q=60", "Shot on ${cam.name}"))
        slideModels.add(SlideModel(foto, "Shot on ${cam.name}"))
        imageSlider.setImageList(slideModels, true)

        title="Detalles"

        db = FirebaseFirestore.getInstance()

        setOf(db.collection("cameras").whereEqualTo("selected", true))
        pintar()
        btnFav.setOnClickListener {
            if (cam.selected == false) {
                Toast.makeText(this, "Item añadido", Toast.LENGTH_SHORT).show()
                btnFav.setBackgroundResource(R.drawable.fav)
                cam.selected=true
            } else {
                Toast.makeText(this, "Item eliminado", Toast.LENGTH_SHORT).show()
                btnFav.setBackgroundResource(R.drawable.ic_favorite_border_black_24dp)
                cam.selected=false
            }
        }
    }

    private fun pintar() {
        tvtitledetail.text = cam.name
        tvvotosdetail.text = cam.price.toString()+"€"
        rbEstrellas2.rating = cam.rate

        // Pintamos la foto mediante la libreria picasso
    }

    fun addData() {
        Toast.makeText(this, "Item añadido", Toast.LENGTH_SHORT).show()
        cam.selected = true
        setOf(db.collection("cameras").whereEqualTo("selected", true))

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

