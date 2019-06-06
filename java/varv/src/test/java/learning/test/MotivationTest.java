package learning.test;

import io.vavr.control.Try;
import org.junit.Test;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class MotivationTest {

    @Test
    public void couldNotThrowException(){
        Stream<String> stream = Stream.of("notANumber","2");
        Optional<Try<Integer>> integer = stream
                .map(s->
                    Try.of(()->Integer.valueOf(s)))
                .findFirst();
        assertTrue(integer.isPresent());
        assertTrue(integer.get().isFailure());
    }

    @Test(expected = Exception.class)
    public void onErrorThrowException() throws Throwable {
        Integer neverReached = Stream.of("notANumber","2")
                .map(s-> Try.of(()->Integer.valueOf(s)))
                .filter(t->t.isFailure())
                .findAny()
                .get().getOrElseThrow(ex->ex);
    }

    @Test
    public void onErrorSkipError(){
        Optional<Try<Integer>> res = Stream.of("notANumber","2")
                .map(s-> Try.of(()->Integer.valueOf(s)))
                .filter(t->t.isSuccess())
                .findFirst();
        assertTrue(res.isPresent());
        assertTrue(res.get().isSuccess());
        assertThat(res.get().get(),is(2));
    }

    @Test
    public void splitInTwoStreams(){
        List<Try<Integer>> results =Stream.of("notANumber","2")
                .map(s-> Try.of(()->Integer.valueOf(s)))
                .collect(Collectors.toList());
        List<Integer> success = results
                .stream()
                .filter(t->t.isSuccess())
                .map(t->t.get())
                .collect(Collectors.toList());
        List<Throwable> errors = results
                .stream()
                .filter(t->t.isFailure())
                .map(t->t.getCause())
                .collect(Collectors.toList());
        assertThat(success.size(),is(1));
        assertThat(errors.size(),is(1));

    }
}
