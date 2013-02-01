package com.james.skiller.model;

import java.util.Collections;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class MegaListTaskRow extends MegaListRow implements Parcelable {
	public static final Parcelable.Creator<MegaListTaskRow> CREATOR = new Parcelable.Creator<MegaListTaskRow>() {
		public MegaListTaskRow createFromParcel(Parcel in) {
			return new MegaListTaskRow(in);
		}

		public MegaListTaskRow[] newArray(int size) {
			return new MegaListTaskRow[size];
		}
	};

	private boolean status;

	public MegaListTaskRow(int id, String name, boolean status) {
		super(id, name);
		this.status = status;
	}

	public MegaListTaskRow(Parcel in) {
		super(in);
		status = (Boolean) in.readValue(this.getClass().getClassLoader());
	}

	public MegaListRow createFromParcel(Parcel source) {
		source.readValue(this.getClass().getClassLoader());
		return null;
	}

	public int describeContents() {
		return 0;
	}

	@Override
	public List<MegaListRow> getChildren() {
		return Collections.emptyList();
	}

	public boolean getStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public void toggle_status() {
		status = !status;
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(getId());
		dest.writeString(getName());
		dest.writeValue(status);
	}
}