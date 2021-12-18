package infinityshopping.online.app.service.impl;

import static org.assertj.core.api.Assertions.assertThat;

import infinityshopping.online.app.InfinityshoppingApp;
import infinityshopping.online.app.config.Constants;
import infinityshopping.online.app.domain.Cart;
import infinityshopping.online.app.domain.Payment;
import infinityshopping.online.app.domain.PaymentCart;
import infinityshopping.online.app.domain.ShipmentCart;
import infinityshopping.online.app.domain.User;
import infinityshopping.online.app.domain.enumeration.PaymentStatusEnum;
import infinityshopping.online.app.repository.CartRepository;
import infinityshopping.online.app.repository.PaymentCartRepository;
import infinityshopping.online.app.repository.PaymentRepository;
import infinityshopping.online.app.repository.ShipmentCartRepository;
import infinityshopping.online.app.repository.UserRepository;
import infinityshopping.online.app.security.AuthoritiesConstants;
import infinityshopping.online.app.security.SecurityUtils;
import infinityshopping.online.app.service.AddVat;
import infinityshopping.online.app.service.UserNotFoundException;
import infinityshopping.online.app.service.dto.PaymentCartDTO;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(classes = InfinityshoppingApp.class)
public class PaymentCartServiceImplTests implements AddVat {

  private static Random random = new Random();
  private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

  // PaymentCart
  private static final String DEFAULT_NAME = "AAAAAAAAAA";
  private static final BigDecimal DEFAULT_PRICE_NET = new BigDecimal(random.nextInt(10000));
  private static final BigDecimal DEFAULT_VAT = new BigDecimal(random.nextInt(30 - 5) + 5);
  private final BigDecimal defaultPriceGross = addVat(DEFAULT_PRICE_NET, DEFAULT_VAT);

  private static final String DEFAULT_NAME_2 = "CCCCCCCCCC";
  private static final BigDecimal DEFAULT_PRICE_NET_2 = new BigDecimal("200");
  private static final BigDecimal DEFAULT_VAT_2 = new BigDecimal("5");
  private static final BigDecimal DEFAULT_PRICE_GROSS_2 = new BigDecimal("210");

  private static final PaymentStatusEnum DEFAULT_PAYMENT_STATUS_ENUM
      = PaymentStatusEnum.WaitingForBankTransfer;

  // Cart
  private static final BigDecimal DEFAULT_AMOUNT_OF_CART_NET = new BigDecimal("100");
  private static final BigDecimal DEFAULT_AMOUNT_OF_CART_GROSS = new BigDecimal("123");

  private static final BigDecimal DEFAULT_AMOUNT_OF_SHIPMENT_NET = new BigDecimal("10");
  private static final BigDecimal DEFAULT_AMOUNT_OF_SHIPMENT_GROSS = new BigDecimal("10.8");

  private static final BigDecimal DEFAULT_AMOUNT_OF_ORDER_NET = new BigDecimal("110");
  private static final BigDecimal DEFAULT_AMOUNT_OF_ORDER_GROSS = new BigDecimal("133.8");

  // ShipmentCart
  private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
  private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
  private static final String DEFAULT_STREET = "AAAAAAAAAA";
  private static final String DEFAULT_POSTAL_CODE = "AAAAAAAAAA";
  private static final String DEFAULT_CITY = "AAAAAAAAAA";
  private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
  private static final String DEFAULT_PHONE_TO_THE_RECEIVER = "AAAAAAAAAA";
  private static final String DEFAULT_FIRM = "AAAAAAAAAA";
  private static final String DEFAULT_TAX_NUMBER = "AAAAAAAAAA";

  //Payment
  private static final String DEFAULT_Payment_NAME = "VVVV";
  private static BigDecimal DEFAULT_Payment_PRICE_NET = new BigDecimal(random.nextInt(10000));
  private static BigDecimal DEFAULT_Payment_VAT = new BigDecimal(random.nextInt(30 - 5) + 5);
  private final BigDecimal defaultPaymentPriceGross
      = addVat(DEFAULT_Payment_PRICE_NET, DEFAULT_Payment_VAT);

  private static final PaymentStatusEnum DEFAULT_PAYMENT_STATUS_ENUM_FROM_PAYMENT
      = PaymentStatusEnum.PreparationForShipping;

  private static final Instant DEFAULT_Payment_TIME = Instant.ofEpochMilli(0L);
  private static final Instant UPDATED_Payment_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

  private PaymentCart paymentCart;

  private PaymentCart paymentCart2;

