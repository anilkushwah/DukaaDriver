package com.dollop.dukaadriver.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dollop.dukaadriver.R;
import com.dollop.dukaadriver.UtilityTools.MarshMallowPermission;
import com.dollop.dukaadriver.UtilityTools.NetworkUtil;
import com.dollop.dukaadriver.UtilityTools.SessionManager;
import com.dollop.dukaadriver.UtilityTools.Utility;
import com.dollop.dukaadriver.UtilityTools.Utils;
import com.dollop.dukaadriver.UtilityTools.multipart.AppHelper;
import com.dollop.dukaadriver.UtilityTools.multipart.VolleyMultipartRequest;
import com.dollop.dukaadriver.UtilityTools.multipart.VolleySingleton;
import com.dollop.dukaadriver.model.AllResponse;
import com.dollop.dukaadriver.model.ItemModel;
import com.dollop.dukaadriver.model.OrderDTO;
import com.dollop.dukaadriver.model.VehicalDTO;
import com.dollop.dukaadriver.retrofit.ApiClient;
import com.dollop.dukaadriver.retrofit.ApiInterface;
import com.simplify.ink.InkView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

import static com.dollop.dukaadriver.UtilityTools.multipart.AppHelper.getFileDataFromDrawable;


public class DeliveryPrefrences extends AppCompatActivity implements View.OnClickListener {
    RelativeLayout signature_view_RL;
    ImageView final_image, delivery_image;
    Button upload_proof_and_order;
    //   private SignaturePad mSignaturePad;
    SessionManager sessionManager;
    LinearLayout image_LL;

    public static final int GALLERY = 1;
    protected static final int CAMERA_REQUEST = 2;
    Uri insuranceUri;
    Bitmap insuranceBitmap;
    boolean insuranceCamera;

    InkView ink;
    Bitmap bitmap = null;
    TextView item_tv, total_amount_tv, trash_sign, tv_distributor;
    OrderDTO mOrderDTO;
    ScrollView scrollViewId;

