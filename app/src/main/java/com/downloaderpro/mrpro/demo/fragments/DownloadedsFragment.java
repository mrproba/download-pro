package com.downloaderpro.mrpro.demo.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.CallSuper;
import androidx.annotation.MainThread;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.downloaderpro.mrpro.demo.DownloadedsVideoAdapter;
import com.downloaderpro.mrpro.demo.DownloadedsVideoItem;
import com.downloaderpro.mrpro.demo.FileDir;
import com.downloaderpro.mrpro.demo.R;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class DownloadedsFragment extends Fragment {
  private View view;

  private RecyclerView recyclerView;
  private DownloadedsVideoAdapter downloadedsVideoAdapter;
  private List<DownloadedsVideoItem> dVIList;
  private DownloadedsVideoAdapter adapter;

  public DownloadedsFragment() {
    // Default Constructor
  }

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // TODO: Implement this method
    view = inflater.inflate(R.layout.fragment_downloadeds, container, false);
    EventBus.getDefault().register(this);

    dVIList = new ArrayList<>();

    recyclerView = view.findViewById(R.id.downloadeds_recycler_view);
    getVideos();

    adapter = new DownloadedsVideoAdapter(view.getContext(), dVIList);
    LinearLayoutManager lManager =
        new LinearLayoutManager(view.getContext(), LinearLayout.VERTICAL, false);
    lManager.setReverseLayout(true);
    lManager.setStackFromEnd(true);
    recyclerView.setLayoutManager(lManager);
    recyclerView.setAdapter(adapter);
    adapter.setOnItemClickListener(
        (view, path1) -> {
          Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(path1));
          intent.setDataAndType(Uri.parse(path1), "video/*");
          startActivity(intent);
        });

    return view;
  }

  private void getVideos() {
    dVIList.clear();
    File file = new File(FileDir.getFileDir().getAbsolutePath());

    File[] files = file.listFiles();
    if (files != null) {
      for (File file1 : files) {
        if (!file1.getName().startsWith(".")) {
          if (file1.getAbsolutePath().endsWith(".mp4")
              || file1.getAbsolutePath().endsWith(".mkv")
              || file1.getAbsolutePath().endsWith(".webm")) {
            dVIList.add(new DownloadedsVideoItem(file1.getPath()));
          }
        }
      }
    }
  }

  @Subscribe(threadMode = ThreadMode.MAIN)
  public void onDataEvent(DownloadedsVideoItem event) {
    String path = event.getPath();
    DownloadedsVideoItem dVI = new DownloadedsVideoItem(path);
    dVIList.add(dVI);
    adapter.addNewVideo(dVI);
  }

  @Override
  @MainThread
  @CallSuper
  public void onDestroyView() {
    super.onDestroyView();
    EventBus.getDefault().unregister(this);
  }
}
