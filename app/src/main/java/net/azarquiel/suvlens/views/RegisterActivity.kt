package net.azarquiel.suvlens.views

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import net.azarquiel.suvlens.R
import java.util.*

class RegisterActivity : AppCompatActivity() {

    private val GOOGLE_SIGN_IN = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        when {
            btnfbr.isPressed -> {
                Toast.makeText(this, "Facebook", Toast.LENGTH_LONG).show()
            }
            btngr.isPressed -> {
                Toast.makeText(this, "Google", Toast.LENGTH_LONG).show()
            }
            btntwr.isPressed -> {
                Toast.makeText(this, "Twitter", Toast.LENGTH_LONG).show()
            }
            btngologin.isPressed -> {
                Toast.makeText(this, "Login", Toast.LENGTH_LONG).show()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
            btnregisterr.isPressed -> {
                Toast.makeText(this, "Register", Toast.LENGTH_LONG).show()
            }
        }

        setup()
        session()
    }

//    override fun onStart() {
//        super.onStart()
//
//        authLayout.visibility = View.VISIBLE
//    }

    private fun session() {
        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val email = prefs.getString("email", null)
        val provider = prefs.getString("provider", null)

        if (email != null && provider != null) {
            authLayout.visibility = View.INVISIBLE
            showMain(email, ProviderType.valueOf(provider))

        }
    }

    private fun setup() {
        title = "Register"

        btnregisterr.setOnClickListener {
            if (etuserr.text.isNotEmpty() && etpassr.text.isNotEmpty()) {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                    etuserr.text.toString(),
                    etpassr.text.toString()
                ).addOnCompleteListener {
                    if (it.isSuccessful) {
                        showMain(it.result?.user?.email ?: "", ProviderType.BASIC)
                    } else {
                        showAlert()
                    }
                }
            }
        }

        btngologin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        btngr.setOnClickListener {
            //Config
            val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

            val googleClient = GoogleSignIn.getClient(this, googleConf)
            googleClient.signOut()

            startActivityForResult(googleClient.signInIntent, GOOGLE_SIGN_IN)
        }

        ivregister.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }
    }

    private fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Error de autenticación")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showMain(email: String, provider: ProviderType) {
        val mainIntent = Intent(this, MainActivity::class.java).apply {
            putExtra("email", email)
            putExtra("provider", provider.name)
        }
        startActivity(mainIntent)
    }

    var selectedPhoto: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            selectedPhoto = data.data

            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhoto)

            val bitmapDrawable = BitmapDrawable(bitmap)
            ivregister.setBackgroundDrawable(bitmapDrawable)
        }

        if (requestCode == GOOGLE_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            try {

                val account = task.getResult(ApiException::class.java)

                if (account != null) {
                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                    FirebaseAuth.getInstance().signInWithCredential(credential)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                showMain(account.email ?: "", ProviderType.GOOGLE)
                            } else {
                                showAlert()
                            }
                            uploadFBS()
                        }
                }
            } catch (e: ApiException) {
                showAlert()
            }
        }
    }

    private fun uploadFBS() {
        if (selectedPhoto != null) return
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/users/$filename")

        ref.putFile(selectedPhoto!!).addOnSuccessListener {
            Log.d("RegisterActivity", "Succesfully uploaded: ${it.metadata?.path}")

            ref.downloadUrl.addOnSuccessListener {
                it.toString()
                Log.d("RegisterAc", "File location: $it")

//                saveToFBS()
            }
        }
    }

//    private fun saveToFBS(image: String){
//        val uid = FirebaseAuth.getInstance().uid
//        val ref =FirebaseFirestore.getInstance().collection("users").document()
//
//        val user = User(uid, etname.text.toString(), image)
//
//        ref.setValue(user)
//    }
}

class User(val uid: String, val username: String, val image: String)