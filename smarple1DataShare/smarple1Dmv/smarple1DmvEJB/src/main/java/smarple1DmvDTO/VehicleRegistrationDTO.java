package smarple1DmvDTO;

import java.io.Serializable;
import java.util.Date;

public class VehicleRegistrationDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private long id;
	private String tagNo;
	private String vin;
	private String make;
	private String model;
	private String color;
	private Date year;
	private Date expiration;
	
	public long getId() {
		return id;
	}
	public void setId(long id){
		this.id = id;
	}
	public String getTagNo() {
		return tagNo;
	}
	public void setTagNo(String tagNo) {
		this.tagNo = tagNo;
	}
	public String getVin() {
		return vin;
	}
	public void setVin(String vin) {
		this.vin = vin;
	}
	public String getMake() {
		return make;
	}
	public void setMake(String make) {
		this.make = make;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public Date getYear() {
		return year;
	}
	public void setYear(Date year) {
		this.year = year;
	}
	public Date getExpiration() {
		return expiration;
	}
	public void setExpiration(Date expiration) {
		this.expiration = expiration;
	}
	public String toString(){
		return "ID:" + this.id + ",Make:" + this.make + ",Model:" + this.model + ",TagNo:" + this.tagNo + ",Vin:" + this.vin + ",Year:" + this.year + ",Color:" + this.color + ",Expiration:" + this.expiration;
	}
}
