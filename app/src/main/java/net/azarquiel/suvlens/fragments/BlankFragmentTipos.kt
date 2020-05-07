package net.azarquiel.suvlens.fragments

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import net.azarquiel.suvlens.R
import net.azarquiel.suvlens.adapters.RvAdapterTipos
import net.azarquiel.suvlens.model.Camera

class BlankFragmentTipos : Fragment() {
    private lateinit var db: FirebaseFirestore
    private var cams: ArrayList<Camera> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val r= inflater.inflate(R.layout.fragment_blank_tipos, container, false)



        return r
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = RvAdapterTipos(requireActivity().baseContext, R.layout.row)
        val rv = view.findViewById(R.id.rvt) as RecyclerView

        db = FirebaseFirestore.getInstance()
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(activity)

//        cvmarca.setOnClickListener{onClick(view)}
        setListener()
        adapter.setCameras(cams)
        adapter.notifyDataSetChanged()
    }


    fun onClick(v: View) {
        Toast.makeText(context, "kaka", Toast.LENGTH_LONG).show()
//        val marcapulsada = v.tag as Marca
//        val intent = Intent(getActivity(), MarcasActivity::class.java)
//        intent.putExtra("marcapulsada", marcapulsada)
//        startActivity(intent)
    }

    private fun setListener() {
        val adapter = RvAdapterTipos(requireActivity().baseContext, R.layout.row)

        val docRef = db.collection("tipos")
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
            val photo = d["photo"] as String
//            val brand = d["brand"] as String
//            val type = d["type"] as String
            cams.add(Camera(name = name, photo = photo))

//            cams.add(Camera(name = name, price = price, photo = photo, brand = brand, type = type))

//            if (price > 3330.0 && price < 6000.0) {
//                cams.add(Camera(name = name, price = price, photo = photo, brand = brand))
//            }
        }
    }

}

