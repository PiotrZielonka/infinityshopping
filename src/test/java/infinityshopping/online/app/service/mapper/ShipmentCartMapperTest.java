package infinityshopping.online.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ShipmentCartMapperTest {

    private ShipmentCartMapper shipmentCartMapper;

    @BeforeEach
    public void setUp() {
        shipmentCartMapper = new ShipmentCartMapperImpl();
    }
}
