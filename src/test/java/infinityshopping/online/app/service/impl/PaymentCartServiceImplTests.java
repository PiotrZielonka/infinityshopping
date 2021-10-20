package infinityshopping.online.app.service.impl;

import static org.assertj.core.api.Assertions.assertThat;

import infinityshopping.online.app.InfinityshoppingApp;
import infinityshopping.online.app.config.Constants;
import infinityshopping.online.app.domain.Cart;
import infinityshopping.online.app.domain.PaymentCart;
import infinityshopping.online.app.domain.User;
import infinityshopping.online.app.repository.CartRepository;
import infinityshopping.online.app.repository.PaymentCartRepository;
import infinityshopping.online.app.repository.UserRepository;
import infinityshopping.online.app.security.AuthoritiesConstants;
import infinityshopping.online.app.service.dto.PaymentCartDTO;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest(classes = InfinityshoppingApp.class)
public class PaymentCartServiceImplTests {

  // PaymentCart
  private static final String DEFAULT_NAME = "AAAAAAAAAA";
  private static final String UPDATED_NAME = "BBBBBBBBBB";

  private static final BigDecimal DEFAULT_PRICE_NET = new BigDecimal("100");
  private static final BigDecimal UPDATED_PRICE_NET = new BigDecimal("155");

  private static final BigDecimal DEFAULT_VAT = new BigDecimal("23");
  private static final BigDecimal UPDATED_VAT = new BigDecimal("8");

  private static final BigDecimal DEFAULT_PRICE_GROSS = new BigDecimal("123");
  private static final BigDecimal UPDATED_PRICE_GROSS = new BigDecimal("167.44");

  private static final String DEFAULT_NAME_2 = "CCCCCCCCCC";
  private static final BigDecimal DEFAULT_PRICE_NET_2 = new BigDecimal("200");
  private static final BigDecimal DEFAULT_VAT_2 = new BigDecimal("5");
  private static final BigDecimal DEFAULT_PRICE_GROSS_2 = new BigDecimal("210");

  // Cart
  private static final BigDecimal DEFAULT_AMOUNT_OF_CART_NET = new BigDecimal("100");
  private static final BigDecimal DEFAULT_AMOUNT_OF_CART_GROSS = new BigDecimal("123");

  private static final BigDecimal DEFAULT_AMOUNT_OF_SHIPMENT_NET = new BigDecimal("10");
  private static final BigDecimal DEFAULT_AMOUNT_OF_SHIPMENT_GROSS = new BigDecimal("10.8");

  private static final BigDecimal DEFAULT_AMOUNT_OF_ORDER_NET = new BigDecimal("110");
  private static final BigDecimal DEFAULT_AMOUNT_OF_ORDER_GROSS = new BigDecimal("133.8");

  private PaymentCart paymentCart;

  private PaymentCart paymentCart2;

  User currentLoggedUser = new User();


  @Autowired
  private PaymentCartServiceImpl paymentCartServiceImpl;

  @Autowired
  private PaymentCartRepository paymentCartRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private CartRepository cartRepository;

  @Autowired
  private EntityManager em;

  public static PaymentCart createEntity(EntityManager em) {
    PaymentCart paymentCart = new PaymentCart()
        .name(DEFAULT_NAME)
        .priceNet(DEFAULT_PRICE_NET)
        .vat(DEFAULT_VAT)
        .priceGross(DEFAULT_PRICE_GROSS);
    return paymentCart;
  }

  public static PaymentCart createEntity2(EntityManager em) {
    PaymentCart paymentCart2 = new PaymentCart()
        .name(DEFAULT_NAME_2)
        .priceNet(DEFAULT_PRICE_NET_2)
        .vat(DEFAULT_VAT_2)
        .priceGross(DEFAULT_PRICE_GROSS_2);
    return paymentCart2;
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
  }

  @Test
  @Transactional
  public void shouldFindPaymentCartById() throws Exception {
    // given
    // @BeforeEach

    // when
    Optional<PaymentCartDTO> testPaymentCart
        = paymentCartServiceImpl.findOne(paymentCart.getId());

    // then
    assertThat(testPaymentCart.get().getId()).isEqualTo(paymentCart.getId());
    assertThat(testPaymentCart.get().getName()).isEqualTo(DEFAULT_NAME);
    assertThat(testPaymentCart.get().getPriceNet()).isEqualTo(DEFAULT_PRICE_NET);
    assertThat(testPaymentCart.get().getVat()).isEqualTo(DEFAULT_VAT);
    assertThat(testPaymentCart.get().getPriceGross()).isEqualTo(DEFAULT_PRICE_GROSS);
  }

