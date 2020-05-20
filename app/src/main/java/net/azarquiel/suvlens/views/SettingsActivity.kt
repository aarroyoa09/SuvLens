package net.azarquiel.suvlens.views

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_settings.*
import net.azarquiel.suvlens.R


class SettingsActivity : AppCompatActivity() {

    private var user1 = FirebaseAuth.getInstance().currentUser


    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        //SetUp
        val bundle = intent.extras
        val email = bundle?.getString("email")
        val provider = bundle?.getString("provider")
        setUp(email ?: "", provider ?: "")

    }


    private fun setUp(email: String, provider: String) {
        title = "Ajustes"


        btnLogOut.setOnClickListener {
            if (user1 == null) {
                Toast.makeText(
                    this,
                    "No estás logeado aún",
                    Toast.LENGTH_LONG
                ).show()
                val intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)
            } else {
                showAlert()
            }
        }

        ivsettings.setOnClickListener {
            if (user1 == null) {
                Toast.makeText(
                    this,
                    "Necesitas estar logeado para elegir una foto de perfil",
                    Toast.LENGTH_LONG
                ).show()
                val intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent, 0)
            }

        }
    }

    private fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Cerrar sesión")
        builder.setMessage("¿Está seguro de que quiere salir?")
        builder.setPositiveButton(
            "Aceptar",
            //Borrado de datos de usuario

            DialogInterface.OnClickListener { dialogInterface: DialogInterface, i: Int ->
                val prefs =
                    getSharedPreferences(
                        getString(R.string.prefs_file),
                        Context.MODE_PRIVATE
                    ).edit()
                prefs.clear()
                prefs.apply()

                FirebaseAuth.getInstance().signOut()
                onBackPressed()


                val intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)

            })
        builder.setNegativeButton("Cancelar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}
