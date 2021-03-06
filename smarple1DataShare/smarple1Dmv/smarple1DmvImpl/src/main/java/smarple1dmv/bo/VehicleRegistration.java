package smarple1dmv.bo;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "smarple1dmv_vehicle_registration")
public class VehicleRegistration {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(name = "TAG_NO")
	private String tagNo;
	
	@Column(name = "VIN")
	private String vin;
	
	@Column(name = "MAKE")
	private String make;
	
	@Column(name = "MODEL")
	private String model;
	
	@Column(name = "COLOR")
	private COLOR color;
	
	@Column(name = "YEAR")
	private Date year;
	
	@Column(name = "EXPIRATION")
	private Date expiration;
	
	@ManyToMany(mappedBy="registrations")
	private Set<Person> owners;
	
	public enum COLOR {red, blue, yellow, green, black};
	
	public VehicleRegistration(){}

	public VehicleRegistration(Person owner){
		this.getOwners().add(owner);
	}
	
	public long getId() {
		return id;
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
	
	public COLOR getColor() {
		return color;
	}
	
	public void setColor(COLOR color) {
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
	
	public Set<Person> getOwners() {
		if(owners == null){ owners = new HashSet<Person>();}
		return owners;
	}
	
	public void addOwner(Person newOwner) {
		if(owners == null){ owners = new HashSet<Person>();}
		owners.add(newOwner);
	}
}
