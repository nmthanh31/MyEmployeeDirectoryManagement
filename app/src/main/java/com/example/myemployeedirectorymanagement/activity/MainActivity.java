package com.example.myemployeedirectorymanagement.activity;

import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.myemployeedirectorymanagement.R;
import com.example.myemployeedirectorymanagement.fragment.DepartmentFragment;
import com.example.myemployeedirectorymanagement.fragment.EmployeeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private FrameLayout flFragment;

    private BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        flFragment = findViewById(R.id.flFragment);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        replaceFragment(new DepartmentFragment());


        bottomNavigationView.setOnItemSelectedListener(item ->{
            if (item.getItemId() == R.id.menuDepartment){
                replaceFragment(new DepartmentFragment());
                return true;
            }
            if (item.getItemId() == R.id.menuEmployee){
                replaceFragment(new EmployeeFragment());
                return true;
            }
            return false;
        });

    }
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.flFragment, fragment);
        fragmentTransaction.commit();
    }
}