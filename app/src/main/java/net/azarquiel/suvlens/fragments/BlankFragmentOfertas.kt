package net.azarquiel.suvlens.fragments

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import net.azarquiel.suvlens.R
import net.azarquiel.suvlens.adapters.OfertasAdapter
import net.azarquiel.suvlens.adapters.RvAdapterMarcas
import net.azarquiel.suvlens.model.Camera

class BlankFragmentOfertas : Fragment(), SearchView.OnQueryTextListener {
    private lateinit var db: FirebaseFirestore
    private var cams: ArrayList<Camera> = ArrayList()
    private lateinit var searchView: SearchView
    private lateinit var adapter: OfertasAdapter
    private lateinit var lm: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_blank_ofertas, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = OfertasAdapter(requireActivity().baseContext, R.layout.row2)
        val rv = view.findViewById(R.id.rv1) as RecyclerView
        db = FirebaseFirestore.getInstance()
        rv.adapter = adapter
        rv.setLayoutManager(GridLayoutManager(context, 2))
        setListener()
        adapter.setCameras(cams)

//        adapter.notifyDataSetChanged()

//        lm = LinearLayoutManager(activity)
    }

    private fun setListener() {
        val docRef = db.collection("ofertas")
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


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.main, menu)
        // ************* <Filtro> ************
        val searchItem = menu.findItem(R.id.search)
        searchView = searchItem.actionView as SearchView
        searchView.setQueryHint("Search...")
        searchView.setOnQueryTextListener(this)
        // ************* </Filtro> ************

    }


    // ************* <Filtro> ************
    override fun onQueryTextChange(query: String): Boolean {
        val adapter = OfertasAdapter(requireActivity().baseContext, R.layout.row2)
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

    //
    private fun documentToList(documents: List<DocumentSnapshot>) {
        cams.clear()
        documents.forEach { d ->
            val name = d["name"] as String
            val price = d["price"] as Double
            val photo = d["photo"] as String
            val brand = d["brand"] as String
            val type = d["type"] as String

            cams.add(Camera(name = name, price = price, photo = photo, brand = brand, type = type))

//            if (price > 3330.0 && price < 6000.0) {
//                cams.add(Camera(name = name, price = price, photo = photo, brand = brand))
//            }
        }
    }
}

