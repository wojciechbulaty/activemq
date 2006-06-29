/**
 *
 * Copyright 2005-2006 The Apache Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.activemq.tool;

import org.apache.activemq.tool.properties.JmsClientSystemProperties;
import org.apache.activemq.tool.properties.JmsConsumerSystemProperties;
import org.apache.activemq.tool.properties.JmsConsumerProperties;
import org.apache.activemq.tool.properties.JmsClientProperties;
import org.apache.activemq.tool.sampler.ThroughputSamplerTask;

import javax.jms.JMSException;
import java.util.Properties;

public class JmsConsumerSystem extends AbstractJmsClientSystem {
    protected JmsConsumerSystemProperties sysTest = new JmsConsumerSystemProperties();
    protected JmsConsumerProperties consumer = new JmsConsumerProperties();

    public JmsClientSystemProperties getSysTest() {
        return sysTest;
    }

    public void setSysTest(JmsClientSystemProperties sysTestProps) {
        sysTest = (JmsConsumerSystemProperties)sysTestProps;
    }

    public JmsClientProperties getJmsClientProperties() {
        return getConsumer();
    }

    public JmsConsumerProperties getConsumer() {
        return consumer;
    }

    public void setConsumer(JmsConsumerProperties consumer) {
        this.consumer = consumer;
    }

    protected void runJmsClient(String clientName, int clientDestIndex, int clientDestCount) {
        ThroughputSamplerTask sampler = getTpSampler();

        JmsConsumerClient consumerClient = new JmsConsumerClient(consumer, jmsConnFactory);
        consumerClient.setClientName(clientName);

        if (sampler != null) {
            sampler.registerClient(consumerClient);
        }

        try {
            consumerClient.receiveMessages(clientDestIndex, clientDestCount);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        JmsConsumerSystem sys = new JmsConsumerSystem();
        sys.configureProperties(AbstractJmsClientSystem.parseStringArgs(args));

        try {
            sys.runSystemTest();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
