package com.example.money;
import static com.example.money.HomeFragment.Username;
//import static com.example.money.MoneyDateFragment.update;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
public class ScarchFragement extends Fragment {
    private FirebaseFirestore db,db_query;
    public static List<MoneyIn> moneyInList =new ArrayList<>();
    public static  List<Integer> sp_money1 =new ArrayList<>();
    Spinner spinner,spinner_category;
    Button button_scarch;
    SeekBar seekBar;
    TextView textView_seebar,textView_money, txt_balance;
    private String scarch_name,scarch_category,scarch_date;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db= FirebaseFirestore.getInstance();
        moneyInList.clear();
        sp_money1.clear();
        Log.d("User","開始");
        db.collection("Shared_Deposit").orderBy("time", Query.Direction.DESCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    for (DocumentSnapshot d : list) {
                        MoneyIn moneyIn=d.toObject(MoneyIn.class);
                        moneyInList.add(moneyIn);
                        sp_money1.add(Integer.parseInt(String.valueOf(d.get("money"))));
                    }
                    Log.d("消費金額",""+ sp_money1);
                    int sum_money =0;
                    for(int i: sp_money1){
                        sum_money+=i;
                    }
                    textView_money.setText(""+sum_money );
                    MoneyDateFragment moneyDateFragment=new MoneyDateFragment();
                    moneyDateFragment.update();
                    Log.d("存入",""+moneyInList);
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_scarch, container, false);

