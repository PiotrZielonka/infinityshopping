package infinityshopping.online.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import infinityshopping.online.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ShipmentOrderMainTest {

  @Test
  void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(ShipmentOrderMain.class);
    ShipmentOrderMain shipmentOrderMain1 = new ShipmentOrderMain();
    shipmentOrderMain1.setId(1L);
    ShipmentOrderMain shipmentOrderMain2 = new ShipmentOrderMain();
    shipmentOrderMain2.setId(shipmentOrderMain1.getId());
    assertThat(shipmentOrderMain1).isEqualTo(shipmentOrderMain2);
    shipmentOrderMain2.setId(2L);
    assertThat(shipmentOrderMain1).isNotEqualTo(shipmentOrderMain2);
    shipmentOrderMain1.setId(null);
    assertThat(shipmentOrderMain1).isNotEqualTo(shipmentOrderMain2);
  }
}
