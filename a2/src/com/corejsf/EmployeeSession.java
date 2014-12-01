package com.corejsf;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
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
    /** 1 hour in milliseconds to add to timeout. */
    private long hour = 3600000;

    public Token generateToken(final int empId) {
    	Date date = new Date();
    	date.setTime(date.getTime() + hour);
    	String token = UUID.randomUUID().toString().replaceAll("-", "");
    	currentToken = new Token(token, empId, date.getTime());
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
    		if( currentToken == null ) {
    			throw new WebApplicationException(Response.Status.UNAUTHORIZED);
    		}
    		int id = currentToken.getId();
    		return id;
    	}
    	throw new WebApplicationException(Response.Status.UNAUTHORIZED);
    }

    public void timeoutToken() {
    	Date date = new Date();

    	List<Token> tokens = tokenList.getTokens();

    	for( Token token : tokens) {
    		if( token.getTimeout() < date.getTime() ) {
    			tokenList.remove(token);
    		}
    	}
    }
}
