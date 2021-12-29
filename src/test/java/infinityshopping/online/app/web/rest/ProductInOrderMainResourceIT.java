package infinityshopping.online.app.web.rest;

import static infinityshopping.online.app.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import infinityshopping.online.app.IntegrationTest;
import infinityshopping.online.app.config.Constants;
import infinityshopping.online.app.domain.OrderMain;
import infinityshopping.online.app.domain.Product;
import infinityshopping.online.app.domain.ProductInOrderMain;
import infinityshopping.online.app.domain.User;
import infinityshopping.online.app.domain.enumeration.OrderMainStatusEnum;
import infinityshopping.online.app.domain.enumeration.ProductCategoryEnum;
import infinityshopping.online.app.repository.CartRepository;
import infinityshopping.online.app.repository.OrderMainRepository;
import infinityshopping.online.app.repository.ProductInOrderMainRepository;
import infinityshopping.online.app.repository.ProductRepository;
import infinityshopping.online.app.repository.UserRepository;
import infinityshopping.online.app.security.AuthoritiesConstants;
import infinityshopping.online.app.service.AddVat;
import infinityshopping.online.app.service.dto.ProductInOrderMainDTO;
import infinityshopping.online.app.service.mapper.ProductInOrderMainMapper;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

@IntegrationTest
@AutoConfigureMockMvc
class ProductInOrderMainResourceIT implements AddVat {

  private static Random random = new Random();
  private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

  // Product
  private static final ProductCategoryEnum DEFAULT_PRODUCT_CATEGORY_ENUM
      = ProductCategoryEnum.Vitamins;
  private static final ProductCategoryEnum DEFAULT_PRODUCT_CATEGORY_ENUM_2
      = ProductCategoryEnum.Minerals;

  private static final String DEFAULT_NAME = "AAAAAAAAAA";
  private static final String DEFAULT_NAME_2 = "BBBBBBBBB";

  private static final Instant DEFAULT_CREATE_TIME = Instant.ofEpochMilli(0L);
  private static final Instant DEFAULT_CREATE_TIME_2 = Instant.ofEpochMilli(0L);

  private static final Instant DEFAULT_UPDATE_TIME = Instant.ofEpochMilli(0L);
  private static final Instant DEFAULT_UPDATE_TIME_2 = Instant.ofEpochMilli(0L);

  private static BigDecimal DEFAULT_QUANTITY = new BigDecimal(random.nextInt(100 - 1) + 1);
  private static BigDecimal DEFAULT_QUANTITY_2 = new BigDecimal(random.nextInt(100 - 1) + 1);

  private static final BigDecimal DEFAULT_Product_STOCK
      = new BigDecimal(random.nextInt(1000 - 101) + 101);
  private static final BigDecimal DEFAULT_Product_STOCK_2
      = new BigDecimal(random.nextInt(1000 - 101) + 101);

  private static final BigDecimal DEFAULT_PRICE_NET = new BigDecimal("7.53");
  private static final BigDecimal DEFAULT_PRICE_NET_2 = new BigDecimal("250.50");

  private static final BigDecimal DEFAULT_VAT
      = new BigDecimal(random.nextInt(30 - 5) + 5);
  private static final BigDecimal DEFAULT_VAT_2
      = new BigDecimal(random.nextInt(30 - 5) + 5);

  private final BigDecimal defaultPriceGross
      = addVat(DEFAULT_PRICE_NET, DEFAULT_VAT);
  private final BigDecimal defaultPriceGross2
      = addVat(DEFAULT_PRICE_NET_2, DEFAULT_VAT_2);

  private static final BigDecimal DEFAULT_TOTAL_PRICE_NET
      = DEFAULT_QUANTITY.multiply(DEFAULT_PRICE_NET);
  private static final BigDecimal DEFAULT_TOTAL_PRICE_NET_2
      = DEFAULT_QUANTITY_2.multiply(DEFAULT_PRICE_NET_2);

  private final BigDecimal defaultTotalPriceGross
      = DEFAULT_QUANTITY.multiply(defaultPriceGross);

  private final BigDecimal defaultTotalPriceGross2
      = DEFAULT_QUANTITY_2.multiply(defaultPriceGross2);

  private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
  private static final String DEFAULT_DESCRIPTION_2 = "BBBBBBBBB";

  private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
  private static final byte[] DEFAULT_IMAGE_2 = TestUtil.createByteArray(1, "0");

