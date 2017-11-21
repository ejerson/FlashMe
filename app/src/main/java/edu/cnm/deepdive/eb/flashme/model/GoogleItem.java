package edu.cnm.deepdive.eb.flashme.model;


import android.os.Parcel;
import android.os.Parcelable;

public class GoogleItem implements Parcelable {

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

  public GoogleItem() {
  }

  protected GoogleItem(Parcel in) {
    this.link = in.readString();
  }

  public static final Creator<GoogleItem> CREATOR = new Creator<GoogleItem>() {
    @Override
    public GoogleItem createFromParcel(Parcel source) {
      return new GoogleItem(source);
    }

    @Override
    public GoogleItem[] newArray(int size) {
      return new GoogleItem[size];
    }
  };
}
