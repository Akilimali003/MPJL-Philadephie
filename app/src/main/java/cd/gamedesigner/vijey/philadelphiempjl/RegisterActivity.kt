package cd.gamedesigner.vijey.philadelphiempjl

import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.Toast
import cd.gamedesigner.vijey.philadelphiempjl.models.User
import cd.gamedesigner.vijey.philadelphiempjl.views.BiographyActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        text_view_already_register_register_activity.setOnClickListener {
            finish()
        }

        btn_signin_register_activity.setOnClickListener {
            val alertDialog = AlertDialog.Builder(this)
            alertDialog.setTitle("Créer compte")
            alertDialog.setMessage("Etes-vous sure de vouloir enregister cet email ?")
            alertDialog.setPositiveButton("Oui"){ dialog: DialogInterface?, which: Int ->
                toast("Patienter svp...")
                //call the method
                performeRegister()
            }
            alertDialog.setNegativeButton("Non"){dialog: DialogInterface?, which: Int -> }

            alertDialog.show()
        }
    }


    //register function
    private fun performeRegister(){
        val email = edit_email_register_activity.text.toString()
        val password = edit_password_register_activity.text.toString()
        val username = edit_username_register_activity.text.toString()

        progressBar.visibility = View.VISIBLE

        //if mail, password or username is empty
        if(email.isEmpty() || password.isEmpty() || username.isEmpty()){
            progressBar.visibility = View.INVISIBLE
            toast("Completer tous les champs vide s'il vous plait !")
            return
        }
        //Firebase Authentication to create a user with email and password
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if(!it.isSuccessful){
                    progressBar.visibility = View.INVISIBLE

                    edit_email_register_activity.setText("")
                    edit_password_register_activity.setText("")
                    edit_username_register_activity.setText("")
                    return@addOnCompleteListener
                } else {
                    edit_email_register_activity.setText("")
                    edit_password_register_activity.setText("")
                    edit_username_register_activity.setText("")

                    toast("Bienvenu ${it.result.user.uid}")
                    //call the method saveUser
                    saveUserToFirebaseDatabase()
                    progressBar.visibility = View.INVISIBLE
                }
            }.addOnFailureListener{

                progressBar.visibility = View.INVISIBLE
                toast("Impossible de créer un compte: ${it.message}")

                edit_email_register_activity.setText("")
                edit_password_register_activity.setText("")
                edit_username_register_activity.setText("")
            }
    }

    //Save the user to FirebaseDatabase
    private fun saveUserToFirebaseDatabase(){
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        val user = User(
            uid,
            edit_username_register_activity.text.toString()
        )
        ref.setValue(user).addOnSuccessListener {
            toast("Utilisateur enregistrer avec succes !")

            val intent = Intent(this, BiographyActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)

        }.addOnFailureListener{
            toast("Enregistrement d'utilisateur impossible: ${it.message}")
        }
    }

    private fun toast(message: String){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
