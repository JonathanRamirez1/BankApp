package com.jonathan.banco.view.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jonathan.banco.R;
import com.jonathan.banco.databinding.FragmentTransferBinding;

public class TransferFragment extends Fragment {

    private FragmentTransferBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding= FragmentTransferBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }
}