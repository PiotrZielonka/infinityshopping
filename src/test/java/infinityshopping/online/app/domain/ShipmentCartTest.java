package infinityshopping.online.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import infinityshopping.online.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ShipmentCartTest {

  @Test
  void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(ShipmentCart.class);
    ShipmentCart shipmentCart1 = new ShipmentCart();
    shipmentCart1.setId(1L);
    ShipmentCart shipmentCart2 = new ShipmentCart();
    shipmentCart2.setId(shipmentCart1.getId());
    assertThat(shipmentCart1).isEqualTo(shipmentCart2);
    shipmentCart2.setId(2L);
    assertThat(shipmentCart1).isNotEqualTo(shipmentCart2);
    shipmentCart1.setId(null);
    assertThat(shipmentCart1).isNotEqualTo(shipmentCart2);
  }
}
