package infinityshopping.online.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PaymentMapperTest {

  private PaymentMapper paymentMapper;

  @BeforeEach
  public void setUp() {
    paymentMapper = new PaymentMapperImpl();
  }
}
