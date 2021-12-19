package infinityshopping.online.app.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import infinityshopping.online.app.InfinityshoppingApp;
import infinityshopping.online.app.domain.Product;
import infinityshopping.online.app.domain.enumeration.ProductCategoryEnum;
import infinityshopping.online.app.repository.ProductRepository;
import infinityshopping.online.app.service.dto.ProductDTO;
import infinityshopping.online.app.service.mapper.ProductMapper;
import infinityshopping.online.app.web.rest.TestUtil;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(classes = InfinityshoppingApp.class)
class ProductServiceImplTests {

  private static final ProductCategoryEnum DEFAULT_PRODUCT_CATEGORY_ENUM
      = ProductCategoryEnum.Vitamins;
  private static final ProductCategoryEnum UPDATED_PRODUCT_CATEGORY_ENUM
      = ProductCategoryEnum.Minerals;

  private static final String DEFAULT_NAME = "AAAAAAAAAA";
  private static final String UPDATED_NAME = "BBBBBBBBBB";

  private static BigDecimal DEFAULT_QUANTITY = BigDecimal.ONE;
  private static BigDecimal UPDATED_QUANTITY = new BigDecimal("2");

  private static BigDecimal DEFAULT_PRICE_NET = new BigDecimal("100");
  private static BigDecimal UPDATED_PRICE_NET = new BigDecimal("155");

  private static BigDecimal DEFAULT_VAT = new BigDecimal("23");
  private static BigDecimal UPDATED_VAT = new BigDecimal("8");

  private static BigDecimal DEFAULT_PROPER_PRICE_GROSS = new BigDecimal("123.00");
  private static BigDecimal UPDATED_PROPER_PRICE_GROSS = new BigDecimal("167.40");
  private static BigDecimal DEFAULT_FAKE_PRICE_GROSS = BigDecimal.ZERO;

  private static BigDecimal DEFAULT_STOCK = BigDecimal.ZERO;
  private static BigDecimal UPDATED_STOCK = BigDecimal.ONE;

  private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
  private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

  private static final Instant DEFAULT_CREATE_TIME = Instant.ofEpochMilli(0L);
  private static final Instant UPDATED_CREATE_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

  private static final Instant DEFAULT_UPDATE_TIME = Instant.ofEpochMilli(0L);
  private static final Instant UPDATED_UPDATE_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

  private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
  private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(1, "1");
  private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
  private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

  @Autowired
  private ProductServiceImpl productServiceImpl;

  @Autowired
  private ProductMapper productMapper;

  @Autowired
  private EntityManager em;

  @Autowired
  private ProductRepository productRepository;

  private Product product;