  private ShipmentCart shipmentCart;

  private ShipmentCart shipmentCart2;

  private Payment payment;

  User currentLoggedUser = new User();


  @Autowired
  private PaymentCartServiceImpl paymentCartServiceImpl;

  @Autowired
  private PaymentCartRepository paymentCartRepository;

  @Autowired
  private ShipmentCartRepository shipmentCartRepository;

  @Autowired
  private PaymentRepository paymentRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private CartRepository cartRepository;

  @Autowired
  private EntityManager em;


  public PaymentCart createEntity(EntityManager em) {
    PaymentCart paymentCart = new PaymentCart()
        .name(DEFAULT_NAME)
        .priceNet(DEFAULT_PRICE_NET)
        .vat(DEFAULT_VAT)
        .priceGross(defaultPriceGross)
        .paymentStatus(DEFAULT_PAYMENT_STATUS_ENUM);
    return paymentCart;
  }

  public static PaymentCart createEntity2(EntityManager em) {
    PaymentCart paymentCart2 = new PaymentCart()
        .name(DEFAULT_NAME_2)
        .priceNet(DEFAULT_PRICE_NET_2)
        .vat(DEFAULT_VAT_2)
        .priceGross(DEFAULT_PRICE_GROSS_2)
        .paymentStatus(DEFAULT_PAYMENT_STATUS_ENUM);
    return paymentCart2;
  }

  public static ShipmentCart createEntityShipmentCart(EntityManager em) {
    ShipmentCart shipmentCart = new ShipmentCart()
        .firstName(DEFAULT_FIRST_NAME)
        .lastName(DEFAULT_LAST_NAME)
        .street(DEFAULT_STREET)
        .postalCode(DEFAULT_POSTAL_CODE)
        .city(DEFAULT_CITY)
        .country(DEFAULT_COUNTRY)
        .phoneToTheReceiver(DEFAULT_PHONE_TO_THE_RECEIVER)
        .firm(DEFAULT_FIRM)
        .taxNumber(DEFAULT_TAX_NUMBER);
    return shipmentCart;
  }

  public Payment createEntityPayment(EntityManager em) {
    Payment payment = new Payment()
        .name(DEFAULT_Payment_NAME)
        .priceNet(DEFAULT_Payment_PRICE_NET)
        .vat(DEFAULT_Payment_VAT)
        .priceGross(defaultPaymentPriceGross)
        .paymentStatus(DEFAULT_PAYMENT_STATUS_ENUM_FROM_PAYMENT)
        .createTime(DEFAULT_Payment_TIME)
        .updateTime(UPDATED_Payment_TIME);
    return payment;
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
    cart.setAmountOfCartGross(DEFAULT_AMOUNT_OF_CART_GROSS);
    cart.setAmountOfShipmentNet(DEFAULT_AMOUNT_OF_SHIPMENT_NET);
    cart.setAmountOfShipmentGross(DEFAULT_AMOUNT_OF_SHIPMENT_GROSS);
    cart.setAmountOfOrderNet(DEFAULT_AMOUNT_OF_ORDER_NET);
    cart.setAmountOfOrderGross(DEFAULT_AMOUNT_OF_ORDER_GROSS);
    cartRepository.save(cart);
    user.setCart(cart);
    userRepository.saveAndFlush(user);

    // given PaymentCart for User
    paymentCart = createEntity(em);
    paymentCart.setCart(cart);
    paymentCartRepository.save(paymentCart);
    cart.setPaymentCart(paymentCart);
    cartRepository.save(cart);

    // given ShipmentCart for User
    shipmentCart = createEntityShipmentCart(em);
    shipmentCart.setCart(cart);
    shipmentCartRepository.save(shipmentCart);
    cart.setShipmentCart(shipmentCart);
    cartRepository.save(cart);

    // given
    // given User2
    User user2 = new User();
    user2.setLogin("caroline");
    user2.setPassword(RandomStringUtils.random(60));
    user2.setActivated(true);
    user2.setEmail("caroline@example.com");
    user2.setFirstName("Caroline");
    user2.setLastName("Something");
    user2.setImageUrl("http://placehold.it/50x50");
    user2.setLangKey(Constants.DEFAULT_LANGUAGE);

    // given Cart2 for User2
    Cart cart2 = new Cart();
    cart2.setUser(user2);
    cart2.setAmountOfCartNet(BigDecimal.ZERO);
    cart2.setAmountOfCartGross(BigDecimal.ZERO);
    cart2.setAmountOfShipmentNet(BigDecimal.ZERO);
    cart2.setAmountOfShipmentGross(BigDecimal.ZERO);
    cart2.setAmountOfOrderNet(BigDecimal.ZERO);
    cart2.setAmountOfOrderGross(BigDecimal.ZERO);
    cartRepository.save(cart2);
    user2.setCart(cart2);
    userRepository.saveAndFlush(user2);

    // given PaymentCart2
    paymentCart2 = createEntity2(em);
    paymentCart2.setCart(cart2);
    paymentCartRepository.save(paymentCart2);
    cart2.setPaymentCart(paymentCart2);
    cartRepository.save(cart2);

    // given ShipmentCart2
    shipmentCart2 = createEntityShipmentCart(em);
    shipmentCart2.setCart(cart2);
    shipmentCartRepository.save(shipmentCart2);
    cart2.setShipmentCart(shipmentCart2);
    cartRepository.save(cart2);

    // given Payment
    payment = createEntityPayment(em);
    paymentRepository.save(payment);
  }

