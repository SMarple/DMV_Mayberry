package smarple1mayberry.bo;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "smarple1may_poi")
public class POI {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(name="FIRST_NAME")
	private String firstName;
	
	@Column(name="MIDDLE_NAME")
	private String middleName;
	
	@Column(name="LAST_NAME")
	private String lastName;
	
	@Transient
	private Collection<POI> aliases;
	
	@OneToMany(mappedBy="poi",targetEntity=Activity.class,
		       fetch=FetchType.EAGER)
	private Set<Activity> activities;

	public long getId() {
		return id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Collection<POI> getAliases() {
		return aliases;
	}

	public void setAliases(Collection<POI> aliases) {
		this.aliases = aliases;
	}

	public Set<Activity> getActivities() {		
		if(activities == null){
			return new HashSet<Activity>();
		}
		else{
			return activities;
		}
	}
	
	public void addActivity(Activity act){
		if(activities == null){
			activities = new HashSet<Activity>();
			activities.add(act);
		}
		else{
			activities.add(act);
		}
	}
}