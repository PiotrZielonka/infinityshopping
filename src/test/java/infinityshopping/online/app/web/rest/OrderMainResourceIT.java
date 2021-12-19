package infinityshopping.online.app.web.rest;

import static infinityshopping.online.app.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import infinityshopping.online.app.IntegrationTest;
import infinityshopping.online.app.config.Constants;
import infinityshopping.online.app.domain.Cart;
import infinityshopping.online.app.domain.OrderMain;
import infinityshopping.online.app.domain.PaymentCart;
import infinityshopping.online.app.domain.PaymentOrderMain;
import infinityshopping.online.app.domain.Product;
import infinityshopping.online.app.domain.ProductInCart;
import infinityshopping.online.app.domain.ProductInOrderMain;
import infinityshopping.online.app.domain.ShipmentCart;
import infinityshopping.online.app.domain.ShipmentOrderMain;
import infinityshopping.online.app.domain.User;
import infinityshopping.online.app.domain.enumeration.OrderMainStatusEnum;
import infinityshopping.online.app.domain.enumeration.PaymentStatusEnum;
import infinityshopping.online.app.domain.enumeration.ProductCategoryEnum;
import infinityshopping.online.app.repository.CartRepository;
import infinityshopping.online.app.repository.OrderMainRepository;
import infinityshopping.online.app.repository.PaymentCartRepository;
import infinityshopping.online.app.repository.PaymentOrderMainRepository;
import infinityshopping.online.app.repository.ProductInCartRepository;
import infinityshopping.online.app.repository.ProductInOrderMainRepository;
import infinityshopping.online.app.repository.ProductRepository;
import infinityshopping.online.app.repository.ShipmentCartRepository;
import infinityshopping.online.app.repository.ShipmentOrderMainRepository;
import infinityshopping.online.app.repository.UserRepository;
import infinityshopping.online.app.security.AuthoritiesConstants;
import infinityshopping.online.app.security.SecurityUtils;
import infinityshopping.online.app.service.AddVat;
import infinityshopping.online.app.service.UserNotFoundException;
import infinityshopping.online.app.service.dto.OrderMainDTO;
import infinityshopping.online.app.service.mapper.OrderMainMapper;
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


@IntegrationTest
@AutoConfigureMockMvc
class OrderMainResourceIT implements AddVat {

  private static Random random = new Random();
  private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

  // OrderMain
  private static final String DEFAULT_BUYER_LOGIN = "AAAAAAAAAA";
  private static final String DEFAULT_BUYER_FIRST_NAME = "AAAAAAAAAA";
  private static final String DEFAULT_BUYER_LAST_NAME = "AAAAAAAAAA";
  private static final String DEFAULT_BUYER_EMAIL = "AAAAAAAAAA";
  private static final String DEFAULT_BUYER_PHONE = "AAAAAAAAAA";

  private static final OrderMainStatusEnum DEFAULT_ORDERMAIN_STATUS
      = OrderMainStatusEnum.PreparationForShipping;
  private static final OrderMainStatusEnum UPDATED_ORDERMAIN_STATUS
      = OrderMainStatusEnum.Delivered;

  private static final Instant DEFAULT_CREATE_TIME = Instant.now().truncatedTo(ChronoUnit.SECONDS);
  private static final Instant DEFAULT_UPDATE_TIME = Instant.now().truncatedTo(ChronoUnit.SECONDS);

  // Product
  private static final ProductCategoryEnum DEFAULT_PRODUCT_CATEGORY_ENUM_Vitamins
      = ProductCategoryEnum.Vitamins;

  private static final ProductCategoryEnum DEFAULT_PRODUCT_CATEGORY_ENUM_Minerals
      = ProductCategoryEnum.Minerals;

  private static final String DEFAULT_Product_NAME = "AAAAAAAAAA";
  private static final String DEFAULT_Product_NAME_2 = "BBBBBBBBB";

  private static final BigDecimal DEFAULT_Product_QUANTITY
      = new BigDecimal(random.nextInt(100 - 1) + 1);
  private static final BigDecimal DEFAULT_Product_QUANTITY_2
      = new BigDecimal(random.nextInt(100 - 1) + 1);

  private static final BigDecimal DEFAULT_Product_PRICE_NET
      = new BigDecimal(random.nextInt(10000));
  private static final BigDecimal DEFAULT_Product_PRICE_NET_2
      = new BigDecimal(random.nextInt(10000));

  private static final BigDecimal DEFAULT_Product_VAT
      = new BigDecimal(random.nextInt(30 - 5) + 5);
  private static final BigDecimal DEFAULT_Product_VAT_2
      = new BigDecimal(random.nextInt(30 - 5) + 5);

  private final BigDecimal defaultProductPriceGross
      = addVat(DEFAULT_Product_PRICE_NET, DEFAULT_Product_VAT);

  private final BigDecimal defaultProductPriceGross2
      = addVat(DEFAULT_Product_PRICE_NET_2, DEFAULT_Product_VAT_2);

  private static BigDecimal DEFAULT_Product_STOCK
      = new BigDecimal(random.nextInt(1000 - 101) + 101);
  private static BigDecimal DEFAULT_Product_STOCK_2
      = new BigDecimal(random.nextInt(1000 - 101) + 101);
  private static BigDecimal DEFAULT_STOCK_EXCEPTION = new BigDecimal("1111");

  private static final String DEFAULT_Product_CATEGORY = "AAAAAAAAAA";
  private static final String DEFAULT_Product_CATEGORY_2 = "BBBBBBBBB";

  private static final String DEFAULT_Product_DESCRIPTION = "AAAAAAAAAA";
  private static final String DEFAULT_Product_DESCRIPTION_2 = "BBBBBBBBB";

  private static final byte[] DEFAULT_Product_IMAGE = TestUtil.createByteArray(1, "0");
  private static final byte[] DEFAULT_Product_IMAGE_2 = TestUtil.createByteArray(1, "0");

  private static final String DEFAULT_Product_IMAGE_CONTENT_TYPE = "image/jpg";
  private static final String DEFAULT_Product_IMAGE_CONTENT_TYPE_2 = "image/jpg";

  private static final Instant DEFAULT_Product_CREATE_TIME
      = Instant.now().truncatedTo(ChronoUnit.SECONDS);
  private static final Instant DEFAULT_Product_CREATE_TIME_2
      = Instant.now().truncatedTo(ChronoUnit.SECONDS);

  private static final Instant DEFAULT_Product_UPDATE_TIME
      = Instant.now().truncatedTo(ChronoUnit.SECONDS);
  private static final Instant DEFAULT_Product_UPDATE_TIME_2
      = Instant.now().truncatedTo(ChronoUnit.SECONDS);

  // Cart
  private static final BigDecimal DEFAULT_AMOUNT_OF_CART_NET
      = new BigDecimal(random.nextInt(10000));
  private final BigDecimal defaultAmountOfCartGross
      = addVat(DEFAULT_AMOUNT_OF_CART_NET, DEFAULT_Product_VAT);

  private static final BigDecimal DEFAULT_AMOUNT_OF_SHIPMENT_NET
      = new BigDecimal(random.nextInt(100));
  private final BigDecimal defaultAmountOfShipmentGross
      = addVat(DEFAULT_AMOUNT_OF_SHIPMENT_NET, DEFAULT_Product_VAT);

  private static final BigDecimal DEFAULT_AMOUNT_OF_ORDER_NET
      = DEFAULT_AMOUNT_OF_CART_NET.add(DEFAULT_AMOUNT_OF_SHIPMENT_NET);
  private final BigDecimal defaultAmountOfOrderGross
      = defaultAmountOfCartGross.add(defaultAmountOfShipmentGross);

  // ProductInCart
  private static final BigDecimal DEFAULT_ProductInCart_TOTAL_PRICE_NET
      = DEFAULT_Product_QUANTITY.multiply(DEFAULT_Product_PRICE_NET);
  private static final BigDecimal DEFAULT_ProductInCart_TOTAL_PRICE_NET_2
      = DEFAULT_Product_QUANTITY_2.multiply(DEFAULT_Product_PRICE_NET_2);

  private final BigDecimal defaultProductInCartTotalPriceGross
      = DEFAULT_Product_QUANTITY.multiply(defaultProductPriceGross);

  private static final BigDecimal DEFAULT_ProductInCart_PRICE_GROSS_2 =
      DEFAULT_Product_PRICE_NET_2.add(
          DEFAULT_Product_PRICE_NET_2.multiply(DEFAULT_Product_VAT_2.movePointLeft(2)));

  private static final BigDecimal DEFAULT_ProductInCart_TOTAL_PRICE_GROSS_2
      = DEFAULT_Product_QUANTITY_2.multiply(DEFAULT_ProductInCart_PRICE_GROSS_2);

