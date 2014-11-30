package com.corejsf;

import java.io.Serializable;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import com.corejsf.Access.TokenManager;
import com.corejsf.Model.Token;

/**
 * Creates tokens, holds tokens in database and removes tokens.
 * @author Gabriel
 *
 */
@ApplicationScoped
public class EmployeeSession implements Serializable {
	/** Token manager that does CRUD operations. */
    @Inject private TokenManager tokenList;
    /** Token object. */
    private Token currentToken;

    public Token generateToken(final int empId) {
    	String token = UUID.randomUUID().toString().replaceAll("-", "");
    	currentToken = new Token(token, empId);
    	tokenList.persist(currentToken);

    	return currentToken;
    }

    public boolean removeToken(String tokenToRemove) {
    	currentToken = tokenList.find(tokenToRemove);
    	if (currentToken == null) {
    		return false;
    	}
    	tokenList.remove(currentToken);
    	return true;
    }

    public int getEmployeeId(String tokenToVerify) throws WebApplicationException {
    	if (tokenToVerify != null) {
    		currentToken = tokenList.find(tokenToVerify);
    		int id = currentToken.getId();
    		return id;
    	}
    	throw new WebApplicationException(Response.Status.UNAUTHORIZED);
    }
}
