import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;


public class CountingBitsTest {

    @Test
    public void simpleSample() {
        CountingBits counter = new CountingBits();
        assertThat(counter.countBits(0), is(new int[]{0}));
        assertThat(counter.countBits(1), is(new int[]{0, 1}));
        assertThat(counter.countBits(5), is(new int[]{0, 1, 1, 2, 1, 2}));
        assertThat(counter.countBits(10), is(new int[]{0, 1, 1, 2, 1, 2, 2, 3, 1, 2, 2}));
        assertThat(counter.countBits(16), is(new int[]{0,1,1,2,1,2,2,3,1,2,2,3,2,3,3,4,1}));
    }

    @Test
    public void base2logs() {
        CountingBits counter = new CountingBits();
        assertThat(counter.base2logs(0), is(new int[0]));
        assertThat(counter.base2logs(3), is(new int[]{1, 2}));
        assertThat(counter.base2logs(6), is(new int[]{1, 2, 4}));
        assertThat(counter.base2logs(20), is(new int[]{1, 2, 4, 8, 16}));
    }
}
