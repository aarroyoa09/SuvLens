package net.azarquiel.suvlens.fragments

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import net.azarquiel.suvlens.R
import net.azarquiel.suvlens.adapters.RvAdapterRangos
import net.azarquiel.suvlens.model.Camera
import net.azarquiel.suvlens.model.Marca
import net.azarquiel.suvlens.views.MarcasActivity

class BlankFragmentRangos : Fragment() {
    private lateinit var db: FirebaseFirestore
    private var cams: ArrayList<Camera> = ArrayList()
    private lateinit var adapter: RvAdapterRangos

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_blank_price, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = RvAdapterRangos(requireActivity().baseContext, R.layout.rowmarcas)
        val rv = view.findViewById(R.id.rvp) as RecyclerView

        db = FirebaseFirestore.getInstance()
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(activity)
        setListener()
        adapter.setCameras(cams)
    }

    private fun setListener() {
        val docRef = db.collection("rangoprecio")
        docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(ContentValues.TAG, "Listen failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null && !snapshot.isEmpty) {
                documentToList(snapshot.documents)
                adapter.setCameras(cams)
            } else {
                Log.d(ContentValues.TAG, "Current data: null")
            }
        }
    }

    private fun documentToList(documents: List<DocumentSnapshot>) {
        cams.clear()
        documents.forEach { d ->
            val name = d["name"] as String
//            val price = d["price"] as Double
//            val photo = d["photo"] as String
//            val brand = d["brand"] as String
//            val type = d["type"] as String
            cams.add(Camera(name = name))

//            cams.add(Camera(name = name, price = price, photo = photo, brand = brand, type = type))

//            if (price > 3330.0 && price < 6000.0) {
//                cams.add(Camera(name = name, price = price, photo = photo, brand = brand))
//            }
        }
    }

}

