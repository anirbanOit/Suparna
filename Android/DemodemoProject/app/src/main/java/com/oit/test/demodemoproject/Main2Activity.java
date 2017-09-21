package com.oit.test.demodemoproject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.oit.test.demodemoproject.APIs.APIClient;
import com.oit.test.demodemoproject.APIs.APIInterface;
import com.oit.test.demodemoproject.Models.Student;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Main2Activity extends AppCompatActivity  {
    APIInterface api;
    MyAdapter adapter;
    RecyclerView showdata_rv;
    EditText search;
    ProgressDialog pd;
    List<Student> mFilteredList = new ArrayList<>();
    List<Student> Studentlist = new ArrayList<>();
    RelativeLayout keyboard;
    RecyclerView key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        pd = new ProgressDialog(Main2Activity.this);
        pd.setMessage("Please Wait.....");
        pd.show();
        pd.setCancelable(true);
        showdata_rv = (RecyclerView) findViewById(R.id.showdata_lv);
        search = (EditText) findViewById(R.id.search_et);
        keyboard = (RelativeLayout) findViewById(R.id.mainlayoutforrecycleview);
        api = APIClient.getClient().create(APIInterface.class);
        key = (RecyclerView) findViewById(R.id.showdata_lv);
        keyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        });
        key.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        });
        Call<List<Student>> call = api.getMyJSON();
        call.enqueue(new Callback<List<Student>>() {
            @Override
            public void onResponse(Call<List<Student>> call, Response<List<Student>> response) {
                List studentslist = response.body();
                ImageConvertor imageConvertor = new ImageConvertor();
                mFilteredList.addAll(response.body());
                setAdapter();
                for (int i = 0; i < studentslist.size(); i++) {
                    Student obj;
                    obj = (Student) studentslist.get(i);
                    obj.setDecodedImage(imageConvertor.base64StringTImage(obj.getPhoto()));
                    Studentlist.add(obj);
                }
                if (Studentlist.isEmpty()) {
                    pd.dismiss();
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(Main2Activity.this);
                    alertDialog.setTitle("Warning");
                    alertDialog.setMessage("No Data Found, Redirecting ..");
                    alertDialog.setPositiveButton("Ok,I Got It!!", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(Main2Activity.this, MainActivity.class));
                            finish();
                        }
                    });
                    alertDialog.show();
                }

            }

            @Override
            public void onFailure(Call<List<Student>> call, Throwable t) {
                Toast.makeText(Main2Activity.this, "failure", Toast.LENGTH_LONG).show();
            }
        });
        addtextlistner();
    }

    void addtextlistner() {
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String srch;
                srch = charSequence.toString();
                mFilteredList.clear();
                if (srch.isEmpty()) {
                    mFilteredList.addAll(Studentlist);
                } else {
                    for (Student student : Studentlist) {
                        if (student.getFirstName().contains(srch)) {
                            mFilteredList.add(student);
                        }
                    }
                    if (mFilteredList.isEmpty()) {
                        //Toast.makeText(Main2Activity.this, "No Data Found", Toast.LENGTH_LONG).show();
                    }
                }
                setAdapter();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void setAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(Main2Activity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        showdata_rv.setLayoutManager(layoutManager);
        adapter = new MyAdapter(mFilteredList, Main2Activity.this);
        showdata_rv.setAdapter(adapter);
    }



    //*******************  view holder  *****************************
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,ClickListener{
        TextView name_tv;
        ImageView image;
        TextView age;
        TextView dept;
        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            name_tv = (TextView)itemView.findViewById(R.id.name_tv);
            image = (ImageView)itemView.findViewById(R.id.studphoto_iv);
            age = (TextView)itemView.findViewById(R.id.age_tv);
            dept = (TextView)itemView.findViewById(R.id.dept_tv);
        }
        @Override
        public void onClick(View view) {
            showDialog(view, getPosition());
        }
        @Override
        public  void showDialog(View v, int position) {

            final Dialog dialog = new Dialog(v.getContext());
            dialog.setContentView(R.layout.dialog);
            // set the custom dialog components - text, image and button
            ImageView pic = (ImageView) dialog.findViewById(R.id.image_div);
            pic.setImageBitmap(mFilteredList.get(position).getDecodedImage());
            TextView name = (TextView) dialog.findViewById(R.id.name_tv);
            name.setText(mFilteredList.get(position).getFirstName() + " " + mFilteredList.get(position).getLastName());
            TextView gender = (TextView) dialog.findViewById(R.id.gender_tv);
            gender.setText(mFilteredList.get(position).getGender());

            TextView depart = (TextView) dialog.findViewById(R.id.dept_tv);
            depart.setText(mFilteredList.get(position).getDept());
            dialog.show();
        }

    }

    //**********************  Adapter  ******************************************
    public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
        private List<Student> studentslist;

        public MyAdapter(List<Student> studentslist, Context context) {
            this.studentslist = studentslist;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
            LayoutInflater inflater = getLayoutInflater();
            View v = inflater.inflate(R.layout.row_list_j,parent, false);
            MyViewHolder holder = new MyViewHolder(v);
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            pd.dismiss();
            holder.name_tv.setText(studentslist.get(position).getFirstName() + " " + studentslist.get(position).getLastName());
            age_calculation(studentslist.get(position).getDob());
            holder.dept.setText(studentslist.get(position).getDept());
            holder.image.setImageBitmap(studentslist.get(position).getDecodedImage());
        }

        @Override
        public int getItemCount() {
            return studentslist.size();
        }


        //***************** age Calculation ************************************************
        String age_calculation(String dob) {
            char seperator;
            if (dob.matches("([0-9]{2}).*")) {
                seperator = dob.charAt(2);
            } else {
                seperator = dob.charAt(1);
            }

            String[] dateParts = dob.split(String.valueOf(seperator));
            String day = dateParts[1];
            String month = dateParts[0];
            String year = dateParts[2];
            int monthint = Integer.parseInt(month);
            int yearint = Integer.parseInt(year);
            Calendar c = Calendar.getInstance();
            int current = c.get(Calendar.YEAR);
            int monthcur = c.get(Calendar.MONTH);
            if (monthint < monthcur) {
                return ("" + (current - yearint) + "years");
            } else if (current != yearint)
                return ("" + ((current - yearint) - 1) + "years");
            else
                return ("" + (current - yearint) + "years");

        }
    }
}
