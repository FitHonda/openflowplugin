/*
 * Copyright (c) 2015 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.openflowplugin.impl.statistics.services;

import com.google.common.util.concurrent.FutureCallback;
import java.util.concurrent.Future;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.opendaylight.openflowplugin.api.openflow.device.RequestContext;
import org.opendaylight.openflowplugin.api.openflow.device.Xid;
import org.opendaylight.openflowplugin.impl.rpc.AbstractRequestContext;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.table.statistics.rev131215.GetFlowTablesStatisticsInputBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.table.statistics.rev131215.GetFlowTablesStatisticsOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.openflow.common.types.rev130731.MultipartType;
import org.opendaylight.yang.gen.v1.urn.opendaylight.openflow.protocol.rev130731.MultipartRequestInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.openflow.protocol.rev130731.OfHeader;
import org.opendaylight.yang.gen.v1.urn.opendaylight.openflow.protocol.rev130731.multipart.request.multipart.request.body.MultipartRequestTableCase;
import org.opendaylight.yangtools.yang.common.RpcResult;

/**
 * Test for {@link OpendaylightFlowTableStatisticsServiceImpl}
 */
public class OpendaylightFlowTableStatisticsServiceImplTest extends AbstractStatsServiceTest {

    @Captor
    private ArgumentCaptor<MultipartRequestInput> requestInput;

    private RequestContext<Object> rqContext;

    private OpendaylightFlowTableStatisticsServiceImpl flowTableStatisticsService;

    public void setUp() {
        flowTableStatisticsService = new OpendaylightFlowTableStatisticsServiceImpl(rqContextStack, deviceContext);

        rqContext = new AbstractRequestContext<Object>(42L) {
            @Override
            public void close() {
                //NOOP
            }
        };
        Mockito.when(rqContextStack.<Object>createRequestContext()).thenReturn(rqContext);
    }

    @Test
    public void testGetFlowTablesStatistics() throws Exception {
        Mockito.doAnswer(answerVoidToCallback).when(outboundQueueProvider)
                .commitEntry(Matchers.eq(42L), requestInput.capture(), Matchers.any(FutureCallback.class));

        GetFlowTablesStatisticsInputBuilder input = new GetFlowTablesStatisticsInputBuilder()
                .setNode(createNodeRef("unitProt:123"));

        final Future<RpcResult<GetFlowTablesStatisticsOutput>> resultFuture
                = flowTableStatisticsService.getFlowTablesStatistics(input.build());

        Assert.assertTrue(resultFuture.isDone());
        final RpcResult<GetFlowTablesStatisticsOutput> rpcResult = resultFuture.get();
        Assert.assertTrue(rpcResult.isSuccessful());
        Assert.assertEquals(MultipartType.OFPMPTABLE, requestInput.getValue().getType());
    }

    @Test
    public void testBuildRequest() throws Exception {
        Xid xid = new Xid(42L);
        GetFlowTablesStatisticsInputBuilder input = new GetFlowTablesStatisticsInputBuilder()
                .setNode(createNodeRef("unitProt:123"));
        final OfHeader request = flowTableStatisticsService.buildRequest(xid, input.build());
        Assert.assertTrue(request instanceof MultipartRequestInput);
        final MultipartRequestInput mpRequest = (MultipartRequestInput) request;
        Assert.assertEquals(MultipartType.OFPMPTABLE, mpRequest.getType());
        Assert.assertTrue(mpRequest.getMultipartRequestBody() instanceof MultipartRequestTableCase);
        final MultipartRequestTableCase mpRequestBody = (MultipartRequestTableCase) (mpRequest.getMultipartRequestBody());
        Assert.assertTrue(mpRequestBody.getMultipartRequestTable().isEmpty());
    }
}