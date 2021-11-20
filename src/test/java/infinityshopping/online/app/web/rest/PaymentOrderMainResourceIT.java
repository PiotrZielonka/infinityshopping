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
import infinityshopping.online.app.domain.OrderMain;
import infinityshopping.online.app.domain.Payment;
import infinityshopping.online.app.domain.PaymentOrderMain;
import infinityshopping.online.app.domain.User;
import infinityshopping.online.app.domain.enumeration.OrderMainStatusEnum;
import infinityshopping.online.app.domain.enumeration.PaymentStatusEnum;
import infinityshopping.online.app.repository.OrderMainRepository;
import infinityshopping.online.app.repository.PaymentOrderMainRepository;
import infinityshopping.online.app.repository.PaymentRepository;
import infinityshopping.online.app.security.AuthoritiesConstants;
import infinityshopping.online.app.service.AddVat;
import infinityshopping.online.app.service.dto.PaymentOrderMainDTO;
import infinityshopping.online.app.service.mapper.PaymentOrderMainMapper;
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
class PaymentOrderMainResourceIT implements AddVat {

  private static Random random = new Random();
  private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

  // PaymentOrderMain
  private static final String DEFAULT_NAME = "AAAAAAAAAA";
  private static final BigDecimal DEFAULT_PRICE_NET = new BigDecimal(random.nextInt(10000));
  private static final BigDecimal DEFAULT_VAT = new BigDecimal(random.nextInt(30 - 5) + 5);
  private final BigDecimal defaultPriceGross = addVat(DEFAULT_PRICE_NET, DEFAULT_VAT);

  // OrderMain
  private static final String DEFAULT_BUYER_LOGIN = "AAAAAAAAAA";
  private static final String DEFAULT_BUYER_FIRST_NAME = "AAAAAAAAAA";
  private static final String DEFAULT_BUYER_LAST_NAME = "AAAAAAAAAA";
  private static final String DEFAULT_BUYER_EMAIL = "AAAAAAAAAA";
  private static final String DEFAULT_BUYER_PHONE = "AAAAAAAAAA";
  private static final BigDecimal DEFAULT_AMOUNT_OF_CART_NET
      = new BigDecimal(random.nextInt(1000000));
  private final BigDecimal defaultAmountOfCartGross
      = addVat(DEFAULT_AMOUNT_OF_CART_NET, DEFAULT_VAT);

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

  //Payment
  private static final String DEFAULT_Payment_NAME = "VVVVVVVVVV";
  private static BigDecimal DEFAULT_Payment_PRICE_NET = new BigDecimal(random.nextInt(10000));
  private static BigDecimal DEFAULT_Payment_VAT = new BigDecimal(random.nextInt(30 - 5) + 5);
  private final BigDecimal defaultPaymentPriceGross
      = addVat(DEFAULT_Payment_PRICE_NET, DEFAULT_Payment_VAT);

  private static final PaymentStatusEnum DEFAULT_PAYMENT_STATUS_ENUM_FROM_PAYMENT
      = PaymentStatusEnum.PreparationForShipping;

  private static final Instant DEFAULT_Payment_TIME = Instant.ofEpochMilli(0L);
  private static final Instant UPDATED_Payment_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

  private static final String ENTITY_API_URL = "/api/payment-order-main";
  private static final String ENTITY_API_URL_ORDER_DETAILS
      = ENTITY_API_URL + "/orderDetails" + "/{id}";
  private static final String ENTITY_API_URL_EDIT = ENTITY_API_URL + "/edit" + "/{id}";
  private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";


  @Autowired
  private PaymentOrderMainRepository paymentOrderMainRepository;

  @Autowired
  private OrderMainRepository orderMainRepository;

  @Autowired
  private PaymentRepository paymentRepository;

  @Autowired
  private PaymentOrderMainMapper paymentOrderMainMapper;

  @Autowired
  private EntityManager em;

  @Autowired
  private MockMvc restPaymentOrderMainMockMvc;

  private PaymentOrderMain paymentOrderMain;

  private OrderMain orderMain;

  private Payment payment;