  // ShipmentCart & ShipmentOrderMain
  private static final String DEFAULT_ShipmentCart_FIRST_NAME = "AAAAAAAAAA";
  private static final String DEFAULT_ShipmentCart_LAST_NAME = "AAAAAAAAAA";
  private static final String DEFAULT_ShipmentCart_STREET = "AAAAAAAAAA";
  private static final String DEFAULT_ShipmentCart_POSTAL_CODE = "AAAAAAAAAA";
  private static final String DEFAULT_ShipmentCart_CITY = "AAAAAAAAAA";
  private static final String DEFAULT_ShipmentCart_COUNTRY = "AAAAAAAAAA";
  private static final String DEFAULT_ShipmentCart_PHONE_TO_THE_RECEIVER = "AAAAAAAAAA";
  private static final String DEFAULT_ShipmentCart_FIRM = "AAAAAAAAAA";
  private static final String DEFAULT_ShipmentCart_TAX_NUMBER = "AAAAAAAAAA";

  // PaymentCart & PaymentOrderMain
  private static final String DEFAULT_PaymentCart_NAME = "DHL bank transfer";
  private static final BigDecimal DEFAULT_PaymentCart_PRICE_NET = new BigDecimal("3.0");
  private static final BigDecimal DEFAULT_PaymentCart_VAT = new BigDecimal("23");
  private static final BigDecimal DEFAULT_PaymentCart_PRICE_GROSS = new BigDecimal("3.69");
  private static final PaymentStatusEnum DEFAULT_PaymentCart_STATUS_ENUM
      = PaymentStatusEnum.WaitingForBankTransfer;

  private static final String ENTITY_API_URL = "/api/order-mains";
  private static final String ENTITY_API_URL_ALL = ENTITY_API_URL + "/all";
  private static final String ENTITY_API_URL_CURRENT_USER = ENTITY_API_URL + "/currentUser";
  private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
  private static final String ENTITY_API_URL_EDIT_STATUS = ENTITY_API_URL + "/editStatus/{id}";
  private static final String ENTITY_API_URL_DELETE = ENTITY_API_URL + "/delete/{id}";

  @Autowired
  private OrderMainRepository orderMainRepository;

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private OrderMainMapper orderMainMapper;

  @Autowired
  private EntityManager em;

  @Autowired
  private MockMvc restOrderMainMockMvc;

  @Autowired
  private ProductInOrderMainRepository productInOrderMainRepository;

  @Autowired
  private ProductInCartRepository productInCartRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private CartRepository cartRepository;

  @Autowired
  private PaymentCartRepository paymentCartRepository;

  @Autowired
  private PaymentOrderMainRepository paymentOrderMainRepository;

  @Autowired
  private ShipmentCartRepository shipmentCartRepository;

  @Autowired
  private ShipmentOrderMainRepository shipmentOrderMainRepository;

  private User currentLoggedUser;

  private OrderMain orderMain;

  private Product product;

  private Product product2;

  private OrderMain orderMainCurrentUserLogin;

  private ProductInOrderMain productInOrderMain;

  private ProductInCart productInCart;

  private ProductInCart productInCart2;

  private PaymentOrderMain paymentOrderMain;