        spinner=view.findViewById(R.id.User_sp);
        button_scarch=view.findViewById(R.id.button_scarch);
        spinner_category=view.findViewById(R.id.spinner);
        seekBar=view.findViewById(R.id.seekBar);
        textView_seebar=view.findViewById(R.id.txt_date);
        textView_money=view.findViewById(R.id.txt_money);
        if(Username.indexOf("請選擇姓名")==Username.size()-1){
            Username.remove("請選擇姓名");
        }
        Username.add("請選擇姓名");
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_dropdown_item_1line,Username){
            @Override
            public int getCount() {
                return Username.size()-1;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);
        spinner.setSelection(Username.size()-1);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                scarch_name=parent.getItemAtPosition(position).toString();

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textView_seebar.setText(""+progress+"月");
                scarch_date=String.valueOf(progress);
                Log.d("篩選",""+scarch_name);
                if(scarch_name.equals("All") &&!scarch_category.equals("ALL")){
                    Log.d("篩選","全部");
                    moneyInList.clear();
                    sp_money1.clear();
                    db_query=FirebaseFirestore.getInstance();
                    Query query=db_query.collection("Shared_Deposit").whereEqualTo("category",scarch_category).whereEqualTo("month",scarch_date);
                    query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            QuerySnapshot querySnapshot=task.isSuccessful()?task.getResult():null;
                            for(DocumentSnapshot documentSnapshot:querySnapshot.getDocuments()){
                                MoneyIn moneyIn=documentSnapshot.toObject(MoneyIn.class);
                                moneyInList.add(moneyIn);
                                sp_money1.add(Integer.parseInt(String.valueOf(documentSnapshot.get("money"))));
                            }
                            Log.d("消費金額",""+ sp_money1);
                            int sum_money =0;
                            for(int i: sp_money1){
                                sum_money+=i;
                            }
                            textView_money.setText(""+sum_money );
                            Log.d("搜尋使用者LIST",""+moneyInList);
                            MoneyDateFragment moneyDateFragment=new MoneyDateFragment();
                            moneyDateFragment.update();
                        }
                    });
                }else{
                    if(!scarch_name.equals("All") && scarch_category.equals("ALL")){
                        Log.d("篩選","特定");
                        moneyInList.clear();
                        sp_money1.clear();
                        db_query=FirebaseFirestore.getInstance();
                        Query query=db_query.collection("Shared_Deposit").whereEqualTo("name",scarch_name).whereEqualTo("month",scarch_date);
                        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                QuerySnapshot querySnapshot=task.isSuccessful()?task.getResult():null;
                                for(DocumentSnapshot documentSnapshot:querySnapshot.getDocuments()){
                                    MoneyIn moneyIn=documentSnapshot.toObject(MoneyIn.class);
                                    moneyInList.add(moneyIn);
                                    sp_money1.add(Integer.parseInt(String.valueOf(documentSnapshot.get("money"))));
                                }
                                Log.d("消費金額",""+ sp_money1);
                                int sum_money =0;
                                for(int i: sp_money1){
                                    sum_money+=i;
                                }
                                textView_money.setText(""+sum_money );
                                Log.d("搜尋使用者LIST",""+moneyInList);
                                MoneyDateFragment moneyDateFragment=new MoneyDateFragment();
                                moneyDateFragment.update();
                            }
                        });
                    }else{
                        if(scarch_name.equals("All") && scarch_category.equals("ALL")){
                            Log.d("篩選","特定");
                            moneyInList.clear();
                            sp_money1.clear();
                            db_query=FirebaseFirestore.getInstance();
                            Query query=db_query.collection("Shared_Deposit").whereEqualTo("month",scarch_date);
                            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    QuerySnapshot querySnapshot=task.isSuccessful()?task.getResult():null;
                                    for(DocumentSnapshot documentSnapshot:querySnapshot.getDocuments()){
                                        MoneyIn moneyIn=documentSnapshot.toObject(MoneyIn.class);
                                        moneyInList.add(moneyIn);
                                        sp_money1.add(Integer.parseInt(String.valueOf(documentSnapshot.get("money"))));
                                    }
                                    Log.d("消費金額",""+ sp_money1);
                                    int sum_money =0;
                                    for(int i: sp_money1){
                                        sum_money+=i;
                                    }
                                    textView_money.setText(""+sum_money );
                                    Log.d("搜尋使用者LIST",""+moneyInList);
                                    MoneyDateFragment moneyDateFragment=new MoneyDateFragment();
                                    moneyDateFragment.update();
                                }
                            });
                        }else{
                            Log.d("篩選","特定");
                            moneyInList.clear();
                            sp_money1.clear();
                            db_query=FirebaseFirestore.getInstance();
                            Query query=db_query.collection("Shared_Deposit").whereEqualTo("name",scarch_name)
                                    .whereEqualTo("category",scarch_category).whereEqualTo("month",scarch_date);
                            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    QuerySnapshot querySnapshot=task.isSuccessful()?task.getResult():null;
                                    for(DocumentSnapshot documentSnapshot:querySnapshot.getDocuments()){
                                        MoneyIn moneyIn=documentSnapshot.toObject(MoneyIn.class);
                                        moneyInList.add(moneyIn);
                                        sp_money1.add(Integer.parseInt(String.valueOf(documentSnapshot.get("money"))));
                                    }
                                    Log.d("消費金額",""+ sp_money1);
                                    int sum_money =0;
                                    for(int i: sp_money1){
                                        sum_money+=i;
                                    }
                                    textView_money.setText(""+sum_money );
                                    Log.d("搜尋使用者LIST",""+moneyInList);
                                    MoneyDateFragment moneyDateFragment=new MoneyDateFragment();
                                    moneyDateFragment.update();
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
        button_scarch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("搜尋按鈕點擊","成功");
                moneyInList.clear();
                sp_money1.clear();
                db_query=FirebaseFirestore.getInstance();
                Query query=db_query.collection("Shared_Deposit").whereEqualTo("name",scarch_name)
                        .whereEqualTo("category",scarch_category).whereEqualTo("month",scarch_date);
                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        QuerySnapshot querySnapshot=task.isSuccessful()?task.getResult():null;
                        for(DocumentSnapshot documentSnapshot:querySnapshot.getDocuments()){
                            MoneyIn moneyIn=documentSnapshot.toObject(MoneyIn.class);
                            moneyInList.add(moneyIn);
                            sp_money1.add(Integer.parseInt(String.valueOf(documentSnapshot.get("money"))));
                        }
                        Log.d("消費金額",""+ sp_money1);
                        int sum_money =0;
                        for(int i: sp_money1){
                            sum_money+=i;
                        }
                        textView_money.setText(""+sum_money );
                        Log.d("搜尋使用者LIST",""+moneyInList);
                        MoneyDateFragment moneyDateFragment=new MoneyDateFragment();
                        moneyDateFragment.update();
                    }
                });
            }
        });
        String [] abc=view.getResources().getStringArray(R.array.list);
        ArrayAdapter<String> adapter1=new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_dropdown_item_1line,abc){
            @Override
            public int getCount() {
                // to show hint "Select Gender" and don't able to select
                return abc.length-1;
            }
        };
        adapter1.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner_category.setAdapter(adapter1);
        spinner_category.setSelection(abc.length-1);
        spinner_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                scarch_category=parent.getItemAtPosition(position).toString();
                Log.d("搜尋類別",scarch_category);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        return view;
    }
    @Override
    public void onPause() {
        super.onPause();
    }
}