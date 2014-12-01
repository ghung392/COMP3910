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
	/** Timeout period. */
	private long timeout;

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
	public Token(String token, int empId, long time ) {
		uuid = token;
		id = empId;
		timeout = time;
	}

	/**
	 * Getter for uuid.
	 * @return uuid
	 */
	@XmlAttribute
	@Column(name = "TokenID")
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
	 * Getter for token id.
	 * @return token id
	 */
	@XmlElement
	@Column(name = "EmpId")
	public int getId() {
		return id;
	}
	/**
	 * Setter for token id.
	 * @param newValue of id
	 */
	public void setId(int newValue) {
		id = newValue;
	}

	/**
	 * Getter for timeout.
	 * @return timeout time
	 */
	@XmlElement
	@Column(name = "Timeout")
	public long getTimeout() {
		return timeout;
	}
	/**
	 * Setter for timeout.
	 * @param newValue of timeout
	 */
	public void setTimeout(long newValue) {
		timeout = newValue;
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
