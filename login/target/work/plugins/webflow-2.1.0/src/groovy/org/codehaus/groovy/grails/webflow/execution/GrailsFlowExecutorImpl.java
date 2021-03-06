/* Copyright 2004-2005 Graeme Rocher
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.codehaus.groovy.grails.webflow.execution;

import org.codehaus.groovy.grails.web.servlet.mvc.GrailsWebRequest;
import org.codehaus.groovy.grails.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.webflow.context.ExternalContext;
import org.springframework.webflow.context.ExternalContextHolder;
import org.springframework.webflow.core.FlowException;
import org.springframework.webflow.definition.registry.FlowDefinitionLocator;
import org.springframework.webflow.definition.registry.NoSuchFlowDefinitionException;
import org.springframework.webflow.execution.FlowExecutionFactory;
import org.springframework.webflow.execution.repository.FlowExecutionRepository;
import org.springframework.webflow.execution.repository.FlowExecutionRestorationFailureException;
import org.springframework.webflow.execution.repository.snapshot.SnapshotUnmarshalException;
import org.springframework.webflow.executor.FlowExecutionResult;
import org.springframework.webflow.executor.FlowExecutorImpl;

/**
 * @author Graeme Rocher
 * @since 1.1
 */
public class GrailsFlowExecutorImpl extends FlowExecutorImpl {

    private final Logger log = LoggerFactory.getLogger(getClass());

    /**
     * Create a new flow executor.
     *
     * @param definitionLocator   the locator for accessing flow definitions to execute
     * @param executionFactory    the factory for creating executions of flow definitions
     * @param executionRepository the repository for persisting paused flow executions
     */
    public GrailsFlowExecutorImpl(FlowDefinitionLocator definitionLocator,
            FlowExecutionFactory executionFactory, FlowExecutionRepository executionRepository) {
        super(definitionLocator, executionFactory, executionRepository);
        this.executionRepository = executionRepository;
    }

    FlowExecutionRepository executionRepository;

    @Override
    public FlowExecutionResult resumeExecution(String flowExecutionKey, ExternalContext context) throws FlowException {

        //Check if FlowExecutions Flowid matches flowId
        try {
            ExternalContextHolder.setExternalContext(context);
            GrailsWebRequest webRequest = WebUtils.retrieveGrailsWebRequest();

            if (isNotValidFlowDefinitionId(webRequest)) {
                return launchExecution(webRequest.getControllerName() + "/" +
                        webRequest.getActionName(), context.getRequestMap(), context);
            }
        }
        finally {
            ExternalContextHolder.setExternalContext(null);
        }

        try {
            return super.resumeExecution(flowExecutionKey, context);
        }
        catch (FlowExecutionRestorationFailureException e) {
            if (e.getCause() instanceof SnapshotUnmarshalException) {
                log.info("Classes changed during reload, restarting flow...");
                GrailsWebRequest webRequest = WebUtils.retrieveGrailsWebRequest();
                return launchExecution(webRequest.getControllerName() + "/" + webRequest.getActionName(),
                        context.getRequestMap(), context);
            }

            throw e;
        }
    }

    private boolean isNotValidFlowDefinitionId(GrailsWebRequest webRequest) {
        final FlowDefinitionLocator locator = getDefinitionLocator();
        final String requestPath = webRequest.getControllerName() + "/" + webRequest.getActionName();

        try {
            locator.getFlowDefinition(requestPath);
            return false;
        }
        catch (NoSuchFlowDefinitionException e) {
            return true;
        }
    }
}
