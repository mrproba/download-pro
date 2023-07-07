package com.downloaderpro.mrpro.demo.downloadingservice;

public class ServiceCreatedBus {
  private boolean isCreated;

  public ServiceCreatedBus(boolean isCreated) {
    this.isCreated = isCreated;
  }

  public boolean getIsCreated() {
    return this.isCreated;
  }
}
