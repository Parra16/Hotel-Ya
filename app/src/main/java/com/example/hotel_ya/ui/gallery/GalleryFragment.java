package com.example.hotel_ya.ui.gallery;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.hotel_ya.R;
import com.example.hotel_ya.databinding.FragmentGalleryBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;
    private FragmentGalleryBinding binding;

    ListView objLista;

    RadioButton objUsuarios;
    RadioButton objHoteles;
    RadioButton objHabitaciones;
    RadioButton objReservaciones;

    //Firebase
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    DatabaseReference databaseReferenceHotel;
    StorageReference storageProfilePicsRef;
    StorageReference storageProfilePicsRefHotel;

    //Listas
    ArrayAdapter<String> adapter;
    List<String> listaUsuarios1 = new ArrayList<>();
    List<String> listaUsuarios2 = new ArrayList<>();
    List<String> listaFotos = new ArrayList<>();
    List<String> listaID = new ArrayList<>();

    ArrayAdapter<String> adapterHotel;
    List<String> listaHotel1 = new ArrayList<>();
    List<String> listaHotel2 = new ArrayList<>();
    List<String> listaFotosHotel = new ArrayList<>();
    List<String> listaIDHotel = new ArrayList<>();

    //Cadenas
    String sUSU_ID=""
            , sUSU_Correo=""
            , sUSU_Contrasenia=""
            , sUSU_Nombre=""
            , sUSU_Apellidos=""
            , sUSU_Ruta=""
            , sUSU_Telefono=""
            , sUSU_Pais=""
            , sUSU_Estado=""
            , sUSU_CodigoPostal=""
            , sUSU_Domicilio=""
            , sUSU_Sexo=""
            , sUSU_FechaNacimiento=""
            , sUSU_Reputacion=""
            , sUSU_FechaCreacion=""
            , sUSU_Status=""
            , sUSU_PermisoHotel=""
            , sUSU_TipoUsuario="";

    //Cadenas Hotel
    String sHOT_ID=""
            , sHOT_RazonSocial=""
            , sHOT_FotoHotel=""
            , sHOT_Telefono=""
            , sHOT_Pais=""
            , sHOT_Estado=""
            , sHOT_Domicilio=""
            , sHOT_CodigoPostal=""
            , sHOT_CoordenadasGPS=""
            , sHOT_Calificacion=""
            , sHOT_DescripcionAdicional=""
            , sHOT_TotalHabitaciones=""
            , sHOT_FechaCreacion=""
            , sHOT_Status="";//14

    long aux_conteo_snapshots=0;
    long aux_conteo_snapshotsHotel=0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("RTDB_Users");
        databaseReferenceHotel = FirebaseDatabase.getInstance().getReference().child("RTDB_Hotels");
        storageProfilePicsRef = FirebaseStorage.getInstance().getReference().child("STORAGE_Users_Profile_Pics");
        storageProfilePicsRefHotel = FirebaseStorage.getInstance().getReference().child("STORAGE_Hotels_Profile_Pics");

        asignar(root);

        listar();





        return root;
    }

    private void listar() {

        if (getActivity()==null){
            return ;
        }

        objUsuarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "USUARIOS", Toast.LENGTH_SHORT).show();

                databaseReference.child( "Usuarios1" ).orderByChild( "c_usu_ao_fecha_creacion" ).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        DataSnapshot objSnapshot2;
                        objSnapshot2 = dataSnapshot;
                        aux_conteo_snapshots = objSnapshot2.getChildrenCount();

                        listaUsuarios1.clear();
                        listaUsuarios2.clear();
                        listaFotos.clear();
                        listaID.clear();

                        for (DataSnapshot objSnapshot : dataSnapshot.getChildren()){

                            sUSU_ID = objSnapshot.child("c_usu_aa_id_usuario").getValue().toString();

                            sUSU_Correo = objSnapshot.child("c_usu_ab_correo").getValue().toString();
                            sUSU_Contrasenia = objSnapshot.child("c_usu_ac_contrasenia").getValue().toString();

                            sUSU_Nombre = objSnapshot.child("c_usu_ad_nombre").getValue().toString();
                            sUSU_Apellidos = objSnapshot.child("c_usu_ae_apellidos").getValue().toString();
                            sUSU_Ruta = objSnapshot.child("c_usu_af_ruta_img").getValue().toString();
                            sUSU_Telefono = objSnapshot.child("c_usu_ag_telefono").getValue().toString();
                            sUSU_Pais = objSnapshot.child("c_usu_ah_pais").getValue().toString();
                            sUSU_Estado = objSnapshot.child("c_usu_ai_estado").getValue().toString();
                            sUSU_CodigoPostal = objSnapshot.child("c_usu_aj_codigo_postal").getValue().toString();
                            sUSU_Domicilio = objSnapshot.child("c_usu_ak_domicilio").getValue().toString();
                            sUSU_Sexo = objSnapshot.child("c_usu_al_sexo").getValue().toString();
                            sUSU_FechaNacimiento = objSnapshot.child("c_usu_am_fecha_nacimiento").getValue().toString();
                            sUSU_Reputacion = objSnapshot.child("c_usu_an_reputacion").getValue().toString();

                            sUSU_FechaCreacion = objSnapshot.child("c_usu_ao_fecha_creacion").getValue().toString();
                            sUSU_Status = objSnapshot.child("c_usu_ap_status").getValue().toString();
                            sUSU_PermisoHotel = objSnapshot.child("c_usu_aq_permiso_hotel").getValue().toString();
                            sUSU_TipoUsuario = objSnapshot.child("c_usu_ar_tipo_usuario").getValue().toString();

                            listaUsuarios1.add(
                                    //"\nID: "+sUSU_ID
                                    //+"\nCorreo: "+sUSU_Correo
                                    //+"\nContraseña: "+sUSU_Contrasenia
                                    "\nNombre(s): "+sUSU_Nombre
                                            +"\nApellidos: "+sUSU_Apellidos
                                            //+"\nRuta Imágen: "+sUSU_Ruta
                                            +"\nTeléfono: "+sUSU_Telefono
                                            +"\nPaís: "+sUSU_Pais
                                            +"\nEstado: "+sUSU_Estado
                                            +"\nCódigo Postal: "+sUSU_CodigoPostal
                                            +"\nDomicilio: "+sUSU_Domicilio
                                            +"\nSexo: "+sUSU_Sexo
                                            +"\nFecha de Nacimiento: "+sUSU_FechaNacimiento
                                            //+"\nReputación: "+sUSU_Reputacion
                                            //+"\nFecha de Creación: "+sUSU_FechaCreacion
                                            //+"\nStatus: "+sUSU_Status
                                            //+"\nPermiso Hotel: "+sUSU_PermisoHotel
                                            //+"\nTipo de Usuario: "+sUSU_TipoUsuario
                                            +"\n" );
                            listaUsuarios2.add(
                                    "\nID: "+sUSU_ID
                                            +"\nCorreo: "+sUSU_Correo
                                            +"\nContraseña: "+sUSU_Contrasenia
                                            +"\nNombre(s): "+sUSU_Nombre
                                            +"\nApellidos: "+sUSU_Apellidos
                                            +"\nRuta Imágen: "+sUSU_Ruta
                                            +"\nTeléfono: "+sUSU_Telefono
                                            +"\nPaís: "+sUSU_Pais
                                            +"\nEstado: "+sUSU_Estado
                                            +"\nCódigo Postal: "+sUSU_CodigoPostal
                                            +"\nDomicilio: "+sUSU_Domicilio
                                            +"\nSexo: "+sUSU_Sexo
                                            +"\nFecha de Nacimiento: "+sUSU_FechaNacimiento
                                            +"\nReputación: "+sUSU_Reputacion
                                            +"\nFecha de Creación: "+sUSU_FechaCreacion
                                            +"\nStatus: "+sUSU_Status
                                            +"\nPermiso Hotel: "+sUSU_PermisoHotel
                                            +"\nTipo de Usuario: "+sUSU_TipoUsuario
                                            +"\n" );
                            listaFotos.add( sUSU_Ruta );
                            listaID.add( sUSU_ID );
                        }
                        //null

                        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, listaUsuarios1);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        objLista.setAdapter(adapter);

                        objLista.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_expandable_list_item_2){
                            @Override
                            public int getCount() {
                                return (int)aux_conteo_snapshots;
                            }

                            @NonNull
                            @Override
                            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

                                if(convertView == null) {
                                    convertView = ((LayoutInflater)getActivity().getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.auxiliar_lista, parent, false);
                                }

                                ((TextView)convertView.findViewById(R.id.idAuxiliarListaTextViewInformacion)).setText( listaUsuarios1.get(position) );

                                ImageView ivImageView = convertView.findViewById(R.id.idAuxiliarListaImageViewImagen);

                                if (!listaFotos.get(position).isEmpty()){
                                    Picasso.get().load(listaFotos.get(position)).into(ivImageView);
                                }

                                Button objInformacion = convertView.findViewById(R.id.idAuxiliarListaButtonInformacion);
                                objInformacion.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        Toast.makeText(getContext(), "Aqui", Toast.LENGTH_SHORT).show();

                                        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.auxiliar_lista_informacion, null);
                                        ((TextView)dialogView.findViewById(R.id.idAuxiliarListaInformacionTextViewInformacion)).setText(listaUsuarios2.get(position));

                                        ImageView ivImageView = dialogView.findViewById(R.id.idAuxiliarListaInformacionImageViewImagen);
                                        if (!listaFotos.get(position).isEmpty()){
                                            Picasso.get().load(listaFotos.get(position)).into(ivImageView);
                                        } else {
                                            ivImageView.setImageResource(R.drawable.ic_menu_camera);
                                        }

                                        AlertDialog.Builder dialogo = new AlertDialog.Builder(getContext());
                                        dialogo.setTitle("Información");
                                        dialogo.setView(dialogView);
                                        dialogo.setPositiveButton("Aceptar", null);
                                        dialogo.show();

                                    }
                                });

                                return convertView;

                            }
                        });

                        objLista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {



                            }
                        });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

        objHoteles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "HOTELES", Toast.LENGTH_SHORT).show();

                listaHotel1.clear();
                listaHotel2.clear();
                listaFotosHotel.clear();
                listaIDHotel.clear();

                databaseReferenceHotel.child( "Hoteles1" ).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        //Toast.makeText(getContext(), String.valueOf(snapshot.getChildrenCount()) + snapshot.getChildren() + snapshot.getKey(), Toast.LENGTH_SHORT).show();

                        ArrayList<String> referencias = new ArrayList<>();
                        referencias.clear();
                        for(DataSnapshot ds : snapshot.getChildren()){

                            //ds.getValue();
                            //ds.getRef();
                            //Toast.makeText(getContext(), ds.getRef().getKey(), Toast.LENGTH_SHORT).show();

                            referencias.add( ds.getRef().getKey() );
                        }
                        Toast.makeText(getContext(), String.valueOf(referencias.size() ) , Toast.LENGTH_SHORT).show();

                        Log.d("--------VALOR", String.valueOf(referencias.size()) );


                        for (int i=0; i < referencias.size(); i++){

                            int aux_i = i;

                            Log.d("--------VALOR", referencias.get(i) );

                            databaseReferenceHotel.child( "Hoteles1" ).child( referencias.get( i ).toString() ).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    ArrayList<String> referenciasIDHoteles = new ArrayList<>();
                                    referenciasIDHoteles.clear();
                                    for(DataSnapshot ds : snapshot.getChildren()){

                                        //ds.getValue();
                                        //ds.getRef();
                                        //Toast.makeText(getContext(), ds.getRef().getKey(), Toast.LENGTH_SHORT).show();

                                        referenciasIDHoteles.add( ds.getRef().getKey() );
                                    }
                                    Toast.makeText(getContext(), String.valueOf(referenciasIDHoteles.size()) , Toast.LENGTH_SHORT).show();

                                    Log.d("--------VALOR_2", String.valueOf( referenciasIDHoteles.size()) );

                                    for (int j=0; j < referenciasIDHoteles.size(); j++){

                                        Log.d("--------VALOR_2", referenciasIDHoteles.get(j) );

                                        databaseReferenceHotel.child( "Hoteles1" ).child( referencias.get( aux_i ).toString() ).child( referenciasIDHoteles.get(j) ).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                //String sHOT_ID = dataSnapshot.child("c_hot_aa_id_hotel").getValue().toString();

                                                //String sHOT_RazonSocial="";
                                                //if (dataSnapshot.child("c_hot_ab_razon_social").exists()){
                                                //sHOT_RazonSocial = dataSnapshot.child("c_hot_ab_razon_social").getValue().toString();
                                                //}

                                                databaseReferenceHotel.child( "Hoteles1" ).child( referencias.get( aux_i ).toString() ).orderByChild( "c_hot_aa_id_hotel" ).addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                        DataSnapshot objSnapshot2;
                                                        objSnapshot2 = dataSnapshot;
                                                        aux_conteo_snapshotsHotel = objSnapshot2.getChildrenCount();

                                                        for (DataSnapshot objSnapshot : dataSnapshot.getChildren()){

                                                            sHOT_ID= objSnapshot.child( "c_hot_aa_id_hotel" ).getValue().toString();
                                                            sHOT_RazonSocial= objSnapshot.child( "c_hot_ab_razon_social" ).getValue().toString();
                                                            sHOT_FotoHotel= objSnapshot.child( "c_hot_ac_foto_hotel" ).getValue().toString();
                                                            sHOT_Telefono= objSnapshot.child( "c_hot_ad_telefono" ).getValue().toString();
                                                            sHOT_Pais= objSnapshot.child( "c_hot_ae_pais" ).getValue().toString();
                                                            sHOT_Estado= objSnapshot.child( "c_hot_af_estado" ).getValue().toString();
                                                            sHOT_Domicilio= objSnapshot.child( "c_hot_ag_domicilio" ).getValue().toString();
                                                            sHOT_CodigoPostal= objSnapshot.child( "c_hot_ah_codigo_postal" ).getValue().toString();
                                                            sHOT_CoordenadasGPS= objSnapshot.child( "c_hot_ai_coordenadas" ).getValue().toString();
                                                            sHOT_Calificacion= objSnapshot.child( "c_hot_aj_calificacion" ).getValue().toString();
                                                            sHOT_DescripcionAdicional= objSnapshot.child( "c_hot_ak_descripcion" ).getValue().toString();
                                                            sHOT_TotalHabitaciones= objSnapshot.child( "c_hot_al_total_habitaciones" ).getValue().toString();
                                                            sHOT_FechaCreacion= objSnapshot.child( "c_hot_am_fecha_creacion" ).getValue().toString();
                                                            sHOT_Status= objSnapshot.child( "c_hot_an_status" ).getValue().toString();

                                                            Log.d("--------VALOR_3_ID", sHOT_ID );
                                                            Log.d("--------VALOR_3_Nombre", sHOT_RazonSocial );

                                                            listaHotel1.add(
                                                                    //"\nID: " + sHOT_ID
                                                                    "\nRazón Social: " + sHOT_RazonSocial
                                                                            //+"\nFoto Hotel Ruta: " + sHOT_FotoHotel
                                                                            +"\nTeléfono: " + sHOT_Telefono
                                                                            +"\nPaís: " + sHOT_Pais
                                                                            +"\nEstado: " + sHOT_Estado
                                                                            +"\nDomicilio: " + sHOT_Domicilio
                                                                            +"\nCódigo Postal: " + sHOT_CodigoPostal
                                                                            //+"\nCoordenadas GPS: " + sHOT_Telefono
                                                                            +"\nCalificación: " + sHOT_Calificacion
                                                                            +"\nDescripción Adicional: " + sHOT_DescripcionAdicional
                                                                    //+"\nTotal de Habitaciones: " + sHOT_Telefono
                                                                    //+"\nFecha de Creación: " + sHOT_Telefono
                                                                    //+"\nStatus: " + sHOT_Telefono
                                                            );

                                                            listaHotel2.add(
                                                                    "\nID: " + sHOT_ID
                                                                            +"\nRazón Social: " + sHOT_RazonSocial
                                                                            +"\nFoto Hotel Ruta: " + sHOT_FotoHotel
                                                                            +"\nTeléfono: " + sHOT_Telefono
                                                                            +"\nPaís: " + sHOT_Pais
                                                                            +"\nEstado: " + sHOT_Estado
                                                                            +"\nDomicilio: " + sHOT_Domicilio
                                                                            +"\nCódigo Postal: " + sHOT_CodigoPostal
                                                                            +"\nCoordenadas GPS: " + sHOT_CoordenadasGPS
                                                                            +"\nCalificación: " + sHOT_Calificacion
                                                                            +"\nDescripción Adicional: " + sHOT_DescripcionAdicional
                                                                            +"\nTotal de Habitaciones: " + sHOT_TotalHabitaciones
                                                                            +"\nFecha de Creación: " + sHOT_FechaCreacion
                                                                            +"\nStatus: " + sHOT_Status
                                                            );






                                                            listaFotosHotel.add( sHOT_FotoHotel );
                                                            listaIDHotel.add( sHOT_ID );

                                                        }

                                                        if (getActivity()==null){
                                                            return ;
                                                        }

                                                        adapterHotel = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, listaHotel1);
                                                        adapterHotel.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                                                        objLista.setAdapter(adapterHotel);

                                                        objLista.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_expandable_list_item_2){
                                                            @Override
                                                            public int getCount() {
                                                                return (int)listaHotel1.size();
                                                            }

                                                            @NonNull
                                                            @Override
                                                            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

                                                                if(convertView == null) {
                                                                    convertView = ((LayoutInflater)getActivity().getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.auxiliar_lista, parent, false);
                                                                }

                                                                ((TextView)convertView.findViewById(R.id.idAuxiliarListaTextViewInformacion)).setText( listaHotel1.get(position) );

                                                                ImageView ivImageView;
                                                                ivImageView = new ImageView(getContext());
                                                                ivImageView = convertView.findViewById(R.id.idAuxiliarListaImageViewImagen);

                                                                if (!listaFotosHotel.get(position).isEmpty()){
                                                                    Picasso.get().load(listaFotosHotel.get(position)).into(ivImageView);
                                                                } else {
                                                                    ivImageView.setImageResource(R.drawable.ic_menu_camera);
                                                                }

                                                                Button objInformacion = convertView.findViewById(R.id.idAuxiliarListaButtonInformacion);
                                                                objInformacion.setOnClickListener(new View.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(View view) {

                                                                        Toast.makeText(getContext(), "Aqui", Toast.LENGTH_SHORT).show();

                                                                        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.auxiliar_lista_informacion, null);
                                                                        ((TextView)dialogView.findViewById(R.id.idAuxiliarListaInformacionTextViewInformacion)).setText(listaHotel2.get(position));

                                                                        ImageView ivImageView = dialogView.findViewById(R.id.idAuxiliarListaInformacionImageViewImagen);
                                                                        if (!listaFotosHotel.get(position).isEmpty()){
                                                                            Picasso.get().load(listaFotosHotel.get(position)).into(ivImageView);
                                                                        }

                                                                        AlertDialog.Builder dialogo = new AlertDialog.Builder(getContext());
                                                                        dialogo.setTitle("Información");
                                                                        dialogo.setView(dialogView);
                                                                        dialogo.setPositiveButton("Aceptar", null);
                                                                        dialogo.show();

                                                                    }
                                                                });

                                                                return convertView;

                                                            }
                                                        });


                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });







                                            }


                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });


                                    }


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

        objHabitaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "HABITACIONES", Toast.LENGTH_SHORT).show();
            }
        });

        objReservaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "RESERVACIONES", Toast.LENGTH_SHORT).show();
            }
        });



    }

    private void asignar(View root) {

        objLista = root.findViewById(R.id.idListarListViewListar);

        objUsuarios = root.findViewById(R.id.idListarRadioButtonUsuarios);
        objHoteles = root.findViewById(R.id.idListarRadioButtonHoteles);
        objHabitaciones = root.findViewById(R.id.idListarRadioButtonHabitaciones);
        objReservaciones = root.findViewById(R.id.idListarRadioButtonReservaciones);


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}