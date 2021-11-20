package infinityshopping.online.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import infinityshopping.online.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrderMainDTOTest {

  @Test
  void dtoEqualsVerifier() throws Exception {
    TestUtil.equalsVerifier(OrderMainDTO.class);
    OrderMainDTO orderMainDto1 = new OrderMainDTO();
    orderMainDto1.setId(1L);
    OrderMainDTO orderMainDto2 = new OrderMainDTO();
    assertThat(orderMainDto1).isNotEqualTo(orderMainDto2);
    orderMainDto2.setId(orderMainDto1.getId());
    assertThat(orderMainDto1).isEqualTo(orderMainDto2);
    orderMainDto2.setId(2L);
    assertThat(orderMainDto1).isNotEqualTo(orderMainDto2);
    orderMainDto1.setId(null);
    assertThat(orderMainDto1).isNotEqualTo(orderMainDto2);
  }
}
