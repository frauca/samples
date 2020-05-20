package frauca.statemachine;

import frauca.statemachine.workflow.Event;
import frauca.statemachine.workflow.State;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class StatemachineApplicationTests {

    @Autowired
    StateMachine<State, Event> stateMachine;

    @Test
    void contextLoads() {
    }

    @Test
    public void makeAllTheSteps(){
        assertThat(stateMachine).isNotNull();

        assertThat(stateMachine.getState().getId()).isEqualTo(State.GET_EGG);

        stateMachine.sendEvent(Event.NEXT_STATE);

        assertThat(stateMachine.getState().getId()).isEqualTo(State.BREAK_EGG);

        stateMachine.sendEvent(Event.NEXT_STATE);

        assertThat(stateMachine.getState().getId()).isEqualTo(State.BEAT_EGG);

        stateMachine.sendEvent(Event.NEXT_STATE);

        assertThat(stateMachine.getState().getId()).isEqualTo(State.HEAT_OMELETE);

        stateMachine.sendEvent(Event.NEXT_STATE);

        assertThat(stateMachine.getState().getId()).isEqualTo(State.EAT_OMELETE);

        assertThat(stateMachine.isComplete()).isTrue();
    }

}
