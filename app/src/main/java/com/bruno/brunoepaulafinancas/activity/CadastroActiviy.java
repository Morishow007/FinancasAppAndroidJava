package com.bruno.brunoepaulafinancas.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bruno.brunoepaulafinancas.R;
import com.bruno.brunoepaulafinancas.config.ConfiguracaoFirebase;
import com.bruno.brunoepaulafinancas.helper.Base64Custom;
import com.bruno.brunoepaulafinancas.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CadastroActiviy extends AppCompatActivity {


    private EditText campoNome, campoEmail, campoSenha;
    private Button botaoCadastrar;
    private FirebaseAuth autenticacao;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_activiy);

        campoEmail = findViewById(R.id.editEmail);
        campoNome = findViewById(R.id.editNome);
        campoSenha = findViewById(R.id.editSenha);
        botaoCadastrar = findViewById(R.id.buttonCadastrar);
        getSupportActionBar().setTitle("Cadastro Novo Usuario");

        botaoCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String textoNome = campoNome.getText().toString();
                String textoEmail = campoEmail.getText().toString();
                String textoSenha = campoSenha.getText().toString();

                if (!textoNome.isEmpty() && !textoEmail.isEmpty() && !textoSenha.isEmpty()){

                    usuario = new Usuario();
                    usuario.setNome(textoNome);
                    usuario.setEmail(textoEmail);
                    usuario.setSenha(textoSenha);

                    cadastrarUsuario();

                } else{
                    Toast.makeText(CadastroActiviy.this,
                            "Preencha todos os campos",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void cadastrarUsuario(){
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    String idUsuario = Base64Custom.codificarBase64(usuario.getEmail());

                    DatabaseReference firebaseDataBase = FirebaseDatabase.getInstance().getReference();
                    firebaseDataBase.child("usuarios")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(usuario);
                    finish();

                }else{
                    String excecao = "";

                    try {
                        throw task.getException();
                    }   catch(FirebaseAuthWeakPasswordException e){
                        excecao = "Digite uma senha mais forte!";
                    }   catch(FirebaseAuthInvalidCredentialsException e){
                        excecao = "Digite um e-mail valido!";
                    }   catch(FirebaseAuthUserCollisionException e){
                        excecao = "Essa conta ja foi cadastrada";
                    }   catch (Exception e){
                        excecao = "Erro ao cadastrar usuario:" + e.getMessage();
                        e.printStackTrace();
                    }

                    Toast.makeText(CadastroActiviy.this,
                            excecao,
                            Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}