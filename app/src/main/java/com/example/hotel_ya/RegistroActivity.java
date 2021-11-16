package com.example.hotel_ya;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.hotel_ya.ui.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;

public class RegistroActivity extends AppCompatActivity {

    EditText objCorreo;
    EditText objContrasenia;

    EditText objNombre;
    EditText objApellidos;

    ImageView objFotoPerfil;

    EditText objTelefono;
    EditText objPais;
    Spinner objEstado;
    EditText objCodigoPostal;
    EditText objDomicilio;

    RadioGroup objGrupoGenero;
    RadioButton objMasculino;
    RadioButton objFemenino;

    EditText objFechaNacimiento;

    RadioGroup objGrupoTipoUsuario;
    RadioButton objTipoReservaciones;
    RadioButton objTipoHoteles;

    Button objRegistrar;
    Button objLimpiar;
    Button objRegresarLogin;

    //Firebase
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    StorageReference storageProfilePicsRef;

    //Imagenes
    String ruta="";
    Uri imageUri;
    String myUri = "";

    //Cadenas
    String sCorreo="", sContrasenia="", sNombre="", sApellidos="", sRuta="", sTelefono="", sPais="", sEstado="", sCodigoPostal="", sDomicilio="", sSexo="", sFechaNacimiento="", sTipoUsuario="";
    int aux_permiso_hotel=0;

    //Fecha
    DatePickerDialog dpd;
    Calendar c;
    private static int anio, mes, dia;
    DatePickerDialog.OnDateSetListener setListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("RTDB_Users");
        storageProfilePicsRef = FirebaseStorage.getInstance().getReference().child("STORAGE_Users_Profile_Pics");

        asignar();

        fecha();

        registrar();

        tomarFoto();

        limpiarCampos();

