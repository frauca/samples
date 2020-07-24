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
import frauca.statemachine.workflow.action.BaseAction;
import frauca.statemachine.workflow.guard.BaseGuard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.config.configurers.ExternalTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;

import java.util.EnumSet;

@Configuration
@EnableStateMachineFactory
public class StateMachineConfiguration extends EnumStateMachineConfigurerAdapter<State, Event> {

    @Autowired
    BaseAction action;

    @Autowired
    BaseGuard guard;

    @Override
    public void configure(StateMachineConfigurationConfigurer<State, Event> config)
            throws Exception {
        config
                .withConfiguration()
                .autoStartup(true)
                .listener(listener());
    }

    @Override
    public void configure(StateMachineStateConfigurer<State, Event> states)
            throws Exception {
        states
                .withStates()
                    .initial(State.GET_EGG,action)
                    .state(State.BREAK_EGG, action)
                    .state(State.BEAT_EGG)
                    .state(State.HEAT_OMELETE)
                    .end(State.EAT_OMELETE);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<State, Event> transitions)
            throws Exception {
        ExternalTransitionConfigurer<State, Event> transition = transitions.withExternal();
        State[] states = EnumSet.allOf(State.class).toArray(new State[0]);
        for (int i = 0; i < states.length - 1; i++) {
            transition = transition.source(states[i]).target(states[i + 1])
                    .event(Event.NEXT_STATE)
                    .guard(guard);
            if (i < states.length - 2) {
                transition = transition.and().withExternal();
            }
        }
    }

    @Bean
    public StateMachineListener<State, Event> listener() {
        return new StateMachineListenerAdapter<State, Event>() {
            @Override
            public void stateChanged(org.springframework.statemachine.state.State<State, Event> from, org.springframework.statemachine.state.State<State, Event> to) {
                System.out.println("State change to " + to.getId());
            }
        };
    }
}