  private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
  private static final String DEFAULT_IMAGE_CONTENT_TYPE_2 = "image/jpg";

  // ProductInOrderMain
  private static final String DEFAULT_CATEGORY = String.valueOf(DEFAULT_PRODUCT_CATEGORY_ENUM);
  private static final String DEFAULT_CATEGORY_2 = String.valueOf(DEFAULT_PRODUCT_CATEGORY_ENUM_2);

  private static BigDecimal UPDATED_QUANTITY = new BigDecimal(random.nextInt(100 - 1) + 1);
  private static BigDecimal UPDATED_HIGHER_QUANTITY
      = new BigDecimal(random.nextInt(2000 - 1001) + 1001);

  private static final Long DEFAULT_PRODUCT_ID = 1L;
  private static final Long DEFAULT_PRODUCT_ID_2 = 2L;

  // OrderMain
  private static final String DEFAULT_BUYER_LOGIN = "AAAAAAAAAA";

  private static final String DEFAULT_BUYER_FIRST_NAME = "AAAAAAAAAA";

  private static final String DEFAULT_BUYER_LAST_NAME = "AAAAAAAAAA";

  private static final String DEFAULT_BUYER_EMAIL = "AAAAAAAAAA";

  private static final String DEFAULT_BUYER_PHONE = "AAAAAAAAAA";

  private static final BigDecimal DEFAULT_AMOUNT_OF_CART_NET
      = DEFAULT_TOTAL_PRICE_NET.add(DEFAULT_TOTAL_PRICE_NET_2);

  private final BigDecimal defaultAmountOfCartGross
      = defaultTotalPriceGross.add(defaultTotalPriceGross2);

  private static final BigDecimal DEFAULT_AMOUNT_OF_SHIPMENT_NET
      = new BigDecimal(random.nextInt(100));

  private final BigDecimal defaultAmountOfShipmentGross
      = addVat(DEFAULT_AMOUNT_OF_SHIPMENT_NET, DEFAULT_VAT);

  private static final BigDecimal DEFAULT_AMOUNT_OF_ORDER_NET
      = DEFAULT_AMOUNT_OF_CART_NET.add(DEFAULT_AMOUNT_OF_SHIPMENT_NET);

  private final BigDecimal defaultAmountOfOrderGross
      = defaultAmountOfCartGross.add(defaultAmountOfShipmentGross);

  private static final OrderMainStatusEnum DEFAULT_ORDERMAIN_STATUS
      = OrderMainStatusEnum.PreparationForShipping;

  private static final Instant DEFAULT_CREATE_TIME_ORDER_MAIN = Instant.now()
      .truncatedTo(ChronoUnit.SECONDS);

  private static final Instant DEFAULT_UPDATE_TIME_ORDER_MAIN = Instant.now()
      .truncatedTo(ChronoUnit.SECONDS);


  private static final String ENTITY_API_URL = "/api/product-in-order-mains";
  private static final String ENTITY_API_URL_ORDER_DETAILS
      = ENTITY_API_URL + "/orderDetails" + "/{id}";
  private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
  private static final String ENTITY_API_URL_BY_ID = ENTITY_API_URL + "/byId/{id}";


  @Autowired
  private ProductInOrderMainRepository productInOrderMainRepository;

  @Autowired
  private ProductInOrderMainMapper productInOrderMainMapper;

  @Autowired
  private EntityManager em;

  @Autowired
  private MockMvc restProductInOrderMainMockMvc;

  @Autowired
  private OrderMainRepository orderMainRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private CartRepository cartRepository;

  @Autowired
  private ProductRepository productRepository;

  private ProductInOrderMain productInOrderMain;

  private ProductInOrderMain productInOrderMain2;

  private OrderMain orderMain;

  private Product product;

  private Product product2;


