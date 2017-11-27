package edu.cnm.deepdive.eb.flashme.model;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * A class that serves as the model for the result retrieved from querying Google's Custom Search
 * API. This class implements the Parcelable interface to simplify data access for the components of
 * this app.
 */
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

  /**
   * An Empty Constructor.
   */
  public GoogleItem() {
  }

  /**
   * Receives a parcel and saves its value in the link field.
   * @param in Passes a parcel.
   */
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
