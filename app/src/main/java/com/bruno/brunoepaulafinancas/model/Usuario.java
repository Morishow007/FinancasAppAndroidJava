package com.bruno.brunoepaulafinancas.model;

import com.bruno.brunoepaulafinancas.config.ConfiguracaoFirebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;

public class Usuario {

   private String nome ;
   private String email;
   private String senha;
   private double receitaTotal = 0;
   private double despesaTotal = 0;

    public Usuario() {
    }
//
//    public void salvar(){
//        DatabaseReference firebaseDataBase = FirebaseDatabase.getInstance().getReference();
//        firebaseDataBase.child("usuarios")
//                .child(email)
//                .setValue("testesetrsefds");
//
//    }


    public double getReceitaTotal() {
        return receitaTotal;
    }

    public void setReceitaTotal(double receitaTotal) {
        this.receitaTotal = receitaTotal;
    }

    public double getDespesaTotal() {
        return despesaTotal;
    }

    public void setDespesaTotal(double despesaTotal) {
        this.despesaTotal = despesaTotal;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Exclude
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
