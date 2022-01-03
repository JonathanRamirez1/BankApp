package com.jonathan.bankapp.view.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.jonathan.bankapp.R;
import com.jonathan.bankapp.databinding.ItemHistoryBinding;
import com.jonathan.bankapp.databinding.ItemPayQrBinding;
import com.jonathan.bankapp.databinding.ItemTransferBinding;
import com.jonathan.bankapp.databinding.ItemWithdrawalsBinding;
import com.jonathan.bankapp.utils.ItemsHome;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List list;

    public HomeAdapter(List itemClass) {
        this.list = itemClass;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemWithdrawalsBinding itemWithdrawalsBinding = ItemWithdrawalsBinding.inflate(layoutInflater, parent, false);
        ItemTransferBinding itemTransferBinding = ItemTransferBinding.inflate(layoutInflater, parent, false);
        ItemPayQrBinding itemPayQrBinding = ItemPayQrBinding.inflate(layoutInflater, parent, false);
        ItemHistoryBinding itemHistoryBinding = ItemHistoryBinding.inflate(layoutInflater, parent, false);
        RecyclerView.ViewHolder var9 = null;
         switch(viewType) {
            case 0:
                var9 = (RecyclerView.ViewHolder) (new HomeAdapter.ViewHolderWithdrawals(itemWithdrawalsBinding));
                break;
            case 1:
                var9 = (RecyclerView.ViewHolder) (new HomeAdapter.ViewHolderTransfer(itemTransferBinding));
                break;
             case 2:
                 var9 = (RecyclerView.ViewHolder) (new HomeAdapter.ViewHolderPayWithQR(itemPayQrBinding));
                 break;
             case 3:
                 var9 = (RecyclerView.ViewHolder) (new HomeAdapter.ViewHolderHistory(itemHistoryBinding));
                 break;
        }
        assert var9 != null;
        return var9;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case 0:
                ((HomeAdapter.ViewHolderWithdrawals)holder).launchWithdrawalsFragment((ItemsHome)this.list.get(position));
                break;
            case 1:
                ((HomeAdapter.ViewHolderTransfer)holder).launchTransferFragment((ItemsHome)this.list.get(position));
                break;
            case 2:
                ((HomeAdapter.ViewHolderPayWithQR)holder).launchPayWithQRFragment((ItemsHome)this.list.get(position));
                break;
            case 3:
                ((HomeAdapter.ViewHolderHistory)holder).launchHistoryFragment((ItemsHome)this.list.get(position));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

    @Override
    public int getItemViewType(int position) {
        byte itemType;
         switch(((ItemsHome)this.list.get(position)).getViewType()) {
            case 0:
                itemType = 0;
                break;
             case 1:
                 itemType = 1;
                 break;
             case 2:
                 itemType = 2;
                 break;
             case 3:
                 itemType = 3;
                 break;
             default:
                 itemType = -1;
        }
        return itemType;
    }

    public static final class ViewHolderWithdrawals extends  RecyclerView.ViewHolder {
        @NotNull
        private final ItemWithdrawalsBinding binding;
        private NavController navController;

        public final void launchWithdrawalsFragment(ItemsHome itemsHome) {

            binding.getRoot().setOnClickListener(view -> {
                navController = Navigation.findNavController(view);
                navController.navigate(R.id.withdrawalsFragment);
            });
        }

        @NonNull
        public final  ItemWithdrawalsBinding getBinding() {
            return this.binding;
        }

        public ViewHolderWithdrawals(@NonNull ItemWithdrawalsBinding binding) {
            super((View)binding.getRoot());
            this.binding = binding;
        }
    }

    public static final class ViewHolderTransfer extends  RecyclerView.ViewHolder {
        @NotNull
        private final ItemTransferBinding binding;
        private NavController navController;

        public final void launchTransferFragment(ItemsHome itemsHome) {

            binding.getRoot().setOnClickListener(view -> {
                navController = Navigation.findNavController(view);
                navController.navigate(R.id.transferFragment);
            });
        }

        @NonNull
        public final  ItemTransferBinding getBinding() {
            return this.binding;
        }

        public ViewHolderTransfer(@NonNull ItemTransferBinding binding) {
            super((View)binding.getRoot());
            this.binding = binding;
        }
    }

    public static final class ViewHolderPayWithQR extends  RecyclerView.ViewHolder {
        @NotNull
        private final ItemPayQrBinding binding;
        private NavController navController;

        public final void launchPayWithQRFragment(ItemsHome itemsHome) {

            binding.getRoot().setOnClickListener(view -> {
                navController = Navigation.findNavController(view);
                navController.navigate(R.id.payWithQRFragment);
            });
        }

        @NonNull
        public final  ItemPayQrBinding getBinding() {
            return this.binding;
        }

        public ViewHolderPayWithQR(@NonNull ItemPayQrBinding binding) {
            super((View)binding.getRoot());
            this.binding = binding;
        }
    }

    public static final class ViewHolderHistory extends  RecyclerView.ViewHolder {
        @NotNull
        private final ItemHistoryBinding binding;
        private NavController navController;

        public final void launchHistoryFragment(ItemsHome itemsHome) {

            binding.getRoot().setOnClickListener(view -> {
                navController = Navigation.findNavController(view);
                navController.navigate(R.id.historyFragment);
            });
        }

        @NonNull
        public final  ItemHistoryBinding getBinding() {
            return this.binding;
        }

        public ViewHolderHistory(@NonNull ItemHistoryBinding binding) {
            super((View)binding.getRoot());
            this.binding = binding;
        }
    }
}
