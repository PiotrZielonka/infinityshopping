package infinityshopping.online.app.web.rest;

import static infinityshopping.online.app.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import infinityshopping.online.app.IntegrationTest;
import infinityshopping.online.app.config.Constants;
import infinityshopping.online.app.domain.Cart;
import infinityshopping.online.app.domain.PaymentCart;
import infinityshopping.online.app.domain.User;
import infinityshopping.online.app.repository.CartRepository;
import infinityshopping.online.app.repository.PaymentCartRepository;
import infinityshopping.online.app.repository.UserRepository;
import infinityshopping.online.app.security.AuthoritiesConstants;
import infinityshopping.online.app.service.dto.PaymentCartDTO;
import infinityshopping.online.app.service.mapper.PaymentCartMapper;
import java.math.BigDecimal;
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
class PaymentCartResourceIT {

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


  private static final String ENTITY_API_URL = "/api/payment-cart";
  private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

  private static Random random = new Random();
  private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

  @Autowired
  private PaymentCartRepository paymentCartRepository;

  @Autowired
  private CartRepository cartRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PaymentCartMapper paymentCartMapper;

  @Autowired
  private EntityManager em;

  @Autowired
  private MockMvc restPaymentCartMockMvc;

  private PaymentCart paymentCart;

  private PaymentCart paymentCart2;


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

  public static PaymentCart createUpdatedEntity(EntityManager em) {
    PaymentCart paymentCart = new PaymentCart()
        .name(UPDATED_NAME)
        .priceNet(UPDATED_PRICE_NET)
        .vat(UPDATED_VAT)
        .priceGross(UPDATED_PRICE_GROSS);
    return paymentCart;
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
  @WithMockUser(username = "alice", authorities = AuthoritiesConstants.USER)
  void getPaymentCartOfCurrentLoggedUser() throws Exception {
    // Get only the paymentCart of current user
    restPaymentCartMockMvc
        .perform(get(ENTITY_API_URL + "/userPaymentCart"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.id").value(paymentCart.getId()))
        .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
        .andExpect(jsonPath("$.priceNet").value((DEFAULT_PRICE_NET)))
        .andExpect(jsonPath("$.vat").value((DEFAULT_VAT)))
        .andExpect(jsonPath("$.priceGross").value((DEFAULT_PRICE_GROSS)));
  }

  @Test
  @Transactional
  @WithMockUser(username = "alice", authorities = AuthoritiesConstants.USER)
  void getPaymentCartById() throws Exception {
    // Get the paymentCart
    restPaymentCartMockMvc
        .perform(get(ENTITY_API_URL_ID, paymentCart.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.id").value(paymentCart.getId().intValue()))
        .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
        .andExpect(jsonPath("$.priceNet").value(sameNumber(DEFAULT_PRICE_NET)))
        .andExpect(jsonPath("$.vat").value(sameNumber(DEFAULT_VAT)))
        .andExpect(jsonPath("$.priceGross").value(sameNumber(DEFAULT_PRICE_GROSS)));
  }

  @Test
  @Transactional
  @WithMockUser(username = "alice", authorities = AuthoritiesConstants.USER)
  void getNonExistingPaymentCart() throws Exception {
    // Get the paymentCart
    restPaymentCartMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  @WithMockUser(username = "alice", authorities = AuthoritiesConstants.USER)
  void putNewPaymentCart() throws Exception {

    final int databaseSizeBeforeUpdate = paymentCartRepository.findAll().size();

    // Update the paymentCart
    PaymentCart updatedPaymentCart = paymentCartRepository.findById(paymentCart.getId()).get();
    // Disconnect from session so that the
    // updates on updatedPaymentCart are not directly saved in db
    em.detach(updatedPaymentCart);
    updatedPaymentCart
        .id(paymentCart.getId())
        .name(UPDATED_NAME)
        .priceNet(UPDATED_PRICE_NET)
        .vat(UPDATED_VAT)
        .priceGross(UPDATED_PRICE_GROSS);
    PaymentCartDTO paymentCartDto = paymentCartMapper.toDto(updatedPaymentCart);

    restPaymentCartMockMvc
        .perform(
            put(ENTITY_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(paymentCartDto))
        )
        .andExpect(status().isOk());

    // Validate the PaymentCart in the database
    List<PaymentCart> paymentCartList = paymentCartRepository.findAll();
    assertThat(paymentCartList).hasSize(databaseSizeBeforeUpdate);
    PaymentCart testPaymentCart = paymentCartList.get(paymentCartList.size() - 2);
    assertThat(testPaymentCart.getName()).isEqualTo(UPDATED_NAME);
    assertThat(testPaymentCart.getPriceNet()).isEqualTo(UPDATED_PRICE_NET);
    assertThat(testPaymentCart.getVat()).isEqualTo(UPDATED_VAT);
    assertThat(testPaymentCart.getPriceGross()).isEqualTo(UPDATED_PRICE_GROSS);

    // Validate the all amounts in the Cart in the database
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
  void putNonExistingPaymentCart() throws Exception {
    int databaseSizeBeforeUpdate = paymentCartRepository.findAll().size();
    paymentCart.setId(count.incrementAndGet());

    // Create the PaymentCart
    PaymentCartDTO paymentCartDto = paymentCartMapper.toDto(paymentCart);

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restPaymentCartMockMvc
        .perform(
            put(ENTITY_API_URL_ID, paymentCartDto.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(paymentCartDto))
        )
        .andExpect(status().isMethodNotAllowed());
  }

  @Test
  @Transactional
  @WithMockUser(username = "alice", authorities = AuthoritiesConstants.USER)
  void putWithIdMismatchPaymentCart() throws Exception {
    int databaseSizeBeforeUpdate = paymentCartRepository.findAll().size();
    paymentCart.setId(count.incrementAndGet());

    // Create the PaymentCart
    PaymentCartDTO paymentCartDto = paymentCartMapper.toDto(paymentCart);

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restPaymentCartMockMvc
        .perform(
            put(ENTITY_API_URL_ID, count.incrementAndGet())
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(paymentCartDto))
        )
        .andExpect(status().isMethodNotAllowed());
  }

  @Test
  @Transactional
  @WithMockUser(username = "alice", authorities = AuthoritiesConstants.USER)
  void putWithMissingIdPathParamPaymentCart() throws Exception {
    int databaseSizeBeforeUpdate = paymentCartRepository.findAll().size();
    paymentCart.setId(count.incrementAndGet());

    // Create the PaymentCart
    PaymentCartDTO paymentCartDto = paymentCartMapper.toDto(paymentCart);

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restPaymentCartMockMvc
        .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(paymentCartDto)))
        .andExpect(status().isInternalServerError());
  }
}
