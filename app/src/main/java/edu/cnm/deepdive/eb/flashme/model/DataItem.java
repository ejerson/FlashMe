package edu.cnm.deepdive.eb.flashme.model;

import android.os.Parcel;

public class DataItem implements android.os.Parcelable {

  private String link;

  public String getLink() {
    return link;
  }

  public void setLink(String link) {
    this.link = link;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.link);
  }

  public DataItem() {
  }

  protected DataItem(Parcel in) {
    this.link = in.readString();
  }

  public static final Creator<DataItem> CREATOR = new Creator<DataItem>() {
    @Override
    public DataItem createFromParcel(Parcel source) {
      return new DataItem(source);
    }

    @Override
    public DataItem[] newArray(int size) {
      return new DataItem[size];
    }
  };
}


