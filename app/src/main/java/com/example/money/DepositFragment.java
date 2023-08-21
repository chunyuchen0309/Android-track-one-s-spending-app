package com.example.money;

import static com.example.money.HomeFragment.Username;
import static com.example.money.ScarchFragement.moneyInList;
import static com.example.money.ScarchFragement.sp_money1;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firestore.v1.Value;
import com.google.protobuf.StringValue;

import java.util.ArrayList;
import java.util.List;


public class DepositFragment extends Fragment {

    private FirebaseFirestore db,db_query;
    public static List<Savemoney> savemoneyList =new ArrayList<>();
    List<Integer> sp_money=new ArrayList<>();
    View view;
    TextView txt_money,txt_seebar,txt_balance;
    Spinner sp_user,sp_category;
    Button btn_scrach;
    SeekBar seekBar;
    private String scarch_name,scarch_category,scarch_date;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db= FirebaseFirestore.getInstance();
        savemoneyList.clear();
        sp_money1.clear();
        Log.d("Deposit","開始");
        db.collection("Shared_Deposit").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    for (DocumentSnapshot d : list) {
                        MoneyIn moneyIn=d.toObject(MoneyIn.class);
                        moneyInList.add(moneyIn);
                        sp_money1.add(Integer.parseInt(String.valueOf(d.get("money"))));
                    }
                }
            }
        });


        db.collection("Shared_Save_money").orderBy("time", Query.Direction.DESCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    for (DocumentSnapshot d : list) {
                        Savemoney savemoney=d.toObject(Savemoney.class);
                        savemoneyList.add(savemoney);
                        sp_money.add(Integer.parseInt(String.valueOf(d.get("money"))));
                    }
                    Log.d("存入金額",""+sp_money);
                    int sum_money =0;
                    for(int i:sp_money){
                        sum_money+=i;
                    }
                    txt_money.setText(""+sum_money );
                    SavemoneyFragment savemoneyFragment=new SavemoneyFragment();
                    savemoneyFragment.Save_money_update();
                    Log.d("存款金額",""+savemoneyList);
                }
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_deposit, container, false);
        txt_money=view.findViewById(R.id.save_txt_money);
        sp_user=view.findViewById(R.id.save_sp_user);
        sp_category=view.findViewById(R.id.save_sp_category);
        btn_scrach=view.findViewById(R.id.save_btn);
        seekBar=view.findViewById(R.id.save_seekBar);
        txt_seebar=view.findViewById(R.id.save_txt_date);
        txt_balance=view.findViewById(R.id.txt_balance);
        if(Username.indexOf("請選擇姓名")==Username.size()-1){
            Username.remove("請選擇姓名");
        }
        Username.add("請選擇姓名");
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_dropdown_item_1line,Username){
            @Override
            public int getCount() {
                // to show hint "Select Gender" and don't able to select

                return Username.size()-1;
            }
        };

        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        sp_user.setAdapter(adapter);
        sp_user.setSelection(Username.size()-1);
        //Username.remove("請選擇姓名");
        sp_user.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                scarch_name=parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        String [] abc=view.getResources().getStringArray(R.array.list_savemoney);

        ArrayAdapter<String> adapter1=new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_dropdown_item_1line,abc){
            @Override
            public int getCount() {
                // to show hint "Select Gender" and don't able to select
                return abc.length-1;
            }
        };
        adapter1.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        sp_category.setAdapter(adapter1);
        sp_category.setSelection(abc.length-1);
        sp_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                scarch_category=parent.getItemAtPosition(position).toString();
                Log.d("搜尋類別",scarch_category);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                txt_seebar.setText(""+progress+"月");
                scarch_date=String.valueOf(progress);
                Log.d("篩選",""+scarch_name);
                if(scarch_name.equals("All") &&!scarch_category.equals("ALL")){
                    Log.d("篩選","全部");
                    savemoneyList.clear();
                    sp_money.clear();
                    db_query=FirebaseFirestore.getInstance();
                    Query query=db_query.collection("Shared_Save_money").whereEqualTo("category",scarch_category).whereEqualTo("month",scarch_date);
                    query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            QuerySnapshot querySnapshot=task.isSuccessful()?task.getResult():null;
                            for(DocumentSnapshot documentSnapshot:querySnapshot.getDocuments()){
                                Savemoney savemoney=documentSnapshot.toObject(Savemoney.class);
                                savemoneyList.add(savemoney);
                                sp_money.add(Integer.parseInt(String.valueOf(documentSnapshot.get("money"))));
                            }
                            int sum_money =0;
                            for(int i:sp_money){
                                sum_money+=i;
                            }
                            Log.d("消費金額",""+sp_money);
                            txt_money.setText(""+sum_money );
                            int sum_money1 =0;
                            for(int i:sp_money1){
                                sum_money1+=i;
                            }
                            txt_balance.setText(String.valueOf(sum_money-sum_money1));
                            Log.d("搜尋使用者LIST",""+savemoneyList);
                            SavemoneyFragment savemoneyFragment=new SavemoneyFragment();
                            savemoneyFragment.Save_money_update();
                        }
                    });
                }else{
                    if(!scarch_name.equals("All")&&scarch_category.equals("ALL")){
                        Log.d("篩選","特定");
                        savemoneyList.clear();
                        sp_money.clear();
                        db_query=FirebaseFirestore.getInstance();
                        Query query=db_query.collection("Shared_Save_money").whereEqualTo("name",scarch_name).whereEqualTo("month",scarch_date);
                        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                QuerySnapshot querySnapshot=task.isSuccessful()?task.getResult():null;
                                for(DocumentSnapshot documentSnapshot:querySnapshot.getDocuments()){
                                    Savemoney savemoney=documentSnapshot.toObject(Savemoney.class);
                                    savemoneyList.add(savemoney);
                                    sp_money.add(Integer.parseInt(String.valueOf(documentSnapshot.get("money"))));
                                }
                                Log.d("消費金額",""+sp_money);
                                int sum_money =0;
                                for(int i:sp_money){
                                    sum_money+=i;
                                }
                                txt_money.setText(""+sum_money );
                                Log.d("搜尋使用者LIST",""+savemoneyList);
                                int sum_money1 =0;
                                for(int i:sp_money1){
                                    sum_money1+=i;
                                }
                                txt_balance.setText(String.valueOf(sum_money-sum_money1));
                                SavemoneyFragment savemoneyFragment=new SavemoneyFragment();
                                savemoneyFragment.Save_money_update();
                            }
                        });
                    }else{
                        if(scarch_name.equals("All")&&scarch_category.equals("ALL")){
                            Log.d("篩選","特定");
                            savemoneyList.clear();
                            sp_money.clear();
                            db_query=FirebaseFirestore.getInstance();
                            Query query=db_query.collection("Shared_Save_money").whereEqualTo("month",scarch_date);
                            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    QuerySnapshot querySnapshot=task.isSuccessful()?task.getResult():null;
                                    for(DocumentSnapshot documentSnapshot:querySnapshot.getDocuments()){
                                        Savemoney savemoney=documentSnapshot.toObject(Savemoney.class);
                                        savemoneyList.add(savemoney);
                                        sp_money.add(Integer.parseInt(String.valueOf(documentSnapshot.get("money"))));
                                    }
                                    Log.d("消費金額",""+sp_money);
                                    int sum_money =0;
                                    for(int i:sp_money){
                                        sum_money+=i;
                                    }
                                    txt_money.setText(""+sum_money );
                                    Log.d("搜尋使用者LIST",""+savemoneyList);
                                    int sum_money1 =0;
                                    for(int i:sp_money1){
                                        sum_money1+=i;
                                    }
                                    txt_balance.setText(String.valueOf(sum_money-sum_money1));
                                    SavemoneyFragment savemoneyFragment=new SavemoneyFragment();
                                    savemoneyFragment.Save_money_update();
                                }
                            });
                        }else{
                            Log.d("篩選","特定");
                            savemoneyList.clear();
                            sp_money.clear();
                            db_query=FirebaseFirestore.getInstance();
                            Query query=db_query.collection("Shared_Save_money").whereEqualTo("name",scarch_name).whereEqualTo("category",scarch_category).whereEqualTo("month",scarch_date);
                            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    QuerySnapshot querySnapshot=task.isSuccessful()?task.getResult():null;
                                    for(DocumentSnapshot documentSnapshot:querySnapshot.getDocuments()){
                                        Savemoney savemoney=documentSnapshot.toObject(Savemoney.class);
                                        savemoneyList.add(savemoney);
                                        sp_money.add(Integer.parseInt(String.valueOf(documentSnapshot.get("money"))));
                                    }
                                    Log.d("消費金額",""+sp_money);
                                    int sum_money =0;
                                    for(int i:sp_money){
                                        sum_money+=i;
                                    }
                                    txt_money.setText(""+sum_money );
                                    Log.d("搜尋使用者LIST",""+savemoneyList);
                                    int sum_money1 =0;
                                    for(int i:sp_money1){
                                        sum_money1+=i;
                                    }
                                    txt_balance.setText(String.valueOf(sum_money-sum_money1));
                                    SavemoneyFragment savemoneyFragment=new SavemoneyFragment();
                                    savemoneyFragment.Save_money_update();
                                }
                            });
                        }
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        btn_scrach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("搜尋按鈕點擊","成功");
                savemoneyList.clear();
                sp_money.clear();
                db_query=FirebaseFirestore.getInstance();
                Query query=db_query.collection("Shared_Save_money").whereEqualTo("name",scarch_name).whereEqualTo("category",scarch_category).whereEqualTo("month",scarch_date);
                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        QuerySnapshot querySnapshot=task.isSuccessful()?task.getResult():null;
                        for(DocumentSnapshot documentSnapshot:querySnapshot.getDocuments()){
                            Savemoney savemoney=documentSnapshot.toObject(Savemoney.class);
                            savemoneyList.add(savemoney);
                            sp_money.add(Integer.parseInt(String.valueOf(documentSnapshot.get("money"))));
                        }
                        Log.d("消費金額",""+sp_money);
                        int sum_money =0;
                        for(int i:sp_money){
                            sum_money+=i;
                        }
                        txt_money.setText(""+sum_money );
                        int sum_money1 =0;
                        for(int i:sp_money1){
                            sum_money1+=i;
                        }
                        txt_balance.setText(String.valueOf(sum_money-sum_money1));
                        Log.d("搜尋使用者LIST",""+savemoneyList);
                        SavemoneyFragment savemoneyFragment=new SavemoneyFragment();
                        savemoneyFragment.Save_money_update();
                    }
                });
            }
        });
        return view;
    }

    @Override
    public void onPause() {

        super.onPause();
    }
}