  private ShipmentOrderMain shipmentOrderMain;


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
        .createTime(DEFAULT_CREATE_TIME)
        .updateTime(DEFAULT_UPDATE_TIME);
    return orderMain;
  }

  public OrderMain createEntityCurrentLogin(EntityManager em) {
    OrderMain orderMainCurrentUserLogin = new OrderMain()
        .buyerLogin("alice")
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
        .createTime(DEFAULT_CREATE_TIME)
        .updateTime(DEFAULT_UPDATE_TIME);
    return orderMainCurrentUserLogin;
  }

  public Product createEntityProduct(EntityManager em) {
    Product product = new Product()
        .productCategoryEnum(DEFAULT_PRODUCT_CATEGORY_ENUM_Vitamins)
        .name(DEFAULT_Product_NAME)
        .quantity(DEFAULT_Product_QUANTITY)
        .priceNet(DEFAULT_Product_PRICE_NET)
        .vat(DEFAULT_Product_VAT)
        .priceGross(defaultProductPriceGross)
        .stock(DEFAULT_Product_STOCK)
        .description(DEFAULT_Product_DESCRIPTION)
        .createTime(DEFAULT_Product_CREATE_TIME)
        .updateTime(DEFAULT_Product_UPDATE_TIME)
        .image(DEFAULT_Product_IMAGE)
        .imageContentType(DEFAULT_Product_IMAGE_CONTENT_TYPE);
    return product;
  }

  public Product createEntityProduct2(EntityManager em) {
    Product product = new Product()
        .productCategoryEnum(DEFAULT_PRODUCT_CATEGORY_ENUM_Minerals)
        .name(DEFAULT_Product_NAME_2)
        .quantity(DEFAULT_Product_QUANTITY_2)
        .priceNet(DEFAULT_Product_PRICE_NET_2)
        .vat(DEFAULT_Product_VAT_2)
        .priceGross(defaultProductPriceGross2)
        .stock(DEFAULT_Product_STOCK_2)
        .description(DEFAULT_Product_DESCRIPTION_2)
        .createTime(DEFAULT_Product_CREATE_TIME_2)
        .updateTime(DEFAULT_Product_UPDATE_TIME_2)
        .image(DEFAULT_Product_IMAGE_2)
        .imageContentType(DEFAULT_Product_IMAGE_CONTENT_TYPE_2);
    return product;
  }

  public ProductInCart createEntityProductInCart(EntityManager em) {
    ProductInCart productInCart = new ProductInCart()
        .category(DEFAULT_Product_CATEGORY)
        .name(DEFAULT_Product_NAME)
        .quantity(DEFAULT_Product_QUANTITY)
        .priceNet(DEFAULT_Product_PRICE_NET)
        .priceGross(defaultProductPriceGross)
        .vat(DEFAULT_Product_VAT)
        .totalPriceNet(DEFAULT_ProductInCart_TOTAL_PRICE_NET)
        .totalPriceGross(defaultProductInCartTotalPriceGross)
        .stock(DEFAULT_Product_STOCK)
        .description(DEFAULT_Product_DESCRIPTION)
        .image(DEFAULT_Product_IMAGE)
        .imageContentType(DEFAULT_Product_IMAGE_CONTENT_TYPE);
    return productInCart;
  }

  public static ProductInCart createEntityProductInCart2(EntityManager em) {
    ProductInCart productInCart2 = new ProductInCart()
        .category(DEFAULT_Product_CATEGORY_2)
        .name(DEFAULT_Product_NAME_2)
        .quantity(DEFAULT_Product_QUANTITY_2)
        .priceNet(DEFAULT_Product_PRICE_NET_2)
        .priceGross(DEFAULT_ProductInCart_PRICE_GROSS_2)
        .vat(DEFAULT_Product_VAT_2)
        .totalPriceNet(DEFAULT_ProductInCart_TOTAL_PRICE_NET_2)
        .totalPriceGross(DEFAULT_ProductInCart_TOTAL_PRICE_GROSS_2)
        .stock(DEFAULT_Product_STOCK_2)
        .description(DEFAULT_Product_DESCRIPTION_2)
        .image(DEFAULT_Product_IMAGE_2)
        .imageContentType(DEFAULT_Product_IMAGE_CONTENT_TYPE_2);
    return productInCart2;
  }

  public ProductInOrderMain createEntityProductInOrderMain(EntityManager em) {
    ProductInOrderMain productInOrderMain = new ProductInOrderMain()
        .category(DEFAULT_Product_CATEGORY)
        .name(DEFAULT_Product_NAME)
        .quantity(DEFAULT_Product_QUANTITY)
        .priceNet(DEFAULT_Product_PRICE_NET)
        .priceGross(defaultProductPriceGross)
        .vat(DEFAULT_Product_VAT)
        .totalPriceNet(DEFAULT_ProductInCart_TOTAL_PRICE_NET)
        .totalPriceGross(defaultProductInCartTotalPriceGross)
        .stock(DEFAULT_Product_STOCK)
        .description(DEFAULT_Product_DESCRIPTION)
        .image(DEFAULT_Product_IMAGE)
        .imageContentType(DEFAULT_Product_IMAGE_CONTENT_TYPE);
    return productInOrderMain;
  }

  public static ShipmentOrderMain createEntityShipmentOrderMain(EntityManager em) {
    ShipmentOrderMain shipmentOrderMain = new ShipmentOrderMain()
        .firstName(DEFAULT_ShipmentCart_FIRST_NAME)
        .lastName(DEFAULT_ShipmentCart_LAST_NAME)
        .street(DEFAULT_ShipmentCart_STREET)
        .postalCode(DEFAULT_ShipmentCart_POSTAL_CODE)
        .city(DEFAULT_ShipmentCart_CITY)
        .country(DEFAULT_ShipmentCart_COUNTRY)
        .phoneToTheReceiver(DEFAULT_ShipmentCart_PHONE_TO_THE_RECEIVER)
        .firm(DEFAULT_ShipmentCart_FIRM)
        .taxNumber(DEFAULT_ShipmentCart_TAX_NUMBER);
    return shipmentOrderMain;
  }

  public static PaymentOrderMain createEntityPaymentOrderMain(EntityManager em) {
    PaymentOrderMain paymentOrderMain = new PaymentOrderMain()
        .name(DEFAULT_PaymentCart_NAME)
        .priceNet(DEFAULT_PaymentCart_PRICE_NET)
        .vat(DEFAULT_PaymentCart_VAT)
        .priceGross(DEFAULT_PaymentCart_PRICE_GROSS);
    return paymentOrderMain;
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

    // given Cart for User
    Cart cart = new Cart();
    cart.setUser(user);
    cart.setAmountOfCartNet(DEFAULT_AMOUNT_OF_CART_NET);
    cart.setAmountOfCartGross(defaultAmountOfCartGross);
    cart.setAmountOfShipmentNet(DEFAULT_AMOUNT_OF_SHIPMENT_NET);
    cart.setAmountOfShipmentGross(defaultAmountOfShipmentGross);
    cart.setAmountOfOrderNet(DEFAULT_AMOUNT_OF_ORDER_NET);
    cart.setAmountOfOrderGross(defaultAmountOfOrderGross);
    cartRepository.save(cart);
    user.setCart(cart);
    userRepository.save(user);

    // given PaymentCart
    PaymentCart paymentCart = new PaymentCart();
    paymentCart.setName(DEFAULT_PaymentCart_NAME);
    paymentCart.setPriceNet(DEFAULT_PaymentCart_PRICE_NET);
    paymentCart.setVat(DEFAULT_PaymentCart_VAT);
    paymentCart.setPriceGross(DEFAULT_PaymentCart_PRICE_GROSS);
    paymentCart.setPaymentStatus(DEFAULT_PaymentCart_STATUS_ENUM);
    paymentCart.setCart(cart);
    cart.setPaymentCart(paymentCart);
    cartRepository.save(cart);
    paymentCartRepository.save(paymentCart);

    // given ShipmentCart
    ShipmentCart shipmentCart = new ShipmentCart();
    shipmentCart.setFirstName(DEFAULT_ShipmentCart_FIRST_NAME);
    shipmentCart.setLastName(DEFAULT_ShipmentCart_LAST_NAME);
    shipmentCart.setFirm(DEFAULT_ShipmentCart_FIRM);
    shipmentCart.setStreet(DEFAULT_ShipmentCart_STREET);
    shipmentCart.setPostalCode(DEFAULT_ShipmentCart_POSTAL_CODE);
    shipmentCart.setCity(DEFAULT_ShipmentCart_CITY);
    shipmentCart.setCountry(DEFAULT_ShipmentCart_COUNTRY);
    shipmentCart.setPhoneToTheReceiver(DEFAULT_ShipmentCart_PHONE_TO_THE_RECEIVER);
    shipmentCart.setFirm(DEFAULT_ShipmentCart_FIRM);
    shipmentCart.setTaxNumber(DEFAULT_ShipmentCart_TAX_NUMBER);
    shipmentCart.setCart(cart);
    shipmentCartRepository.save(shipmentCart);
    cart.setShipmentCart(shipmentCart);
    cartRepository.save(cart);

    // given Product
    product = createEntityProduct(em);
    product2 = createEntityProduct2(em);
    productRepository.save(product);
    productRepository.save(product2);

    // given ProductInCart
    productInCart = createEntityProductInCart(em);
    productInCart.setCart(cart);
    productInCart.setProductId(product.getId());
    productInCartRepository.save(productInCart);
    cart.addProductInCart(productInCart);

    // given ProductInCart2
    productInCart2 = createEntityProductInCart2(em);
    productInCart2.setCart(cart);
    productInCart2.setProductId(product2.getId());
    productInCartRepository.save(productInCart2);
    cart.addProductInCart(productInCart2);

    orderMain = createEntityOrderMain(em);
  }

  @Test
  @Transactional
  @WithMockUser(username = "alice", authorities = AuthoritiesConstants.USER)
  void createOrderMain() throws Exception {
    currentLoggedUser = checkIfUserExist();
    final int databaseOrderMainSizeBeforeCreate = orderMainRepository.findAll().size();

    // Create the OrderMain
    OrderMainDTO orderMainDto = orderMainMapper.toDto(orderMain);
    restOrderMainMockMvc.perform(post(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(orderMainDto)))
        .andExpect(status().isCreated());

    // Validate the OrderMain in the database
    List<OrderMain> orderMainList = orderMainRepository.findAll();
    assertThat(orderMainList).hasSize(databaseOrderMainSizeBeforeCreate + 1);
    OrderMain testOrderMain = orderMainList.get(orderMainList.size() - 1);
    assertThat(testOrderMain.getId()).isNotNull();
    assertThat(testOrderMain.getBuyerLogin()).isEqualTo(currentLoggedUser.getLogin());
    assertThat(testOrderMain.getBuyerFirstName()).isEqualTo(currentLoggedUser.getFirstName());
    assertThat(testOrderMain.getBuyerLastName()).isEqualTo(currentLoggedUser.getLastName());
    assertThat(testOrderMain.getBuyerEmail()).isEqualTo(currentLoggedUser.getEmail());
    assertThat(testOrderMain.getBuyerPhone()).isEqualTo(DEFAULT_BUYER_PHONE);
    assertThat(testOrderMain.getAmountOfCartNet()).isEqualTo(DEFAULT_AMOUNT_OF_CART_NET);
    assertThat(testOrderMain.getAmountOfCartGross()).isEqualTo(defaultAmountOfCartGross);
    assertThat(testOrderMain.getAmountOfShipmentNet()).isEqualTo(DEFAULT_AMOUNT_OF_SHIPMENT_NET);
    assertThat(testOrderMain.getAmountOfShipmentGross())
        .isEqualTo(defaultAmountOfShipmentGross);
    assertThat(testOrderMain.getAmountOfOrderNet()).isEqualTo(DEFAULT_AMOUNT_OF_ORDER_NET);
    assertThat(testOrderMain.getAmountOfOrderGross()).isEqualTo(defaultAmountOfOrderGross);
    assertThat(testOrderMain.getOrderMainStatus())
        .isEqualTo(OrderMainStatusEnum.valueOf(String.valueOf(DEFAULT_PaymentCart_STATUS_ENUM)));
    assertNotNull(testOrderMain.getCreateTime());
    assertNotNull(testOrderMain.getUpdateTime());
  }

  @Test
  @Transactional
  @WithMockUser(username = "alice", authorities = AuthoritiesConstants.USER)
  void shouldSetAllAmountsOfCartZeroAndNotBeProductInCartAfterCreatingOrderMain() throws Exception {
    currentLoggedUser = checkIfUserExist();
    final int databaseProductInCartSizeBeforeCreateOrderMain
        = productInCartRepository.findAll().size();

    // Create the OrderMain
    OrderMainDTO orderMainDto = orderMainMapper.toDto(orderMain);
    restOrderMainMockMvc.perform(post(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(orderMainDto)))
        .andExpect(status().isCreated());

    // Validate the amountOfCart of currentUser is 0.0
    assertThat(currentLoggedUser.getCart().getAmountOfCartNet()).isEqualTo(BigDecimal.ZERO);
    assertThat(currentLoggedUser.getCart().getAmountOfCartGross()).isEqualTo(BigDecimal.ZERO);
    assertThat(currentLoggedUser.getCart().getAmountOfOrderNet()).isEqualTo(BigDecimal.ZERO);
    assertThat(currentLoggedUser.getCart().getAmountOfOrderGross()).isEqualTo(BigDecimal.ZERO);

    // Validate the ProductInCart in the database
    List<ProductInCart> productInCartList = productInCartRepository.findAll();
    assertThat(productInCartList).hasSize(databaseProductInCartSizeBeforeCreateOrderMain - 2);
  }

  @Test
  @Transactional
  @WithMockUser(username = "alice", authorities = AuthoritiesConstants.USER)
  void shouldBePaymentOrderMainAndOneToOneAfterCreatingOrderMain() throws Exception {
    currentLoggedUser = checkIfUserExist();
    final int databaseOrderMainSizeBeforeCreate = orderMainRepository.findAll().size();
    final int databasePaymentOrderMainBeforeCreate = paymentOrderMainRepository.findAll().size();

    // Create the OrderMain
    OrderMainDTO orderMainDto = orderMainMapper.toDto(orderMain);
    restOrderMainMockMvc.perform(post(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(orderMainDto)))
        .andExpect(status().isCreated());

    // Validate the OrderMain in the database
    List<OrderMain> orderMainList = orderMainRepository.findAll();
    assertThat(orderMainList).hasSize(databaseOrderMainSizeBeforeCreate + 1);
    OrderMain testOrderMain = orderMainList.get(orderMainList.size() - 1);

    // Validate the PaymentOrderMain in the database
    List<PaymentOrderMain> paymentOrderMainList
        = paymentOrderMainRepository.findAll();
    assertThat(paymentOrderMainList).hasSize(databasePaymentOrderMainBeforeCreate + 1);
    PaymentOrderMain testPaymentOrderMain
        = paymentOrderMainList.get(paymentOrderMainList.size() - 1);
    assertThat(testPaymentOrderMain.getId()).isNotNull();
    assertThat(testPaymentOrderMain.getOrderMain().getId()).isEqualTo(testOrderMain.getId());
    assertThat(testPaymentOrderMain.getName()).isEqualTo(DEFAULT_PaymentCart_NAME);
    assertThat(testPaymentOrderMain.getPriceNet()).isEqualTo(DEFAULT_PaymentCart_PRICE_NET);
    assertThat(testPaymentOrderMain.getVat()).isEqualTo(DEFAULT_PaymentCart_VAT);
    assertThat(testPaymentOrderMain.getPriceGross()).isEqualTo(DEFAULT_PaymentCart_PRICE_GROSS);
  }

  @Test
  @Transactional
  @WithMockUser(username = "alice", authorities = AuthoritiesConstants.USER)
  void shouldBeShipmentOrderMainAndOneToOneAfterCreatingOrderMain() throws Exception {
    currentLoggedUser = checkIfUserExist();
    final int databaseOrderMainSizeBeforeCreate = orderMainRepository.findAll().size();
    final int databaseShipmentOrderMainBeforeCreate = shipmentOrderMainRepository.findAll().size();

    // Create the OrderMain
    OrderMainDTO orderMainDto = orderMainMapper.toDto(orderMain);
    restOrderMainMockMvc.perform(post(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(orderMainDto)))
        .andExpect(status().isCreated());

    // Validate the OrderMain in the database
    List<OrderMain> orderMainList = orderMainRepository.findAll();
    assertThat(orderMainList).hasSize(databaseOrderMainSizeBeforeCreate + 1);
    OrderMain testOrderMain = orderMainList.get(orderMainList.size() - 1);

    // Validate the ShipmentOrderMain in the database
    List<ShipmentOrderMain> shipmentOrderMainList = shipmentOrderMainRepository.findAll();
    assertThat(shipmentOrderMainList)
        .hasSize(databaseShipmentOrderMainBeforeCreate + 1);
    ShipmentOrderMain testShipmentOrderMain
        = shipmentOrderMainList.get(shipmentOrderMainList.size() - 1);
    assertThat(testShipmentOrderMain.getId()).isNotNull();
    assertThat(testShipmentOrderMain.getFirstName()).isEqualTo(DEFAULT_ShipmentCart_FIRST_NAME);
    assertThat(testShipmentOrderMain.getLastName()).isEqualTo(DEFAULT_ShipmentCart_LAST_NAME);
    assertThat(testShipmentOrderMain.getStreet()).isEqualTo(DEFAULT_ShipmentCart_STREET);
    assertThat(testShipmentOrderMain.getPostalCode()).isEqualTo(DEFAULT_ShipmentCart_POSTAL_CODE);
    assertThat(testShipmentOrderMain.getCity()).isEqualTo(DEFAULT_ShipmentCart_CITY);
    assertThat(testShipmentOrderMain.getCountry())
        .isEqualTo(DEFAULT_ShipmentCart_COUNTRY);
    assertThat(testShipmentOrderMain.getPhoneToTheReceiver())
        .isEqualTo(DEFAULT_ShipmentCart_PHONE_TO_THE_RECEIVER);
    assertThat(testShipmentOrderMain.getFirm()).isEqualTo(DEFAULT_ShipmentCart_FIRM);
    assertThat(testShipmentOrderMain.getTaxNumber()).isEqualTo(DEFAULT_ShipmentCart_TAX_NUMBER);
    assertThat(testShipmentOrderMain.getOrderMain().getId()).isEqualTo(testOrderMain.getId());;
  }

  @Test
  @Transactional
  @WithMockUser(username = "alice", authorities = AuthoritiesConstants.USER)
  void shouldBeFirstProductInOrderMainAndOneToManyAfterCreatingOrderMain() throws Exception {
    currentLoggedUser = checkIfUserExist();
    final int databaseProductInOrderMainSizeBeforeCreateOrderMain
        = productInOrderMainRepository.findAll().size();

    // Create the OrderMain
    OrderMainDTO orderMainDto = orderMainMapper.toDto(orderMain);
    restOrderMainMockMvc.perform(post(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(orderMainDto)))
        .andExpect(status().isCreated());

    // Validate the OrderMain in the database
    List<OrderMain> orderMainList = orderMainRepository.findAll();
    OrderMain testOrderMain = orderMainList.get(orderMainList.size() - 1);

    // Validate the ProductInOrderMains size in the database
    List<ProductInOrderMain> productInOrderMainList = productInOrderMainRepository.findAll();
    assertThat(productInOrderMainList)
        .hasSize(databaseProductInOrderMainSizeBeforeCreateOrderMain + 2);

    // Validate ProductInOrderMain in the database
    ProductInOrderMain testProductInOrderMain
        = productInOrderMainList.get(productInOrderMainList.size() - 2);

    // Validate id relation OneToMany between ProductInOrderMain and OrderMain
    assertNotNull(testProductInOrderMain.getOrderMain());
    assertThat(testProductInOrderMain.getOrderMain().getId()).isEqualTo(testOrderMain.getId());

    assertThat(testProductInOrderMain.getCategory()).isEqualTo(productInCart.getCategory());
    assertThat(testProductInOrderMain.getName()).isEqualTo(productInCart.getName());
    assertThat(testProductInOrderMain.getQuantity()).isEqualTo(productInCart.getQuantity());
    assertThat(testProductInOrderMain.getPriceNet()).isEqualTo(productInCart.getPriceNet());
    assertThat(testProductInOrderMain.getVat()).isEqualTo(productInCart.getVat());
    assertThat(testProductInOrderMain.getPriceGross()).isEqualTo(productInCart.getPriceGross());
    assertThat(testProductInOrderMain.getTotalPriceNet())
        .isEqualTo(productInCart.getTotalPriceNet());
    assertThat(testProductInOrderMain.getTotalPriceGross())
        .isEqualTo(productInCart.getTotalPriceGross());
    assertThat(testProductInOrderMain.getStock()).isEqualTo(productInCart.getStock());
    assertThat(testProductInOrderMain.getDescription()).isEqualTo(productInCart.getDescription());
    assertThat(testProductInOrderMain.getImage())
        .isEqualTo(productInCart.getImage());
    assertThat(testProductInOrderMain.getImageContentType())
        .isEqualTo(productInCart.getImageContentType());
    assertThat(testProductInOrderMain.getProductId()).isEqualTo(product.getId());
  }

  @Test
  @Transactional
  @WithMockUser(username = "alice", authorities = AuthoritiesConstants.USER)
  void shouldBeSecondProductInOrderMainAndOneToManyAfterCreatingOrderMain() throws Exception {
    currentLoggedUser = checkIfUserExist();
    final int databaseProductInOrderMainSizeBeforeCreateOrderMain
        = productInOrderMainRepository.findAll().size();

    // Create the OrderMain
    OrderMainDTO orderMainDto = orderMainMapper.toDto(orderMain);
    restOrderMainMockMvc.perform(post(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(orderMainDto)))
        .andExpect(status().isCreated());

    // Validate the OrderMain in the database
    List<OrderMain> orderMainList = orderMainRepository.findAll();
    OrderMain testOrderMain = orderMainList.get(orderMainList.size() - 1);

    // Validate the ProductInOrderMains size in the database
    List<ProductInOrderMain> productInOrderMainList = productInOrderMainRepository.findAll();
    assertThat(productInOrderMainList)
        .hasSize(databaseProductInOrderMainSizeBeforeCreateOrderMain + 2);

    // Validate ProductInOrderMain2 in the database
    ProductInOrderMain testProductInOrderMain2
        = productInOrderMainList.get(productInOrderMainList.size() - 1);

    // Validate id relation OneToMany between ProductInOrderMain2 and OrderMain
    assertNotNull(testProductInOrderMain2.getOrderMain());
    assertThat(testProductInOrderMain2.getOrderMain().getId()).isEqualTo(testOrderMain.getId());

    assertThat(testProductInOrderMain2.getCategory()).isEqualTo(productInCart2.getCategory());
    assertThat(testProductInOrderMain2.getName()).isEqualTo(productInCart2.getName());
    assertThat(testProductInOrderMain2.getQuantity()).isEqualTo(productInCart2.getQuantity());
    assertThat(testProductInOrderMain2.getPriceNet()).isEqualTo(productInCart2.getPriceNet());
    assertThat(testProductInOrderMain2.getVat()).isEqualTo(productInCart2.getVat());
    assertThat(testProductInOrderMain2.getPriceGross()).isEqualTo(productInCart2.getPriceGross());
    assertThat(testProductInOrderMain2.getTotalPriceNet())
        .isEqualTo(productInCart2.getTotalPriceNet());
    assertThat(testProductInOrderMain2.getTotalPriceGross())
        .isEqualTo(productInCart2.getTotalPriceGross());
    assertThat(testProductInOrderMain2.getStock()).isEqualTo(productInCart2.getStock());
    assertThat(testProductInOrderMain2.getDescription()).isEqualTo(productInCart2.getDescription());
    assertThat(testProductInOrderMain2.getImage()).isEqualTo(productInCart2.getImage());
    assertThat(testProductInOrderMain2.getImageContentType())
        .isEqualTo(productInCart2.getImageContentType());
    assertThat(testProductInOrderMain2.getProductId()).isEqualTo(product2.getId());
  }

  @Test
  @Transactional
  void createOrderMainByAnyoneShouldThrowStatusUnauthorized() throws Exception {
    final int databaseOrderMainSizeBeforeCreate = orderMainRepository.findAll().size();
    final int databaseProductInCartSizeBeforeCreateOrderMain
        = productInCartRepository.findAll().size();
    final int databaseProductInOrderMainSizeBeforeCreateOrderMain
        = productInOrderMainRepository.findAll().size();
    final int databasePaymentOrderMainBeforeCreate = paymentOrderMainRepository.findAll().size();
    final int databaseShipmentOrderMainBeforeCreate = shipmentOrderMainRepository.findAll().size();

    // Create the OrderMain
    OrderMainDTO orderMainDto = orderMainMapper.toDto(orderMain);
    restOrderMainMockMvc.perform(post(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(orderMainDto)))
        .andExpect(status().isUnauthorized());

    // Validate the OrderMain in the database
    List<OrderMain> orderMainList = orderMainRepository.findAll();
    assertThat(orderMainList).hasSize(databaseOrderMainSizeBeforeCreate);

    // Validate the ProductInOrderMains size in the database
    List<ProductInOrderMain> productInOrderMainList = productInOrderMainRepository.findAll();
    assertThat(productInOrderMainList)
        .hasSize(databaseProductInOrderMainSizeBeforeCreateOrderMain);

    // Validate the ProductInCart in the database
    List<ProductInCart> productInCartList = productInCartRepository.findAll();
    assertThat(productInCartList).hasSize(databaseProductInCartSizeBeforeCreateOrderMain);

    // Validate the PaymentOrderMain in the database
    List<PaymentOrderMain> paymentOrderMainList
        = paymentOrderMainRepository.findAll();
    assertThat(paymentOrderMainList).hasSize(databasePaymentOrderMainBeforeCreate);

    // Validate the ShipmentOrderMain in the database
    List<ShipmentOrderMain> shipmentOrderMainList = shipmentOrderMainRepository.findAll();
    assertThat(shipmentOrderMainList)
        .hasSize(databaseShipmentOrderMainBeforeCreate);
  }

  @Test
  @Transactional
  @WithMockUser(username = "alice", authorities = AuthoritiesConstants.USER)
  void afterCreatingOrderMainOrderMainStatusShouldBeEqualPaymentCartStatus()
      throws Exception {
    int databaseOrderMainSizeBeforeCreate = orderMainRepository.findAll().size();

    // Create the OrderMain
    OrderMainDTO orderMainDto = orderMainMapper.toDto(orderMain);
    restOrderMainMockMvc.perform(post(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(orderMainDto)))
        .andExpect(status().isCreated());

    // Validate the OrderMain in the database
    List<OrderMain> orderMainList = orderMainRepository.findAll();
    assertThat(orderMainList).hasSize(databaseOrderMainSizeBeforeCreate + 1);
    OrderMain testOrderMain = orderMainList.get(orderMainList.size() - 1);
    assertThat(testOrderMain.getOrderMainStatus())
        .isEqualTo(OrderMainStatusEnum.valueOf(String.valueOf(DEFAULT_PaymentCart_STATUS_ENUM)));
  }

  @Test
  @Transactional
  @WithMockUser(username = "alice", authorities = AuthoritiesConstants.USER)
  void ifProductDoesNotExistInStockShouldThrowExceptionWhileOrderMainIsCreated()
      throws Exception {
    // given ProductInCart
    productInCart.setQuantity(DEFAULT_STOCK_EXCEPTION);
    productInCartRepository.save(productInCart);

    // when
    // Create the OrderMain
    OrderMainDTO orderMainDto = orderMainMapper.toDto(orderMain);
    restOrderMainMockMvc.perform(post(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(orderMainDto)))
        .andExpect(status().isBadRequest());
  }

  @Test
  @Transactional
  @WithMockUser(username = "alice", authorities = AuthoritiesConstants.USER)
  void createOrderMainWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = orderMainRepository.findAll().size();

    // Create the OrderMain with an existing ID
    orderMain.setId(1L);
    OrderMainDTO orderMainDto = orderMainMapper.toDto(orderMain);

    // An entity with an existing ID cannot be created, so this API call must fail
    restOrderMainMockMvc.perform(post(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(orderMainDto)))
        .andExpect(status().isBadRequest());

    // Validate the OrderMain in the database
    List<OrderMain> orderMainList = orderMainRepository.findAll();
    assertThat(orderMainList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  @WithMockUser(username = "admin", password = "admin", authorities = AuthoritiesConstants.ADMIN)
  void getAllOrderMains() throws Exception {
    // Initialize the database
    orderMainRepository.save(orderMain);

    // Get all the orderMainList
    restOrderMainMockMvc.perform(get(ENTITY_API_URL_ALL))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(orderMain.getId().intValue())))
        .andExpect(jsonPath("$.[*].buyerLogin").value(hasItem(DEFAULT_BUYER_LOGIN)))
        .andExpect(jsonPath("$.[*].buyerFirstName").value(hasItem(DEFAULT_BUYER_FIRST_NAME)))
        .andExpect(jsonPath("$.[*].buyerLastName").value(hasItem(DEFAULT_BUYER_LAST_NAME)))
        .andExpect(jsonPath("$.[*].buyerEmail").value(hasItem(DEFAULT_BUYER_EMAIL)))
        .andExpect(jsonPath("$.[*].buyerPhone").value(hasItem(DEFAULT_BUYER_PHONE)))
        .andExpect(jsonPath("$.[*].amountOfCartNet")
            .value(hasItem(sameNumber(DEFAULT_AMOUNT_OF_CART_NET))))
        .andExpect(jsonPath("$.[*].amountOfCartGross")
            .value(hasItem(sameNumber(defaultAmountOfCartGross))))
        .andExpect(jsonPath("$.[*].amountOfShipmentNet")
            .value(hasItem(sameNumber(DEFAULT_AMOUNT_OF_SHIPMENT_NET))))
        .andExpect(jsonPath("$.[*].amountOfShipmentGross")
            .value(hasItem(sameNumber(defaultAmountOfShipmentGross))))
        .andExpect(jsonPath("$.[*].amountOfOrderNet")
            .value(hasItem(sameNumber(DEFAULT_AMOUNT_OF_ORDER_NET))))
        .andExpect(jsonPath("$.[*].amountOfOrderGross")
            .value(hasItem(sameNumber(defaultAmountOfOrderGross))))
        .andExpect(jsonPath("$.[*].orderMainStatus")
            .value(hasItem(DEFAULT_ORDERMAIN_STATUS.toString())))
        .andExpect(jsonPath("$.[*].createTime").exists())
        .andExpect(jsonPath("$.[*].updateTime").exists());
  }

  @Test
  @Transactional
  void getAllOrderMainsByAnyoneShouldThrowStatusUnauthorized() throws Exception {
    // Initialize the database
    orderMainRepository.save(orderMain);

    // Get all the orderMainList
    restOrderMainMockMvc.perform(get(ENTITY_API_URL_ALL))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @Transactional
  @WithMockUser(username = "alice", authorities = AuthoritiesConstants.USER)
  void getAllOrderMainsByUserShouldThrowStatusForbidden() throws Exception {
    // Initialize the database
    orderMainRepository.save(orderMain);

    // Get all the orderMainList
    restOrderMainMockMvc.perform(get(ENTITY_API_URL_ALL))
        .andExpect(status().isForbidden());
  }

  @Test
  @Transactional
  @WithMockUser(username = "alice", authorities = AuthoritiesConstants.USER)
  void getAllOrderMainByCurrentUserLogin() throws Exception {
    // Initialize the database
    orderMainCurrentUserLogin = createEntityCurrentLogin(em);

    orderMainRepository.save(orderMainCurrentUserLogin);

    // Get all the orderMainList of current user
    restOrderMainMockMvc.perform(get(ENTITY_API_URL_CURRENT_USER))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(
            jsonPath("$.[*].id").value(hasItem(orderMainCurrentUserLogin.getId().intValue())))
        .andExpect(jsonPath("$.[*].buyerLogin").value(hasItem("alice")))
        .andExpect(jsonPath("$.[*].buyerFirstName").value(hasItem(DEFAULT_BUYER_FIRST_NAME)))
        .andExpect(jsonPath("$.[*].buyerLastName").value(hasItem(DEFAULT_BUYER_LAST_NAME)))
        .andExpect(jsonPath("$.[*].buyerEmail").value(hasItem(DEFAULT_BUYER_EMAIL)))
        .andExpect(jsonPath("$.[*].buyerPhone").value(hasItem(DEFAULT_BUYER_PHONE)))
        .andExpect(jsonPath("$.[*].amountOfCartNet")
            .value(hasItem(sameNumber(DEFAULT_AMOUNT_OF_CART_NET))))
        .andExpect(jsonPath("$.[*].amountOfCartGross")
            .value(hasItem(sameNumber(defaultAmountOfCartGross))))
        .andExpect(jsonPath("$.[*].amountOfShipmentNet")
            .value(hasItem(sameNumber(DEFAULT_AMOUNT_OF_SHIPMENT_NET))))
        .andExpect(jsonPath("$.[*].amountOfShipmentGross")
            .value(hasItem(sameNumber(defaultAmountOfShipmentGross))))
        .andExpect(jsonPath("$.[*].amountOfOrderNet")
            .value(hasItem(sameNumber(DEFAULT_AMOUNT_OF_ORDER_NET))))
        .andExpect(jsonPath("$.[*].amountOfOrderGross")
            .value(hasItem(sameNumber(defaultAmountOfOrderGross))))
        .andExpect(jsonPath("$.[*].orderMainStatus")
            .value(hasItem(DEFAULT_ORDERMAIN_STATUS.toString())))
        .andExpect(jsonPath("$.[*].createTime").exists())
        .andExpect(jsonPath("$.[*].updateTime").exists());
  }

  @Test
  @Transactional
  @WithMockUser(username = "alice", authorities = AuthoritiesConstants.USER)
  void getAllOrderMainByCurrentUserLoginShouldNotGetAnotherLogin() throws Exception {
    // Initialize the database
    orderMainRepository.save(orderMain);

    // Get all the orderMainList
    restOrderMainMockMvc.perform(get(ENTITY_API_URL_CURRENT_USER))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.[*].id").doesNotExist())
        .andExpect(jsonPath("$.[*].buyerLogin").doesNotExist())
        .andExpect(jsonPath("$.[*].buyerFirstName").doesNotExist())
        .andExpect(jsonPath("$.[*].buyerLastName").doesNotExist())
        .andExpect(jsonPath("$.[*].buyerEmail").doesNotExist())
        .andExpect(jsonPath("$.[*].buyerPhone").doesNotExist())
        .andExpect(jsonPath("$.[*].amountOfCartNet").doesNotExist())
        .andExpect(jsonPath("$.[*].amountOfCartGross").doesNotExist())
        .andExpect(jsonPath("$.[*].amountOfShipmentNet").doesNotExist())
        .andExpect(jsonPath("$.[*].amountOfShipmentGross").doesNotExist())
        .andExpect(jsonPath("$.[*].amountOfOrderNet").doesNotExist())
        .andExpect(jsonPath("$.[*].amountOfOrderGross").doesNotExist())
        .andExpect(jsonPath("$.[*].orderMainStatus").doesNotExist())
        .andExpect(jsonPath("$.[*].createTime").doesNotExist())
        .andExpect(jsonPath("$.[*].updateTime").doesNotExist());
  }

  @Test
  @Transactional
  @WithMockUser(username = "admin", password = "admin", authorities = AuthoritiesConstants.ADMIN)
  void getOrderMain() throws Exception {
    // Initialize the database
    orderMainRepository.save(orderMain);

    // Get the orderMain
    restOrderMainMockMvc.perform(get(ENTITY_API_URL_ID, orderMain.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.id").value(orderMain.getId().intValue()))
        .andExpect(jsonPath("$.buyerLogin").value(DEFAULT_BUYER_LOGIN))
        .andExpect(jsonPath("$.buyerFirstName").value(DEFAULT_BUYER_FIRST_NAME))
        .andExpect(jsonPath("$.buyerLastName").value(DEFAULT_BUYER_LAST_NAME))
        .andExpect(jsonPath("$.buyerEmail").value(DEFAULT_BUYER_EMAIL))
        .andExpect(jsonPath("$.buyerPhone").value(DEFAULT_BUYER_PHONE))
        .andExpect(jsonPath("$.amountOfCartNet")
            .value(sameNumber(DEFAULT_AMOUNT_OF_CART_NET)))
        .andExpect(jsonPath("$.amountOfCartGross")
            .value(sameNumber(defaultAmountOfCartGross)))
        .andExpect(jsonPath("$.amountOfShipmentNet")
            .value(sameNumber(DEFAULT_AMOUNT_OF_SHIPMENT_NET)))
        .andExpect(jsonPath("$.amountOfShipmentGross")
            .value(sameNumber(defaultAmountOfShipmentGross)))
        .andExpect(jsonPath("$.amountOfOrderNet")
            .value(sameNumber(DEFAULT_AMOUNT_OF_ORDER_NET)))
        .andExpect(jsonPath("$.amountOfOrderGross")
            .value(sameNumber(defaultAmountOfOrderGross)))
        .andExpect(jsonPath("$.orderMainStatus").value(DEFAULT_ORDERMAIN_STATUS.toString()))
        .andExpect(jsonPath("$.createTime").exists())
        .andExpect(jsonPath("$.updateTime").exists());
  }

  @Test
  @Transactional
  @WithMockUser
  void getNonExistingOrderMain() throws Exception {
    // Get the orderMain
    restOrderMainMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  @WithMockUser(username = "admin", password = "admin", authorities = AuthoritiesConstants.ADMIN)
  void partialUpdateOrderMainStatusWithPatch() throws Exception {
    // Initialize the database
    orderMainRepository.save(orderMain);

    final int databaseSizeBeforeUpdate = orderMainRepository.findAll().size();

    // Update the orderMain using partial update
    OrderMain partialUpdatedOrderMain = new OrderMain();
    partialUpdatedOrderMain.setId(orderMain.getId());
    partialUpdatedOrderMain.orderMainStatus(UPDATED_ORDERMAIN_STATUS);

    restOrderMainMockMvc.perform(patch(ENTITY_API_URL_EDIT_STATUS, partialUpdatedOrderMain.getId())
                .contentType("application/merge-patch+json")
                .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrderMain)))
        .andExpect(status().isOk());

    // Validate the OrderMain in the database
    List<OrderMain> orderMainList = orderMainRepository.findAll();
    assertThat(orderMainList).hasSize(databaseSizeBeforeUpdate);
    OrderMain testOrderMain = orderMainList.get(orderMainList.size() - 1);
    assertThat(testOrderMain.getBuyerLogin()).isEqualTo(DEFAULT_BUYER_LOGIN);
    assertThat(testOrderMain.getBuyerFirstName()).isEqualTo(DEFAULT_BUYER_FIRST_NAME);
    assertThat(testOrderMain.getBuyerLastName()).isEqualTo(DEFAULT_BUYER_LAST_NAME);
    assertThat(testOrderMain.getBuyerEmail()).isEqualTo(DEFAULT_BUYER_EMAIL);
    assertThat(testOrderMain.getBuyerPhone()).isEqualTo(DEFAULT_BUYER_PHONE);
    assertThat(testOrderMain.getAmountOfCartNet()).isEqualByComparingTo(DEFAULT_AMOUNT_OF_CART_NET);
    assertThat(testOrderMain.getAmountOfCartGross()).isEqualByComparingTo(defaultAmountOfCartGross);
    assertThat(testOrderMain.getAmountOfShipmentNet())
        .isEqualByComparingTo(DEFAULT_AMOUNT_OF_SHIPMENT_NET);
    assertThat(testOrderMain.getAmountOfShipmentGross())
        .isEqualByComparingTo(defaultAmountOfShipmentGross);
    assertThat(testOrderMain.getAmountOfOrderNet())
        .isEqualByComparingTo(DEFAULT_AMOUNT_OF_ORDER_NET);
    assertThat(testOrderMain.getAmountOfOrderGross())
        .isEqualByComparingTo(defaultAmountOfOrderGross);
    assertThat(testOrderMain.getOrderMainStatus())
        .isEqualTo(UPDATED_ORDERMAIN_STATUS);
    assertNotNull(testOrderMain.getCreateTime());
    assertNotNull(testOrderMain.getUpdateTime());
  }

  @Test
  @Transactional
  @WithMockUser(username = "alice", authorities = AuthoritiesConstants.USER)
  void partialUpdateOrderMainStatusWithPatchByUserShouldThrowStatusForbidden() throws Exception {
    // Initialize the database
    orderMainRepository.save(orderMain);

    final int databaseSizeBeforeUpdate = orderMainRepository.findAll().size();

    // Update the orderMain using partial update
    OrderMain partialUpdatedOrderMain = new OrderMain();
    partialUpdatedOrderMain.setId(orderMain.getId());
    partialUpdatedOrderMain.orderMainStatus(UPDATED_ORDERMAIN_STATUS);

    restOrderMainMockMvc.perform(patch(ENTITY_API_URL_EDIT_STATUS, partialUpdatedOrderMain.getId())
                .contentType("application/merge-patch+json")
                .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrderMain)))
        .andExpect(status().isForbidden());

    // Validate the OrderMain in the database
    List<OrderMain> orderMainList = orderMainRepository.findAll();
    assertThat(orderMainList).hasSize(databaseSizeBeforeUpdate);
    OrderMain testOrderMain = orderMainList.get(orderMainList.size() - 1);
    assertThat(testOrderMain.getBuyerLogin()).isEqualTo(DEFAULT_BUYER_LOGIN);
    assertThat(testOrderMain.getBuyerFirstName()).isEqualTo(DEFAULT_BUYER_FIRST_NAME);
    assertThat(testOrderMain.getBuyerLastName()).isEqualTo(DEFAULT_BUYER_LAST_NAME);
    assertThat(testOrderMain.getBuyerEmail()).isEqualTo(DEFAULT_BUYER_EMAIL);
    assertThat(testOrderMain.getBuyerPhone()).isEqualTo(DEFAULT_BUYER_PHONE);
    assertThat(testOrderMain.getAmountOfCartNet()).isEqualByComparingTo(DEFAULT_AMOUNT_OF_CART_NET);
    assertThat(testOrderMain.getAmountOfCartGross()).isEqualByComparingTo(defaultAmountOfCartGross);
    assertThat(testOrderMain.getAmountOfShipmentNet())
        .isEqualByComparingTo(DEFAULT_AMOUNT_OF_SHIPMENT_NET);
    assertThat(testOrderMain.getAmountOfShipmentGross())
        .isEqualByComparingTo(defaultAmountOfShipmentGross);
    assertThat(testOrderMain.getAmountOfOrderNet())
        .isEqualByComparingTo(DEFAULT_AMOUNT_OF_ORDER_NET);
    assertThat(testOrderMain.getAmountOfOrderGross())
        .isEqualByComparingTo(defaultAmountOfOrderGross);
    assertThat(testOrderMain.getOrderMainStatus())
        .isEqualTo(DEFAULT_ORDERMAIN_STATUS);
    assertNotNull(testOrderMain.getCreateTime());
    assertNotNull(testOrderMain.getUpdateTime());
  }

  @Test
  @Transactional
  void partialUpdateOrderMainStatusWithPatchByAnyoneShouldThrowStatusUnauthorized()
      throws Exception {
    // Initialize the database
    orderMainRepository.saveAndFlush(orderMain);

    final int databaseSizeBeforeUpdate = orderMainRepository.findAll().size();

    // Update the orderMain using partial update
    OrderMain partialUpdatedOrderMain = new OrderMain();
    partialUpdatedOrderMain.setId(orderMain.getId());
    partialUpdatedOrderMain.orderMainStatus(UPDATED_ORDERMAIN_STATUS);

    restOrderMainMockMvc.perform(patch(ENTITY_API_URL_EDIT_STATUS, partialUpdatedOrderMain.getId())
                .contentType("application/merge-patch+json")
                .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrderMain)))
        .andExpect(status().isUnauthorized());

    // Validate the OrderMain in the database
    List<OrderMain> orderMainList = orderMainRepository.findAll();
    assertThat(orderMainList).hasSize(databaseSizeBeforeUpdate);
    OrderMain testOrderMain = orderMainList.get(orderMainList.size() - 1);
    assertThat(testOrderMain.getBuyerLogin()).isEqualTo(DEFAULT_BUYER_LOGIN);
    assertThat(testOrderMain.getBuyerFirstName()).isEqualTo(DEFAULT_BUYER_FIRST_NAME);
    assertThat(testOrderMain.getBuyerLastName()).isEqualTo(DEFAULT_BUYER_LAST_NAME);
    assertThat(testOrderMain.getBuyerEmail()).isEqualTo(DEFAULT_BUYER_EMAIL);
    assertThat(testOrderMain.getBuyerPhone()).isEqualTo(DEFAULT_BUYER_PHONE);
    assertThat(testOrderMain.getAmountOfCartNet()).isEqualByComparingTo(DEFAULT_AMOUNT_OF_CART_NET);
    assertThat(testOrderMain.getAmountOfCartGross()).isEqualByComparingTo(defaultAmountOfCartGross);
    assertThat(testOrderMain.getAmountOfShipmentNet())
        .isEqualByComparingTo(DEFAULT_AMOUNT_OF_SHIPMENT_NET);
    assertThat(testOrderMain.getAmountOfShipmentGross())
        .isEqualByComparingTo(defaultAmountOfShipmentGross);
    assertThat(testOrderMain.getAmountOfOrderNet())
        .isEqualByComparingTo(DEFAULT_AMOUNT_OF_ORDER_NET);
    assertThat(testOrderMain.getAmountOfOrderGross())
        .isEqualByComparingTo(defaultAmountOfOrderGross);
    assertThat(testOrderMain.getOrderMainStatus())
        .isEqualTo(DEFAULT_ORDERMAIN_STATUS);
    assertNotNull(testOrderMain.getCreateTime());
    assertNotNull(testOrderMain.getUpdateTime());
  }

  @Test
  @Transactional
  @WithMockUser(username = "admin", password = "admin", authorities = AuthoritiesConstants.ADMIN)
  void patchNonExistingOrderMain() throws Exception {
    int databaseSizeBeforeUpdate = orderMainRepository.findAll().size();
    orderMain.setId(count.incrementAndGet());

    // Create the OrderMain
    OrderMainDTO orderMainDto = orderMainMapper.toDto(orderMain);

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restOrderMainMockMvc.perform(patch(ENTITY_API_URL_EDIT_STATUS, orderMainDto.getId())
                .contentType("application/merge-patch+json")
                .content(TestUtil.convertObjectToJsonBytes(orderMainDto)))
        .andExpect(status().isBadRequest());

    // Validate the OrderMain in the database
    List<OrderMain> orderMainList = orderMainRepository.findAll();
    assertThat(orderMainList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  @WithMockUser(username = "admin", password = "admin", authorities = AuthoritiesConstants.ADMIN)
  void patchWithIdMismatchOrderMain() throws Exception {
    int databaseSizeBeforeUpdate = orderMainRepository.findAll().size();
    orderMain.setId(count.incrementAndGet());

    // Create the OrderMain
    OrderMainDTO orderMainDto = orderMainMapper.toDto(orderMain);

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restOrderMainMockMvc.perform(patch(ENTITY_API_URL_EDIT_STATUS, count.incrementAndGet())
                .contentType("application/merge-patch+json")
                .content(TestUtil.convertObjectToJsonBytes(orderMainDto)))
        .andExpect(status().isBadRequest());

    // Validate the OrderMain in the database
    List<OrderMain> orderMainList = orderMainRepository.findAll();
    assertThat(orderMainList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  @WithMockUser(username = "admin", password = "admin", authorities = AuthoritiesConstants.ADMIN)
  void patchWithMissingIdPathParamOrderMain() throws Exception {
    orderMainRepository.save(orderMain);
    OrderMain updatedOrderMain = orderMainRepository.findById(orderMain.getId()).get();

    int databaseSizeBeforeUpdate = orderMainRepository.findAll().size();

    OrderMainDTO orderMainDto = orderMainMapper.toDto(updatedOrderMain);
    orderMainDto.setId(count.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restOrderMainMockMvc.perform(patch(ENTITY_API_URL + "/editStatus")
                .contentType("application/merge-patch+json")
                .content(TestUtil.convertObjectToJsonBytes(orderMainDto)))
        .andExpect(status().isMethodNotAllowed());

    // Validate the OrderMain in the database
    List<OrderMain> orderMainList = orderMainRepository.findAll();
    assertThat(orderMainList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  @WithMockUser(username = "admin", password = "admin", authorities = AuthoritiesConstants.ADMIN)
  void deleteOrderMainAndAllRelationshipOrderMainByIdOrderMain() throws Exception {
    // given OrderMain
    orderMain = createEntityOrderMain(em);
    orderMainRepository.save(orderMain);

    // given ProducInOrderMain
    productInOrderMain = createEntityProductInOrderMain(em);
    productInOrderMain.setOrderMain(orderMain);
    productInOrderMain.setProductId(product.getId());
    productInOrderMainRepository.save(productInOrderMain);

    // given PaymentOrderMain
    paymentOrderMain = createEntityPaymentOrderMain(em);
    paymentOrderMain.setOrderMain(orderMain);
    paymentOrderMainRepository.save(paymentOrderMain);

    // given ShipmentOrderMain
    shipmentOrderMain = createEntityShipmentOrderMain(em);
    shipmentOrderMain.setOrderMain(orderMain);
    shipmentOrderMainRepository.save(shipmentOrderMain);

    // set Relationship for OrderMain
    orderMain.setShipmentOrderMain(shipmentOrderMain);
    orderMain.setPaymentOrderMain(paymentOrderMain);
    orderMain.addProductInOrderMain(productInOrderMain);
    orderMainRepository.save(orderMain);

    int databasePaymentOrderMainSizeBeforeDelete = paymentOrderMainRepository.findAll().size();
    int databaseShipmentOrderMainSizeBeforeDelete = shipmentOrderMainRepository.findAll().size();
    int databaseProductInOrderMainSizeBeforeDelete = productInOrderMainRepository.findAll().size();
    int databaseSizeBeforeDelete = orderMainRepository.findAll().size();

    // Delete the orderMain
    restOrderMainMockMvc.perform(delete(ENTITY_API_URL_DELETE, orderMain.getId())
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent());

    // Validate the OrderMain database
    List<OrderMain> orderMainList = orderMainRepository.findAll();
    assertThat(orderMainList).hasSize(databaseSizeBeforeDelete - 1);

    // Validate the ProductInOrder database
    List<ProductInOrderMain> productInOrderList = productInOrderMainRepository.findAll();
    assertThat(productInOrderList).hasSize(databaseProductInOrderMainSizeBeforeDelete - 1);

    // Validate the PaymentOrderMain in the database
    List<PaymentOrderMain> paymentOrderMainList = paymentOrderMainRepository.findAll();
    assertThat(paymentOrderMainList).hasSize(databasePaymentOrderMainSizeBeforeDelete - 1);

    // Validate the ShipmentOrderMain in the database
    List<ShipmentOrderMain> shipmentOrderMainList = shipmentOrderMainRepository.findAll();
    assertThat(shipmentOrderMainList).hasSize(databaseShipmentOrderMainSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  @WithMockUser
  void deleteOrderMainAndAllRealationshipOrderMainByUserShouldThrowStatusForbidden()
      throws Exception {
    // given OrderMain
    orderMain = createEntityOrderMain(em);
    orderMainRepository.save(orderMain);

    // given ProducInOrderMain
    productInOrderMain = createEntityProductInOrderMain(em);
    productInOrderMain.setOrderMain(orderMain);
    productInOrderMain.setProductId(product.getId());
    productInOrderMainRepository.save(productInOrderMain);

    // given PaymentOrderMain
    paymentOrderMain = createEntityPaymentOrderMain(em);
    paymentOrderMain.setOrderMain(orderMain);
    paymentOrderMainRepository.save(paymentOrderMain);

    // given ShipmentOrderMain
    shipmentOrderMain = createEntityShipmentOrderMain(em);
    shipmentOrderMain.setOrderMain(orderMain);
    shipmentOrderMainRepository.save(shipmentOrderMain);

    // set Relationship for OrderMain
    orderMain.setShipmentOrderMain(shipmentOrderMain);
    orderMain.setPaymentOrderMain(paymentOrderMain);
    orderMain.addProductInOrderMain(productInOrderMain);
    orderMainRepository.save(orderMain);

    int databasePaymentOrderMainSizeBeforeDelete = paymentOrderMainRepository.findAll().size();
    int databaseShipmentOrderMainSizeBeforeDelete = shipmentOrderMainRepository.findAll().size();
    int databaseProductInOrderSizeBeforeDelete = productInOrderMainRepository.findAll().size();
    int databaseSizeBeforeDelete = orderMainRepository.findAll().size();

    // Delete the orderMain
    restOrderMainMockMvc.perform(delete(ENTITY_API_URL_DELETE, orderMain.getId())
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isForbidden());

    // Validate the OrderMain database
    List<OrderMain> orderMainList = orderMainRepository.findAll();
    assertThat(orderMainList).hasSize(databaseSizeBeforeDelete);

    // Validate the ProductInOrderMain database
    List<ProductInOrderMain> productInOrderMainList = productInOrderMainRepository.findAll();
    assertThat(productInOrderMainList).hasSize(databaseProductInOrderSizeBeforeDelete);

    // Validate the PaymentOrderMain in the database
    List<PaymentOrderMain> paymentOrderMainList = paymentOrderMainRepository.findAll();
    assertThat(paymentOrderMainList).hasSize(databasePaymentOrderMainSizeBeforeDelete);

    // Validate the ShipmentOrderMain in the database
    List<ShipmentOrderMain> shipmentOrderMainList = shipmentOrderMainRepository.findAll();
    assertThat(shipmentOrderMainList).hasSize(databaseShipmentOrderMainSizeBeforeDelete);
  }

  @Test
  @Transactional
  void deleteOrderMainAndAllRealationshipOrderMainByUserShouldThrowStatusUnauthorized()
      throws Exception {
    // given OrderMain
    orderMain = createEntityOrderMain(em);
    orderMainRepository.save(orderMain);

    // given ProducInOrderMain
    productInOrderMain = createEntityProductInOrderMain(em);
    productInOrderMain.setOrderMain(orderMain);
    productInOrderMain.setProductId(product.getId());
    productInOrderMainRepository.save(productInOrderMain);

    // given PaymentOrderMain
    paymentOrderMain = createEntityPaymentOrderMain(em);
    paymentOrderMain.setOrderMain(orderMain);
    paymentOrderMainRepository.save(paymentOrderMain);

    // given ShipmentOrderMain
    shipmentOrderMain = createEntityShipmentOrderMain(em);
    shipmentOrderMain.setOrderMain(orderMain);
    shipmentOrderMainRepository.save(shipmentOrderMain);

    // set Relationship for OrderMain
    orderMain.setShipmentOrderMain(shipmentOrderMain);
    orderMain.setPaymentOrderMain(paymentOrderMain);
    orderMain.addProductInOrderMain(productInOrderMain);
    orderMainRepository.save(orderMain);

    int databasePaymentOrderMainSizeBeforeDelete = paymentOrderMainRepository.findAll().size();
    int databaseShipmentOrderMainSizeBeforeDelete = shipmentOrderMainRepository.findAll().size();
    int databaseProductInOrderSizeBeforeDelete = productInOrderMainRepository.findAll().size();
    int databaseSizeBeforeDelete = orderMainRepository.findAll().size();

    // Delete the orderMain
    restOrderMainMockMvc.perform(delete(ENTITY_API_URL_DELETE, orderMain.getId())
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized());

    // Validate the OrderMain database
    List<OrderMain> orderMainList = orderMainRepository.findAll();
    assertThat(orderMainList).hasSize(databaseSizeBeforeDelete);

    // Validate the ProductInOrderMain database
    List<ProductInOrderMain> productInOrderMainList = productInOrderMainRepository.findAll();
    assertThat(productInOrderMainList).hasSize(databaseProductInOrderSizeBeforeDelete);

    // Validate the PaymentOrderMain in the database
    List<PaymentOrderMain> paymentOrderMainList = paymentOrderMainRepository.findAll();
    assertThat(paymentOrderMainList).hasSize(databasePaymentOrderMainSizeBeforeDelete);

    // Validate the ShipmentOrderMain in the database
    List<ShipmentOrderMain> shipmentOrderMainList = shipmentOrderMainRepository.findAll();
    assertThat(shipmentOrderMainList).hasSize(databaseShipmentOrderMainSizeBeforeDelete);
  }

  private User checkIfUserExist() {
    return userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()
            .orElseThrow(UserNotFoundException::new))
        .orElseThrow(UserNotFoundException::new);
  }
}