  public PaymentOrderMain createEntity(EntityManager em) {
    PaymentOrderMain paymentOrderMain = new PaymentOrderMain()
        .name(DEFAULT_NAME)
        .priceNet(DEFAULT_PRICE_NET)
        .vat(DEFAULT_VAT)
        .priceGross(defaultPriceGross);
    return paymentOrderMain;
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

    // given OrderMain
    orderMain = createEntityOrderMain(em);
    orderMainRepository.save(orderMain);

    // given PaymentOrderMain
    paymentOrderMain = createEntity(em);
    paymentOrderMain.setOrderMain(orderMain);
    paymentOrderMainRepository.save(paymentOrderMain);

    // set relationship for OrderMain
    orderMain.setPaymentOrderMain(paymentOrderMain);
    orderMainRepository.save(orderMain);

    // given Payment
    payment = createEntityPayment(em);
    paymentRepository.save(payment);
  }

  @Test
  @Transactional
  @WithMockUser(username = "alice", authorities = AuthoritiesConstants.USER)
  void getPaymentOrderMainByIdOrderMain() throws Exception {
    // Get the paymentOrderMain
    restPaymentOrderMainMockMvc.perform(get(ENTITY_API_URL_ORDER_DETAILS, orderMain.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.id").value(paymentOrderMain.getId().intValue()))
        .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
        .andExpect(jsonPath("$.priceNet").value(sameNumber(DEFAULT_PRICE_NET)))
        .andExpect(jsonPath("$.vat").value(sameNumber(DEFAULT_VAT)))
        .andExpect(jsonPath("$.priceGross").value(sameNumber(defaultPriceGross)));
  }

  @Test
  @Transactional
  void getPaymentOrderMainByIdOrderMainByAnyoneShouldThrowStatusUnauthorized() throws Exception {
    // Get the paymentOrderMain
    restPaymentOrderMainMockMvc.perform(get(ENTITY_API_URL_ORDER_DETAILS, orderMain.getId()))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @Transactional
  @WithMockUser(username = "alice", authorities = AuthoritiesConstants.USER)
  void getNonExistingPaymentOrderMainByIdOrderMain() throws Exception {
    // Get the paymentOrderMain
    restPaymentOrderMainMockMvc.perform(get(ENTITY_API_URL_ORDER_DETAILS, Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  @WithMockUser(username = "alice", authorities = AuthoritiesConstants.USER)
  void getPaymentOrderMain() throws Exception {
    // Get the paymentOrderMain
    restPaymentOrderMainMockMvc.perform(get(ENTITY_API_URL_ID, paymentOrderMain.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.id").value(paymentOrderMain.getId().intValue()))
        .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
        .andExpect(jsonPath("$.priceNet").value(sameNumber(DEFAULT_PRICE_NET)))
        .andExpect(jsonPath("$.vat").value(sameNumber(DEFAULT_VAT)))
        .andExpect(jsonPath("$.priceGross").value(sameNumber(defaultPriceGross)));
  }

  @Test
  @Transactional
  void getPaymentOrderMainByAnyoneShouldThrowStatusUnauthorized() throws Exception {
    // Get the paymentOrderMain
    restPaymentOrderMainMockMvc.perform(get(ENTITY_API_URL_ID, paymentOrderMain.getId()))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @Transactional
  @WithMockUser(username = "alice", authorities = AuthoritiesConstants.USER)
  void getNonExistingPaymentOrderMain() throws Exception {
    // Get the paymentOrderMain
    restPaymentOrderMainMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  @WithMockUser(username = "admin", password = "admin", authorities = AuthoritiesConstants.ADMIN)
  void putNewPaymentOrderMain() throws Exception {
    final int databaseSizeBeforeUpdate = paymentOrderMainRepository.findAll().size();
    PaymentOrderMain updatedPaymentOrderMain
        = paymentOrderMainRepository.findById(paymentOrderMain.getId()).get();
    PaymentOrderMainDTO paymentOrderMainDto = paymentOrderMainMapper.toDto(updatedPaymentOrderMain);
    paymentOrderMainDto.setName(DEFAULT_Payment_NAME);
    paymentOrderMainDto.setPriceNet(null);
    paymentOrderMainDto.setVat(null);
    paymentOrderMainDto.setPriceGross(null);

    restPaymentOrderMainMockMvc.perform(put(ENTITY_API_URL_EDIT, paymentOrderMainDto.getId())
                 .contentType(MediaType.APPLICATION_JSON)
                 .content(TestUtil.convertObjectToJsonBytes(paymentOrderMainDto)))
        .andExpect(status().isOk());

    // Validate the PaymentOrderMain in the database
    List<PaymentOrderMain> paymentOrderMainList = paymentOrderMainRepository.findAll();
    assertThat(paymentOrderMainList).hasSize(databaseSizeBeforeUpdate);
    PaymentOrderMain testPaymentOrderMain
        = paymentOrderMainList.get(paymentOrderMainList.size() - 1);
    assertThat(testPaymentOrderMain.getId()).isEqualTo(paymentOrderMain.getId());
    assertThat(testPaymentOrderMain.getName()).isEqualTo(DEFAULT_Payment_NAME);
    assertThat(testPaymentOrderMain.getPriceNet()).isEqualTo(DEFAULT_Payment_PRICE_NET);
    assertThat(testPaymentOrderMain.getVat()).isEqualTo(DEFAULT_Payment_VAT);
    assertThat(testPaymentOrderMain.getPriceGross()).isEqualTo(defaultPaymentPriceGross);
    assertThat(testPaymentOrderMain.getOrderMain().getId())
        .isEqualTo(paymentOrderMain.getOrderMain().getId());
  }

  @Test
  @Transactional
  @WithMockUser(username = "alice", authorities = AuthoritiesConstants.USER)
  void putNewPaymentOrderMainByUserShouldThrowStatusForbidden() throws Exception {
    PaymentOrderMain updatedPaymentOrderMain
        = paymentOrderMainRepository.findById(paymentOrderMain.getId()).get();
    PaymentOrderMainDTO paymentOrderMainDto = paymentOrderMainMapper.toDto(updatedPaymentOrderMain);
    paymentOrderMainDto.setName(DEFAULT_Payment_NAME);
    paymentOrderMainDto.setPriceNet(null);
    paymentOrderMainDto.setVat(null);
    paymentOrderMainDto.setPriceGross(null);

    restPaymentOrderMainMockMvc.perform(put(ENTITY_API_URL_EDIT, paymentOrderMainDto.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(paymentOrderMainDto)))
        .andExpect(status().isForbidden());
  }

  @Test
  @Transactional
  void putNewPaymentOrderMainByAnyoneShouldThrowStatusUnauthorized() throws Exception {
    PaymentOrderMain updatedPaymentOrderMain
        = paymentOrderMainRepository.findById(paymentOrderMain.getId()).get();
    PaymentOrderMainDTO paymentOrderMainDto = paymentOrderMainMapper.toDto(updatedPaymentOrderMain);
    paymentOrderMainDto.setName(DEFAULT_Payment_NAME);
    paymentOrderMainDto.setPriceNet(null);
    paymentOrderMainDto.setVat(null);
    paymentOrderMainDto.setPriceGross(null);

    restPaymentOrderMainMockMvc.perform(put(ENTITY_API_URL_EDIT, paymentOrderMainDto.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(paymentOrderMainDto)))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @Transactional
  @WithMockUser(username = "admin", password = "admin", authorities = AuthoritiesConstants.ADMIN)
  void putNonExistingPaymentOrderMain() throws Exception {
    int databaseSizeBeforeUpdate = paymentOrderMainRepository.findAll().size();
    PaymentOrderMain updatedPaymentOrderMain
        = paymentOrderMainRepository.findById(paymentOrderMain.getId()).get();
    PaymentOrderMainDTO paymentOrderMainDto = paymentOrderMainMapper.toDto(updatedPaymentOrderMain);
    paymentOrderMainDto.setId(count.incrementAndGet());
    paymentOrderMainDto.setName(DEFAULT_Payment_NAME);
    paymentOrderMainDto.setPriceNet(null);
    paymentOrderMainDto.setVat(null);
    paymentOrderMainDto.setPriceGross(null);

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restPaymentOrderMainMockMvc.perform(put(ENTITY_API_URL_EDIT, paymentOrderMainDto.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(paymentOrderMainDto)))
        .andExpect(status().isBadRequest());

    // Validate the PaymentOrderMain in the database
    List<PaymentOrderMain> paymentOrderMainList = paymentOrderMainRepository.findAll();
    assertThat(paymentOrderMainList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  @WithMockUser(username = "admin", password = "admin", authorities = AuthoritiesConstants.ADMIN)
  void putNonExistingPaymentName() throws Exception {
    int databaseSizeBeforeUpdate = paymentOrderMainRepository.findAll().size();
    PaymentOrderMain updatedPaymentOrderMain
        = paymentOrderMainRepository.findById(paymentOrderMain.getId()).get();
    PaymentOrderMainDTO paymentOrderMainDto = paymentOrderMainMapper.toDto(updatedPaymentOrderMain);
    paymentOrderMainDto.setName(DEFAULT_NAME);
    paymentOrderMainDto.setPriceNet(null);
    paymentOrderMainDto.setVat(null);
    paymentOrderMainDto.setPriceGross(null);

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restPaymentOrderMainMockMvc.perform(put(ENTITY_API_URL_EDIT, paymentOrderMainDto.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(paymentOrderMainDto)))
        .andExpect(status().isInternalServerError());

    // Validate the PaymentOrderMain in the database
    List<PaymentOrderMain> paymentOrderMainList = paymentOrderMainRepository.findAll();
    assertThat(paymentOrderMainList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  @WithMockUser(username = "admin", password = "admin", authorities = AuthoritiesConstants.ADMIN)
  void putWithIdMismatchPaymentOrderMain() throws Exception {
    int databaseSizeBeforeUpdate = paymentOrderMainRepository.findAll().size();
    PaymentOrderMain updatedPaymentOrderMain
        = paymentOrderMainRepository.findById(paymentOrderMain.getId()).get();
    PaymentOrderMainDTO paymentOrderMainDto = paymentOrderMainMapper.toDto(updatedPaymentOrderMain);
    paymentOrderMainDto.setId(count.incrementAndGet());
    paymentOrderMainDto.setName(DEFAULT_Payment_NAME);
    paymentOrderMainDto.setPriceNet(null);
    paymentOrderMainDto.setVat(null);
    paymentOrderMainDto.setPriceGross(null);

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restPaymentOrderMainMockMvc.perform(put(ENTITY_API_URL_EDIT, count.incrementAndGet())
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(paymentOrderMainDto)))
        .andExpect(status().isBadRequest());

    // Validate the PaymentOrderMain in the database
    List<PaymentOrderMain> paymentOrderMainList = paymentOrderMainRepository.findAll();
    assertThat(paymentOrderMainList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  @WithMockUser(username = "admin", password = "admin", authorities = AuthoritiesConstants.ADMIN)
  void putWithMissingIdPathParamPaymentOrderMain() throws Exception {
    int databaseSizeBeforeUpdate = paymentOrderMainRepository.findAll().size();
    PaymentOrderMain updatedPaymentOrderMain
        = paymentOrderMainRepository.findById(paymentOrderMain.getId()).get();
    PaymentOrderMainDTO paymentOrderMainDto = paymentOrderMainMapper.toDto(updatedPaymentOrderMain);
    paymentOrderMainDto.setId(count.incrementAndGet());
    paymentOrderMainDto.setName(DEFAULT_Payment_NAME);
    paymentOrderMainDto.setPriceNet(null);
    paymentOrderMainDto.setVat(null);
    paymentOrderMainDto.setPriceGross(null);

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restPaymentOrderMainMockMvc.perform(put(ENTITY_API_URL + "/edit")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(paymentOrderMainDto)))
        .andExpect(status().isMethodNotAllowed());

    // Validate the PaymentOrderMain in the database
    List<PaymentOrderMain> paymentOrderMainList = paymentOrderMainRepository.findAll();
    assertThat(paymentOrderMainList).hasSize(databaseSizeBeforeUpdate);
  }
}
