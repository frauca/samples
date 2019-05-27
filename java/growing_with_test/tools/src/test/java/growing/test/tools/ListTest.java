package growing.test.tools;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
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
}
