// Generated by view binder compiler. Do not edit!
package com.downloaderpro.mrpro.demo.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.downloaderpro.mrpro.demo.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentDownloadedsBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final RecyclerView downloadedsRecyclerView;

  private FragmentDownloadedsBinding(@NonNull LinearLayout rootView,
      @NonNull RecyclerView downloadedsRecyclerView) {
    this.rootView = rootView;
    this.downloadedsRecyclerView = downloadedsRecyclerView;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentDownloadedsBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentDownloadedsBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_downloadeds, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentDownloadedsBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.downloadeds_recycler_view;
      RecyclerView downloadedsRecyclerView = ViewBindings.findChildViewById(rootView, id);
      if (downloadedsRecyclerView == null) {
        break missingId;
      }

      return new FragmentDownloadedsBinding((LinearLayout) rootView, downloadedsRecyclerView);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}