  @Test
  @Transactional
  public void shouldFindPaymentCartById() throws Exception {
    // when
    Optional<PaymentCartDTO> testPaymentCart
        = paymentCartServiceImpl.findOne(paymentCart.getId());

    // then
    assertThat(testPaymentCart.get().getId()).isEqualTo(paymentCart.getId());
    assertThat(testPaymentCart.get().getName()).isEqualTo(DEFAULT_NAME);
    assertThat(testPaymentCart.get().getPriceNet()).isEqualTo(DEFAULT_PRICE_NET);
    assertThat(testPaymentCart.get().getVat()).isEqualTo(DEFAULT_VAT);
    assertThat(testPaymentCart.get().getPriceGross()).isEqualTo(defaultPriceGross);
  }

  @Test
  @Transactional
  @WithMockUser(username = "alice", authorities = AuthoritiesConstants.USER)
  public void shouldUpdateExistPaymentCart() throws Exception {
    // given
    PaymentCartDTO paymentCartDto = new PaymentCartDTO();
    paymentCartDto.setId(paymentCart.getId());
    paymentCartDto.setName(DEFAULT_Payment_NAME);
    paymentCartDto.setPriceNet(null);
    paymentCartDto.setVat(null);
    paymentCartDto.setPriceGross(null);

    final int paymentCartDatabaseSizeBeforeSave = paymentCartRepository.findAll().size();
    final int cartDatabaseSizeBeforeSave = paymentCartRepository.findAll().size();

    // when
    paymentCartServiceImpl.save(paymentCartDto);

    // then
    assertThat(paymentCartDatabaseSizeBeforeSave).isEqualTo(paymentCartDatabaseSizeBeforeSave);
    assertThat(cartDatabaseSizeBeforeSave).isEqualTo(cartDatabaseSizeBeforeSave);
    List<PaymentCart> paymentCartList = paymentCartRepository.findAll();
    PaymentCart testPaymentCart = paymentCartList.get(paymentCartList.size() - 2);
    assertThat(testPaymentCart.getId()).isEqualTo(paymentCart.getId());
    assertThat(testPaymentCart.getName()).isEqualTo(DEFAULT_Payment_NAME);
    assertThat(testPaymentCart.getPriceNet()).isEqualTo(DEFAULT_Payment_PRICE_NET);
    assertThat(testPaymentCart.getVat()).isEqualTo(DEFAULT_Payment_VAT);
    assertThat(testPaymentCart.getPriceGross()).isEqualTo(defaultPaymentPriceGross);
    assertThat(testPaymentCart.getPaymentStatus())
        .isEqualTo(DEFAULT_PAYMENT_STATUS_ENUM_FROM_PAYMENT);
  }

  @Test
  @Transactional
  @WithMockUser(username = "alice", authorities = AuthoritiesConstants.USER)
  public void shouldSaveCartIdInPaymentCartAfterSavingPaymentCart() throws Exception {
    // given
    PaymentCartDTO paymentCartDto = new PaymentCartDTO();
    paymentCartDto.setId(paymentCart.getId());
    paymentCartDto.setName(DEFAULT_Payment_NAME);
    paymentCartDto.setPriceNet(null);
    paymentCartDto.setVat(null);
    paymentCartDto.setPriceGross(null);

    final int paymentCartDatabaseSizeBeforeSave = paymentCartRepository.findAll().size();
    final int cartDatabaseSizeBeforeSave = paymentCartRepository.findAll().size();

    // when
    paymentCartServiceImpl.save(paymentCartDto);

    // then
    assertThat(paymentCartDatabaseSizeBeforeSave).isEqualTo(paymentCartDatabaseSizeBeforeSave);
    assertThat(cartDatabaseSizeBeforeSave).isEqualTo(cartDatabaseSizeBeforeSave);
    List<Cart> cartList = cartRepository.findAll();
    Cart testCart = cartList.get(cartList.size() - 2);
    assertThat(testCart.getPaymentCart().getCart()).isEqualTo(paymentCart.getCart());
  }

