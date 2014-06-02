/*
 * Copyright (c) 2014 xipki.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 *
 */

package org.xipki.ca.client.shell;

import java.math.BigInteger;
import java.security.cert.X509Certificate;

import org.apache.felix.gogo.commands.Command;
import org.apache.felix.gogo.commands.Option;
import org.bouncycastle.asn1.x500.X500Name;
import org.xipki.ca.client.api.RAWorker;
import org.xipki.ca.common.CertIDOrError;
import org.xipki.ca.common.PKIStatusInfo;
import org.xipki.security.common.IoCertUtil;

@Command(scope = "caclient", name = "remove", description="Remove certificate")
public class RARemoveCertCommand extends ClientCommand
{
    @Option(name = "-cert",
            description = "Certificate file")
    protected String            certFile;

    @Option(name = "-cacert",
            description = "CA Certificate file")
    protected String            cacertFile;

    @Option(name = "-serial",
            description = "Serial number")
    protected String            serialNumber;

    private RAWorker             raWorker;

    @Override
    protected Object doExecute()
    throws Exception
    {
        if(certFile == null && (cacertFile == null && serialNumber == null))
        {
            System.err.println("either cert or (cacert, serialNumber) must be specified");
            return null;
        }

        CertIDOrError certIdOrError;
        if(certFile != null)
        {
            X509Certificate cert = IoCertUtil.parseCert(certFile);
            certIdOrError = raWorker.removeCert(cert);
        }
        else
        {
            X509Certificate cacert = IoCertUtil.parseCert(cacertFile);
            X500Name issuer = X500Name.getInstance(cacert.getSubjectX500Principal().getEncoded());
            certIdOrError = raWorker.removeCert(issuer, new BigInteger(serialNumber));
        }

        // TODO: check whether the returned one match the requested one
        if(certIdOrError.getError() != null)
        {
            PKIStatusInfo error = certIdOrError.getError();
            System.err.println("Removing certificate failed: status=" + error.getStatus()+
                    ", failureInfo=" + error.getPkiFailureInfo() + ", message=" + error.getStatusMessage());
        }
        else
        {
            System.out.println("Removed certificate");
        }
        return null;
    }

    public void setRaWorker(RAWorker raWorker)
    {
        this.raWorker = raWorker;
    }

}
