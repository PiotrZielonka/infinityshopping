package infinityshopping.online.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import infinityshopping.online.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ShipmentOrderMainDTOTest {

  @Test
  void dtoEqualsVerifier() throws Exception {
    TestUtil.equalsVerifier(ShipmentOrderMainDTO.class);
    ShipmentOrderMainDTO shipmentOrderMainDto1 = new ShipmentOrderMainDTO();
    shipmentOrderMainDto1.setId(1L);
    ShipmentOrderMainDTO shipmentOrderMainDto2 = new ShipmentOrderMainDTO();
    assertThat(shipmentOrderMainDto1).isNotEqualTo(shipmentOrderMainDto2);
    shipmentOrderMainDto2.setId(shipmentOrderMainDto1.getId());
    assertThat(shipmentOrderMainDto1).isEqualTo(shipmentOrderMainDto2);
    shipmentOrderMainDto2.setId(2L);
    assertThat(shipmentOrderMainDto1).isNotEqualTo(shipmentOrderMainDto2);
    shipmentOrderMainDto1.setId(null);
    assertThat(shipmentOrderMainDto1).isNotEqualTo(shipmentOrderMainDto2);
  }
}
