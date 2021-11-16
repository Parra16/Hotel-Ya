package com.example.hotel_ya.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hotel_ya.MainActivity;
import com.example.hotel_ya.R;
import com.example.hotel_ya.RegistroActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;

public class LoginActivity extends AppCompatActivity {

    EditText objCorreo;
    EditText objContrasenia;

    Button objIniciarSesion;
    Button objRegistrarse;
    Button objSalir;

    //Firebase
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    StorageReference storageProfilePicsRef;

    //Cadenas
    String sCorreo="", sContrasenia="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        asignar();
        iniciarSesion();
        registrarse();
        salir();

    }

    private boolean estaVacio(){

        if (sCorreo.equals("") || sContrasenia.equals("")){
            return false;
        } else {
            return true;
        }

    }

    private void obtenerStrings(){

        sCorreo = objCorreo.getText().toString();
        sContrasenia = objContrasenia.getText().toString();

    }

    private void salir() {

        objSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishAffinity();
            }
        });

    }

    private void registrarse() {

        objRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegistroActivity.class));
            }
        });

    }

    private void iniciarSesion() {

        objIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                obtenerStrings();

                if (estaVacio()){

                    mAuth.signInWithEmailAndPassword(sCorreo, sContrasenia).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()){
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                Toast.makeText(LoginActivity.this, "Bienvenido", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(LoginActivity.this, "Error al Iniciar Sesión", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(LoginActivity.this, "Error al Inciar Sesión", Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    Toast.makeText(LoginActivity.this, "Debe llenar los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void asignar() {

        objCorreo = findViewById(R.id.idLoginEditTextCorreoElectronico);
        objContrasenia = findViewById(R.id.idLoginEditTextContrasenia);

        objIniciarSesion = findViewById(R.id.idLoginButtonIniciarSesion);
        objRegistrarse = findViewById(R.id.idLoginButtonRegistrarse);
        objSalir = findViewById(R.id.idLoginButtonSalir);

    }


}