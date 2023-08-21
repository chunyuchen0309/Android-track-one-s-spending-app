package com.example.money;
import android.content.Intent;
import android.os.Bundle;
//import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
public class HomeFragment extends Fragment {
    private Button deposit_send;
    private TextView txt ,txt_date;
    private EditText edt_deposit,edt_detail;
    private String date,time,date1,Month;
    private Spinner spinner,spinner_save;
    private Switch aSwitch;
    private int money;
    FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private BottomNavigationView bottomNavigationView;
    MainActivity mainActivity=new MainActivity();
    public static List<String> Username=new ArrayList<>();
    private String category,category1;
    Boolean switch_boolean=false;
    View view;
    FirebaseDatabase firebaseDatabase;
    FirebaseFirestore db ;
    DatabaseReference myRef;
    CalendarView calendarView ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("star:","HomeFragment");
        view= inflater.inflate(R.layout.fragment_home, container, false);
        txt= view.findViewById(R.id.textView);
        txt_date=view.findViewById(R.id.date);
        edt_deposit=view.findViewById(R.id.deposit);
        deposit_send=view.findViewById(R.id.save_send);
        calendarView =view. findViewById(R.id.calendarView);
        spinner=view.findViewById(R.id.spinner_select);
        edt_detail=view.findViewById(R.id.edt_detail);
        aSwitch=view.findViewById(R.id.switch1);
        spinner_save=view.findViewById(R.id.spinner_save);
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked){
                    switch_boolean=false;
                    edt_deposit.setHint("記帳金額");
                    deposit_send.setText("記帳");
                    spinner.setVisibility(View.VISIBLE);
                    spinner_save.setVisibility(View.INVISIBLE);
                    edt_detail.setVisibility(View.VISIBLE);
                }else{
                    switch_boolean=true;
                    deposit_send.setText("存錢");
                    edt_deposit.setHint("存入金額");
                    spinner.setVisibility(View.INVISIBLE);
                    spinner_save.setVisibility(View.VISIBLE);
                    edt_detail.setVisibility(View.INVISIBLE);
                }
            }
        });
        ArrayAdapter<CharSequence>adapter1=ArrayAdapter.createFromResource(view.getContext(),R.array.list_savemoney_home, R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner_save.setAdapter(adapter1);
        spinner_save.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category1=parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ArrayAdapter<CharSequence>adapter=ArrayAdapter.createFromResource(view.getContext(),R.array.list_home, R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category=parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        db= FirebaseFirestore.getInstance();
        auth= FirebaseAuth.getInstance();
        firebaseDatabase= FirebaseDatabase.getInstance();
        myRef=firebaseDatabase.getReference("userIFO");
        Username.clear();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Username.add(dataSnapshot.getValue().toString());
                }
                Log.d("Username",Username.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() { //日歷
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                date=year+"/"+(month+1)+"/"+dayOfMonth;
                // date1=year+"-"+(month+1)+"-"+dayOfMonth;
                txt_date.setText(year+"/"+(month+1)+"/"+dayOfMonth);
                Month=String.valueOf(month+1);
         }
        });
        deposit_send.setOnClickListener(new View.OnClickListener() { //記帳存入firebase
            @Override
            public void onClick(View v) {
                if (switch_boolean == false) {
                    if (TextUtils.isEmpty(edt_deposit.getText().toString())) {
                        Toast.makeText(getContext(), "Money is Empty", Toast.LENGTH_LONG).show();
                        return;}
                    if (TextUtils.isEmpty(txt_date.getText().toString())) {
                        Toast.makeText(getContext(), "Date is Empty", Toast.LENGTH_LONG).show();
                        return;}
                    if (TextUtils.isEmpty(edt_detail.getText().toString())) {
                        Toast.makeText(getContext(), "note is Empty", Toast.LENGTH_LONG).show();
                        return;}
                    money = Integer.parseInt(edt_deposit.getText().toString());
                    DateFormat df = new SimpleDateFormat("yyyy年MM月dd日HH時mm分ss秒");
                    time = df.format(new Date());
                    Deposit deposit = new Deposit(date, money, auth.getCurrentUser().getDisplayName(),
                            category, edt_detail.getText().toString(), Month, time);
                    db.collection("Shared_Deposit").document(time).set(deposit);
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());// 顯示成功記帳
                    builder.setTitle("記帳成功!");
                    builder.setMessage("花費: " + money);
                    builder.create().show();
                }else{
                    if (TextUtils.isEmpty(edt_deposit.getText().toString())) {
                        Toast.makeText(getContext(), "Money is Empty", Toast.LENGTH_LONG).show();
                        return;}
                    if (TextUtils.isEmpty(txt_date.getText().toString())) {
                        Toast.makeText(getContext(), "Date is Empty", Toast.LENGTH_LONG).show();
                        return;}
                    Log.d("category",""+category1);
                    money = Integer.parseInt(edt_deposit.getText().toString());
                    DateFormat df = new SimpleDateFormat("yyyy年MM月dd日HH時mm分ss秒");
                    time = df.format(new Date());
                    Deposit deposit = new Deposit(date, money, auth.getCurrentUser().getDisplayName(), category1, Month, time);
                    db.collection("Shared_Save_money").document(time).set(deposit);
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());// 顯示成功記帳
                    builder.setTitle("存錢成功!");
                    builder.setMessage("存入: " + money);
                    builder.create().show();
                }
            }
        });
        txt.setText(auth.getCurrentUser().getDisplayName()+" 的記帳本");
     return view;
    }
}