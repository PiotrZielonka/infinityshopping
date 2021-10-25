package infinityshopping.online.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import infinityshopping.online.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductInCartTest {

  @Test
  void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(ProductInCart.class);
    ProductInCart productInCart1 = new ProductInCart();
    productInCart1.setId(1L);
    ProductInCart productInCart2 = new ProductInCart();
    productInCart2.setId(productInCart1.getId());
    assertThat(productInCart1).isEqualTo(productInCart2);
    productInCart2.setId(2L);
    assertThat(productInCart1).isNotEqualTo(productInCart2);
    productInCart1.setId(null);
    assertThat(productInCart1).isNotEqualTo(productInCart2);
  }
}
