package infinityshopping.online.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PaymentOrderMainMapperTest {

  private PaymentOrderMainMapper paymentOrderMainMapper;

  @BeforeEach
  public void setUp() {
    paymentOrderMainMapper = new PaymentOrderMainMapperImpl();
  }
}
