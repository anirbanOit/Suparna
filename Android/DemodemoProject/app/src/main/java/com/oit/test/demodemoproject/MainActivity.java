package com.oit.test.demodemoproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.oit.test.demodemoproject.APIs.APIClient;
import com.oit.test.demodemoproject.APIs.APIInterface;
import com.oit.test.demodemoproject.Models.User;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback{

    EditText firstName;
    EditText lastName;
    Button btn,addbtn;
    TextView textViewMale;
    TextView textViewFemale;
    ImageView imageView;
    TextView datePicker;
    Spinner spinner;
    Calendar c;
    ScrollView keyboard;
    LinearLayout dob;

    APIInterface apiInterface;


    private static int RESULT_LOAD = 1;
    private static final int REQUEST_WRITE_PERMISSION = 786;
    String img_Decodable_Str = null;
    String setFirstName = null;// contains the first name
    String setLastName = null; // contains the last name
    String encoded = null;//contains converted picture
    String setDate = null;//contains the set date
    String gender = null;//contains the set gender
    String setSpinner = null;//contains the set spinner


    static boolean setFemaleFlag = false;
    static boolean setMaleFlag = false;
    boolean isSetNameValidation = false;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestPermission();
        apiInterface = APIClient.getClient().create(APIInterface.class);


        firstName = (EditText) findViewById(R.id.editTextFirstName);
        lastName = (EditText) findViewById(R.id.editTextLastName);
        btn = (Button) findViewById(R.id.button);
        textViewMale = (TextView) findViewById(R.id.textViewmMale);
        textViewFemale = (TextView) findViewById(R.id.textViewmFemale);
        imageView = (ImageView) findViewById(R.id.image_iv);
        datePicker = (TextView) findViewById(R.id.dob_tv);
        spinner = (Spinner) findViewById(R.id.dept_spinner);
        addbtn = (Button) findViewById(R.id.add);
        keyboard = (ScrollView) findViewById(R.id.mainlayoutform);
        dob = (LinearLayout) findViewById(R.id.doblayout);

        c = Calendar.getInstance();
        CreateViewForSpinner();
        datePicker();

        keyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,Main2Activity.class));
            }
        });


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                adapterView.getItemAtPosition(i);
                setSpinner = adapterView.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (gender != null && setDate != null && setSpinner != null) {

                    if ((isSetNameValidation = new NameValidation(firstName.getText().toString(), lastName.getText().toString()).validation()) == true) {
                        setFirstName = firstName.getText().toString();
                        setLastName = lastName.getText().toString();
                    }

                        // Toast.makeText(getApplication(),"Enter A valid name",Toast.LENGTH_SHORT).show();

                    if (setFirstName != null && setLastName != null) {

                        if (encoded == null) {
                            AlertDialog.Builder adb = new AlertDialog.Builder(MainActivity.this);
                            adb.setTitle("WARNING");
                            adb.setMessage("Are You Sure You Want To Upload Your Default Image? ");
                            adb.setCancelable(false);
                            adb.setPositiveButton("YES,I'm Damn Sure", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (gender == "male") {
                                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.male_pic);
                                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                                        byte[] imageBytes = byteArrayOutputStream.toByteArray();
                                        encoded = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                                    } else {
                                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.female_pic);
                                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                                        byte[] imageBytes = byteArrayOutputStream.toByteArray();
                                        encoded = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                                    }
                                    ApiResponseFunction();
                                }
                            });
                            adb.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    AlertDialog.Builder adb = new AlertDialog.Builder(MainActivity.this);
                                    adb.setMessage("Please Select One Profile Image ");
                                }
                            });
                            adb.show();
                        }
                        else
                            ApiResponseFunction();
                    }
                    else  Toast.makeText(getApplicationContext(), "Blank Form Can't Be Submitted", Toast.LENGTH_SHORT).show();
                }
                else   Toast.makeText(getApplicationContext(), "Blank Form Can't Be Submitted", Toast.LENGTH_SHORT).show();
            }
        });

        textViewFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setMaleFlag = new GenderRadio(textViewFemale, textViewMale).SettingGenderFlag(true, setMaleFlag);
                gender = "female";
            }
        });
        textViewMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFemaleFlag = new GenderRadio(textViewFemale, textViewMale).SettingGenderFlag(false, true);
                gender = "male";
            }
        });
    }

    private void ApiResponseFunction() {
        User user = new User(setFirstName, setLastName, gender, setDate, setSpinner, encoded);
        Call<User> call1 = apiInterface.createUser(user);
        call1.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                alertDialog.setMessage("User Created Successfully");
                alertDialog.setPositiveButton("okay", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                alertDialog.show();
                firstName.setText(null);
                lastName.setText(null);
                textViewMale.setBackgroundResource(R.drawable.border);
                setMaleFlag =false;
                setFemaleFlag=false;
                textViewFemale.setBackgroundResource(R.drawable.border);
                imageView.setImageBitmap(null);
                datePicker.setText(null);
                CreateViewForSpinner();
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                call.cancel();
            }
        });
    }
    //***********************************creating view for spinner *************************************************

    private void CreateViewForSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.Department, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

    }


//*********************************Loading Image From Gallery ********************************************

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_WRITE_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loadImageFromGallery();
                }
            });
        }
    }

    public void loadImageFromGallery() {
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, RESULT_LOAD);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        // When an Image is picked
        if (requestCode == RESULT_LOAD && resultCode == RESULT_OK && null != data) {
            // Get the Image from data
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            // Get the cursor
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            // Move to first row
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            img_Decodable_Str = cursor.getString(columnIndex);
            cursor.close();
            if (img_Decodable_Str != null) {
                Toast.makeText(this, " Image Has Been Selected ", Toast.LENGTH_LONG).show();
                ImageConvertor myConvertorImageToBase64 = new ImageConvertor();
                encoded = myConvertorImageToBase64.imageToBase64String(getApplication(), img_Decodable_Str);
            }
            // Set the Image in ImageView after decoding the String
            imageView.setImageBitmap(BitmapFactory.decodeFile(img_Decodable_Str));

            img_Decodable_Str = null;
        } else {

        }
    }
    //*************************DatePicker***************************************************************
    void datePicker()
    {
        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int y = c.get(Calendar.YEAR);
                int m = c.get(Calendar.MONTH);
                int d = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(MainActivity.this, AlertDialog.THEME_HOLO_DARK,new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String s = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        setDate = s;
                        datePicker.setText(s);
                    }
                }, y, m, d);
                dialog.getDatePicker().setMaxDate(c.getTimeInMillis());
                dialog.show();

            }
        });
    }
    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("LOGOUT");
        alertDialog.setMessage("Do You want to Logout??");
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                dialog.cancel();
            }
        });
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(MainActivity.this,Main3Activity.class));
                finish();
            }
        });
        alertDialog.show();
    }


}

