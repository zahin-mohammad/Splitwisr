package com.splitwisr.ui.balances;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.splitwisr.R;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;

public class BalancesAdapter extends RecyclerView.Adapter<BalancesAdapter.BalanceViewHolder> {
    private List<BalanceViewObject> balances = Collections.emptyList();
    SettleBalanceCallBack callBack;
    private static DecimalFormat df = new DecimalFormat("0.00");

    public BalancesAdapter(SettleBalanceCallBack callBack) {
        this.callBack = callBack;
    }

    public static class BalanceViewHolder extends RecyclerView.ViewHolder {
        public ConstraintLayout balanceView;
        public BalanceViewHolder(@NonNull ConstraintLayout itemView) {
            super(itemView);
            balanceView = itemView;
        }
    }

    @NonNull
    @Override
    public BalanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ConstraintLayout balanceView = (ConstraintLayout) LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.balance_view, parent, false);

        return new BalanceViewHolder(balanceView);
    }

    @Override
    public void onBindViewHolder(@NonNull BalanceViewHolder holder, int position) {
        BalanceViewObject balanceViewObject = balances.get(position);
        TextView userText = holder.balanceView.findViewById(R.id.user_text);
        TextView balanceText = holder.balanceView.findViewById(R.id.balance_text);
        userText.setText(
                (balanceViewObject.otherName.isEmpty())
                        ? balanceViewObject.otherEmail
                        : balanceViewObject.otherName );
        balanceText.setText("$" + df.format(Math.abs(balanceViewObject.balance)));
        if (balanceViewObject.balance == 0d) {
            // pass
        } else if (
                (balanceViewObject.owesOtherUser && balanceViewObject.balance > 0) ||
                        (!balanceViewObject.owesOtherUser && balanceViewObject.balance < 0)
        ) {
            balanceText.setTextColor(Color.RED);
        } else {
            balanceText.setTextColor(Color.GREEN);
        }
        Button settleUpButton = holder.balanceView.findViewById(R.id.settle_up);
        settleUpButton.setOnClickListener(v-> this.callBack.callback(balanceViewObject.otherEmail));
    }

    @Override
    public int getItemCount() {
        return balances.size();
    }

    public void setData(List<BalanceViewObject> newBalances) {
        balances = newBalances;
        notifyDataSetChanged();
    }
}
