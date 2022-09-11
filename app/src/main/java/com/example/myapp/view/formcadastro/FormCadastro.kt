package com.example.myapp.view.formcadastro

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapp.R
import com.example.myapp.databinding.ActivityFormCadastroBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException

class FormCadastro : AppCompatActivity() {

    private lateinit var binding: ActivityFormCadastroBinding
    private val auth = FirebaseAuth.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormCadastroBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btCadastrar.setOnClickListener { view ->
            val email = binding.etEmail.text.toString()
            val senha = binding.etSenha.text.toString()

            if (email.isEmpty() || senha.isEmpty()) {
                val snackbar = Snackbar.make(
                    view,
                    "Por favor preencha todos os campos para prosseguir o seu cadastro!",
                    Snackbar.LENGTH_SHORT
                )
                snackbar.setBackgroundTint(Color.RED)
                snackbar.show()
            } else {
                auth.createUserWithEmailAndPassword(email, senha)
                    .addOnCompleteListener { cadastro ->
                        if (cadastro.isSuccessful) {
                            val snackbar = Snackbar.make(
                                view,
                                "Conta criada com sucesso!",
                                Snackbar.LENGTH_SHORT
                            )
                            snackbar.setBackgroundTint(Color.BLUE)
                            snackbar.show()
                            binding.etEmail.setText("")
                            binding.etSenha.setText("")

                        }
                    }.addOnFailureListener { exception ->
                    val mensagemError = when (exception) {
                        is FirebaseAuthWeakPasswordException -> "Crie uma senha de até seis caracteres!"
                        is FirebaseAuthInvalidCredentialsException -> "Digite um e-mail válido"
                        is FirebaseAuthUserCollisionException -> "Error, já existe uma conta criada com esse e-mail, siga para o Login"
                        is FirebaseNetworkException -> "Não foi possível criar a conta, verifique a sua conexão com a internet"
                        else -> "Erro ao criar conta!"

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


    }
}