  @Test
  @Transactional
  @WithMockUser(username = "alice", authorities = AuthoritiesConstants.USER)
  public void shouldSetAmountOfShipmentToProperCartAfterSavingPaymentCart()
      throws Exception {
    // given
    PaymentCartDTO paymentCartDto = new PaymentCartDTO();
    paymentCartDto.setId(paymentCart.getId());
    paymentCartDto.setName(DEFAULT_Payment_NAME);
    paymentCartDto.setPriceNet(null);
    paymentCartDto.setVat(null);
    paymentCartDto.setPriceGross(null);

    final int paymentCartDatabaseSizeBeforeSave = paymentCartRepository.findAll().size();
    final int cartDatabaseSizeBeforeSave = paymentCartRepository.findAll().size();

    // when
    paymentCartServiceImpl.save(paymentCartDto);

    // then
    assertThat(paymentCartDatabaseSizeBeforeSave).isEqualTo(paymentCartDatabaseSizeBeforeSave);
    assertThat(cartDatabaseSizeBeforeSave).isEqualTo(cartDatabaseSizeBeforeSave);
    List<Cart> cartList = cartRepository.findAll();
    Cart testCart = cartList.get(cartList.size() - 2);
    assertThat(testCart.getId()).isEqualTo(paymentCart.getCart().getId());
    assertThat(testCart.getAmountOfCartNet()).isEqualTo(DEFAULT_AMOUNT_OF_CART_NET);
    assertThat(testCart.getAmountOfCartGross()).isEqualTo(DEFAULT_AMOUNT_OF_CART_GROSS);
    assertThat(testCart.getAmountOfShipmentNet()).isEqualTo(DEFAULT_Payment_PRICE_NET);
    assertThat(testCart.getAmountOfShipmentGross()).isEqualTo(defaultPaymentPriceGross);
  }

  @Test
  @Transactional
  @WithMockUser(username = "alice", authorities = AuthoritiesConstants.USER)
  public void shouldSetAmountOfOrderToProperCartAfterSavingPaymentCart()
      throws Exception {
    // given
    PaymentCartDTO paymentCartDto = new PaymentCartDTO();
    paymentCartDto.setId(paymentCart.getId());
    paymentCartDto.setName(DEFAULT_Payment_NAME);
    paymentCartDto.setPriceNet(null);
    paymentCartDto.setVat(null);
    paymentCartDto.setPriceGross(null);

    final int paymentCartDatabaseSizeBeforeSave = paymentCartRepository.findAll().size();
    final int cartDatabaseSizeBeforeSave = paymentCartRepository.findAll().size();

    // when
    paymentCartServiceImpl.save(paymentCartDto);

    // then
    assertThat(paymentCartDatabaseSizeBeforeSave).isEqualTo(paymentCartDatabaseSizeBeforeSave);
    assertThat(cartDatabaseSizeBeforeSave).isEqualTo(cartDatabaseSizeBeforeSave);
    List<Cart> cartList = cartRepository.findAll();
    Cart testCart = cartList.get(cartList.size() - 2);
    assertThat(testCart.getId()).isEqualTo(paymentCart.getCart().getId());
    assertThat(testCart.getAmountOfCartNet()).isEqualTo(DEFAULT_AMOUNT_OF_CART_NET);
    assertThat(testCart.getAmountOfCartGross()).isEqualTo(DEFAULT_AMOUNT_OF_CART_GROSS);
    assertThat(testCart.getAmountOfShipmentNet()).isEqualTo(DEFAULT_Payment_PRICE_NET);
    assertThat(testCart.getAmountOfShipmentGross()).isEqualTo(defaultPaymentPriceGross);
    assertThat(testCart.getAmountOfOrderNet()).isEqualTo(
        DEFAULT_AMOUNT_OF_CART_NET.add(DEFAULT_Payment_PRICE_NET));
    assertThat(testCart.getAmountOfOrderGross()).isEqualTo(
        DEFAULT_AMOUNT_OF_CART_GROSS.add(defaultPaymentPriceGross));
    assertThat(testCart.getPaymentCart().getId()).isEqualTo(paymentCart.getId());
  }

