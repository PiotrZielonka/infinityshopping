package infinityshopping.online.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import infinityshopping.online.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CartDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CartDTO.class);
        CartDTO cartDto1 = new CartDTO();
        cartDto1.setId(1L);
        CartDTO cartDto2 = new CartDTO();
        assertThat(cartDto1).isNotEqualTo(cartDto2);
        cartDto2.setId(cartDto1.getId());
        assertThat(cartDto1).isEqualTo(cartDto2);
        cartDto2.setId(2L);
        assertThat(cartDto1).isNotEqualTo(cartDto2);
        cartDto1.setId(null);
        assertThat(cartDto1).isNotEqualTo(cartDto2);
    }
}
