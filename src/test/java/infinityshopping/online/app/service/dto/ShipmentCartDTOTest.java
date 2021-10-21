package infinityshopping.online.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import infinityshopping.online.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ShipmentCartDTOTest {

  @Test
  void dtoEqualsVerifier() throws Exception {
    TestUtil.equalsVerifier(ShipmentCartDTO.class);
    ShipmentCartDTO shipmentCartDto1 = new ShipmentCartDTO();
    shipmentCartDto1.setId(1L);
    ShipmentCartDTO shipmentCartDto2 = new ShipmentCartDTO();
    assertThat(shipmentCartDto1).isNotEqualTo(shipmentCartDto2);
    shipmentCartDto2.setId(shipmentCartDto1.getId());
    assertThat(shipmentCartDto1).isEqualTo(shipmentCartDto2);
    shipmentCartDto2.setId(2L);
    assertThat(shipmentCartDto1).isNotEqualTo(shipmentCartDto2);
    shipmentCartDto1.setId(null);
    assertThat(shipmentCartDto1).isNotEqualTo(shipmentCartDto2);
  }
}