  public static Product createEntity(EntityManager em) {
    Product product = new Product()
        .productCategoryEnum(DEFAULT_PRODUCT_CATEGORY_ENUM)
        .name(DEFAULT_NAME)
        .quantity(DEFAULT_QUANTITY)
        .priceNet(DEFAULT_PRICE_NET)
        .vat(DEFAULT_VAT)
        .priceGross(DEFAULT_FAKE_PRICE_GROSS)
        .stock(DEFAULT_STOCK)
        .description(DEFAULT_DESCRIPTION)
        .createTime(DEFAULT_CREATE_TIME)
        .updateTime(DEFAULT_UPDATE_TIME)
        .image(DEFAULT_IMAGE)
        .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE);
    return product;
  }

  @BeforeEach
  public void initTest() throws Exception {
    product = createEntity(em);
  }

  @Test
  @Transactional
  void shouldSaveProductAndSetProperPriceGrossAutomatic() throws Exception {
    // given
    final int databaseSizeBeforeSave = productRepository.findAll().size();
    ProductDTO productDto = productMapper.toDto(product);

    // when
    productServiceImpl.save(productDto);

    // then
    List<Product> productList = productRepository.findAll();
    assertThat(productList).hasSize(databaseSizeBeforeSave + 1);
    Product testProduct = productList.get(productList.size() - 1);
    assertThat(testProduct.getProductCategoryEnum()).isEqualTo(DEFAULT_PRODUCT_CATEGORY_ENUM);
    assertThat(testProduct.getName()).isEqualTo(DEFAULT_NAME);
    assertThat(testProduct.getQuantity()).isEqualTo(BigDecimal.ONE);
    assertThat(testProduct.getPriceNet()).isEqualTo(DEFAULT_PRICE_NET);
    assertThat(testProduct.getVat()).isEqualTo(DEFAULT_VAT);
    assertThat(testProduct.getPriceGross()).isEqualTo(DEFAULT_PROPER_PRICE_GROSS);
    assertThat(testProduct.getStock()).isEqualTo(DEFAULT_STOCK);
    assertThat(testProduct.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    assertThat(testProduct.getImage()).isEqualTo(DEFAULT_IMAGE);
    assertThat(testProduct.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
    assertNotNull(testProduct.getCreateTime());
    assertNotNull(testProduct.getUpdateTime());
  }

  @Test
  @Transactional
  void shouldUpdateExistProductAndSetProperPriceGrossAutomatic() throws Exception {
    // given
    productRepository.saveAndFlush(product);
    final int databaseSizeBeforeUpdate = productRepository.findAll().size();

    Product updatedProduct = productRepository.findById(product.getId()).get();

    ProductDTO productDto = productMapper.toDto(updatedProduct);
    productDto.setProductCategoryEnum(UPDATED_PRODUCT_CATEGORY_ENUM);
    productDto.setName(UPDATED_NAME);
    productDto.setQuantity(BigDecimal.ONE);
    productDto.setPriceNet(UPDATED_PRICE_NET);
    productDto.setVat(UPDATED_VAT);
    productDto.setStock(UPDATED_STOCK);
    productDto.setDescription(UPDATED_DESCRIPTION);
    productDto.setCreateTime(UPDATED_CREATE_TIME);
    productDto.setUpdateTime(UPDATED_UPDATE_TIME);
    productDto.setImage(UPDATED_IMAGE);
    productDto.setImageContentType(UPDATED_IMAGE_CONTENT_TYPE);

    // when
    productServiceImpl.save(productDto);

    // then
    List<Product> productList = productRepository.findAll();
    assertThat(productList).hasSize(databaseSizeBeforeUpdate);
    Product testProduct = productList.get(productList.size() - 1);
    assertThat(testProduct.getProductCategoryEnum()).isEqualTo(UPDATED_PRODUCT_CATEGORY_ENUM);
    assertThat(testProduct.getName()).isEqualTo(UPDATED_NAME);
    assertThat(testProduct.getQuantity()).isEqualTo(BigDecimal.ONE);
    assertThat(testProduct.getPriceNet()).isEqualTo(UPDATED_PRICE_NET);
    assertThat(testProduct.getVat()).isEqualTo(UPDATED_VAT);
    assertThat(testProduct.getPriceGross()).isEqualTo(UPDATED_PROPER_PRICE_GROSS);
    assertThat(testProduct.getStock()).isEqualTo(UPDATED_STOCK);
    assertThat(testProduct.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    assertThat(testProduct.getImage()).isEqualTo(UPDATED_IMAGE);
    assertThat(testProduct.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
    assertNotNull(testProduct.getCreateTime());
    assertNotNull(testProduct.getUpdateTime());
  }

  @Test
  @Transactional
  void shouldSetQuantityOneWhileProductIsSavedBecauseAcustomerCanBuyAlwaysAtLeastOneProduct()
      throws Exception {
    // given
    final int databaseSizeBeforeSave = productRepository.findAll().size();

    ProductDTO productDto = productMapper.toDto(product);
    productDto.setQuantity(BigDecimal.ZERO);

    // when
    productServiceImpl.save(productDto);

    // then
    List<Product> productList = productRepository.findAll();
    assertThat(productList).hasSize(databaseSizeBeforeSave + 1);
    Product testProduct = productList.get(productList.size() - 1);
    assertThat(testProduct.getProductCategoryEnum()).isEqualTo(DEFAULT_PRODUCT_CATEGORY_ENUM);
    assertThat(testProduct.getName()).isEqualTo(DEFAULT_NAME);
    assertThat(testProduct.getQuantity()).isEqualTo(BigDecimal.ONE);
    assertThat(testProduct.getPriceNet()).isEqualTo(DEFAULT_PRICE_NET);
    assertThat(testProduct.getVat()).isEqualTo(DEFAULT_VAT);
    assertThat(testProduct.getPriceGross()).isEqualTo(DEFAULT_PROPER_PRICE_GROSS);
    assertThat(testProduct.getStock()).isEqualTo(DEFAULT_STOCK);
    assertThat(testProduct.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    assertThat(testProduct.getImage()).isEqualTo(DEFAULT_IMAGE);
    assertThat(testProduct.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
    assertNotNull(testProduct.getCreateTime());
    assertNotNull(testProduct.getUpdateTime());
  }

  @Test
  @Transactional
  void shouldFindProductById() throws Exception {
    // given
    product.setPriceGross(DEFAULT_PROPER_PRICE_GROSS);
    productRepository.saveAndFlush(product);

    // when
    Optional<ProductDTO> testProduct = productServiceImpl.findOne(product.getId());

    // then
    assertThat(testProduct.get().getId()).isEqualTo(product.getId());
    assertThat(testProduct.get().getProductCategoryEnum()).isEqualTo(
        product.getProductCategoryEnum());
    assertThat(testProduct.get().getName()).isEqualTo(product.getName());
    assertThat(testProduct.get().getQuantity()).isEqualTo(product.getQuantity());
    assertThat(testProduct.get().getPriceNet()).isEqualTo(DEFAULT_PRICE_NET);
    assertThat(testProduct.get().getVat()).isEqualTo(DEFAULT_VAT);
    assertThat(testProduct.get().getPriceGross()).isEqualTo(DEFAULT_PROPER_PRICE_GROSS);
    assertThat(testProduct.get().getStock()).isEqualTo(product.getStock());
    assertThat(testProduct.get().getDescription()).isEqualTo(product.getDescription());
    assertThat(testProduct.get().getCreateTime()).isEqualTo(product.getCreateTime());
    assertThat(testProduct.get().getUpdateTime()).isEqualTo(product.getUpdateTime());
    assertThat(testProduct.get().getImageContentType()).isEqualTo(product.getImageContentType());
    assertThat(testProduct.get().getImage()).isEqualTo(product.getImage());
  }

  @Test
  @Transactional
  void shouldDeleteProduct() throws Exception {
    // given
    productRepository.saveAndFlush(product);

    int databaseSizeBeforeDelete = productRepository.findAll().size();

    // when
    productServiceImpl.delete(product.getId());

    // then
    List<Product> productList = productRepository.findAll();
    assertThat(productList).hasSize(databaseSizeBeforeDelete - 1);
  }
}
