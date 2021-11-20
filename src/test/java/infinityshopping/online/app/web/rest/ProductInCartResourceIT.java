package infinityshopping.online.app.web.rest;

import static infinityshopping.online.app.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import infinityshopping.online.app.IntegrationTest;
import infinityshopping.online.app.config.Constants;
import infinityshopping.online.app.domain.Cart;
import infinityshopping.online.app.domain.PaymentCart;
import infinityshopping.online.app.domain.Product;
import infinityshopping.online.app.domain.ProductInCart;
import infinityshopping.online.app.domain.ShipmentCart;
import infinityshopping.online.app.domain.User;
import infinityshopping.online.app.domain.enumeration.PaymentStatusEnum;
import infinityshopping.online.app.domain.enumeration.ProductCategoryEnum;
import infinityshopping.online.app.repository.CartRepository;
import infinityshopping.online.app.repository.PaymentCartRepository;
import infinityshopping.online.app.repository.ProductInCartRepository;
import infinityshopping.online.app.repository.ProductRepository;
import infinityshopping.online.app.repository.ShipmentCartRepository;
import infinityshopping.online.app.repository.UserRepository;
import infinityshopping.online.app.security.AuthoritiesConstants;
import infinityshopping.online.app.security.SecurityUtils;
import infinityshopping.online.app.service.AddVat;
import infinityshopping.online.app.service.UserNotFoundException;
import infinityshopping.online.app.service.dto.ProductInCartDTO;
import infinityshopping.online.app.service.mapper.CartMapper;
import infinityshopping.online.app.service.mapper.ProductInCartMapper;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
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
class ProductInCartResourceIT implements AddVat {

  private static Random random = new Random();
  private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

  private static final String DEFAULT_CATEGORY = String.valueOf(ProductCategoryEnum.Vitamins);
  private static final String DEFAULT_CATEGORY_2 = String.valueOf(ProductCategoryEnum.Minerals);

  private static final String DEFAULT_NAME = "AAAAAAAAAA";
  private static final String DEFAULT_NAME_2 = "BBBBBBBBB";

  private static final BigDecimal DEFAULT_QUANTITY
      = new BigDecimal(random.nextInt(100 - 1) + 1);
  private static final BigDecimal UPDATED_QUANTITY = new BigDecimal("3.00");

  private static final BigDecimal DEFAULT_QUANTITY_2
      = new BigDecimal(random.nextInt(100 - 1) + 1);

  private static final BigDecimal DEFAULT_HIGHER_QUANTITY = new BigDecimal("1250");
  private static final BigDecimal UPDATED_HIGHER_QUANTITY = new BigDecimal("2000");

  private static final BigDecimal DEFAULT_PRICE_NET = new BigDecimal("5.53");
  private static final BigDecimal DEFAULT_PRICE_NET_2 = new BigDecimal("125.50");

  private static final BigDecimal DEFAULT_VAT
      = new BigDecimal(random.nextInt(30 - 5) + 5);
  private static final BigDecimal DEFAULT_VAT_2
      = new BigDecimal(random.nextInt(30 - 5) + 5);

  private final BigDecimal defaultPriceGross
      = addVat(DEFAULT_PRICE_NET, DEFAULT_VAT);

  private final BigDecimal defaultPriceGross2
      =  addVat(DEFAULT_PRICE_NET_2, DEFAULT_VAT_2);

  private static final BigDecimal DEFAULT_TOTAL_PRICE_NET
      = DEFAULT_QUANTITY.multiply(DEFAULT_PRICE_NET);
  private static final BigDecimal DEFAULT_TOTAL_PRICE_NET_2
      = DEFAULT_QUANTITY_2.multiply(DEFAULT_PRICE_NET_2);

  public final BigDecimal defaultTotalPriceGross
      = DEFAULT_QUANTITY.multiply(defaultPriceGross);

  private final BigDecimal defaultTotalPriceGross2
      = DEFAULT_QUANTITY_2.multiply(defaultPriceGross2);

  private static final BigDecimal DEFAULT_STOCK
      = new BigDecimal(random.nextInt(1000 - 101) + 101);
  private static final BigDecimal DEFAULT_STOCK_2 = new BigDecimal("100");

  private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
  private static final String DEFAULT_DESCRIPTION_2 = "BBBBBBBBB";

  private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
  private static final byte[] DEFAULT_IMAGE_2 = TestUtil.createByteArray(1, "0");

  private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
  private static final String DEFAULT_IMAGE_CONTENT_TYPE_2 = "image/jpg";

  // Product
  private static final Instant DEFAULT_CREATE_TIME = Instant.ofEpochMilli(0L);
  private static final Instant DEFAULT_CREATE_TIME2 = Instant.ofEpochMilli(0L);

  private static final Instant DEFAULT_UPDATE_TIME = Instant.ofEpochMilli(0L);
  private static final Instant DEFAULT_UPDATE_TIME2 = Instant.ofEpochMilli(0L);

  private static final ProductCategoryEnum DEFAULT_PRODUCT_CATEGORY_ENUM_Vitamins
      = ProductCategoryEnum.Vitamins;