  @Test
  @Transactional
  @WithMockUser(username = "alice", authorities = AuthoritiesConstants.USER)
  public void shouldUpdateExistPaymentCart() throws Exception {
    // given ShipmentCartDto
    PaymentCartDTO paymentCartDto = new PaymentCartDTO();
    paymentCartDto.setId(paymentCart.getId());
    paymentCartDto.setName(UPDATED_NAME);
    paymentCartDto.setPriceNet(UPDATED_PRICE_NET);
    paymentCartDto.setVat(UPDATED_VAT);
    paymentCartDto.setPriceGross(UPDATED_PRICE_GROSS);

    final int databaseSizeBeforeSave = paymentCartRepository.findAll().size();

    // when
    paymentCartServiceImpl.save(paymentCartDto);

    // then
    List<PaymentCart> paymentCartList = paymentCartRepository.findAll();
    PaymentCart testPaymentCart = paymentCartList.get(paymentCartList.size() - 2);
    assertThat(databaseSizeBeforeSave).isEqualTo(databaseSizeBeforeSave);
    assertThat(testPaymentCart.getId()).isEqualTo(paymentCart.getId());
    assertThat(testPaymentCart.getName()).isEqualTo(UPDATED_NAME);
    assertThat(testPaymentCart.getPriceNet()).isEqualTo(UPDATED_PRICE_NET);
    assertThat(testPaymentCart.getVat()).isEqualTo(UPDATED_VAT);
    assertThat(testPaymentCart.getPriceGross()).isEqualTo(UPDATED_PRICE_GROSS);
  }

  @Test
  @Transactional
  @WithMockUser(username = "alice", authorities = AuthoritiesConstants.USER)
  public void shouldSaveCartIdInPaymentCartAfterSavingPaymentCart() throws Exception {
    // given ShipmentCartDto
    PaymentCartDTO paymentCartDto = new PaymentCartDTO();
    paymentCartDto.setId(paymentCart.getId());
    paymentCartDto.setName(UPDATED_NAME);
    paymentCartDto.setPriceNet(UPDATED_PRICE_NET);
    paymentCartDto.setVat(UPDATED_VAT);
    paymentCartDto.setPriceGross(UPDATED_PRICE_GROSS);

    final int databaseSizeBeforeSave = paymentCartRepository.findAll().size();

    // when
    paymentCartServiceImpl.save(paymentCartDto);

    // Validate if the CartId exist after saving PaymentCart
    List<Cart> cartList = cartRepository.findAll();
    Cart testCart = cartList.get(cartList.size() - 2);
    assertThat(testCart.getPaymentCart().getCart()).isEqualTo(paymentCart.getCart());
  }

  @Test
  @Transactional
  @WithMockUser(username = "alice", authorities = AuthoritiesConstants.USER)
  public void shouldSetAmountOfShipmentToToProperCartAfterSavingPaymentCart()
      throws Exception {
    // given PaymentCartDto
    PaymentCartDTO paymentCartDto = new PaymentCartDTO();
    paymentCartDto.setId(paymentCart.getId());
    paymentCartDto.setName(UPDATED_NAME);
    paymentCartDto.setPriceNet(UPDATED_PRICE_NET);
    paymentCartDto.setVat(UPDATED_VAT);
    paymentCartDto.setPriceGross(UPDATED_PRICE_GROSS);

    final int databaseSizeBeforeSave = paymentCartRepository.findAll().size();

    // when
    paymentCartServiceImpl.save(paymentCartDto);

    // then
    List<Cart> cartList = cartRepository.findAll();
    Cart testCart = cartList.get(cartList.size() - 2);
    assertThat(testCart.getId()).isEqualTo(paymentCart.getCart().getId());
    assertThat(testCart.getAmountOfCartNet()).isEqualTo(DEFAULT_AMOUNT_OF_CART_NET);
    assertThat(testCart.getAmountOfCartGross()).isEqualTo(DEFAULT_AMOUNT_OF_CART_GROSS);
    assertThat(testCart.getAmountOfShipmentNet()).isEqualTo(UPDATED_PRICE_NET);
    assertThat(testCart.getAmountOfShipmentGross()).isEqualTo(UPDATED_PRICE_GROSS);
  }

  @Test
  @Transactional
  @WithMockUser(username = "alice", authorities = AuthoritiesConstants.USER)
  public void shouldSetAmountOfOrderToToProperCartAfterSavingPaymentCart()
      throws Exception {
    // given PaymentCartDto
    PaymentCartDTO paymentCartDto = new PaymentCartDTO();
    paymentCartDto.setId(paymentCart.getId());
    paymentCartDto.setName(UPDATED_NAME);
    paymentCartDto.setPriceNet(UPDATED_PRICE_NET);
    paymentCartDto.setVat(UPDATED_VAT);
    paymentCartDto.setPriceGross(UPDATED_PRICE_GROSS);

    final int databaseSizeBeforeSave = paymentCartRepository.findAll().size();

    // when
    paymentCartServiceImpl.save(paymentCartDto);

    // then
    List<Cart> cartList = cartRepository.findAll();
    Cart testCart = cartList.get(cartList.size() - 2);
    assertThat(testCart.getId()).isEqualTo(paymentCart.getCart().getId());
    assertThat(testCart.getAmountOfCartNet()).isEqualTo(DEFAULT_AMOUNT_OF_CART_NET);
    assertThat(testCart.getAmountOfCartGross()).isEqualTo(DEFAULT_AMOUNT_OF_CART_GROSS);
    assertThat(testCart.getAmountOfShipmentNet()).isEqualTo(UPDATED_PRICE_NET);
    assertThat(testCart.getAmountOfShipmentGross()).isEqualTo(UPDATED_PRICE_GROSS);
    assertThat(testCart.getAmountOfOrderNet()).isEqualTo(
        DEFAULT_AMOUNT_OF_CART_NET.add(UPDATED_PRICE_NET));
    assertThat(testCart.getAmountOfOrderGross()).isEqualTo(
        DEFAULT_AMOUNT_OF_CART_GROSS.add(UPDATED_PRICE_GROSS));
    assertThat(testCart.getPaymentCart().getId()).isEqualTo(paymentCart.getId());
  }

