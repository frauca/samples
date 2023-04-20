package org.example;

import static org.assertj.core.api.Assertions.assertThat;

import jdk.incubator.concurrent.ScopedValue;
import org.junit.jupiter.api.Test;

/**
 * Add --enable-preview --add-modules=jdk.incubator.concurrent to run it
 */
class ScopedValueTest {
    public static final ScopedValue<String> SHARED_VALUE = ScopedValue.newInstance();

    @Test
    void scopedValue(){
        var shared = "Scoped values are like ThreadLocal variables but inmmutable and cheaper in memory";
        ScopedValue.where(SHARED_VALUE, shared)
            .run(()->checkValue());

    }

    private void checkValue(){
        assertThat(SHARED_VALUE.get()).isEqualTo("Scoped values are like ThreadLocal variables but inmmutable and cheaper in memory");
    }
}
