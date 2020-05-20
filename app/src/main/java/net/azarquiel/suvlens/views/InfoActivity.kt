package net.azarquiel.suvlens.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_info.*
import net.azarquiel.suvlens.R
import net.azarquiel.suvlens.adapters.InfoAdapter
import net.azarquiel.suvlens.model.Info

class InfoActivity : AppCompatActivity() {

    private lateinit var adapter: InfoAdapter
    private lateinit var db: FirebaseFirestore
    private var infos: ArrayList<Info> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)
        db = FirebaseFirestore.getInstance()
        initRV()
        setListener()

        title="Info"


    }

    private fun initRV() {
        adapter = InfoAdapter(this, R.layout.rowinfo)
        rvinfo.adapter = adapter
        rvinfo.layoutManager = LinearLayoutManager(this)
    }

    private fun setListener() {
        val docRef = db.collection("infos")
        docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w("TAG", "Listen failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null && !snapshot.isEmpty) {
                documentToList(snapshot.documents)
                adapter.setInfos(infos)
            } else {
                Log.d("TAG", "Current data: null")
            }
        }
    }

    private fun documentToList(documents: List<DocumentSnapshot>) {
        infos.clear()
        documents.forEach { d ->
            val name = d["name"] as String
            val link = d["link"] as String
            infos.add(Info(name = name, link = link))
        }
    }
}
