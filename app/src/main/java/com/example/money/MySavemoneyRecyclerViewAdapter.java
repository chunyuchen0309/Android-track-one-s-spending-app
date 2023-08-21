package com.example.money;

import static com.example.money.DepositFragment.savemoneyList;
import static com.example.money.ScarchFragement.moneyInList;

import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.money.databinding.FragmentItem2Binding;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class MySavemoneyRecyclerViewAdapter extends RecyclerView.Adapter<MySavemoneyRecyclerViewAdapter.ViewHolder> {

    private final List<Savemoney> mValues;
    FirebaseFirestore firestore;

    public MySavemoneyRecyclerViewAdapter(List<Savemoney> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentItem2Binding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mNameView.setText(" "+mValues.get(position).getName());
        holder.mMoney.setText(""+mValues.get(position).getMoney());
        holder.mDate.setText(""+mValues.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mNameView;
        public final TextView mMoney;
        public final TextView mDate;
        public Savemoney mItem;

        public ViewHolder(FragmentItem2Binding binding) {
            super(binding.getRoot());
            mNameView = binding.saveName;
            mMoney = binding.savemoney;
            mDate=binding.savedate;
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder=new AlertDialog.Builder(binding.getRoot().getContext());
                    builder.setTitle("存款:"+mItem.getMoney());
                    builder.setMessage("類別:"+mItem.getCategory()+"\n");
                    builder.create().show();
                }
            });
            binding.getRoot().setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder aleartDialog=new AlertDialog.Builder(binding.getRoot().getContext());
                    aleartDialog.setTitle("刪除該項目");
                    aleartDialog.setMessage("類別:"+mItem.getCategory()+"\n花費:"+mItem.getMoney());
                    aleartDialog.setPositiveButton("刪除", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            firestore= FirebaseFirestore.getInstance();
                            firestore.collection("Shared_Save_money").document(""+mItem.getTime()).delete();
                            savemoneyList.remove(getBindingAdapterPosition());
                            SavemoneyFragment savemoneyFragment=new SavemoneyFragment();
                            savemoneyFragment.Save_money_update();
                        }
                    });
                    aleartDialog.setNeutralButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    aleartDialog.show();
                    return false;
                }
            });
        }
        @Override
        public String toString() {
            return super.toString() + " '" + mNameView.getText() + "'";
        }
    }
}