        regresarLogin();


    }


    private void regresarLogin() {

        objRegresarLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegistroActivity.this, LoginActivity.class));
            }
        });

    }

    private void limpiarCampos() {

        objLimpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                limpiar();
            }
        });

    }

    private void limpiar() {

        objCorreo.setText("");
        sCorreo="";
        objContrasenia.setText("");
        sContrasenia="";

        objNombre.setText("");
        sNombre = "";
        objApellidos.setText("");
        sApellidos = "";

        objFotoPerfil.setImageResource(R.drawable.ic_menu_camera);
        sRuta = "";
        ruta = "";
        imageUri = null;
        myUri = "";

        objTelefono.setText("");
        sTelefono = "";
        objPais.setText("");
        sPais = "";
        objEstado.setSelection(0);
        sEstado = "";
        objCodigoPostal.setText("");
        sCodigoPostal = "";
        objDomicilio.setText("");
        sDomicilio = "";

        //objMasculino.setChecked(false);
        //objFemenino.setSelected(false);
        objGrupoGenero.clearCheck();
        sSexo = "";

        objFechaNacimiento.setText("");
        sFechaNacimiento = "";

        //objTipoReservaciones.setSelected(false);
        //objTipoHoteles.setSelected(false);
        objGrupoTipoUsuario.clearCheck();
        aux_permiso_hotel = 0;
        sTipoUsuario = "";

    }

    private boolean estaVacio(){

        int aux_EstadoPosicion = objEstado.getSelectedItemPosition();

        if (
                sCorreo.equals("")
                        || sContrasenia.equals("")
                        || sNombre.equals("")
                        || sApellidos.equals("")
                        || sRuta.equals("")
                        || sTelefono.equals("")
                        || sPais.equals("")
                        || sEstado.equals("")
                        || aux_EstadoPosicion==0
                        || sCodigoPostal.equals("")
                        || sDomicilio.equals("")
                        || sSexo.equals("")
                        || sFechaNacimiento.equals("")
                        || sTipoUsuario.equals("")
        ) {
            return false;
        } else {
            return true;
        }
    }

    private void registrar() {

        objRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                obtenerStrings();

                if (estaVacio()){
                    //Toast.makeText(RegisterActivity.this, sCorreo+ sContrasenia+ sNombre+ sApellidos+ sRuta+ sTelefono+ sPais+ sEstado+ sCodigoPostal+ sDomicilio+ sSexo+ sFechaNacimiento+ sTipoUsuario, Toast.LENGTH_SHORT).show();

                    mAuth.createUserWithEmailAndPassword(sCorreo, sContrasenia).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            Toast.makeText(RegistroActivity.this, "Datos Registrados", Toast.LENGTH_SHORT).show();

                            mAuth.signInWithEmailAndPassword(sCorreo, sContrasenia).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (imageUri != null){

                                        final StorageReference fileRef = storageProfilePicsRef.child( mAuth.getCurrentUser().getUid()+".jpg");

                                        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                                Toast.makeText(RegistroActivity.this, "Subiendo", Toast.LENGTH_SHORT).show();

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                                Toast.makeText(RegistroActivity.this, "Error", Toast.LENGTH_SHORT).show();

                                            }
                                        }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                                                UploadTask.TaskSnapshot downloadUri = null;
                                                downloadUri = task.getResult();
                                                Task<Uri> result = downloadUri.getStorage().getDownloadUrl();
                                                result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {

                                                        String aux = uri.toString();

                                                        String aux_ID = mAuth.getCurrentUser().getUid();

                                                        myUri = aux;

                                                        HashMap<String, Object> userMap = new HashMap<>();
                                                        userMap.put("c_usu_aa_id_usuario", aux_ID);

                                                        userMap.put("c_usu_ab_correo", sCorreo);
                                                        userMap.put("c_usu_ac_contrasenia", sContrasenia);

                                                        userMap.put("c_usu_ad_nombre", sNombre);
                                                        userMap.put("c_usu_ae_apellidos", sApellidos);
                                                        userMap.put("c_usu_af_ruta_img", myUri);
                                                        userMap.put("c_usu_ag_telefono", sTelefono);
                                                        userMap.put("c_usu_ah_pais", sPais);
                                                        userMap.put("c_usu_ai_estado", sEstado);
                                                        userMap.put("c_usu_aj_codigo_postal", sCodigoPostal);
                                                        userMap.put("c_usu_ak_domicilio", sDomicilio);
                                                        userMap.put("c_usu_al_sexo", sSexo);
                                                        userMap.put("c_usu_am_fecha_nacimiento", sFechaNacimiento);
                                                        userMap.put("c_usu_an_reputacion", 0);

                                                        userMap.put("c_usu_ao_fecha_creacion", getFechaMilisegundos() * -1);
                                                        userMap.put("c_usu_ap_status", 1);
                                                        userMap.put("c_usu_aq_permiso_hotel", aux_permiso_hotel);
                                                        userMap.put("c_usu_ar_tipo_usuario", sTipoUsuario);

                                                        databaseReference.child( "Usuarios1" ).child( aux_ID ).setValue( userMap ).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                Toast.makeText(RegistroActivity.this, "Completado ", Toast.LENGTH_LONG).show();
                                                                mAuth.signOut();
                                                                limpiar();

                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toast.makeText(RegistroActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });

                                                    }
                                                });

                                            }
                                        });

                                    }

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(RegistroActivity.this, "Error en la BD", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RegistroActivity.this, "Error en la BD", Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    Toast.makeText(RegistroActivity.this, "Debe llenar los campos", Toast.LENGTH_SHORT).show();
                }




            }
        });


    }

    private void obtenerStrings() {

        sCorreo = objCorreo.getText().toString();
        sContrasenia = objContrasenia.getText().toString();

        sNombre = objNombre.getText().toString();
        sApellidos = objApellidos.getText().toString();

        sRuta = ruta; //objFotoPerfil;

        sTelefono = objTelefono.getText().toString();
        sPais = objPais.getText().toString();
        sEstado = objEstado.getSelectedItem().toString();
        sCodigoPostal = objCodigoPostal.getText().toString();
        sDomicilio = objDomicilio.getText().toString();

        String aux_Sexo = "";
        if ( objMasculino.isChecked() ){
            aux_Sexo = "Masculino";
        } else if ( objFemenino.isChecked() ){
            aux_Sexo = "Femenino";
        } else {
            aux_Sexo = "";
        }
        sSexo = aux_Sexo;

        sFechaNacimiento = objFechaNacimiento.getText().toString();

        String aux_TipoUsuario = "";
        if ( objTipoReservaciones.isChecked() ){
            aux_TipoUsuario = "Reservaciones";
            aux_permiso_hotel = 0;
        } else if ( objTipoHoteles.isChecked() ){
            aux_TipoUsuario = "Hoteles";
            aux_permiso_hotel = 1;
        } else {
            aux_TipoUsuario = "";
            aux_permiso_hotel = 0;
        }
        sTipoUsuario = aux_TipoUsuario;

    }

    public void tomarFoto(){
        objFotoPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                camara();
            }
        });
    }

    public void camara(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity( getPackageManager() ) != null){

            File fotoArchivo = null;
            try {
                fotoArchivo = guardarImagen();
            } catch (IOException ex) {
                Log.e("Error", ex.toString());
            }
            if (fotoArchivo != null){
                Uri uri = FileProvider.getUriForFile(this, getPackageName()+".fileprovider", fotoArchivo);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                imageUri = uri;
                startActivityForResult(intent, 1);
            }


        }
    }

    private File guardarImagen() throws IOException{
        String nombreFoto = "foto_";
        File directorio = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File foto = File.createTempFile(nombreFoto, ".jpg", directorio);
        ruta = foto.getAbsolutePath();

        return foto;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK){
            Bitmap imgBitmap = BitmapFactory.decodeFile(ruta);
            objFotoPerfil.setImageBitmap(imgBitmap);
            Toast.makeText(RegistroActivity.this, ruta, Toast.LENGTH_SHORT).show();
        }

    }

    public long getFechaMilisegundos(){

        Calendar calendar = Calendar.getInstance();
        long tiempoUnix = calendar.getTimeInMillis();
        return tiempoUnix;

    }

    public void fecha(){

        c = Calendar.getInstance();
        anio = c.get(Calendar.YEAR);
        mes = c.get(Calendar.MONTH);
        dia = c.get(Calendar.DAY_OF_MONTH);

        objFechaNacimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dpd = new DatePickerDialog( RegistroActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth, setListener, anio, mes, dia);
                dpd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dpd.show();
            }
        });

        setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = dia +"/"+month+"/"+year;
                objFechaNacimiento.setText(date);


            }
        };


    }

    private void asignar() {

        objCorreo = findViewById(R.id.idRegisterEditTextCorreoElectronico);
        objContrasenia = findViewById(R.id.idRegisterEditTextContrasenia);

        objNombre = findViewById(R.id.idRegisterEditTextNombre);
        objApellidos = findViewById(R.id.idRegisterEditTextApellidos);

        objFotoPerfil = findViewById(R.id.idRegisterImageViewFotoPerfil);

        objTelefono = findViewById(R.id.idRegisterEditTextTelefono);
        objPais = findViewById(R.id.idRegisterEditTextPais);

        objEstado = findViewById(R.id.idRegisterSpinnerEstado);
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(RegistroActivity.this, R.array.estado, android.R.layout.simple_spinner_item);
        objEstado.setAdapter(adapter);

        objCodigoPostal = findViewById(R.id.idRegisterEditTextCodigoPostal);
        objDomicilio = findViewById(R.id.idRegisterEditTextDomicilio);

        objGrupoGenero = findViewById(R.id.idRegisterRadioGroupGenero);
        objMasculino = findViewById(R.id.idRegisterRadioButtonMasculino);
        objFemenino = findViewById(R.id.idRegisterRadioButtonFemenino);

        objFechaNacimiento = findViewById(R.id.idRegisterEdiTextFechaNacimiento);

        objGrupoTipoUsuario = findViewById(R.id.idRegisterRadioGroupTipoUsuario);
        objTipoReservaciones = findViewById(R.id.idRegisterRadioButtonReservaciones);
        objTipoHoteles = findViewById(R.id.idRegisterRadioButtonHoteles);

        objRegistrar = findViewById(R.id.idRegisterButtonRegistrar);
        objLimpiar = findViewById(R.id.idRegisterButtonLimpiar);
        objRegresarLogin = findViewById(R.id.idRegisterButtonRegresarLogin);

    }


}