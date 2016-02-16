/*
 *
 * This file is part of the XiPKI project.
 * Copyright (c) 2014 Lijun Liao
 * Author: Lijun Liao
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
 * THE AUTHOR LIJUN LIAO. LIJUN LIAO DISCLAIMS THE WARRANTY OF NON INFRINGEMENT
 * OF THIRD PARTY RIGHTS.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
 *
 * You can be released from the requirements of the license by purchasing
 * a commercial license. Buying such a license is mandatory as soon as you
 * develop commercial activities involving the XiPKI software without
 * disclosing the source code of your own applications.
 *
 * For more information, please contact Lijun Liao at this
 * address: lijun.liao@gmail.com
 */

package org.xipki.security.shell;

import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.Completion;
import org.apache.karaf.shell.api.action.Option;
import org.xipki.security.api.p11.P11KeypairGenerationResult;
import org.xipki.security.p11.iaik.P11KeypairGenerator;
import org.xipki.security.shell.completer.ECCurveNameCompleter;

/**
 * @author Lijun Liao
 */

@Command(scope = "keytool", name = "ec", description="Generate EC keypair in PKCS#11 device")
public class P11ECKeyGenCommand extends P11KeyGenCommand
{
    @Option(name = "-curve",
            description = "EC Curve name",
            required = false)
    @Completion(ECCurveNameCompleter.class)
    protected String curveName = "brainpoolp256r1";

    @Override
    protected Object doExecute()
    throws Exception
    {
        P11KeypairGenerator gen = new P11KeypairGenerator(securityFactory);
        P11KeypairGenerationResult keyAndCert = gen.generateECDSAKeypairAndCert(
                moduleName, getSlotId(),
                curveName, label, getSubject(),
                getKeyUsage(), getExtendedKeyUsage());
        saveKeyAndCert(keyAndCert);
        return null;
    }

}
