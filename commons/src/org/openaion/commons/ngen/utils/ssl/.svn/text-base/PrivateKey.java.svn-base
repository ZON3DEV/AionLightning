/*
 *  This file is part of Zetta-Core Engine <http://www.zetta-core.org>.
 *
 *  Zetta-Core is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published
 *  by the Free Software Foundation, either version 3 of the License,
 *  or (at your option) any later version.
 *
 *  Zetta-Core is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a  copy  of the GNU General Public License
 *  along with Zetta-Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.openaion.commons.ngen.utils.ssl;

import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

public class PrivateKey {
	
	private final static String password = "3O4npw4xC2zDkvnoj6Fv769SlWe/un2AVuNqrGWCJ4M=";
	
	private Key key = null;
	
	private void load (InputStream is) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException
	{
        byte[] bytes = new byte[is.available()];
        is.read(bytes, 0, is.available());
        is.close();
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec peks = new PKCS8EncodedKeySpec(bytes);
        key = kf.generatePrivate(peks);
	}
	
	public Key getKey ()
	{
		return key;
	}
	
	public static char[] getPassword ()
	{
		return password.toCharArray();
	}
	
	public void init (String privateKey)
	{
		try 
		{
			InputStream is = ResourceLoader.loadResource(privateKey);
			load(is);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private static class SingletonHolder {
		protected static PrivateKey instance = null;
	}
	
	public static final PrivateKey getInstance ()
	{
		if (SingletonHolder.instance == null)
		{
			SingletonHolder.instance = new PrivateKey ();
		}
		return SingletonHolder.instance;
	}
}
