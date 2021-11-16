package com.example.hotel_ya.ui.perfilHotel;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hotel_ya.R;

public class PerfilHotelFragment extends Fragment {

    private PerfilHotelViewModel mViewModel;

    public static PerfilHotelFragment newInstance() {
        return new PerfilHotelFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_perfil_hotel, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(PerfilHotelViewModel.class);
        // TODO: Use the ViewModel
    }

}