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
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

public class KeyStore {
	
	private java.security.KeyStore keyStore = null;
	private String privateKey = null;
	private String cert = null;
	private String caCert = null;
	private String crl = null;
	private boolean initialized = false;
	private CertificateAuthority ca;
	private PrivateKey pkey;
	
	private KeyStore (java.security.KeyStore keyStore)
	{
		this.keyStore = keyStore;
		this.initialized = false;
	}
	
	private void load () throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException
	{
		pkey = PrivateKey.getInstance();
		ca = CertificateAuthority.getInstance();
		pkey.init(privateKey);
		ca.init(cert, caCert, crl);
		
		keyStore.load(null);
		keyStore.setCertificateEntry("ca.zetta-core.net", ca.getAuthorityCertificate());
		keyStore.setKeyEntry("lic.zetta-core.net", pkey.getKey(), PrivateKey.getPassword(), ca.getServerChain());
	}
	
	private static class SingletonHolder {
		private static KeyStore instance = null;
	}
	
	public java.security.KeyStore getStore ()
	{
		return keyStore;
	}
	
	public void init (String privateKey, String cert, String caCert, String crl)
	{
		if (initialized)
		{
			throw new RuntimeException("KeyStore already initialized");
		}
		this.privateKey = privateKey;
		this.cert = cert;
		this.caCert = caCert;
		this.crl = crl;
		
		try {
			load();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static KeyStore getInstance ()
	{
		if (SingletonHolder.instance == null)
		{
			try
			{
				SingletonHolder.instance = new KeyStore (java.security.KeyStore.getInstance("JKS", "SUN"));
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return SingletonHolder.instance;
	}
	
	public CertificateAuthority getCertificateAuthority ()
	{
		return this.ca;
	}
}
