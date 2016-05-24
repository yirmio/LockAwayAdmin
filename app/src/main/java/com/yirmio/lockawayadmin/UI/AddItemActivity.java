package com.yirmio.lockawayadmin.UI;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.SaveCallback;


import com.yirmio.lockawayadmin.DAL.ParseConnector;
import com.yirmio.lockawayadmin.R;
import com.yirmio.lockawayadmin.Utils.LockAwayAdminApplication;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AddItemActivity extends Activity implements View.OnClickListener {
    private static final int IMAGE_PICKER_SELECT = 999;
    private static final int RESULT_LOAD_IMAGE = 1;


    private EditText editTextName;
    private EditText editTextDescription;
    private EditText editTextTimeToMake;
    private EditText editTextPrive;
    private CheckBox checkBoxIsGlotenFree;
    private CheckBox checkBoxIsVeg;
    private CheckBox checkBoxIsAvalible;
    private CheckBox checkBoxIsOnSale;
    private ImageView imgViewPhoto;
    private Button btnSend;
    private Button btnChoosePhotos;
    private Spinner spnrItemType;



    private Uri outputFileUri;
    private SpinnerAdapter mTypeSpnrAdptr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_item);
        attachUI();


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.insertItemBtnAddImages:
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, IMAGE_PICKER_SELECT);
//                openImageIntent();
                break;
            case R.id.insertItemBtnSend:
                try {
                    if ((editTextName.getText() == null || editTextName.getText().length() <= 0) ||
                            (editTextDescription.getText() == null || editTextDescription.getText().length() <= 0) ||
                            (editTextTimeToMake.getText() == null || editTextTimeToMake.getText().length() <= 0) ||
                            (editTextPrive.getText() == null || editTextPrive.getText().length() <= 0) ||
                            (imgViewPhoto.getDrawable() == null)
                            ) {
                        Toast.makeText(getApplicationContext(), R.string.check_item, Toast.LENGTH_SHORT).show();
                    } else {
                        //TODO - do async task with progress bar
                        final ParseObject newItem = new ParseObject("MenuObjects");
                        newItem.put("StoreID", LockAwayAdminApplication.getRestID());
                        newItem.put("Name", editTextName.getText().toString());
                        newItem.put("Price", Integer.parseInt(editTextPrive.getText().toString()));
                        newItem.put("Veg", checkBoxIsVeg.isChecked());
                        newItem.put("GlotenFree", checkBoxIsGlotenFree.isChecked());
                        newItem.put("TimeToMake", Integer.parseInt(editTextTimeToMake.getText().toString()));
                        newItem.put("IsAvaliable", checkBoxIsAvalible.isChecked());
                        newItem.put("IsOnSale", checkBoxIsOnSale.isChecked());
                        newItem.put("Description", editTextDescription.getText().toString());
                        newItem.put("ObjectType", ParseConnector.getMenuObjectTyprIDByName(spnrItemType.getSelectedItem().toString()));
                        newItem.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    Toast.makeText(getApplicationContext(), R.string.object_saved, Toast.LENGTH_SHORT).show();
                                    final ParseObject photoObject = new ParseObject("MenuPhotos");
                                    Bitmap bitmap = ((BitmapDrawable) imgViewPhoto.getDrawable()).getBitmap();
                                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                                    // get byte array here
                                    byte[] bytearray = stream.toByteArray();

                                    photoObject.put("MenuObjectID", newItem.getObjectId());
                                    if (bytearray != null) {
                                        final ParseFile file = new ParseFile(newItem.getObjectId() + ".png", bytearray);
                                        file.saveInBackground(new SaveCallback() {
                                            @Override
                                            public void done(ParseException e) {
                                                if (e == null) {
                                                    photoObject.put("PhotoFile", file);
                                                    Toast.makeText(getApplicationContext(), R.string.photo_uploaded, Toast.LENGTH_SHORT).show();
                                                    //Close Current activity
                                                    finish();
                                                } else {
                                                    Toast.makeText(getApplicationContext(), R.string.error_uploading_photo, Toast.LENGTH_SHORT).show();
                                                }
                                                photoObject.saveInBackground(new SaveCallback() {
                                                    @Override
                                                    public void done(ParseException e) {
                                                        if (e != null) {
                                                            Toast.makeText(getApplicationContext(), R.string.error_uploading_photo_object, Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                            }
                                        });

                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), R.string.error_saving_object, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void attachUI() {
        //Checkboxes
        this.checkBoxIsAvalible = (CheckBox) findViewById(R.id.insertItemChckBxAvalibleInMemu);
        this.checkBoxIsGlotenFree = (CheckBox) findViewById(R.id.insertItemChckBxIsGlotenFree);
        this.checkBoxIsVeg = (CheckBox) findViewById(R.id.insertItemChckBxIsVeg);
        this.checkBoxIsOnSale = (CheckBox)findViewById(R.id.insertItemChckBxIsSale);
        //Edit Text
        this.editTextDescription = (EditText) findViewById(R.id.insertItemEditTextDescription);
        this.editTextName = (EditText) findViewById(R.id.insertItemEditTextName);
        this.editTextTimeToMake = (EditText) findViewById(R.id.insertItemEditTextTimeToMake);
        this.editTextPrive = (EditText) findViewById(R.id.insertItemEditTextPrice);
        //ImageView
        this.imgViewPhoto = (ImageView) findViewById(R.id.insertItemImgViewPhoto);
        //Buttons
        this.btnChoosePhotos = (Button) findViewById(R.id.insertItemBtnAddImages);
        this.btnChoosePhotos.setOnClickListener(this);
        this.btnSend = (Button) findViewById(R.id.insertItemBtnSend);
        this.btnSend.setOnClickListener(this);
        //Spinner
        this.spnrItemType = (Spinner)findViewById(R.id.addItemTypeSpinner);
        String[] items = ParseConnector.getAllMenuObjectTypeNames();
        this.mTypeSpnrAdptr = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,items);
        this.spnrItemType.setAdapter(this.mTypeSpnrAdptr);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IMAGE_PICKER_SELECT && resultCode == Activity.RESULT_OK && null != data) {

            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getApplicationContext().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
//            final boolean isCamera;
//            if (data == null) {
//                isCamera = true;
//            } else {
//                final String action = data.getAction();
//                if (action == null) {
//                    isCamera = false;
//                } else {
//                    isCamera = action.equals(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                }
//            }
//
//            Uri selectedImageUri;
//            if (isCamera) {
//                selectedImageUri = outputFileUri;
//            } else {
//                selectedImageUri = data == null ? null : data.getData();
//            }



            insertImageToImageView(picturePath, this.imgViewPhoto);

            // String picturePath contains the path of selected Image
        }
    }


    private void openImageIntent() {

// Determine Uri of camera image to save.
        final File root = new File(Environment.getExternalStorageDirectory() + File.separator + "MyDir" + File.separator);
        root.mkdirs();
//        final String fname = Utils.getUniqueImageFilename();
        final String fname = "img_"+ System.currentTimeMillis() + ".jpg";
        final File sdImageMainDirectory = new File(root, fname);
        outputFileUri = Uri.fromFile(sdImageMainDirectory);

        // Camera.
        final List<Intent> cameraIntents = new ArrayList<Intent>();
        final Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        final PackageManager packageManager = getPackageManager();
        final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for(ResolveInfo res : listCam) {
            final String packageName = res.activityInfo.packageName;
            final Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(packageName);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            cameraIntents.add(intent);
        }

        // Filesystem.
        final Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

        // Chooser of filesystem options.
        final Intent chooserIntent = Intent.createChooser(galleryIntent, "Select Source");

        // Add the camera options.
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[cameraIntents.size()]));

        startActivityForResult(chooserIntent, IMAGE_PICKER_SELECT);

        //                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(i, IMAGE_PICKER_SELECT);
    }

    private void insertImageToImageView(String picturePath, ImageView imgView) {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        //bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(picturePath, bmOptions);
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeFile(picturePath, bmOptions);
        } catch (Exception e) {
            e.printStackTrace();
        }

        int imgViewWidth = imgView.getWidth();
        int imgViewHeight = imgView.getHeight();

        imgView.setImageBitmap(bitmap);
        scaleImage(imgView, imgView.getWidth());

    }

    private void scaleImage(ImageView view, int boundBoxInDp) {
        // Get the ImageView and its bitmap
        Drawable drawing = view.getDrawable();
        Bitmap bitmap = ((BitmapDrawable) drawing).getBitmap();

        // Get current dimensions
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        // Determine how much to scale: the dimension requiring less scaling is
        // closer to the its side. This way the image always stays inside your
        // bounding box AND either x/y axis touches it.
        float xScale = ((float) boundBoxInDp) / width;
        float yScale = ((float) boundBoxInDp) / height;
        float scale = (xScale <= yScale) ? xScale : yScale;

        // Create a matrix for the scaling and add the scaling data
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);

        // Create a new bitmap and convert it to a format understood by the ImageView
        Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        BitmapDrawable result = new BitmapDrawable(scaledBitmap);
        width = scaledBitmap.getWidth();
        height = scaledBitmap.getHeight();

        // Apply the scaled bitmap
        view.setImageDrawable(result);

        // Now change ImageView's dimensions to match the scaled image
//        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
//        params.width = width;
//        params.height = height;
//        view.setLayoutParams(params);
    }

    private int dpToPx(int dp) {
        float density = getApplicationContext().getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }


}
