package frauca.statemachine;

import frauca.statemachine.workflow.Event;
import frauca.statemachine.workflow.State;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.persist.DefaultStateMachinePersister;
import org.springframework.statemachine.persist.StateMachinePersister;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class StatemachineApplicationTests {

    @Autowired
    StateMachineFactory<State, Event> stateMachineFactory;

    @Autowired
    StateMachinePersister<State, Event, String> persister;

    @Test
    public void persistence() throws Exception {
        StateMachine<State, Event> stm1 = stateMachineFactory.getStateMachine();
        stm1.sendEvent(Event.NEXT_STATE);

        persister.persist(stm1,"firstMachine");

        StateMachine<State, Event> stm2 = stateMachineFactory.getStateMachine();
        stm2.sendEvent(Event.NEXT_STATE);
        stm2.sendEvent(Event.NEXT_STATE);

        persister.persist(stm1,"secondMachine");

        assertThat(stm1.getState().getId()).isEqualTo(State.BREAK_EGG);
        assertThat(stm2.getState().getId()).isEqualTo(State.BEAT_EGG);

        persister.restore(stm2,"firstMachine");
        assertThat(stm2.getState().getId()).isEqualTo(stm1.getState().getId());

    }


}
