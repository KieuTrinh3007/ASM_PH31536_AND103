
package com.example.assignmet_ph31536;

import static android.content.ContentValues.TAG;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.assignmet_ph31536.Adapter.AdapterFruit;
import com.example.assignmet_ph31536.Model.FruitModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Home extends AppCompatActivity {
    RecyclerView recyclerView;
    AdapterFruit adapter;
    List<FruitModel> list;
    APIService apiService;
    EditText edten, edsoluong, edgia, search;
    ImageView anh;
    Uri selectedImage;

    FruitModel fruitModel;
    File file;
    MultipartBody.Part multipartBody;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        recyclerView = findViewById(R.id.recyclerViewSinhVien);
        search = findViewById(R.id.search);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIService.DOMAIN)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(APIService.class);

        loadData();

        findViewById(R.id.btnAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                them(Home.this, 0, fruitModel);
            }
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String keyword =editable.toString().trim();
                searchDistributor(keyword);
            }
        });

        findViewById(R.id.giam).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<List<FruitModel>> call = apiService.getGiam();
                call.enqueue(new Callback<List<FruitModel>>() {
                    @Override
                    public void onResponse(Call<List<FruitModel>> call, Response<List<FruitModel>> response) {
                        if (response.isSuccessful()) {
                            list = response.body();

                            adapter = new AdapterFruit(list,  getApplicationContext(), Home.this);

                            recyclerView.setLayoutManager(new LinearLayoutManager(Home.this));
                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<FruitModel>> call, Throwable t) {
                        Log.e("Main", t.getMessage());
                    }
                });
            }
        });

        findViewById(R.id.tang).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<List<FruitModel>> call = apiService.getTang();
                call.enqueue(new Callback<List<FruitModel>>() {
                    @Override
                    public void onResponse(Call<List<FruitModel>> call, Response<List<FruitModel>> response) {
                        if (response.isSuccessful()) {
                            list = response.body();

                            adapter = new AdapterFruit(list,  getApplicationContext(), Home.this);

                            recyclerView.setLayoutManager(new LinearLayoutManager(Home.this));
                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<FruitModel>> call, Throwable t) {
                        Log.e("Main", t.getMessage());
                    }
                });
            }
        });

    }

    void loadData(){
        Call<List<FruitModel>> call = apiService.getFruits();

        call.enqueue(new Callback<List<FruitModel>>() {
            @Override
            public void onResponse(Call<List<FruitModel>> call, Response<List<FruitModel>> response) {
                if (response.isSuccessful()) {
                    list = response.body();

                    adapter = new AdapterFruit(list,  getApplicationContext(), Home.this);

                    recyclerView.setLayoutManager(new LinearLayoutManager(Home.this));
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<FruitModel>> call, Throwable t) {
                Log.e("Main", t.getMessage());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImage = data.getData();
            anh.setImageURI(selectedImage);
        }
    }

    @SuppressLint("SetTextI18n")
    public void them(Context context, int type, FruitModel fruitModel) {
        Dialog dialog = new Dialog(Home.this);
        dialog.setContentView(R.layout.dialog_save);

        edten = dialog.findViewById(R.id.edtten);
        edsoluong = dialog.findViewById(R.id.edtsoluong);
        edgia = dialog.findViewById(R.id.edtgia);
        anh = dialog.findViewById(R.id.imgImage);

        anh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });

        if (type != 0){
            edten.setText(fruitModel.getTen());
            edgia.setText(fruitModel.getGia()+"");
            edsoluong.setText(String.valueOf(fruitModel.getSoLuong()));
            Glide.with(context)
                    .load(fruitModel.getAvatar())
                    .thumbnail(Glide.with(context).load(R.drawable.loading))
                    .into(anh);
            Log.d(TAG, "them: " + fruitModel.getAvatar());
        }
        dialog.findViewById(R.id.btnSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, RequestBody> mapRequestBody = new HashMap<>();
                String ten = edten.getText().toString();
                String soluong = edsoluong.getText().toString();
                String gia = edgia.getText().toString();

                if (file != null) {
                    RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
                    multipartBody = MultipartBody.Part.createFormData("avatar", file.getName(), requestFile);
                } else {
                    multipartBody = null;
                }

                if (ten.length() == 0 || soluong.length() == 0 || gia.length() == 0){
                    Toast.makeText(context, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    Double price = Double.parseDouble(gia);
                    int quantity = Integer.parseInt(soluong);


                    mapRequestBody.put("ten", getRequestBody(ten));
                    mapRequestBody.put("gia", getRequestBody(String.valueOf(price)));
                    mapRequestBody.put("soLuong", getRequestBody(String.valueOf(quantity)));

                    if (price > 0){
                        if (type == 0){

                            Call<FruitModel> call = apiService.addCay(mapRequestBody, multipartBody);
                            call.enqueue(new Callback<FruitModel>() {
                                @Override
                                public void onResponse(Call<FruitModel> call, Response<FruitModel> response) {
                                    if (response.isSuccessful()) {
                                        loadData();
                                        Toast.makeText(Home.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    } else {
                                        Toast.makeText(Home.this, "Thêm thất bại", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                @Override
                                public void onFailure(Call<FruitModel> call, Throwable t) {
                                    Log.e("Home", "Call failed: " + t.toString());
                                    Toast.makeText(Home.this, "Đã xảy ra lỗi khi thêm dữ liệu", Toast.LENGTH_SHORT).show();
                                }

                            });
                        }else{
                            if (multipartBody != null){
                                Call<FruitModel> call = apiService.updateCay(mapRequestBody, fruitModel.get_id() , multipartBody);
                                call.enqueue(new Callback<FruitModel>() {
                                    @Override
                                    public void onResponse(Call<FruitModel> call, Response<FruitModel> response) {
                                        if (response.isSuccessful()) {
                                            loadData();
                                            Toast.makeText(Home.this, "Sửa thành công", Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                        } else {
                                            Toast.makeText(Home.this, "Sửa thất bại", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    @Override
                                    public void onFailure(Call<FruitModel> call, Throwable t) {
                                        Log.e("Home", "Call failed: " + t.toString());
                                        Toast.makeText(Home.this, "Đã xảy ra lỗi khi sửa dữ liệu", Toast.LENGTH_SHORT).show();
                                    }

                                });
                            }else{
                                Call<FruitModel> call = apiService.updateNoImage(mapRequestBody, fruitModel.get_id());
                                call.enqueue(new Callback<FruitModel>() {
                                    @Override
                                    public void onResponse(Call<FruitModel> call, Response<FruitModel> response) {
                                        if (response.isSuccessful()) {
                                            loadData();
                                            Toast.makeText(Home.this, "Sửa thành công no image", Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                        } else {
                                            Toast.makeText(Home.this, "Sửa thất bại no image", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    @Override
                                    public void onFailure(Call<FruitModel> call, Throwable t) {
                                        Log.e("Home", "Call failed: " + t.toString());
                                        Toast.makeText(Home.this, "Đã xảy ra lỗi khi sửa dữ liệu", Toast.LENGTH_SHORT).show();
                                    }

                                });
                            }
                        }
                    }else{
                        Toast.makeText(context, "Phải lớn hơn 0", Toast.LENGTH_SHORT).show();
                    }

                }catch (NumberFormatException e){
                    Toast.makeText(context, "Phải là số", Toast.LENGTH_SHORT).show();
                }

            }
        });

        dialog.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    public  void xoa(String id){
        AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
        builder.setTitle("Delete");
        builder.setMessage("Bạn có chắc chắn muốn xóa?");
        builder.setCancelable(true);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Call<Void> call = apiService.deleteCay(id);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            loadData();
                            Toast.makeText(Home.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Home.this, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.e("Home", "Call failed: " + t.toString());
                        Toast.makeText(Home.this, "Đã xảy ra lỗi khi xóa dữ liệu", Toast.LENGTH_SHORT).show();
                    }

                });
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        builder.show();

    }

    private void chooseImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        getImage.launch(intent);

    }

    ActivityResultLauncher<Intent> getImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult o) {
                    if (o.getResultCode() == Activity.RESULT_OK) {
                        Intent data = o.getData();
                        Uri imageUri = data.getData();

                        Log.d("RegisterActivity", imageUri.toString());

                        file = createFileFormUri(imageUri, "anh");

                        Glide.with(anh)
                                .load(imageUri)
                                .centerCrop()
                                .circleCrop()
                                .into(anh);
                    }
                }
            });

    private File createFileFormUri(Uri path, String name) {
        File _file = new File(Home.this.getCacheDir(), name + ".png");
        try {
            InputStream in = Home.this.getContentResolver().openInputStream(path);
            OutputStream out = new FileOutputStream(_file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
            in.close();
            return _file;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private RequestBody getRequestBody(String value) {
        return RequestBody.create(MediaType.parse("multipart/form-data"), value);
    }

    private void searchDistributor(String keyword) {
        Call<List<FruitModel>> call = apiService.searchCay(keyword);
        call.enqueue(new Callback<List<FruitModel>>() {
            @Override
            public void onResponse(Call<List<FruitModel>> call, Response<List<FruitModel>> response) {
                if (response.isSuccessful()) {
                    list = response.body();

                    adapter = new AdapterFruit(list,  getApplicationContext(), Home.this);

                    recyclerView.setLayoutManager(new LinearLayoutManager(Home.this));
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<FruitModel>> call, Throwable t) {
                Log.e("Search", "Search failed: " + t.toString());
                Toast.makeText(Home.this, "Đã xảy ra lỗi kh tìm kiếm", Toast.LENGTH_SHORT).show();
            }
        });
    }

}