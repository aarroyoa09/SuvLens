package net.azarquiel.suvlens.fragments

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import net.azarquiel.suvlens.R
import net.azarquiel.suvlens.adapters.OfertasAdapter
import net.azarquiel.suvlens.model.Camera

class BlankFragmentOfertas : Fragment(), SearchView.OnQueryTextListener{
    private lateinit var db: FirebaseFirestore
    private var cams: ArrayList<Camera> = ArrayList()
    private lateinit var searchView: SearchView
    private lateinit var adapter: OfertasAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_blank_ofertas, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        //if safe
        adapter = OfertasAdapter(requireActivity().baseContext, R.layout.row2)
        val rv = view.findViewById(R.id.rv1) as RecyclerView
        db = FirebaseFirestore.getInstance()


        rv.layoutManager = GridLayoutManager(context, 2)
        rv.adapter = adapter
        setListener()
        adapter.setCameras(cams)
    }

    private fun setListener() {
        val docRef = db.collection("products")
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


    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        // ************* <Filtro> ************
        val searchItem = menu.findItem(R.id.search)
        searchView = searchItem.actionView as SearchView
        searchView.queryHint = "Search..."
        searchView.setOnQueryTextListener(this)
        // ************* </Filtro> ************
    }

//     ************* <Filtro> ************
    override fun onQueryTextChange(query: String): Boolean {
        val original = ArrayList<Camera>(cams)
        adapter.setCameras(original.filter { camera ->
            camera.name.startsWith(
                query,
                ignoreCase = true
            )
        })
        return false
    }

    override fun onQueryTextSubmit(text: String): Boolean {
        return false
    }

    // ************* </Filtro> ************

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

