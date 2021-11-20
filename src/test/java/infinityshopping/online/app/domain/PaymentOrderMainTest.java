package infinityshopping.online.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import infinityshopping.online.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PaymentOrderMainTest {

  @Test
  void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(PaymentOrderMain.class);
    PaymentOrderMain paymentOrderMain1 = new PaymentOrderMain();
    paymentOrderMain1.setId(1L);
    PaymentOrderMain paymentOrderMain2 = new PaymentOrderMain();
    paymentOrderMain2.setId(paymentOrderMain1.getId());
    assertThat(paymentOrderMain1).isEqualTo(paymentOrderMain2);
    paymentOrderMain2.setId(2L);
    assertThat(paymentOrderMain1).isNotEqualTo(paymentOrderMain2);
    paymentOrderMain1.setId(null);
    assertThat(paymentOrderMain1).isNotEqualTo(paymentOrderMain2);
  }
}