  public Product createEntityProduct(EntityManager em) {
    Product product = new Product()
        .productCategoryEnum(DEFAULT_PRODUCT_CATEGORY_ENUM)
        .name(DEFAULT_NAME)
        .quantity(DEFAULT_QUANTITY)
        .priceNet(DEFAULT_PRICE_NET)
        .vat(DEFAULT_VAT)
        .priceGross(defaultPriceGross)
        .stock(DEFAULT_Product_STOCK)
        .description(DEFAULT_DESCRIPTION)
        .createTime(DEFAULT_CREATE_TIME)
        .updateTime(DEFAULT_UPDATE_TIME)
        .image(DEFAULT_IMAGE)
        .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE);
    return product;
  }

  public Product createEntityProduct2(EntityManager em) {
    Product product = new Product()
        .productCategoryEnum(DEFAULT_PRODUCT_CATEGORY_ENUM_2)
        .name(DEFAULT_NAME_2)
        .quantity(DEFAULT_QUANTITY_2)
        .priceNet(DEFAULT_PRICE_NET_2)
        .vat(DEFAULT_VAT_2)
        .priceGross(defaultPriceGross2)
        .stock(DEFAULT_Product_STOCK_2)
        .description(DEFAULT_DESCRIPTION)
        .createTime(DEFAULT_CREATE_TIME_2)
        .updateTime(DEFAULT_UPDATE_TIME_2)
        .image(DEFAULT_IMAGE_2)
        .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE_2);
    return product;
  }

  public ProductInOrderMain createEntity(EntityManager em) {
    ProductInOrderMain productInOrderMain = new ProductInOrderMain()
        .category(DEFAULT_CATEGORY)
        .name(DEFAULT_NAME)
        .quantity(DEFAULT_QUANTITY)
        .priceNet(DEFAULT_PRICE_NET)
        .vat(DEFAULT_VAT)
        .priceGross(defaultPriceGross)
        .totalPriceNet(DEFAULT_TOTAL_PRICE_NET)
        .totalPriceGross(defaultTotalPriceGross)
        .stock(DEFAULT_Product_STOCK)
        .description(DEFAULT_DESCRIPTION)
        .image(DEFAULT_IMAGE)
        .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE)
        .productId(DEFAULT_PRODUCT_ID);
    return productInOrderMain;
  }

  public ProductInOrderMain createEntity2(EntityManager em) {
    ProductInOrderMain productInOrderMain2 = new ProductInOrderMain()
        .category(DEFAULT_CATEGORY_2)
        .name(DEFAULT_NAME_2)
        .quantity(DEFAULT_QUANTITY_2)
        .priceNet(DEFAULT_PRICE_NET_2)
        .vat(DEFAULT_VAT_2)
        .priceGross(defaultPriceGross2)
        .totalPriceNet(DEFAULT_TOTAL_PRICE_NET_2)
        .totalPriceGross(defaultTotalPriceGross2)
        .stock(DEFAULT_Product_STOCK_2)
        .description(DEFAULT_DESCRIPTION_2)
        .image(DEFAULT_IMAGE_2)
        .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE_2)
        .productId(DEFAULT_PRODUCT_ID_2);
    return productInOrderMain2;
  }

  public OrderMain createEntityOrderMain(EntityManager em) {
    OrderMain orderMain = new OrderMain()
        .buyerLogin(DEFAULT_BUYER_LOGIN)
        .buyerFirstName(DEFAULT_BUYER_FIRST_NAME)
        .buyerLastName(DEFAULT_BUYER_LAST_NAME)
        .buyerEmail(DEFAULT_BUYER_EMAIL)
        .buyerPhone(DEFAULT_BUYER_PHONE)
        .amountOfCartNet(DEFAULT_AMOUNT_OF_CART_NET)
        .amountOfCartGross(defaultAmountOfCartGross)
        .amountOfShipmentNet(DEFAULT_AMOUNT_OF_SHIPMENT_NET)
        .amountOfShipmentGross(defaultAmountOfShipmentGross)
        .amountOfOrderNet(DEFAULT_AMOUNT_OF_ORDER_NET)
        .amountOfOrderGross(defaultAmountOfOrderGross)
        .orderMainStatus(DEFAULT_ORDERMAIN_STATUS)
        .createTime(DEFAULT_CREATE_TIME_ORDER_MAIN)
        .updateTime(DEFAULT_UPDATE_TIME_ORDER_MAIN);
    return orderMain;
  }

  @BeforeEach
  public void initTest() {
    // given
    // given User
    User user = new User();
    user.setLogin("alice");
    user.setPassword(RandomStringUtils.random(60));
    user.setActivated(true);
    user.setEmail("alice@example.com");
    user.setFirstName("Alice");
    user.setLastName("Something");
    user.setImageUrl("http://placehold.it/50x50");
    user.setLangKey(Constants.DEFAULT_LANGUAGE);

    // given Product
    product = createEntityProduct(em);
    productRepository.save(product);

    // given Product2
    product2 = createEntityProduct2(em);
    productRepository.save(product2);

    // given OrderMain
    orderMain = createEntityOrderMain(em);
    orderMainRepository.save(orderMain);

    // given ProducInOrderMain
    productInOrderMain = createEntity(em);
    productInOrderMain.setProductId(product.getId());
    productInOrderMain.setOrderMain(orderMain);
    productInOrderMainRepository.save(productInOrderMain);

    // given ProducInOrderMain2
    productInOrderMain2 = createEntity2(em);
    productInOrderMain2.setProductId(product2.getId());
    productInOrderMain2.setOrderMain(orderMain);
    productInOrderMainRepository.save(productInOrderMain2);

    orderMain.addProductInOrderMain(productInOrderMain);
    orderMain.addProductInOrderMain(productInOrderMain2);
    orderMainRepository.save(orderMain);
  }

  @Test
  @Transactional
  @WithMockUser(username = "alice", authorities = AuthoritiesConstants.USER)
  void getAllProductInOrderMainsByIdOrderMain() throws Exception {
    // Get the orderMain
    restProductInOrderMainMockMvc.perform(get(ENTITY_API_URL_ORDER_DETAILS
            + "?sort=id,desc", orderMain.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.[0].id").value(productInOrderMain2.getId().intValue()))
        .andExpect(jsonPath("$.[0].category").value(DEFAULT_CATEGORY_2))
        .andExpect(jsonPath("$.[0].name").value(DEFAULT_NAME_2))
        .andExpect(jsonPath("$.[0].quantity").value(sameNumber(DEFAULT_QUANTITY_2)))
        .andExpect(jsonPath("$.[0].priceNet").value(sameNumber(DEFAULT_PRICE_NET_2)))
        .andExpect(jsonPath("$.[0].vat").value(sameNumber(DEFAULT_VAT_2)))
        .andExpect(jsonPath("$.[0].priceGross").value(sameNumber(defaultPriceGross2)))
        .andExpect(jsonPath("$.[0].totalPriceNet")
            .value(sameNumber(DEFAULT_TOTAL_PRICE_NET_2)))
        .andExpect(jsonPath("$.[0].totalPriceGross")
            .value(sameNumber(defaultTotalPriceGross2)))
        .andExpect(jsonPath("$.[0].stock").value(sameNumber(DEFAULT_Product_STOCK_2)))
        .andExpect(jsonPath("$.[0].description").value(DEFAULT_DESCRIPTION_2.toString()))
        .andExpect(jsonPath("$.[0].imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE_2))
        .andExpect(jsonPath("$.[0].image")
            .value(Base64Utils.encodeToString(DEFAULT_IMAGE_2)))
        .andExpect(jsonPath("$.[0].productId").value(product2.getId()))
        .andExpect(jsonPath("$.[1].id").value(productInOrderMain.getId().intValue()))
        .andExpect(jsonPath("$.[1].category").value(DEFAULT_CATEGORY))
        .andExpect(jsonPath("$.[1].name").value(DEFAULT_NAME))
        .andExpect(jsonPath("$.[1].quantity").value(sameNumber(DEFAULT_QUANTITY)))
        .andExpect(jsonPath("$.[1].priceNet").value(sameNumber(DEFAULT_PRICE_NET)))
        .andExpect(jsonPath("$.[1].vat").value(sameNumber(DEFAULT_VAT)))
        .andExpect(jsonPath("$.[1].priceGross").value(sameNumber(defaultPriceGross)))
        .andExpect(jsonPath("$.[1].totalPriceNet")
            .value(sameNumber(DEFAULT_TOTAL_PRICE_NET)))
        .andExpect(jsonPath("$.[1].totalPriceGross")
            .value(sameNumber(defaultTotalPriceGross)))
        .andExpect(jsonPath("$.[1].stock").value(sameNumber(DEFAULT_Product_STOCK)))
        .andExpect(jsonPath("$.[1].description").value(DEFAULT_DESCRIPTION.toString()))
        .andExpect(jsonPath("$.[1].imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
        .andExpect(jsonPath("$.[1].image")
            .value(Base64Utils.encodeToString(DEFAULT_IMAGE)))
        .andExpect(jsonPath("$.[1].productId").value(product.getId()));
  }

  @Test
  @Transactional
  void getAllProductInOrderMainsByAnyoneShouldThrowStatusUnauthorized() throws Exception {
    // Get the orderMain
    restProductInOrderMainMockMvc.perform(get(ENTITY_API_URL_ORDER_DETAILS
            + "?sort=id,desc", orderMain.getId()))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @Transactional
  @WithMockUser(username = "alice", authorities = AuthoritiesConstants.USER)
  void getProductInOrderMain() throws Exception {
    // Get the productInOrderMain
    restProductInOrderMainMockMvc.perform(get(
        ENTITY_API_URL + "/byId/{id}", productInOrderMain.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.id").value(productInOrderMain.getId().intValue()))
        .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY))
        .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
        .andExpect(jsonPath("$.quantity").value(sameNumber(DEFAULT_QUANTITY)))
        .andExpect(jsonPath("$.priceNet").value(sameNumber(DEFAULT_PRICE_NET)))
        .andExpect(jsonPath("$.vat").value(sameNumber(DEFAULT_VAT)))
        .andExpect(jsonPath("$.priceGross").value(sameNumber(defaultPriceGross)))
        .andExpect(jsonPath("$.totalPriceNet").value(sameNumber(DEFAULT_TOTAL_PRICE_NET)))
        .andExpect(jsonPath("$.totalPriceGross").value(sameNumber(defaultTotalPriceGross)))
        .andExpect(jsonPath("$.stock").value(sameNumber(DEFAULT_Product_STOCK)))
        .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
        .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
        .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)))
        .andExpect(jsonPath("$.productId").value(product.getId()));
  }

  @Test
  @Transactional
  void getProductInOrderMainByAnyoneShouldThrowStatusUnauthorized()throws Exception {
    // Get the productInOrderMain
    restProductInOrderMainMockMvc.perform(get(ENTITY_API_URL_BY_ID, productInOrderMain.getId()))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @Transactional
  @WithMockUser(username = "alice", authorities = AuthoritiesConstants.USER)
  void getNonExistingProductInOrderMain() throws Exception {
    // Get the productInOrderMain
    restProductInOrderMainMockMvc.perform(get(ENTITY_API_URL_BY_ID, Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  @WithMockUser(username = "admin", password = "admin", authorities = AuthoritiesConstants.ADMIN)
  void putNewProductInOrder() throws Exception {
    final int databaseOrderMainSizeBeforeUpdate = orderMainRepository.findAll().size();
    final int databaseProductInOrderMainSizeBeforeUpdate
        = productInOrderMainRepository.findAll().size();

    // Update the quantity of productInOrderMain
    ProductInOrderMainDTO productInOrderMainDto = productInOrderMainMapper.toDto(
        productInOrderMainRepository.findById(productInOrderMain.getId()).get());
    productInOrderMainDto.setQuantity(UPDATED_QUANTITY);

    restProductInOrderMainMockMvc.perform(put(ENTITY_API_URL_ID, productInOrderMainDto.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(productInOrderMainDto)))
        .andExpect(status().isOk());

    // Validate the ProductInOrderMain in the database
    List<ProductInOrderMain> productInOrderMainList = productInOrderMainRepository.findAll();
    assertThat(productInOrderMainList).hasSize(databaseProductInOrderMainSizeBeforeUpdate);
    ProductInOrderMain testProductInOrderMain
        = productInOrderMainList.get(productInOrderMainList.size() - 2);
    assertThat(testProductInOrderMain.getCategory()).isEqualTo(DEFAULT_CATEGORY);
    assertThat(testProductInOrderMain.getName()).isEqualTo(DEFAULT_NAME);
    assertThat(testProductInOrderMain.getQuantity()).isEqualTo(UPDATED_QUANTITY);
    assertThat(testProductInOrderMain.getPriceNet()).isEqualTo(DEFAULT_PRICE_NET);
    assertThat(testProductInOrderMain.getVat()).isEqualTo(DEFAULT_VAT);
    assertThat(testProductInOrderMain.getPriceGross()).isEqualTo(defaultPriceGross);
    assertThat(testProductInOrderMain.getTotalPriceNet())
        .isEqualTo(UPDATED_QUANTITY.multiply(DEFAULT_PRICE_NET));
    assertThat(testProductInOrderMain.getTotalPriceGross())
        .isEqualTo(UPDATED_QUANTITY.multiply(defaultPriceGross));
    assertThat(testProductInOrderMain.getStock()).isEqualTo(DEFAULT_Product_STOCK);
    assertThat(testProductInOrderMain.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    assertThat(testProductInOrderMain.getImage()).isEqualTo(DEFAULT_IMAGE);
    assertThat(testProductInOrderMain.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);

    // Validate the OrderMain in the database
    List<OrderMain> orderMainList = orderMainRepository.findAll();
    assertThat(orderMainList).hasSize(databaseOrderMainSizeBeforeUpdate);
  }

  @Test
  @Transactional
  @WithMockUser(username = "admin", password = "admin", authorities = AuthoritiesConstants.ADMIN)
  void shouldSetProperAmountsInOrderMainAfterEditingProductInOrder() throws Exception {
    final int databaseOrderMainSizeBeforeUpdate = orderMainRepository.findAll().size();
    final int databaseProductInOrderMainSizeBeforeUpdate
        = productInOrderMainRepository.findAll().size();

    // Update the quantity of productInOrderMain
    ProductInOrderMainDTO productInOrderMainDto = productInOrderMainMapper.toDto(
        productInOrderMainRepository.findById(productInOrderMain.getId()).get());
    productInOrderMainDto.setQuantity(UPDATED_QUANTITY);

    restProductInOrderMainMockMvc.perform(put(ENTITY_API_URL_ID, productInOrderMainDto.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(productInOrderMainDto)))
        .andExpect(status().isOk());

    // Validate the OrderMain in the database
    List<OrderMain> orderMainList = orderMainRepository.findAll();
    assertThat(orderMainList).hasSize(databaseOrderMainSizeBeforeUpdate);
    OrderMain testOrderMain = orderMainList.get(orderMainList.size() - 1);
    assertThat(testOrderMain.getBuyerLogin()).isEqualTo(DEFAULT_BUYER_LOGIN);
    assertThat(testOrderMain.getBuyerFirstName()).isEqualTo(DEFAULT_BUYER_FIRST_NAME);
    assertThat(testOrderMain.getBuyerLastName()).isEqualTo(DEFAULT_BUYER_LAST_NAME);
    assertThat(testOrderMain.getBuyerEmail()).isEqualTo(DEFAULT_BUYER_EMAIL);
    assertThat(testOrderMain.getBuyerPhone()).isEqualTo(DEFAULT_BUYER_PHONE);
    assertThat(testOrderMain.getAmountOfCartNet())
        .isEqualTo((UPDATED_QUANTITY.multiply(DEFAULT_PRICE_NET))
            .add((DEFAULT_QUANTITY_2.multiply(DEFAULT_PRICE_NET_2))));
    assertThat(testOrderMain.getAmountOfCartGross())
        .isEqualTo((UPDATED_QUANTITY.multiply(defaultPriceGross))
            .add((DEFAULT_QUANTITY_2.multiply(defaultPriceGross2))));
    assertThat(testOrderMain.getAmountOfShipmentNet()).isEqualTo(DEFAULT_AMOUNT_OF_SHIPMENT_NET);
    assertThat(testOrderMain.getAmountOfShipmentGross()).isEqualTo(defaultAmountOfShipmentGross);
    assertThat(testOrderMain.getAmountOfOrderNet())
        .isEqualTo((UPDATED_QUANTITY.multiply(DEFAULT_PRICE_NET))
            .add((DEFAULT_QUANTITY_2.multiply(DEFAULT_PRICE_NET_2))
                .add(DEFAULT_AMOUNT_OF_SHIPMENT_NET)));
    assertThat(testOrderMain.getAmountOfOrderGross())
        .isEqualTo((UPDATED_QUANTITY.multiply(defaultPriceGross))
            .add((DEFAULT_QUANTITY_2.multiply(defaultPriceGross2)).add(
                defaultAmountOfShipmentGross)));
    assertThat(testOrderMain.getOrderMainStatus()).isEqualTo(DEFAULT_ORDERMAIN_STATUS);
    assertNotNull(testOrderMain.getCreateTime());
    assertNotNull(testOrderMain.getUpdateTime());

    // Validate the ProductInOrderMain in the database
    List<ProductInOrderMain> productInOrderMainList = productInOrderMainRepository.findAll();
    assertThat(productInOrderMainList).hasSize(databaseProductInOrderMainSizeBeforeUpdate);
  }

  @Test
  @Transactional
  @WithMockUser(username = "admin", password = "admin", authorities = AuthoritiesConstants.ADMIN)
  void putNewProductInOrderMainWithHigherQuantityShouldThrowBadRequest() throws Exception {
    final int databaseProductInOrderMainSizeBeforeUpdate
        = productInOrderMainRepository.findAll().size();

    // Update the higher quantity of productInOrderMain
    ProductInOrderMainDTO productInOrderMainDto = productInOrderMainMapper.toDto(
        productInOrderMainRepository.findById(productInOrderMain.getId()).get());
    productInOrderMainDto.setQuantity(UPDATED_HIGHER_QUANTITY);

    restProductInOrderMainMockMvc.perform(put(ENTITY_API_URL_ID, productInOrderMainDto.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(productInOrderMainDto)))
        .andExpect(status().isBadRequest());

    // Validate the ProductInOrderMain in the database
    List<ProductInOrderMain> productInOrderMaintList = productInOrderMainRepository.findAll();
    assertThat(productInOrderMaintList).hasSize(databaseProductInOrderMainSizeBeforeUpdate);
  }

  @Test
  @Transactional
  @WithMockUser(username = "alice", authorities = AuthoritiesConstants.USER)
  void putNewProductInOrderMainByUserShouldThrowStatusForbidden() throws Exception {
    final int databaseProductInOrderMainSizeBeforeUpdate
        = productInOrderMainRepository.findAll().size();

    // Update the quantity of productInOrderMain
    ProductInOrderMainDTO productInOrderMainDto = productInOrderMainMapper.toDto(
        productInOrderMainRepository.findById(productInOrderMain.getId()).get());
    productInOrderMainDto.setQuantity(UPDATED_QUANTITY);

    restProductInOrderMainMockMvc.perform(put(ENTITY_API_URL_ID, productInOrderMainDto.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(productInOrderMainDto)))
        .andExpect(status().isForbidden());

    // Validate the ProductInOrderMain in the database
    List<ProductInOrderMain> productInOrderMaintList = productInOrderMainRepository.findAll();
    assertThat(productInOrderMaintList).hasSize(databaseProductInOrderMainSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void putNewProductInOrderMainByAnyoneShouldThrowStatusUnauthorized() throws Exception {
    final int databaseProductInOrderMainSizeBeforeUpdate
        = productInOrderMainRepository.findAll().size();

    // Update the quantity of productInOrderMain
    ProductInOrderMainDTO productInOrderMainDto = productInOrderMainMapper.toDto(
        productInOrderMainRepository.findById(productInOrderMain.getId()).get());
    productInOrderMainDto.setQuantity(UPDATED_QUANTITY);

    restProductInOrderMainMockMvc.perform(put(ENTITY_API_URL_ID, productInOrderMainDto.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(productInOrderMainDto)))
        .andExpect(status().isUnauthorized());

    // Validate the ProductInOrderMain in the database
    List<ProductInOrderMain> productInOrderMaintList = productInOrderMainRepository.findAll();
    assertThat(productInOrderMaintList).hasSize(databaseProductInOrderMainSizeBeforeUpdate);
  }

  @Test
  @Transactional
  @WithMockUser(username = "admin", password = "admin", authorities = AuthoritiesConstants.ADMIN)
  void putNonExistingProductInOrderMain() throws Exception {
    final int databaseProductInOrderMainSizeBeforeUpdate
        = productInOrderMainRepository.findAll().size();

    // Update the productInOrderMain
    ProductInOrderMainDTO productInOrderMainDto = productInOrderMainMapper.toDto(
        productInOrderMainRepository.findById(productInOrderMain.getId()).get());
    productInOrderMainDto.setId(count.incrementAndGet());

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restProductInOrderMainMockMvc.perform(put(ENTITY_API_URL_ID, productInOrderMainDto.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(productInOrderMainDto)))
        .andExpect(status().isBadRequest());

    // Validate the ProductInOrderMain in the database
    List<ProductInOrderMain> productInOrderMainList = productInOrderMainRepository.findAll();
    assertThat(productInOrderMainList).hasSize(databaseProductInOrderMainSizeBeforeUpdate);
  }

  @Test
  @Transactional
  @WithMockUser(username = "admin", password = "admin", authorities = AuthoritiesConstants.ADMIN)
  void putWithIdMismatchProductInOrderMain() throws Exception {
    final int databaseProductInOrderMainSizeBeforeUpdate
        = productInOrderMainRepository.findAll().size();

    // Update the productInOrderMain
    ProductInOrderMainDTO productInOrderMainDto = productInOrderMainMapper.toDto(
        productInOrderMainRepository.findById(productInOrderMain.getId()).get());
    productInOrderMainDto.setId(count.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restProductInOrderMainMockMvc.perform(put(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(productInOrderMainDto)))
        .andExpect(status().isBadRequest());

    // Validate the ProductInOrderMain in the database
    List<ProductInOrderMain> productInOrderMainList = productInOrderMainRepository.findAll();
    assertThat(productInOrderMainList).hasSize(databaseProductInOrderMainSizeBeforeUpdate);
  }

  @Test
  @Transactional
  @WithMockUser(username = "admin", password = "admin", authorities = AuthoritiesConstants.ADMIN)
  void putWithMissingIdPathParamProductInOrderMain() throws Exception {
    final int databaseProductInOrderMainSizeBeforeUpdate
        = productInOrderMainRepository.findAll().size();

    // Update the productInOrderMain
    ProductInOrderMainDTO productInOrderMainDto = productInOrderMainMapper.toDto(
        productInOrderMainRepository.findById(productInOrderMain.getId()).get());
    productInOrderMainDto.setId(count.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restProductInOrderMainMockMvc.perform(put(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(productInOrderMainDto)))
        .andExpect(status().isMethodNotAllowed());

    // Validate the ProductInOrderMain in the database
    List<ProductInOrderMain> productInOrderMainList = productInOrderMainRepository.findAll();
    assertThat(productInOrderMainList).hasSize(databaseProductInOrderMainSizeBeforeUpdate);
  }

  @Test
  @Transactional
  @WithMockUser(username = "admin", password = "admin", authorities = AuthoritiesConstants.ADMIN)
  void deleteProductInOrderMain() throws Exception {
    int databaseProductInOrderMainSizeBeforeDelete = productInOrderMainRepository.findAll().size();

    // Delete the productInOrderMain
    restProductInOrderMainMockMvc.perform(delete(ENTITY_API_URL_ID, productInOrderMain.getId())
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent());

    // Validate the ProductInOrderMain in the database
    List<ProductInOrderMain> productInOrderMainList = productInOrderMainRepository.findAll();
    assertThat(productInOrderMainList).hasSize(databaseProductInOrderMainSizeBeforeDelete - 1);

    // Validate the OrderMain amounts in the database
    List<OrderMain> orderMainList = orderMainRepository.findAll();
    OrderMain testOrderMain = orderMainList.get(orderMainList.size() - 1);
    assertThat(testOrderMain.getAmountOfCartNet()).isEqualTo(DEFAULT_TOTAL_PRICE_NET_2);
    assertThat(testOrderMain.getAmountOfCartGross()).isEqualTo(defaultTotalPriceGross2);
    assertThat(testOrderMain.getAmountOfShipmentNet()).isEqualTo(DEFAULT_AMOUNT_OF_SHIPMENT_NET);
    assertThat(testOrderMain.getAmountOfShipmentGross()).isEqualTo(defaultAmountOfShipmentGross);
    assertThat(testOrderMain.getAmountOfOrderNet())
        .isEqualTo(DEFAULT_TOTAL_PRICE_NET_2.add(DEFAULT_AMOUNT_OF_SHIPMENT_NET));
    assertThat(testOrderMain.getAmountOfOrderGross())
        .isEqualTo(defaultTotalPriceGross2.add(defaultAmountOfShipmentGross));
  }

  @Test
  @Transactional
  @WithMockUser(username = "alice", authorities = AuthoritiesConstants.USER)
  void deleteProductInOrderMainByUserShouldThrowStatusForbidden() throws Exception {
    int databaseProductInOrderMainSizeBeforeDelete = productInOrderMainRepository.findAll().size();

    // Delete the productInOrderMain
    restProductInOrderMainMockMvc.perform(delete(ENTITY_API_URL_ID, productInOrderMain.getId())
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isForbidden());

    // Validate the ProductInOrderMain in the database
    List<ProductInOrderMain> productInOrderMainList = productInOrderMainRepository.findAll();
    assertThat(productInOrderMainList).hasSize(databaseProductInOrderMainSizeBeforeDelete);
  }

  @Test
  @Transactional
  void deleteProductInOrderMainByAnyoneShouldThrowStatusUnauthorized() throws Exception {
    int databaseProductInOrderMainSizeBeforeDelete = productInOrderMainRepository.findAll().size();

    // Delete the productInOrderMain
    restProductInOrderMainMockMvc.perform(delete(ENTITY_API_URL_ID, productInOrderMain.getId())
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized());

    // Validate the ProductInOrderMain in the database
    List<ProductInOrderMain> productInOrderMainList = productInOrderMainRepository.findAll();
    assertThat(productInOrderMainList).hasSize(databaseProductInOrderMainSizeBeforeDelete);
  }
}
