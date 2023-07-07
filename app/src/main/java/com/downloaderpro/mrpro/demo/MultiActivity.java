package com.downloaderpro.mrpro.demo;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import com.itsaky.androidide.logsender.LogSender;
import java.util.ArrayList;
import java.util.List;

public class MultiActivity extends AppCompatActivity {
  private String downloadUrl, fileName, addDownload;
  private TabLayout tabLayout;
  private ViewPager viewPager;
  private ViewPagerDownloadsAdapter vPDAdapter;
  private Button requestPermissionBtn;
  private int STORAGE_PERMISSION_REQUEST_CODE = 1;
  private ActivityResultLauncher<Intent> permissionResultLauncher;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    // Remove this line if you don't want AndroidIDE to show this app's logs
    LogSender.startLogging(this);
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_multi);

    SaveDataApp.activityStarted("MultiActivity");

    tabLayout = findViewById(R.id.downloads_tab_layout);
    // set tab text color
    int customTextColor = ContextCompat.getColor(this, R.color.white_smoke);
    int selectedTextColor = ContextCompat.getColor(this, R.color.amber);
    tabLayout.setTabTextColors(customTextColor, selectedTextColor);

    viewPager = findViewById(R.id.downloads_view_pager);
    requestPermissionBtn = findViewById(R.id.request_permission_button);

    if (getIntent() != null) {
      Intent intent = getIntent();
      addDownload = intent.getStringExtra("addDownload");
      downloadUrl = intent.getStringExtra("downloadUrl");
      fileName =
          "." /*adding . for hiding file still not downloaded*/ + intent.getStringExtra("fileName");
    }

    checkOrRequestPermission();

    requestPermissionBtn.setOnClickListener(
        v -> {
          checkOrRequestPermission();
        });

    permissionResultLauncher =
        registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
              if (result.getResultCode() == Activity.RESULT_OK) {
                // Check if the user granted the storage permissions from the app settings
                if (ContextCompat.checkSelfPermission(
                            this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(
                            this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
                  // Storage permissions granted from app settings
                  setViewPager();
                } else {
                  // Storage permissions still not granted
                  // Handle the case when the user denies storage permission
                  Toast.makeText(this, "Storage Access Denied", Toast.LENGTH_LONG);
                  requestPermissionBtn.setVisibility(View.VISIBLE);
                }
              }
            });
  }

  private void checkOrRequestPermission() {
    requestPermissionBtn.setVisibility(View.GONE);
    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
      if (isPermissionGrantedBellowApi30()) {
        setViewPager();
      } else {
        requestStoragePermissionBellowApi30();
      }
    } else {
      requestStoragePermissionAboveApi29();
    }
  }

  private void setViewPager() {
    vPDAdapter =
        new ViewPagerDownloadsAdapter(
            getSupportFragmentManager(), addDownload, downloadUrl, fileName);
    viewPager.setAdapter(vPDAdapter);
    tabLayout.setupWithViewPager(viewPager);
  }

  private boolean isPermissionGrantedBellowApi30() {
    // Check if the permission is already granted
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
      return true; // Permissions are granted at install time on devices below API 23
    }
    int readPermissionResult =
        ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
    int writePermissionResult =
        ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    return readPermissionResult == PackageManager.PERMISSION_GRANTED
        && writePermissionResult == PackageManager.PERMISSION_GRANTED;
  }

  private void requestStoragePermissionBellowApi30() {
    ActivityCompat.requestPermissions(
        this,
        new String[] {
          Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
        },
        STORAGE_PERMISSION_REQUEST_CODE);
  }

  @Override
  public void onRequestPermissionsResult(
      int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    if (requestCode == STORAGE_PERMISSION_REQUEST_CODE) {
      if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
          // Storage permission granted
          setViewPager();
        } else {
          // Storage permission denied
          Toast.makeText(this, "Storage Access Denied", Toast.LENGTH_LONG);
          requestPermissionBtn.setVisibility(View.VISIBLE);
        }
      } else {
        boolean allPermissionsGranted = true;
        for (int result : grantResults) {
          if (result != PackageManager.PERMISSION_GRANTED) {
            allPermissionsGranted = false;
            break;
          }
        }

        if (allPermissionsGranted) {
          // Storage permissions granted
          setViewPager();
        } else {
          // Storage permissions denied
          boolean showRationale =
              ActivityCompat.shouldShowRequestPermissionRationale(
                      this, Manifest.permission.READ_EXTERNAL_STORAGE)
                  || ActivityCompat.shouldShowRequestPermissionRationale(
                      this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

          if (!showRationale) {
            // User denied the permissions and checked "Don't ask again"
            // Show a dialog explaining why the permissions are necessary and direct the user to the
            // app settings
            showPermissionExplanationDialog();
          } else {
            // User denied the permissions but didn't check "Don't ask again"
            // Handle the case when the user denies storage permission
            Toast.makeText(this, "Storage Access Denied", Toast.LENGTH_LONG);
            requestPermissionBtn.setVisibility(View.VISIBLE);
          }
        }
      }
    }
  }

  private void requestStoragePermissionAboveApi29() {
    List<String> permissionList = new ArrayList<>();

    if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        != PackageManager.PERMISSION_GRANTED) {
      permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        != PackageManager.PERMISSION_GRANTED) {
      permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    if (permissionList.isEmpty()) {
      // All permissions are granted, proceed with accessing storage
      setViewPager();
    } else {
      // Request the permissions
      String[] permissions = permissionList.toArray(new String[0]);
      ActivityCompat.requestPermissions(this, permissions, STORAGE_PERMISSION_REQUEST_CODE);
    }
  }

  private void showPermissionExplanationDialog() {
    // Show a dialog explaining why the storage permissions are necessary
    // Provide an option to open the app settings
    // If the user opens the app settings and grants the permissions, you can handle it in
    // onActivityResult()
    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
    Uri uri = Uri.fromParts("package", getPackageName(), null);
    intent.setData(uri);
    permissionResultLauncher.launch(intent);
  }

  @Override
  public void onBackPressed() {
    if (viewPager.getId() == 1) {
      tabLayout.setId(0);
    } else {
      finish();
    }
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    SaveDataApp.activityStopped();
  }
}
