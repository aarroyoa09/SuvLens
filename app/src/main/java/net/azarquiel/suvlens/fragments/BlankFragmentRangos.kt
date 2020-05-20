package net.azarquiel.suvlens.fragments

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import net.azarquiel.suvlens.R
import net.azarquiel.suvlens.adapters.RvAdapterRangos
import net.azarquiel.suvlens.model.Camera

class BlankFragmentRangos : Fragment(), SearchView.OnQueryTextListener {
    private lateinit var db: FirebaseFirestore
    private var cams: ArrayList<Camera> = ArrayList()
    private lateinit var adapter: RvAdapterRangos
    private lateinit var searchView: SearchView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
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
            val photo1 = d["photo1"] as String

            cams.add(Camera(name = name, photo1 = photo1))

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

}

