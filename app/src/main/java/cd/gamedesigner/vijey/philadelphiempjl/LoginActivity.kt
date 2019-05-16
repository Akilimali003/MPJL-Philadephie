package cd.gamedesigner.vijey.philadelphiempjl

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import cd.gamedesigner.vijey.philadelphiempjl.models.User
import cd.gamedesigner.vijey.philadelphiempjl.views.BiographyActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        text_view_not_yet_register_login_activity.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        btn_login_login_activity.setOnClickListener {
            performeLogin()
        }
    }

    private fun performeLogin(){
        val email = edit_email_login_activity.text.toString()
        val password = edit_password_login_activity.text.toString()

        progressBar1.visibility = View.VISIBLE

        //if mail or password is empty
        if(email.isEmpty() || password.isEmpty()){
            progressBar1.visibility = View.INVISIBLE
            toast("Completer l'email et le mot de passe s'il vous plait !")
            return
        }

        //Firebase Authentication to create a user with email and password
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if(!it.isSuccessful){
                    progressBar1.visibility = View.INVISIBLE
                    edit_email_login_activity.setText("")
                    edit_password_login_activity.setText("")
                    return@addOnCompleteListener
                } else {
                    progressBar1.visibility = View.INVISIBLE
                    val intent = Intent(this, BiographyActivity::class.java)
                    startActivity(intent)

                    edit_email_login_activity.setText("")
                    edit_password_login_activity.setText("")
                }
                //kill the login activity in the memory
                finish()
            }.addOnFailureListener{
                progressBar1.visibility = View.INVISIBLE
                toast("Impossible de vous connecter: ${it.message}")
                edit_email_login_activity.setText("")
                edit_password_login_activity.setText("")
            }
    }

    private fun toast(message: String){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
