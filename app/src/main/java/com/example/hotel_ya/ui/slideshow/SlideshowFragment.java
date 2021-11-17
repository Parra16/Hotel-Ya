package com.example.hotel_ya.ui.slideshow;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.hotel_ya.R;
import com.example.hotel_ya.databinding.FragmentSlideshowBinding;
import com.example.hotel_ya.ui.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class SlideshowFragment extends Fragment {

    private SlideshowViewModel slideshowViewModel;
    private FragmentSlideshowBinding binding;

    EditText objID;

    EditText objRazonSocial;

    ImageView objFotoHotel;

    EditText objTelefono;

    EditText objPais;
    Spinner objEstado;

    EditText objDomicilio;
    EditText objCodigoPostal;

    EditText objCoordenadasGPS;
    EditText objCalificacion;

    EditText objDescripcionAdicional;
    EditText objTotalHabitaciones;

    RadioButton objActivo;
    RadioButton objInactivo;

    Button objGuardarCambios;
    Button objLimpiar;

    //Firebase
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    DatabaseReference databaseReferenceHotel;
    StorageReference storageProfilePicsRef;
    StorageReference storageProfilePicsRefHotel;

    //Imagenes
    String ruta="";
    Uri imageUri;
    String myUri = "";

    //Cadenas
    String sIDHotel=""
            , sRazonSocial=""
            , sFotoHotel=""
            , sTelefono=""
            , sPais=""
            , sEstado=""
            , sDomicilio=""
            , sCodigoPostal=""
            , sCoordenadasGPS=""
            , sCalificacion=""
            , sDescripcionAdicional=""
            , sTotalHabitaciones=""
            , sFechaCreacion=""
            , sStatus="";//14

    //Auxiliares
    CountDownTimer mCountDownTimer;
    int aux_info=0;
    String aux_referencia="";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                new ViewModelProvider(this).get(SlideshowViewModel.class);

        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("RTDB_Users");
        databaseReferenceHotel = FirebaseDatabase.getInstance().getReference().child("RTDB_Hotels");
        storageProfilePicsRef = FirebaseStorage.getInstance().getReference().child("STORAGE_Users_Profile_Pics");
        storageProfilePicsRefHotel = FirebaseStorage.getInstance().getReference().child("STORAGE_Hotels_Profile_Pics");

        asignar(root);

        tomarFoto();

        guardar();



        return root;
    }

    public void tomarFoto(){
        objFotoHotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                camara();
            }
        });
    }

    public void camara(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity( getContext().getPackageManager() ) != null){

            File fotoArchivo = null;
            try {
                fotoArchivo = guardarImagen();
            } catch (IOException ex) {
                Log.e("Error", ex.toString());
            }
            if (fotoArchivo != null){
                Uri uri = FileProvider.getUriForFile(getContext(), getContext().getApplicationContext().getPackageName()+".fileprovider", fotoArchivo);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                imageUri = uri;
                startActivityForResult(intent, 1);
            }


        }
    }

    private File guardarImagen() throws IOException{
        String nombreFoto = "foto_";
        File directorio = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File foto = File.createTempFile(nombreFoto, ".jpg", directorio);
        ruta = foto.getAbsolutePath();

        return foto;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == getActivity().RESULT_OK){
            Bitmap imgBitmap = BitmapFactory.decodeFile(ruta);
            objFotoHotel.setImageBitmap(imgBitmap);
            Toast.makeText(getContext(), ruta, Toast.LENGTH_SHORT).show();

//            Bundle bundle = new Bundle();
//            bundle.putString("bun_id", "1" );
//
//            MiHotelFragment fragobj = new MiHotelFragment();
//            fragobj.setArguments(bundle);

        }

    }


    private void guardar() {

        objGuardarCambios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                obtenerStrings();

                if (estaVacio()){

                    if (imageUri != null) { //Guardar Con Foto

                        final StorageReference fileRef = storageProfilePicsRefHotel.child( mAuth.getCurrentUser().getUid() ).child( sIDHotel+".jpg" );

                        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "Falló al Subir Imagen", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                                if (task.isSuccessful()){

                                    UploadTask.TaskSnapshot downloadUri = null;
                                    downloadUri = task.getResult();
                                    Task<Uri> result = downloadUri.getStorage().getDownloadUrl();
                                    result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {

                                            String aux = uri.toString();

                                            //String aux_ID = mAuth.getCurrentUser().getUid();

                                            myUri = aux;

                                            HashMap<String, Object> userMapHotel = new HashMap<>();
                                            //userMapHotel.put("c_hot_aa_id_hotel", aux_ID_Hotel);

                                            userMapHotel.put("c_hot_ab_razon_social", sRazonSocial);
                                            userMapHotel.put("c_hot_ac_foto_hotel", myUri);
                                            userMapHotel.put("c_hot_ad_telefono", sTelefono);
                                            userMapHotel.put("c_hot_ae_pais", sPais);
                                            userMapHotel.put("c_hot_af_estado", sEstado);
                                            userMapHotel.put("c_hot_ag_domicilio", sDomicilio);
                                            userMapHotel.put("c_hot_ah_codigo_postal", sCodigoPostal);
                                            userMapHotel.put("c_hot_ai_coordenadas", sCoordenadasGPS);
                                            userMapHotel.put("c_hot_aj_calificacion", sCalificacion);
                                            userMapHotel.put("c_hot_ak_descripcion", sDescripcionAdicional);
                                            userMapHotel.put("c_hot_al_total_habitaciones", sTotalHabitaciones);

                                            userMapHotel.put("c_hot_am_fecha_creacion", sFechaCreacion);
                                            userMapHotel.put("c_hot_an_status", sStatus);

                                            databaseReferenceHotel.child( "Hoteles1" ).child( mAuth.getCurrentUser().getUid() ).child( aux_referencia ).updateChildren( userMapHotel ).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {

                                                }
                                            }).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    if (task.isSuccessful()){

                                                        Toast.makeText(getContext(), "Guardado DI", Toast.LENGTH_SHORT).show();
                                                        obtenerInformacion();

                                                    }

                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(getContext(), "Falló al Actualizar los Datos", Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                        }
                                    });

                                }

                            }
                        });

                    } else { //Guardar SIN Foto

                        //String aux = uri.toString();

                        //String aux_ID = mAuth.getCurrentUser().getUid();

                        //myUri = aux;

                        HashMap<String, Object> userMapHotel = new HashMap<>();
                        //userMapHotel.put("c_hot_aa_id_hotel", aux_ID_Hotel);

                        userMapHotel.put("c_hot_ab_razon_social", sRazonSocial);
                        //userMapHotel.put("c_hot_ac_foto_hotel", myUri);
                        userMapHotel.put("c_hot_ad_telefono", sTelefono);
                        userMapHotel.put("c_hot_ae_pais", sPais);
                        userMapHotel.put("c_hot_af_estado", sEstado);
                        userMapHotel.put("c_hot_ag_domicilio", sDomicilio);
                        userMapHotel.put("c_hot_ah_codigo_postal", sCodigoPostal);
                        userMapHotel.put("c_hot_ai_coordenadas", sCoordenadasGPS);
                        userMapHotel.put("c_hot_aj_calificacion", sCalificacion);
                        userMapHotel.put("c_hot_ak_descripcion", sDescripcionAdicional);
                        userMapHotel.put("c_hot_al_total_habitaciones", sTotalHabitaciones);

                        userMapHotel.put("c_hot_am_fecha_creacion", sFechaCreacion);
                        userMapHotel.put("c_hot_an_status", sStatus);

                        databaseReferenceHotel.child( "Hoteles1" ).child( mAuth.getCurrentUser().getUid() ).child( aux_referencia ).updateChildren( userMapHotel ).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                            }
                        }).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()){

                                    Toast.makeText(getContext(), "Guardado D", Toast.LENGTH_SHORT).show();
                                    obtenerInformacion();

                                }

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "Falló al Actualizar los Datos", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }

                } else {
                    Toast.makeText(getContext(), "Debe llenar los campos", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void obtenerStrings() {

        sIDHotel= objID.getText().toString();

        sRazonSocial= objRazonSocial.getText().toString();
        sFotoHotel= ruta;
        sTelefono= objTelefono.getText().toString();

        sPais= objPais.getText().toString();
        sEstado= objEstado.getSelectedItem().toString();
        sDomicilio= objDomicilio.getText().toString();
        sCodigoPostal= objCodigoPostal.getText().toString();
        sCoordenadasGPS= objCoordenadasGPS.getText().toString();

        sCalificacion= objCalificacion.getText().toString();
        sDescripcionAdicional= objDescripcionAdicional.getText().toString();
        sTotalHabitaciones= objTotalHabitaciones.getText().toString();

        //sFechaCreacion

        String aux_Status = "";
        if ( objActivo.isChecked() ){
            aux_Status = "1";
        } else if ( objInactivo.isChecked() ){
            aux_Status = "0";
        }

        sStatus= aux_Status;

    }

    private boolean estaVacio(){

        int aux_EstadoPosicion = objEstado.getSelectedItemPosition();

        if (
                sIDHotel.equals("")
                        || sRazonSocial.equals("")
                        //|| sFotoHotel.equals("")
                        || sTelefono.equals("")
                        || sPais.equals("")
                        || sEstado.equals("")
                        || aux_EstadoPosicion==0
                        || sDomicilio.equals("")
                        || sCodigoPostal.equals("")
                        || sCoordenadasGPS.equals("")
                        || sCalificacion.equals("")
                        || sDescripcionAdicional.equals("")
                        || sTotalHabitaciones.equals("")
                        || sFechaCreacion.equals("")
                        || sStatus.equals("")
        ) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null){
            startActivity(new Intent(getContext(), LoginActivity.class));
        } else {
            if (aux_info==0){
                obtenerInformacion();
                aux_info=1;
            }
        }

    }

    private void obtenerInformacion() {

        ProgressDialog TempDialog;

        int i=0;

        TempDialog = new ProgressDialog(getContext());
        TempDialog.setMessage("Please wait...");
        TempDialog.setCancelable(false);
        TempDialog.setProgress(i);
        TempDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        TempDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.GRAY));

        TempDialog.show();

        databaseReferenceHotel.child( "Hoteles1" ).child( mAuth.getCurrentUser().getUid() )./*child( "b31f4cee-97b1-4db7-a384-48742885d038" ).*/addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //Toast.makeText(getContext(), String.valueOf(snapshot.getChildrenCount()) + snapshot.getChildren() + snapshot.getKey(), Toast.LENGTH_SHORT).show();

                ArrayList<String> referencias = new ArrayList<>();
                referencias.clear();
                for(DataSnapshot ds : snapshot.getChildren()){

                    //ds.getValue();
                    //ds.getRef();
                    Toast.makeText(getContext(), ds.getRef().getKey(), Toast.LENGTH_SHORT).show();

                    referencias.add( ds.getRef().getKey() );
                }

                aux_referencia = referencias.get(0);

                databaseReferenceHotel.child( "Hoteles1" ).child( mAuth.getCurrentUser().getUid() ).child( referencias.get(0) ).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Toast.makeText(getContext(), "Aqui", Toast.LENGTH_SHORT).show();

                        if (snapshot.exists()) {
                            //String sNombre = snapshot.child("c_usu_ad_nombre").getValue().toString();

                            sIDHotel= snapshot.child( "c_hot_aa_id_hotel" ).getValue().toString();
                            sRazonSocial= snapshot.child( "c_hot_ab_razon_social" ).getValue().toString();
                            sFotoHotel= snapshot.child( "c_hot_ac_foto_hotel" ).getValue().toString();
                            ruta = sFotoHotel;
                            sTelefono= snapshot.child( "c_hot_ad_telefono" ).getValue().toString();
                            sPais= snapshot.child( "c_hot_ae_pais" ).getValue().toString();
                            sEstado= snapshot.child( "c_hot_af_estado" ).getValue().toString();
                            sDomicilio= snapshot.child( "c_hot_ag_domicilio" ).getValue().toString();
                            sCodigoPostal= snapshot.child( "c_hot_ah_codigo_postal" ).getValue().toString();
                            sCoordenadasGPS= snapshot.child( "c_hot_ai_coordenadas" ).getValue().toString();
                            sCalificacion= snapshot.child( "c_hot_aj_calificacion" ).getValue().toString();
                            sDescripcionAdicional= snapshot.child( "c_hot_ak_descripcion" ).getValue().toString();
                            sTotalHabitaciones= snapshot.child( "c_hot_al_total_habitaciones" ).getValue().toString();
                            sFechaCreacion= snapshot.child( "c_hot_am_fecha_creacion" ).getValue().toString();
                            sStatus= snapshot.child( "c_hot_an_status" ).getValue().toString();

                            //Llenar EditTexts
                            objID.setText(sIDHotel);
                            objID.setClickable(false);

                            objRazonSocial.setText(sRazonSocial);

                            if ( !(sFotoHotel.equals("") || sFotoHotel.isEmpty()) ){
                                Picasso.get().load( sFotoHotel ).into( objFotoHotel );
                            }

                            objTelefono.setText(sTelefono);
                            objPais.setText(sPais);

                            objEstado.setSelection( obtenerPosition(objEstado, sEstado) );

                            objDomicilio.setText(sDomicilio);
                            objCodigoPostal.setText(sCodigoPostal);
                            objCoordenadasGPS.setText(sCoordenadasGPS);
                            objCalificacion.setText(sCalificacion);
                            objDescripcionAdicional.setText(sDescripcionAdicional);
                            objTotalHabitaciones.setText(sTotalHabitaciones);

                            if (sStatus.equals("1")){
                                objActivo.setChecked(true);
                                objInactivo.setChecked(false);
                            } else if ( sStatus.equals("0") ){
                                objInactivo.setChecked(true);
                                objActivo.setChecked(false);
                            }



                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                mCountDownTimer = new CountDownTimer(2000, 1000) {
                    public void onTick(long millisUntilFinished) {
                        TempDialog.setMessage("Please wait..");
                    }

                    public void onFinish() {
                        TempDialog.dismiss();//Your action like intents are placed here

                    }
                }.start();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public static int obtenerPosition(Spinner sp, String item){
        int position = 0;
        for (int i = 0; i < sp.getCount(); i++){
            if (sp.getItemAtPosition(i).toString().equalsIgnoreCase(item)){
                position = i;
            }
        }
        return  position;
    }

    private void asignar(View root) {

        objID = root.findViewById(R.id.idMiHotelEditTextID);

        objRazonSocial = root.findViewById(R.id.idMiHotelEditTextRazonSocial);

        objFotoHotel = root.findViewById(R.id.idMiHotelImageViewFotoHotel);

        objTelefono = root.findViewById(R.id.idMiHotelEditTextTelefono);

        objPais = root.findViewById(R.id.idMiHotelEditTextPais);
        objEstado = root.findViewById(R.id.idMiHotelSpinnerEstado);
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(getContext(), R.array.estado, android.R.layout.simple_spinner_item);
        objEstado.setAdapter(adapter);

        objDomicilio = root.findViewById(R.id.idMiHotelEditTextDomicilio);
        objCodigoPostal = root.findViewById(R.id.idMiHotelEditTextCodigoPostal);

        objCoordenadasGPS = root.findViewById(R.id.idMiHotelEditTextCoordenadasGPS);
        objCalificacion = root.findViewById(R.id.idMiHotelEditTextCalificacion);

        objDescripcionAdicional = root.findViewById(R.id.idMiHotelEditTextDescripcionAdicional);
        objTotalHabitaciones = root.findViewById(R.id.idMiHotelEditTextTotalHabitaciones);

        objActivo = root.findViewById(R.id.idMiHotelRadioButtonActivo);
        objInactivo = root.findViewById(R.id.idMiHotelRadioButtonInactivo);

        objGuardarCambios = root.findViewById(R.id.idMiHotelButtonGuardarCambios);
        objLimpiar = root.findViewById(R.id.idMiHotelButtonLimpiar);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}