    ArrayList<VehicalDTO> mVehicalDTOS;
    RecyclerView rv_ItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_delivery_prefrences);


        mOrderDTO = (OrderDTO) getIntent().getSerializableExtra("model");

        sessionManager = new SessionManager(this);


        signature_view_RL = findViewById(R.id.signature_view_RL);
        rv_ItemList = findViewById(R.id.rv_ItemList);
        tv_distributor = findViewById(R.id.tv_distributor);

        upload_proof_and_order = findViewById(R.id.upload_proof_and_order);
        trash_sign = findViewById(R.id.trash_sign);
        ink = findViewById(R.id.ink);
        ink.setFlags(InkView.FLAG_INTERPOLATION | InkView.FLAG_RESPONSIVE_WIDTH);
        ink.setColor(getResources().getColor(android.R.color.black));
        ink.setMinStrokeWidth(1.5f);
        ink.setMaxStrokeWidth(6f);
        final_image = findViewById(R.id.final_image);
        image_LL = findViewById(R.id.image_LL);
        delivery_image = findViewById(R.id.delivery_image);
        item_tv = findViewById(R.id.item_tv);
        total_amount_tv = findViewById(R.id.total_amount_tv);

        upload_proof_and_order.setOnClickListener(this);
        trash_sign.setOnClickListener(this);
        image_LL.setOnClickListener(this);

        item_tv.setText("" + mOrderDTO.getItemCount() + " Items");
        total_amount_tv.setText("" + mOrderDTO.getTotalAmount());


        scrollViewId = findViewById(R.id.scrollViewId);
        ink.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disable the scroll view to intercept the touch event
                        scrollViewId.requestDisallowInterceptTouchEvent(true);
                        return false;
                    case MotionEvent.ACTION_UP:
                        // Allow scroll View to interceot the touch event
                        scrollViewId.requestDisallowInterceptTouchEvent(false);
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        scrollViewId.requestDisallowInterceptTouchEvent(true);
                        return false;
                    default:
                        return true;
                }
            }
        });

        GetItemListTask();


    }


    @Override
    public void onClick(View v) {
        if (v == upload_proof_and_order) {
            if (ink.getBitmap() != null) {
                bitmap = ink.getBitmap(getResources().getColor(R.color.white));
                //bitmap = ink.getBitmap();
                final_image.setImageBitmap(bitmap);
            }
            if (delivery_image.getDrawable() != null && !ink.isViewEmpty()) {
                if (NetworkUtil.isNetworkAvailable(DeliveryPrefrences.this)) {
                    deliverOrder();
                } else {
                    Utility.netConnect(DeliveryPrefrences.this);
                }
            } else {
                ShowDialog("Click the picture of the delivered order and take the sign of the retailer to make delivery confirm");
            }

            //
        } else if (v == trash_sign) {
            ink.clear();
        } else if (v == image_LL) {
            MarshMallowPermission marshMallowPermission = new MarshMallowPermission(DeliveryPrefrences.this);

            if (marshMallowPermission.checkPermissionForCamera()) {
                openGallery();
            } else {
                marshMallowPermission.requestPermissionForCamera();
            }

        }
    }


    public void deliverOrder() {
        final Dialog dialog = Utils.initProgressDialog(DeliveryPrefrences.this);
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, ApiClient.BASE_URL + "delivered_order", new com.android.volley.Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                dialog.dismiss();
                String resultResponse = new String(response.data);

                Utils.E("resultResponse::" + resultResponse);

                try {
                    JSONObject result = new JSONObject(resultResponse);
                    Utils.E("SignUp::" + result);
                    int status = result.getInt("status");
                    String msg = result.getString("message");

                    if (status == 200) {

                        startActivity(new Intent(DeliveryPrefrences.this, HomeActivity.class));
                        finish();

                    } else {

                        ShowDialog(msg);

                    }
                } catch (JSONException e) {
                    Utils.E("UpdateUserProfileJSONException::" + e);
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;

                String errorMessage = "Unknown error";
                if (networkResponse == null) {
                    if (error.getClass().equals(TimeoutError.class)) {
                        errorMessage = "Request timeout";
                        Utils.T(getApplicationContext(), "Request timeout please check Internet connection");
                    } else if (error.getClass().equals(NoConnectionError.class)) {
                        errorMessage = "Failed to connect server";
                    }
                } else {
                    String result = new String(networkResponse.data);
                    try {
                        JSONObject response = new JSONObject(result);
                        //   response.getJSONArray(result);
                        String status = response.getString("status");
                        String message = response.getString("message");

                        Log.e("Error Status", status);
                        Log.e("Error Message", message);

                        if (networkResponse.statusCode == 404) {
                            errorMessage = "Resource not found";
                        } else if (networkResponse.statusCode == 401) {
                            errorMessage = message + " Please login again";
                        } else if (networkResponse.statusCode == 400) {
                            errorMessage = message + " Check your inputs";
                        } else if (networkResponse.statusCode == 500) {
                            errorMessage = message + " Something is getting wrong";
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Log.i("Error", errorMessage);
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> hm = new HashMap<>();

                hm.put("order_id", mOrderDTO.getId());
                hm.put("driver_id", sessionManager.getRegisterUser().getId());

                Utils.E("SIGNUPIMAGEINFO" + hm);
                return hm;

            }

            @Override
            protected Map<String, DataPart> getByteData() throws IOException {
                Map<String, DataPart> params = new HashMap<>();


                if (delivery_image.getDrawable() != null) {
                    params.put("proof_delivery", new DataPart(System.currentTimeMillis() + ".png",
                            getFileDataFromDrawable(getApplicationContext(),
                                    delivery_image.getDrawable()), "image/png"));
                }

                if (final_image.getDrawable() != null) {

                    params.put("signature_img", new DataPart(System.currentTimeMillis() + ".png",
                            getFileDataFromDrawable(getApplicationContext(),
                                    final_image.getDrawable()), "image/png"));
                }

                Utils.E("params>>" + params);
                return params;

            }


        };
        multipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(multipartRequest);
    }


    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == GALLERY) {
            insuranceUri = data.getData();
            delivery_image.setVisibility(View.VISIBLE);
            delivery_image.setImageURI(insuranceUri);
            insuranceCamera = false;
        } else if (resultCode == RESULT_OK && requestCode == CAMERA_REQUEST) {
            insuranceBitmap = (Bitmap) data.getExtras().get("data");
            delivery_image.setImageBitmap(insuranceBitmap);
            delivery_image.setVisibility(View.VISIBLE);
            insuranceCamera = true;
        }
    }

    private void openGallery() {


        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(DeliveryPrefrences.this);
        builder.setTitle("Choose your profile picture");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, CAMERA_REQUEST);

                } else if (options[item].equals("Choose from Gallery")) {
                    Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                    startActivityForResult(gallery, GALLERY);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();

    }

    private void ShowDialog(String sms) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage(sms)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Utils.I(this, AmountDoneNotificationActivity.class, null);
                        dialog.dismiss();
                    }
                }).show();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Utils.I_clear(DeliveryPrefrences.this, HomeActivity.class, null);
    }


    private void GetItemListTask() {

        StringRequest stringRequest = new StringRequest(1, ApiClient.order_history, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    Utils.E("response::" + response);
                    if (object.getInt("status") == 200) {
                        JSONArray data = object.getJSONArray("data");
                        for (int a = 0; a < data.length(); a++) {
                            JSONObject jsonObject = data.getJSONObject(a);
                            tv_distributor.setText(jsonObject.getString("shop_name"));
                            for (int i = 0; i < jsonObject.getJSONArray("products").length(); i++) {
                                JSONObject Item = jsonObject.getJSONArray("products").getJSONObject(i);

                                ItemModel itemModel = new ItemModel();
                                itemModel.id = Item.getString("id");
                                itemModel.retailer_id = Item.getString("retailer_id");
                                itemModel.order_id = Item.getString("order_id");
                                itemModel.product_id = Item.getString("product_id");
                                itemModel.offer_id = Item.getString("offer_id");
                                itemModel.product_qty = Item.getString("product_qty");
                                itemModel.product_amount = Item.getString("product_amount");
                                itemModel.total_amount = Item.getString("total_amount");
                                itemModel.discount_amount = Item.getString("discount_amount");
                                itemModel.product_discounted_price = Item.getString("product_discounted_price");
                                itemModel.create_date = Item.getString("create_date");
                                itemModel.distributor_id = Item.getString("distributor_id");
                                itemModel.category_id = Item.getString("category_id");
                                itemModel.sub_category_id = Item.getString("sub_category_id");
                                itemModel.brand = Item.getString("brand");
                                itemModel.product_name = Item.getString("product_name");
                                itemModel.packing_id = Item.getString("packing_id");
                                itemModel.unit_per_packing_weight = Item.getString("unit_per_packing_weight");
                                itemModel.total_unit = Item.getString("total_unit");
                                itemModel.total_weight = Item.getString("total_weight");
                                itemModel.unit = Item.getString("unit");
                                itemModel.discount = Item.getString("discount");
                                itemModel.original_price = Item.getString("original_price");
                                itemModel.selling_price = Item.getString("selling_price");
                                itemModel.item_code = Item.getString("item_code");
                                itemModel.product_image = Item.getString("product_image");
                                itemModel.product_availability = Item.getString("product_availability");
                                itemModel.stock_quantity = Item.getString("stock_quantity");
                                itemModel.is_tax = Item.getString("is_tax");
                                itemModel.tax_id = Item.getString("tax_id");
                                itemModel.description = Item.getString("description");
                                itemModel.is_active = Item.getString("is_active");
                                itemModel.is_delete = Item.getString("is_delete");
                                itemModel.packing = Item.getString("packing");
                                itemModel.tax_name = Item.getString("tax_name");
                                itemModels.add(itemModel);

                            }

                        }
                        ItemAdapter itemAdapter = new ItemAdapter(DeliveryPrefrences.this, itemModels);
                        rv_ItemList.setLayoutManager(new LinearLayoutManager(DeliveryPrefrences.this));
                        rv_ItemList.setAdapter(itemAdapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("order_id", mOrderDTO.getId());
                hashMap.put("driver_id", sessionManager.getRegisterUser().getId());
                return hashMap;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    ArrayList<ItemModel> itemModels = new ArrayList<>();

    public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

        ArrayList<ItemModel> itemModels;
        Context context;

        public ItemAdapter(Context context, ArrayList<ItemModel> itemModels) {
            this.context = context;
            this.itemModels = itemModels;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.order_item_list, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            ItemModel itemModel = itemModels.get(position);
            int Position = position + 1;
            holder.item_count_tv.setText(Position + ") ");
            holder.item_name_id.setText(itemModel.product_name);
            int Amount = Integer.parseInt(itemModel.product_amount) * Integer.parseInt(itemModel.product_qty);
            holder.item_price_tv.setText(context.getString(R.string.currency_sign) + Amount);
            holder.item_description_tv.setText(itemModel.packing + " Quantity : " + itemModel.product_qty);
            if (position == itemModels.size() - 1) {
                holder.viewLine.setVisibility(View.GONE);
            } else {
                holder.viewLine.setVisibility(View.VISIBLE);
            }

        }

        @Override
        public int getItemCount() {
            return itemModels.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView item_count_tv;
            TextView item_name_id;
            TextView item_description_tv;
            TextView item_price_tv;
            View viewLine;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                viewLine = itemView.findViewById(R.id.viewLine);
                item_count_tv = itemView.findViewById(R.id.item_count_tv);
                item_description_tv = itemView.findViewById(R.id.item_description_tv);
                item_name_id = itemView.findViewById(R.id.item_name_id);
                item_price_tv = itemView.findViewById(R.id.item_price_tv);
            }
        }
    }

}