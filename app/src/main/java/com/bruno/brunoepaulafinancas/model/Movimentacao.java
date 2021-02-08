package com.bruno.brunoepaulafinancas.model;

import com.bruno.brunoepaulafinancas.config.ConfiguracaoFirebase;
import com.bruno.brunoepaulafinancas.helper.DateCustom;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class Movimentacao {

    private String data;
    private String categoria;
    private String descricao;
    private String tipo;
    private double valor;
    private  String key;

    public Movimentacao() {
    }

    public void salvar(String dataEscolhida){
        DatabaseReference firebase = ConfiguracaoFirebase.getFirebaseDataBase();
        String mesANo = DateCustom.mesAnoDataEscolhida(dataEscolhida);
        firebase.child("movimentacao")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(mesANo)
                .push()
                .setValue(this);

    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }
}
