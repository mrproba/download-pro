package com.downloaderpro.mrpro.demo.fragments;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.CallSuper;
import androidx.annotation.MainThread;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.downloaderpro.mrpro.DownloaderPro;
import com.downloaderpro.mrpro.db.DBHelper;
import com.downloaderpro.mrpro.demo.FileDir;
import com.downloaderpro.mrpro.demo.db.DownloadRoomHelper;
import com.downloaderpro.mrpro.demo.downloadingservice.SendClassesToService;
import com.downloaderpro.mrpro.demo.multi.DownloadingAdapter;
import com.downloaderpro.mrpro.demo.db.DownloadsRoom;
import com.downloaderpro.mrpro.demo.R;
import com.downloaderpro.mrpro.demo.downloadingservice.DownloadingService;
import com.downloaderpro.mrpro.demo.multi.DataEvent;
import com.downloaderpro.mrpro.demo.multi.DownloadingListeners;
import java.io.File;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class DownloadingsFragment extends Fragment {
  private View view;
  private RecyclerView recyclerView;
  private String downloadUrl;
  private String fileName;
  private String addDownload;
  private DownloadsRoom downloadsRoom;
  private DownloadRoomHelper dRHelper;
  private List<DownloadsRoom> dRList;
  private DownloadingAdapter adapter;
  private boolean isAllPaused = true;
  private boolean isServiceRunning = false;
  private List<DownloaderPro> downloaderProList;
  private List<DownloadingListeners> downloadingListenersList;

  public DownloadingsFragment(String addDownload, String dUrl, String dFName) {
    // Fragment Constructor
    this.downloadUrl = dUrl;
    this.fileName = dFName;
    this.addDownload = addDownload;
  }

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    view = inflater.inflate(R.layout.fragment_downloadings, container, false);
    EventBus.getDefault().register(this);

    recyclerView = view.findViewById(R.id.downloading_recycler_view);

    dRHelper = DownloadRoomHelper.getDB(view.getContext());
    dRList = dRHelper.DownloadsRoomDao().getAllDownloadsRoom();

    if (dRList.size() != 0) {
      for (DownloadsRoom room : dRList) {
        File isFile =
            new File(FileDir.getFileDir(), room.getDownloadFileName().replaceAll(" ", "_"));
        if (!isFile.exists() || !isFile.isFile()) {
          dRHelper.DownloadsRoomDao().deleteDownload(room);
        } else {
          if (room.getTotalBytes() == 0) {
            DBHelper.getDB(view.getContext()).DBDao().deleteByUserId(room.getId());
            dRHelper.DownloadsRoomDao().deleteDownload(room);
          } else if (!room.getIsPaused()) {
            isAllPaused = false;
          }
        }
      }
    }
    dRList = dRHelper.DownloadsRoomDao().getAllDownloadsRoom();

    if (addDownload != null && addDownload.equalsIgnoreCase("yes")) {
      dRHelper.DownloadsRoomDao().addDownload(new DownloadsRoom(downloadUrl, fileName));
      downloadsRoom = dRHelper.DownloadsRoomDao().getRecentDownload();
      dRList.add(downloadsRoom);
      isAllPaused = false;
    }

    adapter = new DownloadingAdapter(view.getContext(), dRHelper, dRList);
    if (!checkServiceRunning(DownloadingService.class)) {
      if (!isAllPaused) {
        Intent serviceIntent = new Intent(view.getContext(), DownloadingService.class);
        view.getContext().startService(serviceIntent);
      }
    } else {
      isServiceRunning = true;
    }

    if (isServiceRunning) {
      saveOrGetAdapter(true);
      checkAdapterRecived();
    } else {
      setRecyclerView();
    }

    return view;
  }

  private void checkAdapterRecived() {
    new Handler(Looper.getMainLooper())
        .postDelayed(
            new Runnable() {
              @Override
              public void run() {
                if (downloaderProList == null) {
                  saveOrGetAdapter(true);
                  checkAdapterRecived();
                }
              }
            },
            200);
  }

  private void setRecyclerView() {
    recyclerView.setAdapter(adapter);
    LinearLayoutManager lManager =
        new LinearLayoutManager(view.getContext(), LinearLayout.VERTICAL, false);
    lManager.setReverseLayout(true);
    lManager.setStackFromEnd(true);
    recyclerView.setLayoutManager(lManager);
  }

  private boolean checkServiceRunning(Class<?> serviceClass) {
    ActivityManager manager =
        (ActivityManager) view.getContext().getSystemService(Context.ACTIVITY_SERVICE);
    for (ActivityManager.RunningServiceInfo service :
        manager.getRunningServices(Integer.MAX_VALUE)) {
      if (serviceClass.getName().equals(service.service.getClassName())) {
        return true;
      }
    }
    return false;
  }

  private void saveOrGetAdapter(boolean isAlreadyCreated) {
    SendClassesToService event = new SendClassesToService();
    event.setIsAlreadyCreated(isAlreadyCreated);
    EventBus.getDefault().post(event);
  }

  @Subscribe(threadMode = ThreadMode.MAIN)
  public void onDataEvent(DataEvent event) {
    if (event.getDownloaderPro() != null) {
      downloaderProList = event.getDownloaderPro();
      downloadingListenersList = event.getDownloadingListeners();
      adapter.setDownloaderPro(downloaderProList);
      adapter.setDownloadingListeners(downloadingListenersList);
      setRecyclerView();
    }
  }

  @Override
  @MainThread
  @CallSuper
  public void onDestroyView() {
    super.onDestroyView();
    EventBus.getDefault().unregister(this);
  }
}
