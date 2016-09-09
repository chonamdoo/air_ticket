package kr.ds.handler;

import android.os.Parcel;
import android.os.Parcelable;

import com.facebook.ads.NativeAd;

/**
 * Created by Administrator on 2016-08-05.
 */
public class ListHandler {
    public String name;
    public String image;
    public String title;
    public String link;
    public String start;
    public String end;
    private int isLayout;
    private NativeAd nativeAd;
    private boolean isNativeLayout;
    private String reservation_link;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public int getIsLayout() {
        return isLayout;
    }

    public void setIsLayout(int isLayout) {
        this.isLayout = isLayout;
    }

    public NativeAd getNativeAd() {
        return nativeAd;
    }

    public void setNativeAd(NativeAd nativeAd) {
        this.nativeAd = nativeAd;
    }

    public boolean isNativeLayout() {
        return isNativeLayout;
    }

    public void setNativeLayout(boolean nativeLayout) {
        isNativeLayout = nativeLayout;
    }

    public String getReservation_link() {
        return reservation_link;
    }

    public void setReservation_link(String reservation_link) {
        this.reservation_link = reservation_link;
    }
}
