package net.azarquiel.suvlens.fragments

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import net.azarquiel.suvlens.R
import net.azarquiel.suvlens.adapters.RvAdapterMarcas
import net.azarquiel.suvlens.model.Camera

class BlankFragmentMarcas : Fragment(), SearchView.OnQueryTextListener {
    private lateinit var db: FirebaseFirestore
    private var cams: ArrayList<Camera> = ArrayList()
    private lateinit var mView: View
    private lateinit var adapter: RvAdapterMarcas
    private lateinit var searchView: SearchView
    var color: String = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.fragment_blank_marcas, container, false)
        adapter = RvAdapterMarcas(requireActivity().baseContext, R.layout.rowmarca)

        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val rv = view.findViewById(R.id.rv3) as RecyclerView

        db = FirebaseFirestore.getInstance()
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(activity)
        setListener()
        adapter.setCameras(cams)
    }

    private fun setListener() {
        val docRef = db.collection("marcas")
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


    ////////////////////////////////////////

    fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.main, menu)
        // ************* <Filtro> ************
        val searchItem = menu.findItem(R.id.search)
        searchView = searchItem.actionView as SearchView
        searchView.setQueryHint("Search...")
        searchView.setOnQueryTextListener(this)
        // ************* </Filtro> ************

        return true
    }

    // ************* <Filtro> ************
    override fun onQueryTextChange(query: String): Boolean {
        val original = ArrayList<Camera>(cams)
        adapter.setCameras(original.filter { camera -> camera.name.startsWith(query, ignoreCase = true)})
        return false
    }

    override fun onQueryTextSubmit(text: String): Boolean {
        return false
    }

    // ************* </Filtro> ************


}