  // PaymentCart
  private static final String DEFAULT_PaymentCart_NAME = "DHL bank transfer";
  private static final BigDecimal DEFAULT_PaymentCart_PRICE_NET = new BigDecimal("3.0");
  private static final BigDecimal DEFAULT_PaymentCart_VAT = new BigDecimal("23");
  private static final BigDecimal DEFAULT_PaymentCart_PRICE_GROSS = new BigDecimal("3.69");
  private static final PaymentStatusEnum DEFAULT_PAYMENT_STATUS_ENUM
      = PaymentStatusEnum.WaitingForBankTransfer;

  // Cart
  private static final BigDecimal DEFAULT_AMOUNT_OF_CART_NET = BigDecimal.ZERO;
  private static final BigDecimal DEFAULT_AMOUNT_OF_CART_GROSS = BigDecimal.ZERO;

  private static final BigDecimal DEFAULT_AMOUNT_OF_SHIPMENT_NET
      = DEFAULT_PaymentCart_PRICE_NET;
  private static final BigDecimal DEFAULT_AMOUNT_OF_SHIPMENT_GROSS
      = DEFAULT_PaymentCart_PRICE_GROSS;

  private static final BigDecimal DEFAULT_AMOUNT_OF_ORDER_NET
      = DEFAULT_PaymentCart_PRICE_NET;
  private static final BigDecimal DEFAULT_AMOUNT_OF_ORDER_GROSS
      = DEFAULT_PaymentCart_PRICE_GROSS;

  // ShipmentCart
  private static final String DEFAULT_ShipmentCart_FIRST_NAME = "AAAAAAAAAA";
  private static final String DEFAULT_ShipmentCart_LAST_NAME = "AAAAAAAAAA";
  private static final String DEFAULT_ShipmentCart_STREET = "AAAAAAAAAA";
  private static final String DEFAULT_ShipmentCart_POSTAL_CODE = "AAAAAAAAAA";
  private static final String DEFAULT_ShipmentCart_CITY = "AAAAAAAAAA";
  private static final String DEFAULT_ShipmentCart_COUNTRY = "AAAAAAAAAA";
  private static final String DEFAULT_ShipmentCart_PHONE_TO_THE_RECEIVER = "AAAAAAAAAA";
  private static final String DEFAULT_ShipmentCart_FIRM = "AAAAAAAAAA";
  private static final String DEFAULT_ShipmentCart_TAX_NUMBER = "AAAAAAAAAA";

  private static final String ENTITY_API_URL = "/api/product-in-carts";
  private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
  private static final String ENTITY_API_URL_USER_CART = ENTITY_API_URL + "/userCart";

  @Autowired
  private ProductInCartRepository productInCartRepository;

  @Autowired
  private CartRepository cartRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ProductInCartMapper productInCartMapper;

  @Autowired
  private CartMapper cartMapper;

  @Autowired
  private PaymentCartRepository paymentCartRepository;

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private ShipmentCartRepository shipmentCartRepository;

  @Autowired
  private EntityManager em;

  @Autowired
  private MockMvc restProductInCartMockMvc;

  private Product product;

  private ProductInCart productInCart;

  private ProductInCart productInCart2;

  private User currentLoggedUser;

