
package ru.ivmak.attendance_accounting

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import ru.ivmak.attendance_accounting.databinding.ActivitySplashBinding


const val RC_SIGN_IN = 1;

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private var user: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)

        user = FirebaseAuth.getInstance().currentUser

        // Choose authentication providers
        val providers = arrayListOf(
            AuthUI.IdpConfig.GoogleBuilder().build(),
            AuthUI.IdpConfig.EmailBuilder().build())

        binding.signIn.setOnClickListener {
            signIn(providers)
        }

        if (user != null) {
//            Toast.makeText(this, "User is signed in", Toast.LENGTH_SHORT).show()
            startMainActivity()
        } else {
            binding.signIn.visibility = View.VISIBLE
            binding.textView.visibility = View.VISIBLE
//            signIn(providers)
        }
    }

    private fun signIn(providers: List<AuthUI.IdpConfig>) {
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(),
            RC_SIGN_IN)
    }

    private fun startMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in
                user = FirebaseAuth.getInstance().currentUser
                startMainActivity()
//                Toast.makeText(this, """${user?.displayName} ${user?.uid}""", Toast.LENGTH_LONG).show()
                // ...
            } else {

                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }
    }
}