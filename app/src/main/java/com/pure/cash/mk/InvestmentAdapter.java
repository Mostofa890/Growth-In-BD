package com.pure.cash.mk;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class InvestmentAdapter extends RecyclerView.Adapter<InvestmentAdapter.ViewHolder> {

    private Context context;
    private List<InvestmentModel> investments;

    public InvestmentAdapter(Context context, List<InvestmentModel> investments) {
        this.context = context;
        this.investments = investments;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_investment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        InvestmentModel inv = investments.get(position);

        holder.invAmount.setText("৳ " + inv.getAmount());
        holder.invRate.setText(inv.getRate() + "% বার্ষিক");
        holder.invDuration.setText(inv.getDurationMonths() + " মাস");

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        holder.invStart.setText("শুরু: " + sdf.format(new Date(inv.getStartDate())));
        holder.invEnd.setText("শেষ: " + sdf.format(new Date(inv.getEndDate())));

        holder.invMonthly.setText("মাসিক লাভ: ৳ " + String.format(Locale.getDefault(), "%.2f", inv.getMonthlyEarning()));
        holder.invTotalEarned.setText("মোট লাভ: ৳ " + inv.getTotalEarned());

        // Status badge
        if ("ACTIVE".equals(inv.getStatus())) {
            holder.invStatus.setText("● চলমান");
            holder.invStatus.setBackgroundColor(0xFF22C55E);
            holder.invStatus.setTextColor(0xFF052E16);
        } else if ("MATURED".equals(inv.getStatus())) {
            holder.invStatus.setText("✅ মেয়াদ শেষ");
            holder.invStatus.setBackgroundColor(0xFF38BDF8);
            holder.invStatus.setTextColor(0xFF052E16);
        } else if ("WITHDRAWN".equals(inv.getStatus())) {
            holder.invStatus.setText("↩️ উত্তোলিত");
            holder.invStatus.setBackgroundColor(0xFF64748B);
            holder.invStatus.setTextColor(0xFFFFFFFF);
        } else {
            holder.invStatus.setText(inv.getStatus());
        }
    }

    @Override
    public int getItemCount() {
        return investments.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView invAmount, invRate, invDuration, invStart, invEnd, invMonthly, invTotalEarned, invStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            invAmount = itemView.findViewById(R.id.invAmount);
            invRate = itemView.findViewById(R.id.invRate);
            invDuration = itemView.findViewById(R.id.invDuration);
            invStart = itemView.findViewById(R.id.invStart);
            invEnd = itemView.findViewById(R.id.invEnd);
            invMonthly = itemView.findViewById(R.id.invMonthly);
            invTotalEarned = itemView.findViewById(R.id.invTotalEarned);
            invStatus = itemView.findViewById(R.id.invStatus);
        }
    }
}