  @Test
  @Transactional
  @WithMockUser(username = "alice", authorities = AuthoritiesConstants.USER)
  public void afterSavingPaymentCartTwiceAmountsShouldBeCountProperWithoutDuplicate()
      throws Exception {
    // given
    PaymentCartDTO paymentCartDto = new PaymentCartDTO();
    paymentCartDto.setId(paymentCart.getId());
    paymentCartDto.setName(DEFAULT_Payment_NAME);
    paymentCartDto.setPriceNet(null);
    paymentCartDto.setVat(null);
    paymentCartDto.setPriceGross(null);

    final int paymentCartDatabaseSizeBeforeSave = paymentCartRepository.findAll().size();
    final int cartDatabaseSizeBeforeSave = paymentCartRepository.findAll().size();

    // when
    paymentCartServiceImpl.save(paymentCartDto);
    paymentCartServiceImpl.save(paymentCartDto);

    // then
    assertThat(paymentCartDatabaseSizeBeforeSave).isEqualTo(paymentCartDatabaseSizeBeforeSave);
    assertThat(cartDatabaseSizeBeforeSave).isEqualTo(cartDatabaseSizeBeforeSave);
    List<Cart> cartList = cartRepository.findAll();
    Cart testCart = cartList.get(cartList.size() - 2);
    assertThat(testCart.getId()).isEqualTo(paymentCart.getCart().getId());
    assertThat(testCart.getAmountOfCartNet()).isEqualTo(DEFAULT_AMOUNT_OF_CART_NET);
    assertThat(testCart.getAmountOfCartGross()).isEqualTo(DEFAULT_AMOUNT_OF_CART_GROSS);
    assertThat(testCart.getAmountOfShipmentNet()).isEqualTo(DEFAULT_Payment_PRICE_NET);
    assertThat(testCart.getAmountOfShipmentGross()).isEqualTo(defaultPaymentPriceGross);
    assertThat(testCart.getAmountOfOrderNet()).isEqualTo(
        DEFAULT_AMOUNT_OF_CART_NET.add(DEFAULT_Payment_PRICE_NET));
    assertThat(testCart.getAmountOfOrderGross()).isEqualTo(
        DEFAULT_AMOUNT_OF_CART_GROSS.add(defaultPaymentPriceGross));
    assertThat(testCart.getPaymentCart().getId()).isEqualTo(paymentCart.getId());
  }

  @Test
  @Transactional
  @WithMockUser(username = "alice", authorities = AuthoritiesConstants.USER)
  public void everyUserShouldHaveOnlyOnePaymentCart() throws Exception {
    // given
    final int paymentCartDatabaseSizeBeforeSave = paymentCartRepository.findAll().size();
    final int cartDatabaseSizeBeforeSave = paymentCartRepository.findAll().size();
    currentLoggedUser = checkIfUserExist();

    // when
    paymentCartRepository.save(currentLoggedUser.getCart().getPaymentCart());

    // then
    assertThat(paymentCartDatabaseSizeBeforeSave).isEqualTo(paymentCartDatabaseSizeBeforeSave);
    assertThat(cartDatabaseSizeBeforeSave).isEqualTo(cartDatabaseSizeBeforeSave);
    List<PaymentCart> cartList = paymentCartRepository.findAll();
  }

  @Test
  @Transactional
  @WithMockUser(username = "alice", authorities = AuthoritiesConstants.USER)
  public void loggedUserShouldGetOnlyOneOwnPaymentCart() throws Exception {
    // given
    currentLoggedUser = checkIfUserExist();

    // when
    Optional<PaymentCartDTO> testPaymentCart = paymentCartServiceImpl.findByCartId();

    // then
    assertThat(testPaymentCart.get().getId())
        .isEqualTo(currentLoggedUser.getCart().getPaymentCart().getId());
    assertThat(testPaymentCart.get().getName()).isEqualTo(paymentCart.getName());
    assertThat(testPaymentCart.get().getPriceNet()).isEqualTo(paymentCart.getPriceNet());
    assertThat(testPaymentCart.get().getVat()).isEqualTo(paymentCart.getVat());
    assertThat(testPaymentCart.get().getPriceGross()).isEqualTo(paymentCart.getPriceGross());
  }

  private User checkIfUserExist() {
    currentLoggedUser = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()
            .orElseThrow(() -> new UserNotFoundException()))
        .orElseThrow(() -> new UserNotFoundException());
    return currentLoggedUser;
  }
}