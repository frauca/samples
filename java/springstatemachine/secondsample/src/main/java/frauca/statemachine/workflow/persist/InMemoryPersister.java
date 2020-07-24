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

package frauca.statemachine.workflow.persist;

import frauca.statemachine.workflow.Event;
import frauca.statemachine.workflow.State;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Repository
public class InMemoryPersister implements StateMachinePersist<State, Event, String> {

    private final HashMap<String, StateMachineContext<State, Event>> memory = new HashMap<>();


    @Override
    public void write(StateMachineContext<State, Event> stateMachineContext, String s) throws Exception {
        memory.put(s, stateMachineContext);
    }

    @Override
    public StateMachineContext<State, Event> read(String s) throws Exception {
        return memory.get(s);
    }
}
