package infinityshopping.online.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import infinityshopping.online.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PaymentCartDTOTest {

  @Test
  void dtoEqualsVerifier() throws Exception {
    TestUtil.equalsVerifier(PaymentCartDTO.class);
    PaymentCartDTO paymentCartDto1 = new PaymentCartDTO();
    paymentCartDto1.setId(1L);
    PaymentCartDTO paymentCartDto2 = new PaymentCartDTO();
    assertThat(paymentCartDto1).isNotEqualTo(paymentCartDto2);
    paymentCartDto2.setId(paymentCartDto1.getId());
    assertThat(paymentCartDto1).isEqualTo(paymentCartDto2);
    paymentCartDto2.setId(2L);
    assertThat(paymentCartDto1).isNotEqualTo(paymentCartDto2);
    paymentCartDto1.setId(null);
    assertThat(paymentCartDto1).isNotEqualTo(paymentCartDto2);
  }
}
