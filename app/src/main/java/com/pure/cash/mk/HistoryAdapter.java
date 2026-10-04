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

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private Context context;
    private List<RequestModel> requests;

    public HistoryAdapter(Context context, List<RequestModel> requests) {
        this.context = context;
        this.requests = requests;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RequestModel req = requests.get(position);

        // Type badge
        if ("DEPOSIT".equals(req.getType())) {
            holder.hisType.setText("⬇️ ডিপোজিট");
            holder.hisType.setTextColor(0xFF22C55E);
        } else if ("WITHDRAW".equals(req.getType())) {
            holder.hisType.setText("⬆️ উইথড্র");
            holder.hisType.setTextColor(0xFFF59E0B);
        } else {
            holder.hisType.setText(req.getType());
            holder.hisType.setTextColor(0xFF94A3B8);
        }

        holder.hisAmount.setText("৳ " + req.getAmount());

        // Status
        String statusText = "";
        int statusColor = 0xFF94A3B8;
        if ("PENDING".equals(req.getStatus())) {
            statusText = "⏳ অপেক্ষমাণ";
            statusColor = 0xFFF59E0B;
        } else if ("APPROVED".equals(req.getStatus())) {
            statusText = "✅ অনুমোদিত";
            statusColor = 0xFF22C55E;
        } else if ("REJECTED".equals(req.getStatus())) {
            statusText = "❌ বাতিল";
            statusColor = 0xFFEF4444;
        }
        holder.hisStatus.setText(statusText);
        holder.hisStatus.setTextColor(statusColor);

        // Method
        if (req.getMethod() != null) {
            holder.hisMethod.setText("📱 " + req.getMethod());
        } else {
            holder.hisMethod.setText("");
        }

        // Time
        if (req.getTime() > 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault());
            holder.hisTime.setText("🕐 " + sdf.format(new Date(req.getTime())));
        }
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView hisType, hisAmount, hisStatus, hisMethod, hisTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            hisType = itemView.findViewById(R.id.hisType);
            hisAmount = itemView.findViewById(R.id.hisAmount);
            hisStatus = itemView.findViewById(R.id.hisStatus);
            hisMethod = itemView.findViewById(R.id.hisMethod);
            hisTime = itemView.findViewById(R.id.hisTime);
        }
    }
}
