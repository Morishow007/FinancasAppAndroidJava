package com.bruno.brunoepaulafinancas.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.bruno.brunoepaulafinancas.R;
import com.bruno.brunoepaulafinancas.config.ConfiguracaoFirebase;
import com.bruno.brunoepaulafinancas.helper.DateCustom;
import com.bruno.brunoepaulafinancas.model.Movimentacao;
import com.bruno.brunoepaulafinancas.model.Usuario;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class ReceitasActivity extends AppCompatActivity {
    private TextInputEditText campoData, campoCategoria, campoDescricao;
    private EditText campoValor ;
    private Movimentacao movimentacao;
    private FloatingActionButton fab;
    private DatabaseReference firebaseref = ConfiguracaoFirebase.getFirebaseDataBase();
    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
    private double receitaTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receitas);

        campoCategoria = findViewById(R.id.editCategoria);
        campoData = findViewById(R.id.textData);
        campoDescricao = findViewById(R.id.editDescricao);
        campoValor = findViewById(R.id.textValor);
        fab = findViewById(R.id.fab_salvarReceita);

        //Set data atual
        campoData.setText(DateCustom.dataAtual());

        recuperarReceitaTotal();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvarReceita();
            }
        });

    }

    public void salvarReceita(){

        if(validarCampoReceitas()){
            movimentacao = new Movimentacao();
            String data = campoData.getText().toString();
            double valorRecuperado = Double.parseDouble(campoValor.getText().toString());

            movimentacao.setValor(valorRecuperado);
            movimentacao.setCategoria(campoCategoria.getText().toString());
            movimentacao.setDescricao(campoDescricao.getText().toString());
            movimentacao.setData(data);
            movimentacao.setTipo("r");

            //Dividir metodo
            double receitaAtualizada = receitaTotal+ valorRecuperado;
            atualizarReceita(receitaAtualizada);

            movimentacao.salvar(data);
            finish();
        }
    }

    public Boolean validarCampoReceitas(){
        String textoValor = campoValor.getText().toString();
        String textoData = campoData.getText().toString();
        String textoCategoria = campoCategoria.getText().toString();
        String textoDescricao = campoDescricao.getText().toString();

        if(!textoValor.isEmpty() && !textoCategoria.isEmpty() && !textoData.isEmpty() && !textoDescricao.isEmpty()){
            return true;
        } else{
            Toast.makeText(ReceitasActivity.this,
                    "Preencha todos os campos!",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public void recuperarReceitaTotal(){
        String idUsuario = autenticacao.getCurrentUser().getUid();
        DatabaseReference usuarioRef =  firebaseref.child("usuarios").child(idUsuario);
        usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Usuario usuario = snapshot.getValue(Usuario.class);
                receitaTotal = usuario.getReceitaTotal();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public  void atualizarReceita(Double receita){
        String idUsuario = autenticacao.getCurrentUser().getUid();
        DatabaseReference usuarioRef =  firebaseref.child("usuarios").child(idUsuario);

        usuarioRef.child("receitaTotal").setValue(receita);

    }
}