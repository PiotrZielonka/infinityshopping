package infinityshopping.online.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import infinityshopping.online.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PaymentOrderMainDTOTest {

  @Test
  void dtoEqualsVerifier() throws Exception {
    TestUtil.equalsVerifier(PaymentOrderMainDTO.class);
    PaymentOrderMainDTO paymentOrderMainDto1 = new PaymentOrderMainDTO();
    paymentOrderMainDto1.setId(1L);
    PaymentOrderMainDTO paymentOrderMainDto2 = new PaymentOrderMainDTO();
    assertThat(paymentOrderMainDto1).isNotEqualTo(paymentOrderMainDto2);
    paymentOrderMainDto2.setId(paymentOrderMainDto1.getId());
    assertThat(paymentOrderMainDto1).isEqualTo(paymentOrderMainDto2);
    paymentOrderMainDto2.setId(2L);
    assertThat(paymentOrderMainDto1).isNotEqualTo(paymentOrderMainDto2);
    paymentOrderMainDto1.setId(null);
    assertThat(paymentOrderMainDto1).isNotEqualTo(paymentOrderMainDto2);
  }
}
