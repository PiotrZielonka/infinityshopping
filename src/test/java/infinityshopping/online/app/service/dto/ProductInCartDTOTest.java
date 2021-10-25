package infinityshopping.online.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import infinityshopping.online.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductInCartDTOTest {

  @Test
  void dtoEqualsVerifier() throws Exception {
    TestUtil.equalsVerifier(ProductInCartDTO.class);
    ProductInCartDTO productInCartDto1 = new ProductInCartDTO();
    productInCartDto1.setId(1L);
    ProductInCartDTO productInCartDto2 = new ProductInCartDTO();
    assertThat(productInCartDto1).isNotEqualTo(productInCartDto2);
    productInCartDto2.setId(productInCartDto1.getId());
    assertThat(productInCartDto1).isEqualTo(productInCartDto2);
    productInCartDto2.setId(2L);
    assertThat(productInCartDto1).isNotEqualTo(productInCartDto2);
    productInCartDto1.setId(null);
    assertThat(productInCartDto1).isNotEqualTo(productInCartDto2);
  }
}
