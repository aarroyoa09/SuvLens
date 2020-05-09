package net.azarquiel.suvlens.views

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_tipos.*
import net.azarquiel.suvlens.R
import net.azarquiel.suvlens.adapters.TiposAdapter
import net.azarquiel.suvlens.model.Camera

class TiposActivity : AppCompatActivity() {

    private lateinit var adapter: TiposAdapter
    private lateinit var db: FirebaseFirestore
    private lateinit var aux: Camera
    private var cams: ArrayList<Camera> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tipos)

        db = FirebaseFirestore.getInstance()
        setListener()
        aux = intent.getSerializableExtra("tipopulsado") as Camera
        Log.d("kk", aux.name)
        initRV()
    }

    private fun initRV() {
        adapter = TiposAdapter(this, R.layout.rowcam)
        rvtipos.adapter = adapter
        rvtipos.layoutManager = LinearLayoutManager(this)
    }

    private fun setListener() {

        val docRef = db.collection("cameras")
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
            val photo = d["photo"] as String
            val brand = d["brand"] as String
            val type = d["type"] as String
            val rank = d ["rank"] as String
            val selected = d ["selected"] as Boolean

            if (type == aux.name) {
                cams.add(
                    Camera(
                        name = name,
                        price = price,
                        photo = photo,
                        brand = brand,
                        type = type,
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
