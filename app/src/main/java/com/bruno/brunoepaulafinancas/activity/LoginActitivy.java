package com.bruno.brunoepaulafinancas.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bruno.brunoepaulafinancas.R;
import com.bruno.brunoepaulafinancas.config.ConfiguracaoFirebase;
import com.bruno.brunoepaulafinancas.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class LoginActitivy extends AppCompatActivity {

    private EditText campoEmail, campoSenha;
    private Button buttonLogin;
    private Usuario usuario;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_actitivy);

        campoEmail = findViewById(R.id.editEmailLogin);
        campoSenha = findViewById(R.id.editSenhaLogin);
        buttonLogin = findViewById(R.id.buttonLogin);
        getSupportActionBar().setTitle("Login de Usuario");

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textoEmail = campoEmail.getText().toString();
                String textoSenha = campoSenha.getText().toString();

                if (!textoEmail.isEmpty() && !textoSenha.isEmpty()){

                    usuario = new Usuario();
                    usuario.setSenha( textoSenha );
                    usuario.setEmail( textoEmail );
                    validarLogin();

                } else{
                    Toast.makeText(LoginActitivy.this,
                            "Preencha todos os campos",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public  void validarLogin(){

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.signInWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    abrirTelaPrincipal();

                } else {
                    String excecao = "";

                    try {
                        throw task.getException();
                    } catch (FirebaseAuthInvalidUserException e){
                        excecao = "Usuario nao esta cadastrado";
                    } catch (FirebaseAuthInvalidCredentialsException e){
                        excecao = "E-mail ou senha estao invalidos";

                    } catch (Exception e){
                        excecao = "Erro ao cadastrar usuario:" + e.getMessage();
                        e.printStackTrace();
                    }

                    Toast.makeText(LoginActitivy.this,
                            excecao,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public  void abrirTelaPrincipal(){
        startActivity(new Intent(this, PrincipalActivity.class));
        finish();
    }

}