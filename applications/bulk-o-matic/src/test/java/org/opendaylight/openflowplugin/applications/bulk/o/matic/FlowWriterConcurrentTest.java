/**
 * Copyright (c) 2016, 2017 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.openflowplugin.applications.bulk.o.matic;

import static org.mockito.Mockito.doReturn;

import com.google.common.util.concurrent.Futures;
import java.util.concurrent.ExecutorService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.md.sal.binding.api.WriteTransaction;
import org.opendaylight.controller.md.sal.common.api.data.LogicalDatastoreType;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.Nodes;
import org.opendaylight.yangtools.yang.binding.DataObject;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test for {@link FlowWriterConcurrent}.
 */
@RunWith(MockitoJUnitRunner.class)
public class FlowWriterConcurrentTest {

    private static final Logger LOG = LoggerFactory.getLogger(FlowWriterConcurrentTest.class);
    private static final int FLOWS_PER_DPN = 100;

    @Mock
    private DataBroker mockDataBroker;
    @Mock
    private ExecutorService mockFlowPusher;
    @Mock
    private WriteTransaction writeTransaction;
    @Mock
    private Nodes mockNodes;

    private FlowWriterConcurrent flowWriterConcurrent;

    @Before
    public void setUp() throws Exception {

        doReturn(writeTransaction).when(mockDataBroker).newWriteOnlyTransaction();
        Mockito.when(writeTransaction.submit()).thenReturn(Futures.immediateCheckedFuture(null));

        Mockito.doAnswer(invocation -> {
            ((Runnable) invocation.getArguments()[0]).run();
            return null;
        }).when(mockFlowPusher).execute(ArgumentMatchers.<Runnable>any());

        flowWriterConcurrent = new FlowWriterConcurrent(mockDataBroker, mockFlowPusher);
    }

    @Test
    public void testAddFlows() throws Exception {
        flowWriterConcurrent.addFlows(1, FLOWS_PER_DPN, 10, 10, 10, (short) 0, (short) 1, true);
        Mockito.verify(writeTransaction, Mockito.times(FLOWS_PER_DPN)).put(ArgumentMatchers.<LogicalDatastoreType>any(),
                ArgumentMatchers.<InstanceIdentifier<DataObject>>any(), ArgumentMatchers.<DataObject>any(),
                ArgumentMatchers.anyBoolean());
    }

    @Test
    public void testDeleteFlows() throws Exception {
        flowWriterConcurrent.deleteFlows(1, FLOWS_PER_DPN, 10, (short) 0, (short) 1);
        Mockito.verify(writeTransaction, Mockito.times(FLOWS_PER_DPN))
                .delete(ArgumentMatchers.<LogicalDatastoreType>any(),
                        ArgumentMatchers.<InstanceIdentifier<DataObject>>any());
    }
}