  @Test
  @Transactional
  @WithMockUser(username = "alice", authorities = AuthoritiesConstants.USER)
  public void afterSavingPaymentCartTwiceAmountsShouldBeCountProperWithoutDuplicate()
      throws Exception {
    // given PaymentCartDto
    PaymentCartDTO paymentCartDto = new PaymentCartDTO();
    paymentCartDto.setId(paymentCart.getId());
    paymentCartDto.setName(UPDATED_NAME);
    paymentCartDto.setPriceNet(UPDATED_PRICE_NET);
    paymentCartDto.setVat(UPDATED_VAT);
    paymentCartDto.setPriceGross(UPDATED_PRICE_GROSS);

    final int databaseSizeBeforeSave = paymentCartRepository.findAll().size();

    // when
    paymentCartServiceImpl.save(paymentCartDto);
    paymentCartServiceImpl.save(paymentCartDto);

    // then
    List<Cart> cartList = cartRepository.findAll();
    Cart testCart = cartList.get(cartList.size() - 2);
    assertThat(testCart.getId()).isEqualTo(paymentCart.getCart().getId());
    assertThat(testCart.getAmountOfCartNet()).isEqualTo(DEFAULT_AMOUNT_OF_CART_NET);
    assertThat(testCart.getAmountOfCartGross()).isEqualTo(DEFAULT_AMOUNT_OF_CART_GROSS);
    assertThat(testCart.getAmountOfShipmentNet()).isEqualTo(UPDATED_PRICE_NET);
    assertThat(testCart.getAmountOfShipmentGross()).isEqualTo(UPDATED_PRICE_GROSS);
    assertThat(testCart.getAmountOfOrderNet()).isEqualTo(
        DEFAULT_AMOUNT_OF_CART_NET.add(UPDATED_PRICE_NET));
    assertThat(testCart.getAmountOfOrderGross()).isEqualTo(
        DEFAULT_AMOUNT_OF_CART_GROSS.add(UPDATED_PRICE_GROSS));
    assertThat(testCart.getPaymentCart().getId()).isEqualTo(paymentCart.getId());
  }

  @Test
  @Transactional
  @WithMockUser(username = "alice", authorities = AuthoritiesConstants.USER)
  public void everyUserShouldHaveOnlyOnePaymentCart() throws Exception {
    // given
    // @BeforeEach

    // given logged user
    final int databaseSizeBeforeCreate = paymentCartRepository.findAll().size();
    currentLoggedUser = userRepository.findOneByLogin(getCurrentUserLogin()).get();

    // when
    paymentCartRepository.save(currentLoggedUser.getCart().getPaymentCart());

    // then
    List<PaymentCart> paymentCartList = paymentCartRepository.findAll();
    assertThat(paymentCartList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  @WithMockUser(username = "alice", authorities = AuthoritiesConstants.USER)
  public void loggedUserShouldGetOnlyOneOwnPaymentCart() throws Exception {
    // given
    // @BeforeEach
    currentLoggedUser = userRepository.findOneByLogin(getCurrentUserLogin()).get();

    // when
    Optional<PaymentCartDTO> dbPaymentCart = paymentCartServiceImpl.findByCartId();

    // then
    assertThat(dbPaymentCart.get().getId())
        .isEqualTo(currentLoggedUser.getCart().getPaymentCart().getId());
    assertThat(dbPaymentCart.get().getName()).isEqualTo(paymentCart.getName());
    assertThat(dbPaymentCart.get().getPriceNet()).isEqualTo(paymentCart.getPriceNet());
    assertThat(dbPaymentCart.get().getVat()).isEqualTo(paymentCart.getVat());
    assertThat(dbPaymentCart.get().getPriceGross()).isEqualTo(paymentCart.getPriceGross());
  }

  public String getCurrentUserLogin() {
    org.springframework.security.core.context.SecurityContext securityContext
        = SecurityContextHolder.getContext();
    Authentication authentication = securityContext.getAuthentication();
    String login = null;
    if (authentication != null) {
      if (authentication.getPrincipal() instanceof UserDetails) {
        login = ((UserDetails) authentication.getPrincipal()).getUsername();
      } else if (authentication.getPrincipal() instanceof String) {
        login = (String) authentication.getPrincipal();
      }
    }

    return login;

  }
}