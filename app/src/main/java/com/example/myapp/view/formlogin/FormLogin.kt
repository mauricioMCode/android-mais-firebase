package com.example.myapp.view.formlogin

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.myapp.R
import com.example.myapp.databinding.ActivityFormLoginBinding
import com.example.myapp.view.formcadastro.FormCadastro
import com.example.myapp.view.telaprincipal.TelaPrincipal
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException

class FormLogin : AppCompatActivity() {

    private lateinit var binding: ActivityFormLoginBinding
    private val auth = FirebaseAuth.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btEntrar.setOnClickListener { view ->

            val email = binding.etEmail.text.toString()
            val senha = binding.etSenha.text.toString()

            if (email.isEmpty() || senha.isEmpty()) {
                val snackbar = Snackbar.make(
                    view,
                    "Preencha todos os campos para continuar!",
                    Snackbar.LENGTH_SHORT
                )
                snackbar.show()
            } else {
                auth.signInWithEmailAndPassword(email, senha)
                    .addOnCompleteListener { autenticacao ->
                        if (autenticacao.isSuccessful) {
                            navegarTelaPrincipal()

                        }
                    }.addOnFailureListener { exception ->
                        val mensagemError = when (exception) {
                            is FirebaseAuthWeakPasswordException -> "Não encontramos uma conta associada a este endereço de e-mail!"
                            //is FirebaseAuthUserCollisionException -> "já existe uma conta criada com esse e-mail, siga para o Login"
                            is FirebaseNetworkException -> "Não foi possível acessar a conta, verifique a sua conexão com a internet"
                            is FirebaseAuthInvalidCredentialsException -> "Login e Senha inválidos!"
                            else -> "Erro, Não encontramos uma conta associada a este endereço de e-mail!"

                        }
                        val snackbar = Snackbar.make(
                            view,
                            mensagemError,
                            Snackbar.LENGTH_SHORT
                        )
                        snackbar.setBackgroundTint(Color.RED)
                        snackbar.show()

                    }

            }
        }

        binding.txtTelaCadastro.setOnClickListener {
            val intent = Intent(this, FormCadastro::class.java)
            startActivity(intent)

        }
    }

    private fun navegarTelaPrincipal() {
        val intent = Intent(this, TelaPrincipal::class.java)
        startActivity(intent)
        finish()
    }

    override fun onStart() {
        super.onStart()

        val usuarioAtual = FirebaseAuth.getInstance().currentUser

        if(usuarioAtual != null){
            navegarTelaPrincipal()
        }
    }
}