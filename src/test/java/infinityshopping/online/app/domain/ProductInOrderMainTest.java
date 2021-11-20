package infinityshopping.online.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import infinityshopping.online.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductInOrderMainTest {

  @Test
  void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(ProductInOrderMain.class);
    ProductInOrderMain productInOrderMain1 = new ProductInOrderMain();
    productInOrderMain1.setId(1L);
    ProductInOrderMain productInOrderMain2 = new ProductInOrderMain();
    productInOrderMain2.setId(productInOrderMain1.getId());
    assertThat(productInOrderMain1).isEqualTo(productInOrderMain2);
    productInOrderMain2.setId(2L);
    assertThat(productInOrderMain1).isNotEqualTo(productInOrderMain2);
    productInOrderMain1.setId(null);
    assertThat(productInOrderMain1).isNotEqualTo(productInOrderMain2);
  }
}
