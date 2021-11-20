package infinityshopping.online.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ShipmentOrderMainMapperTest {

  private ShipmentOrderMainMapper shipmentOrderMainMapper;

  @BeforeEach
  public void setUp() {
    shipmentOrderMainMapper = new ShipmentOrderMainMapperImpl();
  }
}
