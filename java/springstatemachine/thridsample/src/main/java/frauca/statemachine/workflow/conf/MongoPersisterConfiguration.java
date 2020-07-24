/*
 * Copyright (c) 2020 by Marfeel Solutions (http://www.marfeel.com)
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Marfeel Solutions S.L and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Marfeel Solutions S.L and its
 * suppliers and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Marfeel Solutions SL.
 */

package frauca.statemachine.workflow.conf;

import frauca.statemachine.workflow.Event;
import frauca.statemachine.workflow.State;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.data.mongodb.MongoDbPersistingStateMachineInterceptor;
import org.springframework.statemachine.data.mongodb.MongoDbStateMachineRepository;
import org.springframework.statemachine.persist.DefaultStateMachinePersister;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.statemachine.persist.StateMachineRuntimePersister;

import java.util.UUID;

@Configuration
public class MongoPersisterConfiguration {
    @Bean
    public StateMachineRuntimePersister<State, Event, String> stateMachineRuntimePersister(
            MongoDbStateMachineRepository repository) {
        return new MongoDbPersistingStateMachineInterceptor<>(repository);
    }

    @Bean
    public StateMachinePersister<State, Event, String> persister(
            StateMachinePersist<State, Event, String> defaultPersist) {

        return new DefaultStateMachinePersister<>(defaultPersist);
    }
}
