package growing.test.tools;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import mockit.Expectations;
import mockit.Mocked;
import mockit.Verifications;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ListTest {

  private List<String> list = new ArrayList<>();


  @BeforeEach
  void emptyList() {
    list.clear();
  }

  @Test
  void whenAddOneElement_thenCountIsOne(){
    list.add("sample");
    assertEquals(1,list.size());
  }

  @Test
  void whenNullAdded_thenThrowNullPointer(){
    list.add("sample");
    assertThrows(IndexOutOfBoundsException.class,()->{
      list.get(3);
    });
  }

  @Test
  void whenStringAdded_ContainsTheString(){
    list.add("sample");
    assertThat(list, hasItem("sample"));

  }

  @Test
  void givenStreaOfList_whenFiltered_testIsCalled(@Mocked Predicate<String> filter) {
    new Expectations(){{
      filter.test(anyString); returns(true,false);
    }};
    list.add("sample");
    list.add("toFilter");

    List<String> result = list.stream().filter(filter).collect(Collectors.toList());
    assertThat(result.size(),is(1));

    new Verifications(){{
      filter.test(anyString); times = 2;
    }};
  }
}
