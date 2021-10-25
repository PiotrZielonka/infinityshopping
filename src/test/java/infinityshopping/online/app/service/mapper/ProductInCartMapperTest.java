package infinityshopping.online.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProductInCartMapperTest {

  private ProductInCartMapper productInCartMapper;

  @BeforeEach
  public void setUp() {
    productInCartMapper = new ProductInCartMapperImpl();
  }
}
