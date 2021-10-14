package infinityshopping.online.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import infinityshopping.online.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductDTO.class);
        ProductDTO productDto1 = new ProductDTO();
        productDto1.setId(1L);
        ProductDTO productDto2 = new ProductDTO();
        assertThat(productDto1).isNotEqualTo(productDto2);
        productDto2.setId(productDto1.getId());
        assertThat(productDto1).isEqualTo(productDto2);
        productDto2.setId(2L);
        assertThat(productDto1).isNotEqualTo(productDto2);
        productDto1.setId(null);
        assertThat(productDto1).isNotEqualTo(productDto2);
    }
}
