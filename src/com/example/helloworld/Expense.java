package com.example.helloworld;

import java.util.Date;
import android.os.Parcel;
import android.os.Parcelable;


public class Expense implements Parcelable {
	private long id;
	private String type;
	private double cost;
	private Date date;
	private String description;
	
	public Expense() {
	}
	
	public String getDescription() {
		return description;
	}
	public void setLocation(String location) {
		this.description = location;
	}
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	public double getCost() {
		return cost;
	}
	public void setCost(double cost) {
		this.cost = cost;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	@Override
	public int describeContents() {
		return 0;
	}

	
	public static final Parcelable.Creator<Expense> CREATOR = new
			Parcelable.Creator<Expense>() {

				@Override
				public Expense createFromParcel(Parcel pc) {
					return new Expense(pc);
				}

				@Override
				public Expense[] newArray(int size) {
					return new Expense[size];
				}
			};
			
	private Expense(Parcel pc) {
		id = pc.readLong();
		description = pc.readString();
		cost = pc.readDouble();
		type = pc.readString();
		//date = pc.readString();
		//DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		date = new Date();
	}

	@Override
	public void writeToParcel(Parcel pc, int flags) {
		pc.writeLong(id);
		pc.writeString(description);		
		pc.writeDouble(cost);
		pc.writeString(type);
		pc.writeString(date.toString()); // this might be wrong..
	}
	
}
