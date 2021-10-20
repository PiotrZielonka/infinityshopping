package infinityshopping.online.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import infinityshopping.online.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PaymentCartTest {

  @Test
  void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(PaymentCart.class);
    PaymentCart paymentCart1 = new PaymentCart();
    paymentCart1.setId(1L);
    PaymentCart paymentCart2 = new PaymentCart();
    paymentCart2.setId(paymentCart1.getId());
    assertThat(paymentCart1).isEqualTo(paymentCart2);
    paymentCart2.setId(2L);
    assertThat(paymentCart1).isNotEqualTo(paymentCart2);
    paymentCart1.setId(null);
    assertThat(paymentCart1).isNotEqualTo(paymentCart2);
  }
}
