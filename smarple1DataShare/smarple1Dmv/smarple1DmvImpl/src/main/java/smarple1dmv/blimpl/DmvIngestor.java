package smarple1dmv.blimpl;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import smarple1dmv.bo.Person;
import smarple1dmv.bo.VehicleRegistration;
import smarple1dmv.dao.PersonDAO;
import smarple1dmv.dao.VehicleDAO;
import smarple1dmv.dao.DAOException;

import ejava.projects.edmv.xml.EDmvParser;

import info.ejava.projects.edmv._1.Dmv;

/**
 * This class is a modified parser from the class example. It ingests the
 * information from the XML file to populate the database.
 *
 */
public class DmvIngestor {
	private static final Log log = LogFactory.getLog(DmvIngestor.class);
	InputStream is;
	PersonDAO personDAO;
	VehicleDAO vehicleDAO;
	EDmvParser parser;
	Map<String, Person> dto2bo = new HashMap<String, Person>();

	public void setInputStream(InputStream is) {
		this.is = is;
	}

	public void setPersonDAO(PersonDAO personDAO) {
		this.personDAO = personDAO;
	}

	public void setVehicleDAO(VehicleDAO vehicleDAO) {
		this.vehicleDAO = vehicleDAO;
	}

	/**
	 * This method will ingest the input data by reading in external DTOs in
	 * from the parser, instantiating project business objects, and inserting
	 * into database. Note that the XML Schema is organized such that object
	 * references are fully resolved. Therefore, there is no specific need to
	 * process the addresses as they come in. They can be stored once we get the
	 * accounts they are related to.
	 * 
	 * @throws JAXBException
	 * @throws XMLStreamException
	 * @throws DAOException
	 */
	public void ingest() throws JAXBException, XMLStreamException, Exception {
		log.debug("Ingest started");
		EDmvParser parser = new EDmvParser(Dmv.class, is);

		Object object = parser.getObject("Person", "VehicleRegistration");
		while (object != null) {
			if (object instanceof gov.ojp.it.jxdm._3_0.Person) {
				createPerson((gov.ojp.it.jxdm._3_0.Person) object);
			}
			if (object instanceof gov.ojp.it.jxdm._3_0.VehicleRegistration) {
				createRegistration((gov.ojp.it.jxdm._3_0.VehicleRegistration) object);
			}
			object = parser.getObject("Person", "VehicleRegistration");
		}
		log.debug("Ingest ended");
	}

	/**
	 * This method is called by the main ingest processing loop. The JAXB/StAX
	 * parser will already have the Person populated with internal property
	 * information.
	 * 
	 * @param personDTO
	 * @throws DAOException
	 */
	private void createPerson(gov.ojp.it.jxdm._3_0.Person personDTO)
			throws DAOException {
		Person personBO = new Person();
		personBO.setLastName(personDTO.getPersonName().getPersonSurName()
				.getValue());
		personBO.setFirstName(personDTO.getPersonName().getPersonGivenName()
				.getValue());
		personBO.setMiddleName(personDTO.getPersonName().getPersonMiddleName()
				.getValue());
		personBO.setNameSuffix(personDTO.getPersonName().getPersonSuffixName()
				.getValue());

		personDAO.createPerson(personBO);
		log.debug("created person:" + personBO);

		// map DTO id to BO object for follow-on ownership processing
		dto2bo.put(personDTO.getId(), personBO);
	}

	/**
	 * This method is called by the main ingest processing loop. The JAXB/StAX
	 * parser will already have the VehicleRegistration populated with internal
	 * property information. The Person information for owners should also
	 * already exist.
	 * 
	 * @param registrationDTO
	 * @throws DAOException
	 */
	private void createRegistration(
			gov.ojp.it.jxdm._3_0.VehicleRegistration registrationDTO)
			throws Exception {
		VehicleRegistration registrationBO = new VehicleRegistration();
		registrationBO.setVin(registrationDTO.getVehicle().getVehicleID()
				.getID().getValue());
		registrationBO.setColor(registrationDTO.getVehicle().getVehicleColorPrimaryCode().getValue());
		registrationBO.setMake(registrationDTO.getVehicle().getVehicleMakeCode().getValue());
		registrationBO.setModel(registrationDTO.getVehicle().getVehicleModelCode().getValue());
		//not sure how to map the rest...
		//registrationBO.setYear(registrationDTO.getVehicle().getVehicleModelYearDate().getValue().toString());
		//registrationBO.setTagNo(registrationDTO.getVehicleLicensePlateID().getID().getValue());

		// add the m-m owners
		for (gov.ojp.it.jxdm._3_0.ReferenceType ref : registrationDTO
				.getVehicle().getPropertyOwnerPerson()) {
			gov.ojp.it.jxdm._3_0.Person ownerDTO = (gov.ojp.it.jxdm._3_0.Person) ref
					.getRef();
			Person ownerBO = dto2bo.get(ownerDTO.getId());
			if (ownerBO == null) {
				throw new DmvException("owner not found:" + ownerDTO.getId());
			}

			registrationBO.getOwners().add(ownerBO);
		}

		vehicleDAO.createRegistration(registrationBO);
		log.debug("created registration:" + registrationBO);

		for (Person ownerBO : registrationBO.getOwners()) {
			ownerBO.addRegistration(registrationBO);
			personDAO.update(ownerBO);
		}
	}
}