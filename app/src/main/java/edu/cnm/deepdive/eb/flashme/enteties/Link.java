package edu.cnm.deepdive.eb.flashme.enteties;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Link {

  @JsonProperty("href")
  private String href;

  public String getHref() {
    return href;
  }

  public void setHref(String href) {
    this.href = href;
  }
}
