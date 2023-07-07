package com.downloaderpro.mrpro.demo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.itsaky.androidide.logsender.LogSender;

public class MainActivity extends AppCompatActivity {
  private Button addDownloadBtn, checkDownloadsBtn;
  private EditText enterLinkETxt, enterFileNameETxt;
  private String downloadUrl, fileName;
  private boolean isMultiActivityAlreadyOpened = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    // Remove this line if you don't want AndroidIDE to show this app's logs
    LogSender.startLogging(this);
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    addDownloadBtn = findViewById(R.id.add_download_btn);
    checkDownloadsBtn = findViewById(R.id.check_downloads_btn);
    enterLinkETxt = findViewById(R.id.enterLinkETxt);
    enterFileNameETxt = findViewById(R.id.enterNameETxt);

    addDownloadBtn.setOnClickListener(
        v -> {
          fileName = enterFileNameETxt.getText().toString();
          downloadUrl = enterLinkETxt.getText().toString();

          Intent intent = new Intent(MainActivity.this, MultiActivity.class);
          intent.putExtra("addDownload", "yes");
          intent.putExtra("downloadUrl", downloadUrl);
          intent.putExtra("fileName", fileName + ".mp4");
          startActivity(intent);
        });

    checkDownloadsBtn.setOnClickListener(
        v -> {
          Intent intent = new Intent(MainActivity.this, MultiActivity.class);
          intent.putExtra("addDownload", "no");
          startActivity(intent);
        });
  }


  @Override
  public void onBackPressed() {
    finishAffinity(); // Cloasing all activities to exit app
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
  }
}