  public ProductInCart createEntity(EntityManager em) {
    ProductInCart productInCart = new ProductInCart()
        .category(DEFAULT_CATEGORY)
        .name(DEFAULT_NAME)
        .quantity(DEFAULT_QUANTITY)
        .priceNet(DEFAULT_PRICE_NET)
        .priceGross(defaultPriceGross)
        .vat(DEFAULT_VAT)
        .totalPriceNet(DEFAULT_TOTAL_PRICE_NET)
        .totalPriceGross(defaultTotalPriceGross)
        .stock(DEFAULT_STOCK)
        .description(DEFAULT_DESCRIPTION)
        .image(DEFAULT_IMAGE)
        .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE);
    return productInCart;
  }

  public ProductInCart createEntity2(EntityManager em) {
    ProductInCart productInCart2 = new ProductInCart()
        .category(DEFAULT_CATEGORY_2)
        .name(DEFAULT_NAME_2)
        .quantity(DEFAULT_QUANTITY_2)
        .priceNet(DEFAULT_PRICE_NET_2)
        .priceGross(defaultPriceGross2)
        .vat(DEFAULT_VAT_2)
        .totalPriceNet(DEFAULT_TOTAL_PRICE_NET_2)
        .totalPriceGross(defaultTotalPriceGross2)
        .stock(DEFAULT_STOCK_2)
        .description(DEFAULT_DESCRIPTION_2)
        .image(DEFAULT_IMAGE_2)
        .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE_2);
    return productInCart2;
  }

  public Product createEntityProduct(EntityManager em) {
    Product product = new Product()
        .productCategoryEnum(DEFAULT_PRODUCT_CATEGORY_ENUM_Vitamins)
        .name(DEFAULT_NAME)
        .quantity(DEFAULT_QUANTITY)
        .priceNet(DEFAULT_PRICE_NET)
        .vat(DEFAULT_VAT)
        .priceGross(defaultPriceGross)
        .stock(DEFAULT_STOCK)
        .description(DEFAULT_DESCRIPTION)
        .createTime(DEFAULT_CREATE_TIME)
        .updateTime(DEFAULT_UPDATE_TIME)
        .image(DEFAULT_IMAGE)
        .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE);
    return product;
  }

  public static ShipmentCart createEntityShipmentCart(EntityManager em) {
    ShipmentCart shipmentCart = new ShipmentCart()
        .firstName(DEFAULT_ShipmentCart_FIRST_NAME)
        .lastName(DEFAULT_ShipmentCart_LAST_NAME)
        .street(DEFAULT_ShipmentCart_STREET)
        .postalCode(DEFAULT_ShipmentCart_POSTAL_CODE)
        .city(DEFAULT_ShipmentCart_CITY)
        .country(DEFAULT_ShipmentCart_COUNTRY)
        .phoneToTheReceiver(DEFAULT_ShipmentCart_PHONE_TO_THE_RECEIVER)
        .firm(DEFAULT_ShipmentCart_FIRM)
        .taxNumber(DEFAULT_ShipmentCart_TAX_NUMBER);
    return shipmentCart;
  }


  @BeforeEach
  public void initUser() throws Exception {
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
    cart.setAmountOfCartGross(DEFAULT_AMOUNT_OF_CART_GROSS);
    cart.setAmountOfShipmentNet(DEFAULT_AMOUNT_OF_SHIPMENT_NET);
    cart.setAmountOfShipmentGross(DEFAULT_AMOUNT_OF_SHIPMENT_GROSS);
    cart.setAmountOfOrderNet(DEFAULT_AMOUNT_OF_ORDER_NET);
    cart.setAmountOfOrderGross(DEFAULT_AMOUNT_OF_ORDER_GROSS);
    cartRepository.save(cart);
    user.setCart(cart);
    userRepository.saveAndFlush(user);

    // given PaymentCart for User
    PaymentCart paymentCart = new PaymentCart();
    paymentCart.setName(DEFAULT_PaymentCart_NAME);
    paymentCart.setPriceNet(DEFAULT_PaymentCart_PRICE_NET);
    paymentCart.setVat(DEFAULT_PaymentCart_VAT);
    paymentCart.setPriceGross(DEFAULT_PaymentCart_PRICE_GROSS);
    paymentCart.setPaymentStatus(DEFAULT_PAYMENT_STATUS_ENUM);
    paymentCart.setCart(cart);
    paymentCartRepository.save(paymentCart);
    cart.setPaymentCart(paymentCart);
    cartRepository.save(cart);

    // given ShipmentCart for User
    ShipmentCart shipmentCart = new ShipmentCart();
    shipmentCart = createEntityShipmentCart(em);
    shipmentCart.setCart(cart);
    shipmentCartRepository.save(shipmentCart);
    cart.setShipmentCart(shipmentCart);
    cartRepository.save(cart);

    // given Entity PruductInCart
    productInCart = createEntity(em);

    // given Product
    product = createEntityProduct(em);
    productRepository.saveAndFlush(product);
  }

  @Test
  @Transactional
  @WithMockUser(username = "alice", authorities = AuthoritiesConstants.USER)
  public void createProductInCartToProperUserOfCart() throws Exception {
    currentLoggedUser = checkIfUserExist();

    // Create the ProductInCart
    final int databaseCartSizeBeforeCreate = cartRepository.findAll().size();
    final int databaseProductInCartSizeBeforeCreate = productInCartRepository.findAll().size();
    productInCart.setProductId(product.getId());
    productInCart.setCart(currentLoggedUser.getCart());
    ProductInCartDTO productInCartDto = productInCartMapper.toDto(productInCart);

    restProductInCartMockMvc.perform(post(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(productInCartDto)))
        .andExpect(status().isCreated());

    // Validate the ProductInCart in the database
    List<ProductInCart> productInCartList = productInCartRepository.findAll();
    assertThat(productInCartList).hasSize(databaseProductInCartSizeBeforeCreate + 1);
    ProductInCart testProductInCart = productInCartList.get(productInCartList.size() - 1);
    assertThat(testProductInCart.getCategory()).isEqualTo(DEFAULT_CATEGORY);
    assertThat(testProductInCart.getName()).isEqualTo(DEFAULT_NAME);
    assertThat(testProductInCart.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
    assertThat(testProductInCart.getPriceNet()).isEqualTo(DEFAULT_PRICE_NET);
    assertThat(testProductInCart.getVat()).isEqualTo(DEFAULT_VAT);
    assertThat(testProductInCart.getPriceGross()).isEqualTo(defaultPriceGross);
    assertThat(testProductInCart.getTotalPriceNet()).isEqualTo(DEFAULT_TOTAL_PRICE_NET);
    assertThat(testProductInCart.getTotalPriceGross()).isEqualTo(defaultTotalPriceGross);
    assertThat(testProductInCart.getStock()).isEqualTo(DEFAULT_STOCK);
    assertThat(testProductInCart.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    assertThat(testProductInCart.getImage()).isEqualTo(DEFAULT_IMAGE);
    assertThat(testProductInCart.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
    assertNotNull(testProductInCart.getCart());
    assertThat(testProductInCart.getProductId()).isEqualTo(product.getId());

    // Validate the Cart in the database
    List<Cart> cartList = cartRepository.findAll();
    assertThat(cartList).hasSize(databaseCartSizeBeforeCreate);
    Cart testCart = cartList.get(cartList.size() - 1);
    assertThat(testCart.getAmountOfCartNet()).isEqualTo(DEFAULT_TOTAL_PRICE_NET);
    assertThat(testCart.getAmountOfCartGross()).isEqualTo(defaultTotalPriceGross);
    assertThat(testCart.getAmountOfShipmentNet())
        .isEqualTo(DEFAULT_AMOUNT_OF_SHIPMENT_NET);
    assertThat(testCart.getAmountOfShipmentGross()).isEqualTo(DEFAULT_AMOUNT_OF_SHIPMENT_GROSS);
    assertThat(testCart.getAmountOfOrderNet())
        .isEqualTo(DEFAULT_TOTAL_PRICE_NET.add(DEFAULT_AMOUNT_OF_SHIPMENT_NET));
    assertThat(testCart.getAmountOfOrderGross())
        .isEqualTo(defaultTotalPriceGross.add(DEFAULT_AMOUNT_OF_SHIPMENT_GROSS));

    // Validate id relation OneToMany between Cart and ProductInCart
    assertNotNull(testProductInCart.getCart().getId());
    assertNotNull(testCart.getId());
    assertThat(testProductInCart.getCart().getId()).isEqualTo(testCart.getId());
  }

  @Test
  @Transactional
  @WithMockUser(username = "alice", authorities = AuthoritiesConstants.USER)
  public void createProductInCartToProperUserOfCartWithoutProductIdShouldThrowStatus500()
      throws Exception {
    // Create the ProductInCart
    ProductInCartDTO productInCartDto = productInCartMapper.toDto(productInCart);

    restProductInCartMockMvc.perform(post(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(productInCartDto)))
        .andExpect(status().isInternalServerError());
  }

  @Test
  @Transactional
  @WithMockUser(username = "alice", authorities = AuthoritiesConstants.USER)
  public void createProductInCartToProperUserOfCartWithWrongQuantityShouldThrowBadRequest()
      throws Exception {
    currentLoggedUser = checkIfUserExist();

    // Create the ProductInCart
    productInCart.setQuantity(DEFAULT_HIGHER_QUANTITY);
    productInCart.setProductId(product.getId());
    productInCart.setCart(currentLoggedUser.getCart());
    ProductInCartDTO productInCartDto = productInCartMapper.toDto(productInCart);

    restProductInCartMockMvc.perform(post(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(productInCartDto)))
        .andExpect(status().isBadRequest());
  }

  @Test
  @Transactional
  public void createProductInCartByAnyoneShouldThrowStatusUnauthorized401()
      throws Exception {
    // Create the ProductInCart
    ProductInCartDTO productInCartDto = productInCartMapper.toDto(productInCart);

    restProductInCartMockMvc.perform(post(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(productInCartDto)))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @Transactional
  @WithMockUser(username = "alice", authorities = AuthoritiesConstants.USER)
  public void createProductInCartWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = productInCartRepository.findAll().size();

    // Create the ProductInCart with an existing ID
    productInCart.setId(1L);
    ProductInCartDTO productInCartDto = productInCartMapper.toDto(productInCart);

    // An entity with an existing ID cannot be created, so this API call must fail
    restProductInCartMockMvc.perform(post(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(productInCartDto)))
        .andExpect(status().isBadRequest());

    // Validate the ProductInCart in the database
    List<ProductInCart> productInCartList = productInCartRepository.findAll();
    assertThat(productInCartList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  @WithMockUser(username = "alice", authorities = AuthoritiesConstants.USER)
  public void getAllProductInCartOfCurrentUser() throws Exception {
    currentLoggedUser = checkIfUserExist();

    productInCart.setCart(currentLoggedUser.getCart());
    productInCart.setProductId(product.getId());
    productInCartRepository.save(productInCart);
    currentLoggedUser.getCart().addProductInCart(productInCart);

    // Get all the productInCartList
    restProductInCartMockMvc.perform(get(ENTITY_API_URL_USER_CART))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(productInCart.getId().intValue())))
        .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY)))
        .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
        .andExpect(jsonPath("$.[*].quantity").value(hasItem(sameNumber(DEFAULT_QUANTITY))))
        .andExpect(jsonPath("$.[*].priceNet").value(hasItem(sameNumber(DEFAULT_PRICE_NET))))
        .andExpect(jsonPath("$.[*].vat").value(hasItem(sameNumber(DEFAULT_VAT))))
        .andExpect(jsonPath("$.[*].priceGross").value(hasItem(sameNumber(defaultPriceGross))))
        .andExpect(jsonPath("$.[*].totalPriceNet")
            .value(hasItem(sameNumber(DEFAULT_TOTAL_PRICE_NET))))
        .andExpect(jsonPath("$.[*].totalPriceGross")
            .value(hasItem(sameNumber(defaultTotalPriceGross))))
        .andExpect(jsonPath("$.[*].stock").value(hasItem(sameNumber(DEFAULT_STOCK))))
        .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
        .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
        .andExpect(jsonPath("$.[*].image")
            .value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
        .andExpect(jsonPath("$.[*].productId").value(hasItem(product.getId().intValue())));
  }

  @Test
  @Transactional
  @WithMockUser(username = "alice", authorities = AuthoritiesConstants.USER)
  public void getProductInCart() throws Exception {
    productInCart.setProductId(product.getId());
    productInCartRepository.save(productInCart);

    // Get the productInCart
    restProductInCartMockMvc.perform(get(ENTITY_API_URL_ID, productInCart.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.id").value(productInCart.getId().intValue()))
        .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY))
        .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
        .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
        .andExpect(jsonPath("$.priceNet").value(sameNumber(DEFAULT_PRICE_NET)))
        .andExpect(jsonPath("$.vat").value(sameNumber(DEFAULT_VAT)))
        .andExpect(jsonPath("$.priceGross").value(sameNumber(defaultPriceGross)))
        .andExpect(jsonPath("$.totalPriceNet").value(sameNumber(DEFAULT_TOTAL_PRICE_NET)))
        .andExpect(jsonPath("$.totalPriceGross").value(sameNumber(defaultTotalPriceGross)))
        .andExpect(jsonPath("$.stock").value(DEFAULT_STOCK))
        .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
        .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
        .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)))
        .andExpect(jsonPath("$.productId").value(product.getId()));
  }

  @Test
  @Transactional
  @WithMockUser(username = "alice", authorities = AuthoritiesConstants.USER)
  public void getNonExistingProductInCart() throws Exception {
    // Get the productInCart
    restProductInCartMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  @WithMockUser(username = "alice", authorities = AuthoritiesConstants.USER)
  public void deleteProductInCart() throws Exception {
    // given Cart
    currentLoggedUser = checkIfUserExist();
    Cart cart = currentLoggedUser.getCart();

    // given ProducInCart
    productInCart.setCart(cart);
    productInCart.setProductId(product.getId());
    productInCartRepository.saveAndFlush(productInCart);
    cart.setAmountOfCartNet(productInCart.getTotalPriceNet());
    cart.setAmountOfCartGross(productInCart.getTotalPriceGross());
    cart.setAmountOfOrderNet(productInCart.getTotalPriceNet().add(DEFAULT_PaymentCart_PRICE_NET));
    cart.setAmountOfOrderGross(productInCart.getTotalPriceGross()
        .add(DEFAULT_PaymentCart_PRICE_GROSS));
    cart.addProductInCart(productInCart);
    cartRepository.save(cart);

    // given ProducInCart2
    productInCart2 = createEntity2(em);
    productInCart2.setCart(cart);
    productInCart2.setProductId(product.getId());
    productInCartRepository.saveAndFlush(productInCart2);
    cart.addProductInCart(productInCart2);
    cart.setAmountOfCartNet(cart.getAmountOfCartNet().add(DEFAULT_TOTAL_PRICE_NET_2));
    cart.setAmountOfCartGross(cart.getAmountOfCartGross().add(defaultTotalPriceGross2));
    cart.setAmountOfOrderNet(cart.getAmountOfOrderNet().add(DEFAULT_TOTAL_PRICE_NET_2));
    cart.setAmountOfOrderGross(cart.getAmountOfOrderGross().add(defaultTotalPriceGross2));
    cartRepository.save(cart);

    // when
    int databaseSizeBeforeDelete = productInCartRepository.findAll().size();

    // Delete the productInCart
    restProductInCartMockMvc.perform(delete(ENTITY_API_URL_ID, productInCart.getId())
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent());

    // then
    // Validate the database contains one less item
    List<ProductInCart> productInCartList = productInCartRepository.findAll();
    assertThat(productInCartList).hasSize(databaseSizeBeforeDelete - 1);

    // Validate the Cart in the database
    List<Cart> cartList = cartRepository.findAll();
    Cart testCart = cartList.get(cartList.size() - 1);
    assertThat(testCart.getAmountOfCartNet()).isEqualTo(
        DEFAULT_TOTAL_PRICE_NET_2.setScale(2, RoundingMode.CEILING));
    assertThat(testCart.getAmountOfCartGross()).isEqualTo(
        defaultTotalPriceGross2.setScale(4, RoundingMode.CEILING));
    assertThat(testCart.getAmountOfShipmentNet()).isEqualTo(DEFAULT_AMOUNT_OF_SHIPMENT_NET);
    assertThat(testCart.getAmountOfShipmentGross()).isEqualTo(DEFAULT_AMOUNT_OF_SHIPMENT_GROSS);
    assertThat(testCart.getAmountOfOrderNet()).isEqualTo(DEFAULT_TOTAL_PRICE_NET_2
        .add(DEFAULT_AMOUNT_OF_SHIPMENT_NET).setScale(2, RoundingMode.CEILING));
    assertThat(testCart.getAmountOfOrderGross()).isEqualTo(defaultTotalPriceGross2
        .add(DEFAULT_AMOUNT_OF_SHIPMENT_GROSS).setScale(4, RoundingMode.CEILING));
  }

  @Test
  @Transactional
  @WithMockUser(username = "alice", authorities = AuthoritiesConstants.USER)
  public void putNewProductInCartWithWrongValueQuantityShouldThrowStatusBadRequest()
      throws Exception {
    currentLoggedUser = checkIfUserExist();

    productInCart.setCart(currentLoggedUser.getCart());
    productInCart.setProductId(product.getId());
    productInCartRepository.save(productInCart);
    currentLoggedUser.getCart().addProductInCart(productInCart);
    cartRepository.save(currentLoggedUser.getCart());

    final int databaseCartSizeBeforeUpdate = cartRepository.findAll().size();
    final int databaseProductInCartSizeBeforeUpdate = productInCartRepository.findAll().size();

    // Update the productInCart
    ProductInCartDTO productInCartDto = productInCartMapper.toDto(
        productInCartRepository.findById(productInCart.getId()).get());
    productInCartDto.setQuantity(UPDATED_HIGHER_QUANTITY);

    restProductInCartMockMvc.perform(put(ENTITY_API_URL_ID, productInCartDto.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(productInCartDto)))
        .andExpect(status().isBadRequest());

    // Validate the ProductInCart in the database
    List<ProductInCart> productInCartList = productInCartRepository.findAll();
    assertThat(productInCartList).hasSize(databaseProductInCartSizeBeforeUpdate);
    ProductInCart testProductInCart = productInCartList.get(productInCartList.size() - 1);
    assertThat(testProductInCart.getCategory()).isEqualTo(DEFAULT_CATEGORY);
    assertThat(testProductInCart.getName()).isEqualTo(DEFAULT_NAME);
    assertThat(testProductInCart.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
    assertThat(testProductInCart.getPriceNet())
        .isEqualTo(DEFAULT_PRICE_NET.setScale(2, RoundingMode.CEILING));
    assertThat(testProductInCart.getVat()).isEqualTo(DEFAULT_VAT);
    assertThat(testProductInCart.getPriceGross())
        .isEqualTo(defaultPriceGross.setScale(4, RoundingMode.CEILING));
    assertThat(testProductInCart.getTotalPriceNet()).isEqualTo(DEFAULT_TOTAL_PRICE_NET.setScale(2));
    assertThat(testProductInCart.getTotalPriceGross())
        .isEqualTo(defaultTotalPriceGross.setScale(4, RoundingMode.CEILING));
    assertThat(testProductInCart.getStock()).isEqualTo(DEFAULT_STOCK);
    assertThat(testProductInCart.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    assertThat(testProductInCart.getImage()).isEqualTo(DEFAULT_IMAGE);
    assertThat(testProductInCart.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
    assertThat(testProductInCart.getProductId()).isEqualTo(product.getId());

    // Validate the Cart in the database
    List<Cart> cartList = cartRepository.findAll();
    assertThat(cartList).hasSize(databaseCartSizeBeforeUpdate);
    Cart testCart = cartList.get(cartList.size() - 1);
    assertThat(testCart.getAmountOfCartNet()).isEqualTo(DEFAULT_AMOUNT_OF_CART_NET);
    assertThat(testCart.getAmountOfCartGross()).isEqualTo(DEFAULT_AMOUNT_OF_CART_GROSS);
    assertThat(testCart.getAmountOfShipmentNet()).isEqualTo(DEFAULT_AMOUNT_OF_SHIPMENT_NET);
    assertThat(testCart.getAmountOfShipmentGross()).isEqualTo(DEFAULT_AMOUNT_OF_SHIPMENT_GROSS);
    assertThat(testCart.getAmountOfOrderNet()).isEqualTo(DEFAULT_AMOUNT_OF_ORDER_NET);
    assertThat(testCart.getAmountOfOrderGross()).isEqualTo(DEFAULT_AMOUNT_OF_ORDER_GROSS);

    List<Product> productList = productRepository.findAll();
    Product testProduct = productList.get(productList.size() - 1);
    assertThat(testProduct.getStock()).isEqualTo(DEFAULT_STOCK);
  }

  @Test
  @Transactional
  @WithMockUser(username = "alice", authorities = AuthoritiesConstants.USER)
  public void putNewProductInCart() throws Exception {
    currentLoggedUser = checkIfUserExist();

    productInCart.setCart(currentLoggedUser.getCart());
    productInCart.setProductId(product.getId());
    productInCartRepository.save(productInCart);
    currentLoggedUser.getCart().addProductInCart(productInCart);
    cartRepository.save(currentLoggedUser.getCart());

    final int databaseCartSizeBeforeUpdate = cartRepository.findAll().size();
    final int databaseProductInCartSizeBeforeUpdate = productInCartRepository.findAll().size();

    // Update the productInCart
    ProductInCartDTO productInCartDto = productInCartMapper.toDto(
        productInCartRepository.findById(productInCart.getId()).get());
    productInCartDto.setQuantity(UPDATED_QUANTITY);

    restProductInCartMockMvc.perform(put(ENTITY_API_URL_ID, productInCartDto.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(productInCartDto)))
        .andExpect(status().isOk());

    // Validate the ProductInCart in the database
    List<ProductInCart> productInCartList = productInCartRepository.findAll();
    assertThat(productInCartList).hasSize(databaseProductInCartSizeBeforeUpdate);
    ProductInCart testProductInCart = productInCartList.get(productInCartList.size() - 1);
    assertThat(testProductInCart.getCategory()).isEqualTo(DEFAULT_CATEGORY);
    assertThat(testProductInCart.getName()).isEqualTo(DEFAULT_NAME);
    assertThat(testProductInCart.getQuantity()).isEqualTo(UPDATED_QUANTITY);
    assertThat(testProductInCart.getPriceNet()).isEqualTo(DEFAULT_PRICE_NET);
    assertThat(testProductInCart.getVat()).isEqualTo(DEFAULT_VAT);
    assertThat(testProductInCart.getPriceGross()).isEqualTo(defaultPriceGross);
    assertThat(testProductInCart.getTotalPriceNet())
        .isEqualTo(UPDATED_QUANTITY.multiply(DEFAULT_PRICE_NET));
    assertThat(testProductInCart.getTotalPriceGross())
        .isEqualTo(UPDATED_QUANTITY.multiply(defaultPriceGross));
    assertThat(testProductInCart.getStock()).isEqualTo(DEFAULT_STOCK);
    assertThat(testProductInCart.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    assertThat(testProductInCart.getImage()).isEqualTo(DEFAULT_IMAGE);
    assertThat(testProductInCart.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
    assertNotNull(testProductInCart.getCart());
    assertThat(testProductInCart.getProductId()).isEqualTo(product.getId());

    // Validate the Cart in the database
    List<Cart> cartList = cartRepository.findAll();
    assertThat(cartList).hasSize(databaseCartSizeBeforeUpdate);
    Cart testCart = cartList.get(cartList.size() - 1);
    assertThat(testCart.getAmountOfCartNet())
        .isEqualTo(UPDATED_QUANTITY.multiply(DEFAULT_PRICE_NET));
    assertThat(testCart.getAmountOfCartGross())
        .isEqualTo(UPDATED_QUANTITY.multiply(defaultPriceGross));
    assertThat(testCart.getAmountOfShipmentNet())
        .isEqualTo(DEFAULT_AMOUNT_OF_SHIPMENT_NET);
    assertThat(testCart.getAmountOfShipmentGross()).isEqualTo(DEFAULT_AMOUNT_OF_SHIPMENT_GROSS);
    assertThat(testCart.getAmountOfOrderNet()).isEqualTo(
        UPDATED_QUANTITY.multiply(DEFAULT_PRICE_NET).add(DEFAULT_AMOUNT_OF_SHIPMENT_NET));
    assertThat(testCart.getAmountOfOrderGross()).isEqualTo(
        UPDATED_QUANTITY.multiply(defaultPriceGross).add(DEFAULT_AMOUNT_OF_SHIPMENT_GROSS));

    // Validate id relation OneToMany between Cart and ProductInCart
    assertNotNull(testProductInCart.getCart().getId());
    assertNotNull(testCart.getId());
    assertThat(testProductInCart.getCart().getId()).isEqualTo(testCart.getId());
  }

  @Test
  @Transactional
  @WithMockUser(username = "alice", authorities = AuthoritiesConstants.USER)
  void putNonExistingProductInCart() throws Exception {
    currentLoggedUser = checkIfUserExist();

    productInCart.setCart(currentLoggedUser.getCart());
    productInCart.setProductId(product.getId());
    productInCartRepository.save(productInCart);
    currentLoggedUser.getCart().addProductInCart(productInCart);
    cartRepository.save(currentLoggedUser.getCart());

    final int databaseProductInCartSizeBeforeUpdate = productInCartRepository.findAll().size();

    // Update the productInCart
    ProductInCartDTO productInCartDto = productInCartMapper.toDto(
        productInCartRepository.findById(productInCart.getId()).get());
    productInCartDto.setId(count.incrementAndGet());

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restProductInCartMockMvc.perform(put(ENTITY_API_URL_ID, productInCartDto.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(productInCartDto)))
        .andExpect(status().isBadRequest());

    // Validate the ProductInCart in the database
    List<ProductInCart> productInCartList = productInCartRepository.findAll();
    assertThat(productInCartList).hasSize(databaseProductInCartSizeBeforeUpdate);
  }

  @Test
  @Transactional
  @WithMockUser(username = "alice", authorities = AuthoritiesConstants.USER)
  void putWithIdMismatchProductInCart() throws Exception {
    currentLoggedUser = checkIfUserExist();

    productInCart.setCart(currentLoggedUser.getCart());
    productInCart.setProductId(product.getId());
    productInCartRepository.save(productInCart);
    currentLoggedUser.getCart().addProductInCart(productInCart);
    cartRepository.save(currentLoggedUser.getCart());

    final int databaseProductInCartSizeBeforeUpdate = productInCartRepository.findAll().size();

    // Update the productInCart
    ProductInCartDTO productInCartDto = productInCartMapper.toDto(
        productInCartRepository.findById(productInCart.getId()).get());
    productInCartDto.setId(count.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restProductInCartMockMvc.perform(put(ENTITY_API_URL_ID, count.incrementAndGet())
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(productInCartDto)))
        .andExpect(status().isBadRequest());

    // Validate the ProductInCart in the database
    List<ProductInCart> productInCartList = productInCartRepository.findAll();
    assertThat(productInCartList).hasSize(databaseProductInCartSizeBeforeUpdate);
  }

  @Test
  @Transactional
  @WithMockUser(username = "alice", authorities = AuthoritiesConstants.USER)
  void putWithMissingIdPathParamProductInCart() throws Exception {
    currentLoggedUser = checkIfUserExist();

    productInCart.setCart(currentLoggedUser.getCart());
    productInCart.setProductId(product.getId());
    productInCartRepository.save(productInCart);
    currentLoggedUser.getCart().addProductInCart(productInCart);
    cartRepository.save(currentLoggedUser.getCart());

    final int databaseProductInCartSizeBeforeUpdate = productInCartRepository.findAll().size();

    // Update the productInCart
    ProductInCartDTO productInCartDto = productInCartMapper.toDto(
        productInCartRepository.findById(productInCart.getId()).get());
    productInCartDto.setId(count.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restProductInCartMockMvc.perform(put(ENTITY_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(productInCartDto)))
        .andExpect(status().isMethodNotAllowed());

    // Validate the ProductInCart in the database
    List<ProductInCart> productInCartList = productInCartRepository.findAll();
    assertThat(productInCartList).hasSize(databaseProductInCartSizeBeforeUpdate);
  }

  @Test
  @Transactional
  public void putNewProductInCartByAnyoneShouldThrowStatusUnauthorized401()
      throws Exception {
    productInCart.setProductId(product.getId());
    productInCartRepository.saveAndFlush(productInCart);

    final int databaseCartSizeBeforeUpdate = cartRepository.findAll().size();
    final int databaseProductInCartSizeBeforeUpdate = productInCartRepository.findAll().size();

    // Update the productInCart
    ProductInCartDTO productInCartDto = productInCartMapper.toDto(
        productInCartRepository.findById(productInCart.getId()).get());
    productInCartDto.setQuantity(UPDATED_QUANTITY);

    restProductInCartMockMvc.perform(put(ENTITY_API_URL_ID, productInCartDto.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(productInCartDto)))
        .andExpect(status().isUnauthorized());

    // Validate the ProductInCart in the database
    List<ProductInCart> productInCartList = productInCartRepository.findAll();
    assertThat(productInCartList).hasSize(databaseProductInCartSizeBeforeUpdate);
    ProductInCart testProductInCart = productInCartList.get(productInCartList.size() - 1);
    assertThat(testProductInCart.getCategory()).isEqualTo(DEFAULT_CATEGORY);
    assertThat(testProductInCart.getName()).isEqualTo(DEFAULT_NAME);
    assertThat(testProductInCart.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
    assertThat(testProductInCart.getPriceNet())
        .isEqualTo(DEFAULT_PRICE_NET.setScale(2, RoundingMode.CEILING));
    assertThat(testProductInCart.getVat()).isEqualTo(DEFAULT_VAT);
    assertThat(testProductInCart.getPriceGross())
        .isEqualTo(defaultPriceGross.setScale(4, RoundingMode.CEILING));
    assertThat(testProductInCart.getTotalPriceNet()).isEqualTo(DEFAULT_TOTAL_PRICE_NET.setScale(2));
    assertThat(testProductInCart.getTotalPriceGross())
        .isEqualTo(defaultTotalPriceGross.setScale(4, RoundingMode.CEILING));
    assertThat(testProductInCart.getStock()).isEqualTo(DEFAULT_STOCK);
    assertThat(testProductInCart.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    assertThat(testProductInCart.getImage()).isEqualTo(DEFAULT_IMAGE);
    assertThat(testProductInCart.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
    assertThat(testProductInCart.getProductId()).isEqualTo(product.getId());

    // Validate the Cart in the database
    List<Cart> cartList = cartRepository.findAll();
    assertThat(cartList).hasSize(databaseCartSizeBeforeUpdate);
    Cart testCart = cartList.get(cartList.size() - 1);
    assertThat(testCart.getAmountOfCartNet()).isEqualTo(DEFAULT_AMOUNT_OF_CART_NET);
    assertThat(testCart.getAmountOfCartGross()).isEqualTo(DEFAULT_AMOUNT_OF_CART_GROSS);
    assertThat(testCart.getAmountOfShipmentNet()).isEqualTo(DEFAULT_AMOUNT_OF_SHIPMENT_NET);
    assertThat(testCart.getAmountOfShipmentGross()).isEqualTo(DEFAULT_AMOUNT_OF_SHIPMENT_GROSS);
    assertThat(testCart.getAmountOfOrderNet()).isEqualTo(DEFAULT_AMOUNT_OF_ORDER_NET);
    assertThat(testCart.getAmountOfOrderGross()).isEqualTo(DEFAULT_AMOUNT_OF_ORDER_GROSS);

    List<Product> productList = productRepository.findAll();
    Product testProduct = productList.get(productList.size() - 1);
    assertThat(testProduct.getStock()).isEqualTo(DEFAULT_STOCK);
  }

  private User checkIfUserExist() {
    currentLoggedUser = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()
            .orElseThrow(() -> new UserNotFoundException()))
        .orElseThrow(() -> new UserNotFoundException());
    return currentLoggedUser;
  }
}
