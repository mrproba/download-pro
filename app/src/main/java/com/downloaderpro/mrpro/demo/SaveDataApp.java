package com.downloaderpro.mrpro.demo;

import android.app.Application;

public class SaveDataApp extends Application {
  private static String activityName;

  public static void activityStarted(String name) {
    activityName = name;
  }

  public static void activityStopped() {
    activityName = null;
  }

  public static String getActivityName() {
    return activityName;
  }
}
