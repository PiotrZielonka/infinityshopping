package infinityshopping.online.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import infinityshopping.online.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductInOrderMainDTOTest {

  @Test
  void dtoEqualsVerifier() throws Exception {
    TestUtil.equalsVerifier(ProductInOrderMainDTO.class);
    ProductInOrderMainDTO productInOrderMainDto1 = new ProductInOrderMainDTO();
    productInOrderMainDto1.setId(1L);
    ProductInOrderMainDTO productInOrderMainDto2 = new ProductInOrderMainDTO();
    assertThat(productInOrderMainDto1).isNotEqualTo(productInOrderMainDto2);
    productInOrderMainDto2.setId(productInOrderMainDto1.getId());
    assertThat(productInOrderMainDto1).isEqualTo(productInOrderMainDto2);
    productInOrderMainDto2.setId(2L);
    assertThat(productInOrderMainDto1).isNotEqualTo(productInOrderMainDto2);
    productInOrderMainDto1.setId(null);
    assertThat(productInOrderMainDto1).isNotEqualTo(productInOrderMainDto2);
  }
}
