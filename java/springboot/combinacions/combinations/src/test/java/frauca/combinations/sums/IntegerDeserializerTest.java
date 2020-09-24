package frauca.combinations.sums;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class IntegerDeserializerTest {

    @InjectMocks
    IntegerDeserializer serializer;

    @Test
    void emptyDeserializer() {
        assertThat(serializer.deserialize("")).isEqualTo(Collections.emptyList());
        assertThat(serializer.deserialize(null)).isEqualTo(Collections.emptyList());
    }

    @Test
    void simgleIntegerDeserializar() {
        assertThat(serializer.deserialize("1")).containsExactly(1);
    }

    @Test
    void errorInput() {
        Assertions.assertThrows(Exception.class,
                () -> serializer.deserialize("error")
        );
    }

    @Test
    void multipleInput() {
        assertThat(serializer.deserialize("123")).containsExactly(1, 2, 3);
    }

}