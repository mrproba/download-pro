package com.downloaderpro.mrpro.demo;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.downloaderpro.mrpro.demo.fragments.DownloadedsFragment;
import com.downloaderpro.mrpro.demo.fragments.DownloadingsFragment;

public class ViewPagerDownloadsAdapter extends FragmentPagerAdapter {
  private String downloadUrl;
  private String downloadFileName;
  private String startDownloading;

  public ViewPagerDownloadsAdapter(
      @NonNull FragmentManager fm, String startDownloading, String dUrl, String dFName) {
    super(fm);
    this.startDownloading = startDownloading;
    this.downloadUrl = dUrl;
    this.downloadFileName = dFName;
  }

  @Override
  public int getCount() {
    return 2;
  }

  @Override
  public Fragment getItem(int position) {
    if (position == 0) {
      return new DownloadingsFragment(startDownloading, downloadUrl, downloadFileName);
    } else {
      return new DownloadedsFragment();
    }
  }

  @Override
  public java.lang.CharSequence getPageTitle(int position) {
    if (position == 0) {
      return "Downloadings";
    } else {
      return "Downloadeds";
    }
  }
}
