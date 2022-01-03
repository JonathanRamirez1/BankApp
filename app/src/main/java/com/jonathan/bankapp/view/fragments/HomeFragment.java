package com.jonathan.bankapp.view.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.jonathan.bankapp.databinding.FragmentHomeBinding;
import com.jonathan.bankapp.utils.ItemsHome;
import com.jonathan.bankapp.view.adapters.HomeAdapter;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

   private FragmentHomeBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setAdapter();
    }

    private void setAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        binding.recyclerViewHome.setLayoutManager(layoutManager);

        ArrayList itemClasses = new ArrayList();
        itemClasses.add(new ItemsHome(0, "Item Withdrawals"));
        itemClasses.add(new ItemsHome(1, "Item Transfer"));
        itemClasses.add(new ItemsHome(2, "Item PayWithQR"));
        itemClasses.add(new ItemsHome(3, "Item History"));

        HomeAdapter adapter = new HomeAdapter((List)itemClasses);
        binding.recyclerViewHome.setAdapter(adapter);
    }
}