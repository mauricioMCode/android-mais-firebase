package com.example.myapp.view.telaprincipal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.myapp.R
import com.example.myapp.databinding.ActivityTelaPrincipalBinding
import com.example.myapp.view.formlogin.FormLogin
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class TelaPrincipal : AppCompatActivity() {

    private lateinit var binding: ActivityTelaPrincipalBinding
    private val db = FirebaseFirestore.getInstance()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTelaPrincipalBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.btSair.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            val voltarTelaLogin = Intent(this,FormLogin::class.java)
            startActivity(voltarTelaLogin)
            finish()
        }

        binding.btGravarDadosDB.setOnClickListener{
            val usuariosMap = hashMapOf(
                "nome" to "Maria",
                "sobrenome" to "de Nazaré",
                "idade" to 68

            )

            db.collection("Usuários").document("Maria")
                .set(usuariosMap).addOnCompleteListener{
                    Log.d("db", "Dados salvos com sucesso!")
                }.addOnFailureListener{
                    //
                }
        }

       binding.btLerDadosDB.setOnClickListener{
           db.collection("Usuários").document("Maria")
               .addSnapshotListener { documento, error ->
                   if (documento != null){
                       val idade = documento.getLong("idade")
                       binding.tvResultadoNome.text = documento.getString("nome")
                       binding.tvResultadoSobrenome.text = documento.getString("sobrenome")
                       binding.tvResultadoIdade.text = idade.toString()

                   }

               }
       }

        binding.btAtualizarDadosDB.setOnClickListener {
            db.collection("Usuários").document("Maria")
                .update("sobrenome", "de Lima", "idade",21).addOnCompleteListener{
                    Log.d("db_update", "Dados atualizados com sucesso!")
                }
        }

        binding.btDeletarDadosDB.setOnClickListener {
            db.collection("Usuários").document("Maria")
                .delete().addOnCompleteListener {
                    Log.d("db_delete", "Dados excluídos com sucesso!")
                }
        }

    }
}