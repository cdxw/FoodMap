package com.myfoodmapapp.myfoodmapapp.data.fragments;


import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.myfoodmapapp.myfoodmapapp.MainActivity;
import com.myfoodmapapp.myfoodmapapp.R;
import com.myfoodmapapp.myfoodmapapp.data.DatabaseManager;
import com.myfoodmapapp.myfoodmapapp.data.model.Health;
import com.myfoodmapapp.myfoodmapapp.data.model.Profile;
import com.myfoodmapapp.myfoodmapapp.data.repo.HealthRepo;
import com.myfoodmapapp.myfoodmapapp.data.repo.ProfileRepo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.myfoodmapapp.myfoodmapapp.data.model.Profile.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserProfileFragment extends Fragment {

    Profile profile = new Profile();
    ProfileRepo profileRepo = new ProfileRepo();

    int duration = Toast.LENGTH_SHORT;
    Health health = new Health();
    HealthRepo healthRepo = new HealthRepo();
    String imagePath ="";
    String previousName = "";

    Button save_btn, imagepickerBtn;

    TextView nameTxt, weightTxt, heightTxt,bmiTxt;
    boolean update = false;

    Bitmap myBitmap;
    Uri picUri;
    View view;
    //private Button btn;
    private CircleImageView imageview;
    private static final String IMAGE_DIRECTORY = "/images";
    private int GALLERY = 1, CAMERA = 2;


    public UserProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // retain this fragment
        setRetainInstance(true);
        Log.d("TAG", "setRetainInstance = true ");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        DatabaseManager db = new DatabaseManager();

        // declare the controls
        save_btn = view.findViewById(R.id.button_saveData);
        imageview = view.findViewById(R.id.img_profile);
        nameTxt = view.findViewById(R.id.name_txt);
        weightTxt = view.findViewById(R.id.weight_txt);
        heightTxt = view.findViewById(R.id.height_txt);
        bmiTxt = view.findViewById(R.id.calc_bmi_txt);

        // Check if the user has a profile
        // load profile if available
        int count = db.countTableRecords("profile");

        if(count > 0){
            loadProfileData(view);
            // update boolean keeps track if database is a new record or update existing record
            update = true;
            previousName = nameTxt.getText().toString();
        }

        // save profile listener
        save_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                saveProfileData(view, update);
            }
        });

        // show the dialog box when clicking the camera button
        imageview.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showPictureDialog();
            }
        });

        db.closeDatabase();

        return view;
    }
    public void saveProfileData(View view, boolean update){

        DatabaseManager db = new DatabaseManager();
        MainActivity mainActivity = new MainActivity();

        int newHealthID = 0;
        int newUserID = 0;
        boolean updated = false;
        Context context = view.getContext().getApplicationContext();



        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        sdf.setTimeZone(TimeZone.getTimeZone("PST"));
        String date = sdf.format(new Date());



        Double weightDouble = 0.0;
        Double heightDouble = 0.0;

        if(imagePath ==""){
            imagePath = db.getImagePath();
        }


        db.closeDatabase();

        try{

            weightDouble = Double.parseDouble(weightTxt.getText().toString());
            heightDouble = Double.parseDouble(heightTxt.getText().toString());

            // Save the profile data
            profile.setName(nameTxt.getText().toString());
            profile.setImagePath(imagePath);

            // Update the users name if they choose
            if(update){
                updated = profileRepo.update(profile, previousName);
            }else{
                newUserID = profileRepo.insert(profile);
            }

            Log.d("newUserID", String.valueOf(newUserID));


            // Save the health data
            Double answer = db.roundResults(calcBMI(weightDouble, heightDouble));
            String stringAnswer = String.valueOf(answer);

            bmiTxt.setText(stringAnswer);

            health.setUserId(newUserID);
            health.setWeight(weightDouble);
            health.setHeight(heightDouble);
            health.setDate(date);
            health.setBMI(answer);

            // The returning ID of the new record - if needed
            newHealthID  = healthRepo.insert(health);

            //Update the profile image in the Nav Drawer
            ((MainActivity)getActivity()).updateProfileImage();

            //Show success message
            CharSequence text = "Profile Data saved";
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();


        }catch (Exception e){
            Log.d("tag", e.getMessage());
            CharSequence text = "Error. Data not saved";
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

        }

    }
    public Double calcBMI(Double weight, Double height){

        Double h = height/100;

        Double bmi = (weight/h)/h;

        return bmi;

    }
    public void loadProfileData(View view){

        DatabaseManager db = new DatabaseManager();
        Profile profile = new Profile();
        Health health = new Health();

        Button save_btn;
        save_btn = view.findViewById(R.id.button_saveData);

        // change the button text to show it is an update
        save_btn.setText("Update Details");

        String height = "";
        String weight = "";
        String name = "";
        String BMI = "";
        String path = "";
        int healthCount = db.countTableRecords("health");

        String selectProfileQuery = "SELECT  * FROM profile";
        String selctHealthQuery = "SELECT  * FROM health" ;
        Cursor cursor = null;
        Cursor healthCursor = null;

        try{
            cursor = db.openDatabase().rawQuery(selectProfileQuery, null);

            // Retrieve the Data from the Profile table
            if (cursor.moveToFirst()) {
                name = cursor.getString(cursor.getColumnIndex("Name"));
                path = cursor.getString(cursor.getColumnIndex("ImagePath"));
            }
        } finally {

            cursor.close();
        }

        if(healthCount >0){
            try{
                healthCursor = db.openDatabase().rawQuery(selctHealthQuery, null);

                // retrieve the data from the health table
                if (healthCursor.moveToLast()) {
                    height = healthCursor.getString(healthCursor.getColumnIndex("Height"));
                    weight = healthCursor.getString(healthCursor.getColumnIndex("Weight"));
                    BMI = healthCursor.getString(healthCursor.getColumnIndex("BMI"));
                }

            } finally {
                healthCursor.close();

            }
        }

        nameTxt.setText(name);
        weightTxt.setText(weight);
        heightTxt.setText(height);
        bmiTxt.setText(BMI);


        // assign the image of the profile if not assign the default image
        File imgFile = new File(String.valueOf(path));

        if (imgFile.exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(String.valueOf(path));
            imageview.setImageBitmap(myBitmap);

        }

    }
    private void showPictureDialog(){

        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(getView().getContext());
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public void choosePhotoFromGallary() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
                "Select Picture"), GALLERY);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(intent, CAMERA);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == getActivity().RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY && resultCode == getActivity().RESULT_OK && null != data) {

            try{
                Uri imageUri = data.getData();
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);

                imagePath =  saveImage(bitmap);
                imageview.setImageURI(imageUri);

            }catch(IOException e){
                Log.d(TAG, "IO error has occurred: " + e.getMessage());
            }
        } else if (requestCode == CAMERA && resultCode == getActivity().RESULT_OK) {

            try{
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                saveImage(thumbnail);
                imageview.setImageBitmap(thumbnail);

                imagePath = saveImage(thumbnail);

            }catch(IOException e){
                Log.d(TAG, "IO error has occurred: " + e.getMessage());
            }

        }
    }

    public String saveImage(Bitmap myBitmap) throws IOException{

        ContextWrapper cw = new ContextWrapper(getActivity().getApplicationContext());

        File directory = cw.getDir("images", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath = new File(directory,"profile.jpg");

        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return mypath.getAbsolutePath();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putString("TAG","Profile");
    }

}

