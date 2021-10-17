package infinityshopping.online.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import infinityshopping.online.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PaymentDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentDTO.class);
        PaymentDTO paymentDto1 = new PaymentDTO();
        paymentDto1.setId(1L);
        PaymentDTO paymentDto2 = new PaymentDTO();
        assertThat(paymentDto1).isNotEqualTo(paymentDto2);
        paymentDto2.setId(paymentDto1.getId());
        assertThat(paymentDto1).isEqualTo(paymentDto2);
        paymentDto2.setId(2L);
        assertThat(paymentDto1).isNotEqualTo(paymentDto2);
        paymentDto1.setId(null);
        assertThat(paymentDto1).isNotEqualTo(paymentDto2);
    }
}
