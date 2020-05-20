package net.azarquiel.suvlens.views

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_rangos.*
import net.azarquiel.suvlens.R
import net.azarquiel.suvlens.adapters.RangosAdapter
import net.azarquiel.suvlens.model.Camera

class RangosActivity : AppCompatActivity() {
    private lateinit var adapter: RangosAdapter
    private lateinit var db: FirebaseFirestore
    private lateinit var aux: Camera
    private var cams: ArrayList<Camera> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rangos)

        db = FirebaseFirestore.getInstance()
        setListener()
        aux = intent.getSerializableExtra("rango") as Camera
        Log.d("kk", aux.name)
        initRV()

        title = aux.name
    }

    private fun initRV() {
        adapter = RangosAdapter(this, R.layout.rowcam)
        rvrank.adapter = adapter
        rvrank.layoutManager = LinearLayoutManager(this)
    }

    private fun setListener() {

        val docRef = db.collection("products")

        docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(MainActivity.TAG, "Listen failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null && !snapshot.isEmpty) {
                documentToList(snapshot.documents)
                adapter.setCameras(cams)
            } else {
                Log.d(MainActivity.TAG, "Current data: null")
            }
        }
    }

    private fun documentToList(documents: List<DocumentSnapshot>) {
        cams.clear()
        documents.forEach { d ->
            val name = d["name"] as String
            val price = d["price"] as Double

            val photo1 = d["photo1"] as String
            val photo2 = d["photo2"] as String
            val photo3 = d["photo3"] as String

            val pic1 = d["pic1"] as String
            val pic2 = d["pic2"] as String
            val pic3 = d["pic3"] as String

            val brand = d["brand"] as String
            val type = d["type"] as String
            val sensor = d["sensor"] as String
            val maxres = d["maxres"] as String
            val maxfps = d["maxfps"] as String
            val rate = d["rate"] as Double
            val iso = d["iso"] as String
            val film = d["film"] as String
            val rank = d["rank"] as String
            val selected = d["selected"] as Boolean


            if (rank == aux.name) {
                cams.add(
                    Camera(
                        name = name,
                        price = price,

                        photo1 = photo1,
                        photo2 = photo2,
                        photo3 = photo3,
                        pic1 = pic1,
                        pic2 = pic2,
                        pic3 = pic3,

                        brand = brand,
                        type = type,
                        sensor = sensor,
                        maxres = maxres,
                        maxfps = maxfps,
                        rate = rate,
                        iso = iso,
                        film = film,
                        rank = rank,
                        selected = selected
                    )
                )
            }
        }
    }


    fun onClickDeta(v: View) {
        val cam = v.tag as Camera
        val intent = Intent(this, DetaActivity::class.java)
        intent.putExtra("cam", cam)
        startActivity(intent)
    }
}
