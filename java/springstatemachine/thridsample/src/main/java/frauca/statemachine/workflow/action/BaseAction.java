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

package frauca.statemachine.workflow.action;

import frauca.statemachine.workflow.Event;
import frauca.statemachine.workflow.State;
import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BaseAction implements Action<State, Event> {
    @Override
    public void execute(StateContext<State, Event> stateContext) {
        log.info("It has been called");
    }
}
