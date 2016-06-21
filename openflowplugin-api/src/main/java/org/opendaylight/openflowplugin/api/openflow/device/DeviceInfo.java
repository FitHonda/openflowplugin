/*
 * Copyright (c) 2015 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.openflowplugin.api.openflow.device;

import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.inet.types.rev100924.IpAddress;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeId;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.nodes.Node;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.nodes.NodeKey;
import org.opendaylight.yangtools.yang.binding.KeyedInstanceIdentifier;

import java.math.BigInteger;

/**
 * API defining basic device information
 */
public interface DeviceInfo {

    /**
     * @return id of encapsulated node
     */
    NodeId getNodeId();

    /**
     * @return {@link Node} instance identifier
     */
    KeyedInstanceIdentifier<Node, NodeKey> getNodeInstanceIdentifier();

    /**
     * @return version
     */
    Short getVersion();

    /**
     * @return datapathId
     */
    BigInteger getDatapathId();

    /**
     * @return IP adress
     */
    IpAddress getIpAddress();

}
