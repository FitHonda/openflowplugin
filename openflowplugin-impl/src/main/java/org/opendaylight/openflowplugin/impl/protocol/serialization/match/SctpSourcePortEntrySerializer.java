/*
 * Copyright (c) 2016 Pantheon Technologies s.r.o. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.openflowplugin.impl.protocol.serialization.match;

import io.netty.buffer.ByteBuf;
import org.opendaylight.openflowjava.protocol.api.util.EncodeConstants;
import org.opendaylight.openflowjava.protocol.api.util.OxmMatchConstants;
import org.opendaylight.yang.gen.v1.urn.opendaylight.model.match.types.rev131026.Match;
import org.opendaylight.yang.gen.v1.urn.opendaylight.model.match.types.rev131026.match.layer._4.match.SctpMatch;

public class SctpSourcePortEntrySerializer extends AbstractMatchEntrySerializer {

    @Override
    public void serialize(final Match match, final ByteBuf outBuffer) {
        super.serialize(match, outBuffer);
        outBuffer.writeShort(((SctpMatch) match.getLayer4Match()).getSctpSourcePort().getValue().toJava());
    }

    @Override
    public boolean matchTypeCheck(final Match match) {
        return match.getLayer4Match() != null
                && match.getLayer4Match() instanceof SctpMatch
                && ((SctpMatch) match.getLayer4Match()).getSctpSourcePort() != null;
    }

    @Override
    protected boolean getHasMask(final Match match) {
        return false;
    }

    @Override
    protected int getOxmFieldCode() {
        return OxmMatchConstants.SCTP_SRC;
    }

    @Override
    protected int getOxmClassCode() {
        return OxmMatchConstants.OPENFLOW_BASIC_CLASS;
    }

    @Override
    protected int getValueLength() {
        return EncodeConstants.SIZE_OF_SHORT_IN_BYTES;
    }
}
