package com.dollop.dukaadriver;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

public class AcceptOrderDriverActivity extends AppCompatActivity {
    ImageView iv_more_menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_order_driver);
        iv_more_menu = findViewById(R.id.iv_more_menu);

        iv_more_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(AcceptOrderDriverActivity.this,v);
                popupMenu.getMenuInflater().inflate(R.menu.order_accept, popupMenu.getMenu());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    popupMenu.setGravity(Gravity.CENTER);
                }
                popupMenu.getMenu().getItem(1).setIcon(R.drawable.backeryy);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        Toast.makeText(AcceptOrderDriverActivity.this,"You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });

                popupMenu.show();

            }
        });
    }


}
