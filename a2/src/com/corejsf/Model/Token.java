package com.corejsf.Model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Token Model that represents a certain token tied with a user session.
 * @author Gabriel
 *
 */
@XmlRootElement(name = "token")
@Entity
@Table(name = "Tokens")
public class Token implements Serializable {
    /** UUID. */
	private String uuid;
	/** Employee Id. */
	private int id;

	/**
	 * Default constructor.
	 */
	public Token() {

	}

	/**
	 * Constructor to instantiate a token.
	 * @param token unique token
	 * @param empId id tied to the session
	 */
	public Token(String token, int empId ) {
		uuid = token;
		id = empId;
	}

	/**
	 * Getter for uuid.
	 * @return uuid
	 */
	@XmlAttribute
	@Column(name = "TokenId")
	@Id
    public String getUuid() {
    	return uuid;
    }

	/**
	 * Setter for uuid.
	 * @param newValue new uuid
	 */
	public void setUuid(String newValue) {
		uuid = newValue;
	}

	/**
	 * Getter for employee id.
	 * @return employee id
	 */
	@XmlElement
	@Column(name = "EmpId")
	public int getId() {
		return id;
	}

	@Override
   public String toString()
   {
      return "Employee{" +
              "id=" + id +
              ", uuid='" + uuid + '\'' +
              ", id='" + id + '\'' +
              '}';
   }
}
