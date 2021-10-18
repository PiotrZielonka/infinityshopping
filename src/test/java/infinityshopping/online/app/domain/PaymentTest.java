package infinityshopping.online.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import infinityshopping.online.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PaymentTest {

  @Test
  void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(Payment.class);
    Payment payment1 = new Payment();
    payment1.setId(1L);
    Payment payment2 = new Payment();
    payment2.setId(payment1.getId());
    assertThat(payment1).isEqualTo(payment2);
    payment2.setId(2L);
    assertThat(payment1).isNotEqualTo(payment2);
    payment1.setId(null);
    assertThat(payment1).isNotEqualTo(payment2);
  }
}
