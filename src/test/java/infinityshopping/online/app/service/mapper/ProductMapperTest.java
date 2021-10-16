package infinityshopping.online.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProductMapperTest {

  private ProductMapper productMapper;

  @BeforeEach
  public void setUp() {
    productMapper = new ProductMapperImpl();
  }
}
