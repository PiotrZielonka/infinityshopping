package infinityshopping.online.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import infinityshopping.online.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrderMainTest {

  @Test
  void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(OrderMain.class);
    OrderMain orderMain1 = new OrderMain();
    orderMain1.setId(1L);
    OrderMain orderMain2 = new OrderMain();
    orderMain2.setId(orderMain1.getId());
    assertThat(orderMain1).isEqualTo(orderMain2);
    orderMain2.setId(2L);
    assertThat(orderMain1).isNotEqualTo(orderMain2);
    orderMain1.setId(null);
    assertThat(orderMain1).isNotEqualTo(orderMain2);
  }
}
