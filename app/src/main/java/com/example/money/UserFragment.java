package com.example.money;

import static com.example.money.DepositFragment.savemoneyList;
import static com.example.money.HomeFragment.Username;
import static com.example.money.ScarchFragement.moneyInList;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserFragment extends Fragment {
    private EditText edt_password,edt_displayname,edt_gmail,edt_deposit;
    private FirebaseAuth mAuth;
    private ToggleButton toggleButton;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseDatabase;
    private Button logout;
    FirebaseFirestore firestore;
    DatabaseReference myRef;
    String temp_name;
    List<Integer> sp_money=new ArrayList<>();
    View view;
    int temp1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
              }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_user, container, false);
        edt_gmail=view.findViewById(R.id.edt_gmail);
        edt_password=view.findViewById(R.id.edt_password);
        edt_displayname=view.findViewById(R.id.edt_displayname);
        edt_deposit=view.findViewById(R.id.edt_deposit);
        toggleButton=view.findViewById(R.id.toggleButton);
        logout=view.findViewById(R.id.logout);
        edt_gmail.setFocusable(false);
        edt_password.setFocusable(false);
        edt_displayname.setFocusable(false);
        edt_deposit.setFocusable(false);
        mAuth=FirebaseAuth.getInstance();
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        edt_gmail.setText(mAuth.getCurrentUser().getEmail());
        edt_password.setText(".........");
        edt_displayname.setText(mAuth.getCurrentUser().getDisplayName());
        temp_name=edt_displayname.getText().toString();
        firestore=FirebaseFirestore.getInstance();
        firestore.collection("Shared_Deposit").whereEqualTo("name",""+mAuth.getCurrentUser().getDisplayName()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    Log.d("成功","錢2");
                    moneyInList.clear();
                    sp_money.clear();
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    for (DocumentSnapshot d : list) {
                        MoneyIn moneyIn=d.toObject(MoneyIn.class);
                        moneyInList.add(moneyIn);
                        sp_money.add(Integer.parseInt(String.valueOf(d.get("money"))));
                    }
                    temp1=0;
                    for(int i:sp_money){
                        temp1+=i;
                    }
                }
            }
        });
        firestore.collection("Shared_Save_money").whereEqualTo("name",""+mAuth.getCurrentUser().getDisplayName()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                Log.d("成功","錢");
                if (!queryDocumentSnapshots.isEmpty()) {
                    Log.d("成功","錢2");
                    savemoneyList.clear();
                    sp_money.clear();
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    for (DocumentSnapshot d : list) {
                        Savemoney savemoney=d.toObject(Savemoney.class);
                        savemoneyList.add(savemoney);
                        sp_money.add(Integer.parseInt(String.valueOf(d.get("money"))));
                    }
                    int sum_money =0;
                    for(int i:sp_money){
                        sum_money+=i;
                    }
                    Log.d("錢",sum_money+"");
                    edt_deposit.setText(String.valueOf(sum_money-temp1));
                }
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent();
                intent.setClass(getContext(), HomeActivity.class);
                startActivity(intent);
                //mainActivity.finish();
            }
        });

        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            String temp1,temp2;
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    temp1=edt_password.getText().toString();
                    Log.d("密碼更改前",temp1);
                    edt_gmail.requestFocus();
                    edt_gmail.setFocusable(true);
                    edt_gmail.setFocusableInTouchMode(true);
                    edt_password.requestFocus();
                    edt_password.setFocusable(true);
                    edt_password.setFocusableInTouchMode(true);
                    edt_displayname.setFocusable(true);
                    edt_displayname.setFocusableInTouchMode(true);
                    edt_displayname.requestFocus();

                }else{
                    Toast.makeText(view.getContext(), "儲存成功",Toast.LENGTH_LONG).show();
                    edt_gmail.setFocusable(false);
                    edt_password.setFocusable(false);
                    edt_displayname.setFocusable(false);
                    temp2=edt_password.getText().toString();
                    Log.d("密碼更改後",temp2);
                    firebaseUser.updateEmail(edt_gmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Log.d("gamil","成功");
                        }
                    });
                    if(!temp2.equals(temp1)){

                        firebaseUser.updatePassword(edt_password.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d("password","成功");
                            }
                        });
                    }
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(edt_displayname.getText().toString())
                            .build();
                        firebaseUser.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            //myRef.child(mAuth.getCurrentUser().getUid()).setValue(name);
                            firebaseDatabase=FirebaseDatabase.getInstance();
                            DatabaseReference databaseReference=firebaseDatabase.getReference("userIFO");
                            databaseReference.child(mAuth.getCurrentUser().getUid()).setValue(edt_displayname.getText().toString());
                            Log.d("displayname","成功");
                            firestore=FirebaseFirestore.getInstance();
                            //firestore.collection("Shared_Deposit").whereEqualTo("name",mAuth.getCurrentUser().getDisplayName());
                            firestore.collection("Shared_Save_money").whereEqualTo("name",temp_name).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    Log.d("tempname",""+temp_name);
                                    String temp_time,temp_time2;
                                    if (!queryDocumentSnapshots.isEmpty()) {
                                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                                        for (DocumentSnapshot d : list) {
                                            Savemoney savemoney=d.toObject(Savemoney.class);
                                            temp_time2=savemoney.getTime();
                                            savemoney.setName(""+edt_displayname.getText().toString());
                                            firestore.collection("Shared_Save_money").document(temp_time2).delete();
                                            firestore.collection("Shared_Save_money").document(temp_time2).set(savemoney);
                                        }
                                    }
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
                                }
                            });
                            firestore.collection("Shared_Deposit").whereEqualTo("name",temp_name).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    Username.clear();
                                    Log.d("tempname",""+temp_name);
                                    String temp_time,temp_time2;
                                    if (!queryDocumentSnapshots.isEmpty()) {
                                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                                        for (DocumentSnapshot d : list) {
                                            MoneyIn moneyIn=d.toObject(MoneyIn.class);
                                            temp_time=moneyIn.getTime();
                                            moneyIn.setName(""+edt_displayname.getText().toString());
                                            firestore.collection("Shared_Deposit").document(temp_time).delete();
                                            firestore.collection("Shared_Deposit").document(temp_time).set(moneyIn);
                                        }
                                    }
                                }
                            });
                        }
                    });
                }
            }
        });
        return view;
    }
}