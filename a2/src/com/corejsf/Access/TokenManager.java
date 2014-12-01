package com.corejsf.Access;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.corejsf.Model.Token;


/**
 * Database CRUD operations for tokens.
 * @author Gabriel
 *
 */
@Stateless
public class TokenManager {
	/** Entity Manager. */
	@PersistenceContext(unitName = "a3")private EntityManager em;

	/**
     * Method to find a token.
     * @param uuid of token to find
     * @return token if found or null
     */
    public Token find(final String uuid) {
        return em.find(Token.class, uuid);
    }
    /**
     * Remove a token from the list.
     * @param token to remove
     */
    public void remove(final Token token) {
        Token tok = find(token.getUuid());
        em.remove(tok);
    }

    /**
     * Add a token to the list.
     * @param newToken new token
     */
    public void persist(final Token newToken) {
        em.persist(newToken);
    }
    /**
     * Gets the whole list of tokens.
     * @return list of tokens
     */
    public List<Token> getTokens() {
        TypedQuery<Token> query = em.createQuery("select t "
                + "from Token t", Token.class);
        List<Token> tokens = query.getResultList();
        return tokens;
    }

}
