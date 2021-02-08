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

public class DespesasActivity extends AppCompatActivity {

    private TextInputEditText  campoData, campoCategoria, campoDescricao;
    private EditText campoValor;
    private Movimentacao movimentacao;
    private FloatingActionButton fab;
    private DatabaseReference firebaseref = ConfiguracaoFirebase.getFirebaseDataBase();
    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
    private double despesaTotal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_despesas);

        campoCategoria = findViewById(R.id.editCategoria);
        campoData = findViewById(R.id.textData);
        campoDescricao = findViewById(R.id.editDescricao);
        campoValor = findViewById(R.id.editValor);
        fab = findViewById(R.id.fab_salvar_despesa);

        //Set data atual
        campoData.setText(DateCustom.dataAtual());

        recuperarDespesaTotal();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvarDespesa();

            }
        });


    }

    public void salvarDespesa(){

        if(validarCampoDespesas()){
            movimentacao = new Movimentacao();
            String data = campoData.getText().toString();
            double valorRecuperado = Double.parseDouble(campoValor.getText().toString());

            movimentacao.setValor(valorRecuperado);
            movimentacao.setCategoria(campoCategoria.getText().toString());
            movimentacao.setDescricao(campoDescricao.getText().toString());
            movimentacao.setData(data);
            movimentacao.setTipo("d");

            //Dividir metodo
            double despesaAtualizada = despesaTotal+ valorRecuperado;
            atualizarDespesa(despesaAtualizada);

            movimentacao.salvar(data);
            finish();
        }
    }

    

    public Boolean validarCampoDespesas(){
        String textoValor = campoValor.getText().toString();
        String textoData = campoData.getText().toString();
        String textoCategoria = campoCategoria.getText().toString();
        String textoDescricao = campoDescricao.getText().toString();

        if(!textoValor.isEmpty() && !textoCategoria.isEmpty() && !textoData.isEmpty() && !textoDescricao.isEmpty()){
            return true;
        } else{
            Toast.makeText(DespesasActivity.this,
                    "Preencha todos os campos!",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public void recuperarDespesaTotal(){
        String idUsuario = autenticacao.getCurrentUser().getUid();
        DatabaseReference usuarioRef =  firebaseref.child("usuarios").child(idUsuario);
        usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Usuario usuario = snapshot.getValue(Usuario.class);
                despesaTotal = usuario.getDespesaTotal();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public  void atualizarDespesa(Double despesa){
        String idUsuario = autenticacao.getCurrentUser().getUid();
        DatabaseReference usuarioRef =  firebaseref.child("usuarios").child(idUsuario);

        usuarioRef.child("despesaTotal").setValue(despesa);

    }
}