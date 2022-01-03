package com.jonathan.bankapp.view.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.jonathan.bankapp.databinding.ItemHistoryTransferBinding;
import com.jonathan.bankapp.utils.Transfer;
import org.jetbrains.annotations.NotNull;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.List;

public class TransferAdapter extends RecyclerView.Adapter<TransferAdapter.ViewHolderHistory> {

    private List<Transfer> listHistory;

    public TransferAdapter(List<Transfer> listHistory) {
        this.listHistory = listHistory;
    }

    @NonNull
    @Override
    public TransferAdapter.ViewHolderHistory onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemHistoryTransferBinding itemHistoryTransferBinding = ItemHistoryTransferBinding.inflate(layoutInflater, parent, false);
        RecyclerView.ViewHolder var9 = null;
        var9 = (RecyclerView.ViewHolder) (new TransferAdapter.ViewHolderHistory(itemHistoryTransferBinding));
        return (ViewHolderHistory) var9;
    }

    @Override
    public void onBindViewHolder(@NonNull TransferAdapter.ViewHolderHistory holder, int position) {
        Transfer transfer = listHistory.get(position);
        NumberFormat formatter = new DecimalFormat("#,###");
        holder.binding.textViewTitle.setText(transfer.getType());
        holder.binding.textViewDescription.setText(transfer.getDescription());
        holder.binding.textViewAmount.setText(MessageFormat.format("COP {0}", formatter.format(Integer.parseInt(transfer.getAmount()))));
        holder.binding.textViewDate.setText(transfer.getDate());
        holder.binding.textViewState.setText(String.format("%s", transfer.isState()));
    }

    @Override
    public int getItemCount() {
        return listHistory.size();
    }

    public final class ViewHolderHistory extends RecyclerView.ViewHolder {

        @NotNull
        private final ItemHistoryTransferBinding binding;

        public final void render(Transfer transfer) {}

        @NonNull
        public final ItemHistoryTransferBinding getBinding() {
            return this.binding;
        }

        public ViewHolderHistory(@NonNull ItemHistoryTransferBinding binding) {
            super((View)binding.getRoot());
            this.binding = binding;
        }
    }
}
