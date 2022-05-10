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
import java.security.cert.CRL;
import java.security.cert.CRLException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.Collection;

public class CertificateAuthority {
	
	private CertificateFactory factory = null;
	private Certificate[] serverChain = null;
	private Certificate authorityCertificate = null;
	private CRL crl = null;
	
	private InputStream load (String filename) throws IOException
	{
		return ResourceLoader.loadResource(filename);
	}
	
	private void loadCert (String certFilename) throws CertificateException, IOException
	{
		InputStream is = load(certFilename);
		Collection<? extends Certificate> c = factory.generateCertificates(is);
        serverChain = new Certificate[c.size()];
        int i = 0;
        for (Certificate crt : c)
        {
        	serverChain[i++] = crt;
        }
	}
	
	private void loadCa (String caFilename) throws IOException, CertificateException
	{
		InputStream is = load(caFilename);
		authorityCertificate = factory.generateCertificate(is);
	}
	
	private void loadCrl (String crlFilename) throws IOException, CRLException
	{
		InputStream is = load(crlFilename);
		crl = factory.generateCRL(is);
	}
	
	public boolean isValid (Certificate cert)
	{
		try {
			cert.verify(authorityCertificate.getPublicKey());
		} catch (Exception e) {
			return false;
		}
		return !crl.isRevoked(cert);
	}
	
	public Certificate[] getServerChain ()
	{
		return serverChain;
	}
	
	public CRL getCrl ()
	{
		return crl;
	}
	
	public Certificate getAuthorityCertificate ()
	{
		return authorityCertificate;
	}
	
	public void init (String cert, String caCert, String crl)
	{
		try {
			factory = CertificateFactory.getInstance("X.509");
			loadCert(cert);
			loadCa(caCert);
			loadCrl(crl);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static final class SingletonHolder 
	{
		protected static CertificateAuthority instance = null;
	}
	
	public static final CertificateAuthority getInstance ()
	{
		if (SingletonHolder.instance == null)
		{
			SingletonHolder.instance = new CertificateAuthority ();
		}
		return SingletonHolder.instance;
	}
	
}
