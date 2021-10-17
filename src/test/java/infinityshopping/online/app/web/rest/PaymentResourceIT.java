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
import infinityshopping.online.app.domain.Payment;
import infinityshopping.online.app.repository.PaymentRepository;
import infinityshopping.online.app.security.AuthoritiesConstants;
import infinityshopping.online.app.service.dto.PaymentDTO;
import infinityshopping.online.app.service.mapper.PaymentMapper;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
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
class PaymentResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static BigDecimal DEFAULT_PRICE_NET = new BigDecimal("200");
    private static BigDecimal UPDATED_PRICE_NET = new BigDecimal("310");

    private static BigDecimal DEFAULT_VAT = new BigDecimal("23");
    private static BigDecimal UPDATED_VAT = new BigDecimal("5");

    private static BigDecimal DEFAULT_PROPER_PRICE_GROSS = new BigDecimal("246");
    private static BigDecimal UPDATED_PROPER_PRICE_GROSS = new BigDecimal("325.50");
    private static BigDecimal DEFAULT_FAKE_PRICE_GROSS = BigDecimal.ZERO;

    private static final Instant DEFAULT_CREATE_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATE_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATE_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATE_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/payments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentMapper paymentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPaymentMockMvc;

    private Payment payment;

    public static Payment createEntity(EntityManager em) {
        Payment payment = new Payment()
            .name(DEFAULT_NAME)
            .priceNet(DEFAULT_PRICE_NET)
            .vat(DEFAULT_VAT)
            .priceGross(DEFAULT_FAKE_PRICE_GROSS)
            .createTime(DEFAULT_CREATE_TIME)
            .updateTime(DEFAULT_UPDATE_TIME);
        return payment;
    }

    @BeforeEach
    public void initTest() {
        payment = createEntity(em);
    }

    @Test
    @Transactional
    @WithMockUser
    void createPaymentByUserShouldThrowStatusForbidden() throws Exception {
        int databaseSizeBeforeCreate = paymentRepository.findAll().size();
        // Create the Payment
        PaymentDTO paymentDto = paymentMapper.toDto(payment);
        restPaymentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paymentDto)))
            .andExpect(status().isForbidden());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void createPaymentByAnyoneShouldThrowStatusUnauthorized() throws Exception {
        int databaseSizeBeforeCreate = paymentRepository.findAll().size();
        // Create the Payment
        PaymentDTO paymentDto = paymentMapper.toDto(payment);
        restPaymentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paymentDto)))
            .andExpect(status().isUnauthorized());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    @WithMockUser(username = "admin", password = "admin", authorities = AuthoritiesConstants.ADMIN)
    void createPaymentAndSetProperPriceGrossAutomatic() throws Exception {
        final int databaseSizeBeforeCreate = paymentRepository.findAll().size();

        // Create the Payment
        PaymentDTO paymentDto = paymentMapper.toDto(payment);
        paymentDto.setPriceGross(null);
        paymentDto.setCreateTime(null);
        paymentDto.setUpdateTime(null);
        restPaymentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paymentDto)))
            .andExpect(status().isCreated());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeCreate + 1);
        Payment testPayment = paymentList.get(paymentList.size() - 1);
        assertThat(testPayment.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPayment.getPriceNet()).isEqualByComparingTo(DEFAULT_PRICE_NET);
        assertThat(testPayment.getVat()).isEqualByComparingTo(DEFAULT_VAT);
        assertThat(testPayment.getPriceGross()).isEqualByComparingTo(DEFAULT_PROPER_PRICE_GROSS.setScale(2));
        assertNotNull(testPayment.getCreateTime());
        assertNotNull(testPayment.getUpdateTime());
    }

    @Test
    @Transactional
    @WithMockUser(username = "admin", password = "admin", authorities = AuthoritiesConstants.ADMIN)
    void createPaymentWithExistingId() throws Exception {
        // Create the Payment with an existing ID
        payment.setId(1L);
        PaymentDTO paymentDto = paymentMapper.toDto(payment);

        int databaseSizeBeforeCreate = paymentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaymentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paymentDto)))
            .andExpect(status().isBadRequest());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    @WithMockUser(username = "admin", password = "admin", authorities = AuthoritiesConstants.ADMIN)
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentRepository.findAll().size();
        // set the field null
        payment.setName(null);

        // Create the Payment, which fails.
        PaymentDTO paymentDto = paymentMapper.toDto(payment);

        restPaymentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paymentDto)))
            .andExpect(status().isBadRequest());

        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    @WithMockUser(username = "admin", password = "admin", authorities = AuthoritiesConstants.ADMIN)
    void checkPriceNetIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentRepository.findAll().size();
        // set the field null
        payment.setPriceNet(null);

        // Create the Payment, which fails.
        PaymentDTO paymentDto = paymentMapper.toDto(payment);

        restPaymentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paymentDto)))
            .andExpect(status().isBadRequest());

        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    @WithMockUser(username = "admin", password = "admin", authorities = AuthoritiesConstants.ADMIN)
    void checkVatIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentRepository.findAll().size();
        // set the field null
        payment.setVat(null);

        // Create the Payment, which fails.
        PaymentDTO paymentDto = paymentMapper.toDto(payment);

        restPaymentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paymentDto)))
            .andExpect(status().isBadRequest());

        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    @WithMockUser(username = "admin", password = "admin", authorities = AuthoritiesConstants.ADMIN)
    void getAllPayments() throws Exception {
        // Initialize the database
        payment.setPriceGross(DEFAULT_PROPER_PRICE_GROSS);
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList
        restPaymentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(payment.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].priceNet").value(hasItem(sameNumber(DEFAULT_PRICE_NET))))
            .andExpect(jsonPath("$.[*].vat").value(hasItem(sameNumber(DEFAULT_VAT))))
            .andExpect(jsonPath("$.[*].priceGross").value(hasItem(sameNumber(DEFAULT_PROPER_PRICE_GROSS))))
            .andExpect(jsonPath("$.[*].createTime").exists())
            .andExpect(jsonPath("$.[*].updateTime").exists());
    }

    @Test
    @Transactional
    @WithMockUser(username = "user", password = "user", authorities = AuthoritiesConstants.USER)
    void getAllPaymentsForPaymentManagementByUserShouldThrowStatusForbidden() throws Exception {
        // Initialize the database
        payment.setPriceGross(DEFAULT_PROPER_PRICE_GROSS);
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList
        restPaymentMockMvc.perform(get(ENTITY_API_URL + "?sort=id,desc")).andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    void getAllProductsForPaymentManagementByAnyoneShouldThrowStatusUnauthorized() throws Exception {
        // Initialize the database
        payment.setPriceGross(DEFAULT_PROPER_PRICE_GROSS);
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList
        restPaymentMockMvc.perform(get(ENTITY_API_URL + "?sort=id,desc")).andExpect(status().isUnauthorized());
    }

    @Test
    @Transactional
    void getAllPaymentsOnlyWithNamePriceGross() throws Exception {
        // Initialize the database
        payment.setPriceGross(DEFAULT_PROPER_PRICE_GROSS);
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList
        restPaymentMockMvc
            .perform(get(ENTITY_API_URL + "/namePriceGross" + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(payment.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].priceNet").doesNotExist())
            .andExpect(jsonPath("$.[*].vat").doesNotExist())
            .andExpect(jsonPath("$.[*].priceGross").value(hasItem(sameNumber(DEFAULT_PROPER_PRICE_GROSS))))
            .andExpect(jsonPath("$.[*].createTime").doesNotExist())
            .andExpect(jsonPath("$.[*].updateTime").doesNotExist());
    }

    @Test
    @Transactional
    @WithMockUser(username = "user", password = "user", authorities = AuthoritiesConstants.USER)
    void getPaymentForPaymentManagementByUserShouldThrowStatusForbidden() throws Exception {
        // Initialize the database
        payment.setPriceGross(DEFAULT_PROPER_PRICE_GROSS);
        paymentRepository.saveAndFlush(payment);

        // Get the payment
        restPaymentMockMvc.perform(get(ENTITY_API_URL_ID, payment.getId())).andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    void getPaymentForPaymentManagementByAnyoneShouldThrowStatusUnauthorized() throws Exception {
        // Initialize the database
        payment.setPriceGross(DEFAULT_PROPER_PRICE_GROSS);
        paymentRepository.saveAndFlush(payment);

        // Get the payment
        restPaymentMockMvc.perform(get(ENTITY_API_URL_ID, payment.getId())).andExpect(status().isUnauthorized());
    }

    @Test
    @Transactional
    @WithMockUser(username = "admin", password = "admin", authorities = AuthoritiesConstants.ADMIN)
    void getPayment() throws Exception {
        // Initialize the database
        payment.setPriceGross(DEFAULT_PROPER_PRICE_GROSS);
        paymentRepository.saveAndFlush(payment);

        // Get the payment
        restPaymentMockMvc
            .perform(get(ENTITY_API_URL_ID, payment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(payment.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.priceNet").value(sameNumber(DEFAULT_PRICE_NET)))
            .andExpect(jsonPath("$.vat").value(sameNumber(DEFAULT_VAT)))
            .andExpect(jsonPath("$.priceGross").value(sameNumber(DEFAULT_PROPER_PRICE_GROSS)));
    }

    @Test
    @Transactional
    @WithMockUser(username = "admin", password = "admin", authorities = AuthoritiesConstants.ADMIN)
    void getNonExistingPayment() throws Exception {
        // Get the payment
        restPaymentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    @WithMockUser(username = "user", password = "user", authorities = AuthoritiesConstants.USER)
    void putNewPaymentByUserShouldThrowStatusForbidden() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        final int databaseSizeBeforeUpdate = paymentRepository.findAll().size();

        // Update the payment
        Payment updatedPayment = paymentRepository.findById(payment.getId()).get();
        // Disconnect from session so that the updates on updatedPayment are not directly saved in db
        em.detach(updatedPayment);
        updatedPayment
            .name(UPDATED_NAME)
            .priceNet(UPDATED_PRICE_NET)
            .vat(UPDATED_VAT)
            .priceGross(null)
            .createTime(UPDATED_CREATE_TIME)
            .updateTime(UPDATED_UPDATE_TIME);
        PaymentDTO paymentDto = paymentMapper.toDto(updatedPayment);

        restPaymentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paymentDto.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paymentDto))
            )
            .andExpect(status().isForbidden());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeUpdate);
        Payment testPayment = paymentList.get(paymentList.size() - 1);
        assertThat(testPayment.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPayment.getPriceNet()).isEqualTo(DEFAULT_PRICE_NET.setScale(2));
        assertThat(testPayment.getVat()).isEqualTo(DEFAULT_VAT.setScale(2));
        assertThat(testPayment.getPriceGross()).isEqualTo(DEFAULT_FAKE_PRICE_GROSS.setScale(2));
        assertNotNull(testPayment.getCreateTime());
        assertNotNull(testPayment.getUpdateTime());
    }

    @Test
    @Transactional
    void putNewPaymentByAnyoneShouldThrowStatusUnauthorized() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        final int databaseSizeBeforeUpdate = paymentRepository.findAll().size();

        // Update the payment
        Payment updatedPayment = paymentRepository.findById(payment.getId()).get();
        // Disconnect from session so that the updates on updatedPayment are not directly saved in db
        em.detach(updatedPayment);
        updatedPayment
            .name(UPDATED_NAME)
            .priceNet(UPDATED_PRICE_NET)
            .vat(UPDATED_VAT)
            .priceGross(null)
            .createTime(UPDATED_CREATE_TIME)
            .updateTime(UPDATED_UPDATE_TIME);
        PaymentDTO paymentDto = paymentMapper.toDto(updatedPayment);

        restPaymentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paymentDto.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paymentDto))
            )
            .andExpect(status().isUnauthorized());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeUpdate);
        Payment testPayment = paymentList.get(paymentList.size() - 1);
        assertThat(testPayment.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPayment.getPriceNet()).isEqualTo(DEFAULT_PRICE_NET.setScale(2));
        assertThat(testPayment.getVat()).isEqualTo(DEFAULT_VAT.setScale(2));
        assertThat(testPayment.getPriceGross()).isEqualTo(DEFAULT_FAKE_PRICE_GROSS.setScale(2));
        assertNotNull(testPayment.getCreateTime());
        assertNotNull(testPayment.getUpdateTime());
    }

    @Test
    @Transactional
    @WithMockUser(username = "admin", password = "admin", authorities = AuthoritiesConstants.ADMIN)
    void putNewPaymentAndSetProperPriceGrossAutomatic() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        final int databaseSizeBeforeUpdate = paymentRepository.findAll().size();

        // Update the payment
        Payment updatedPayment = paymentRepository.findById(payment.getId()).get();
        // Disconnect from session so that the updates on updatedPayment are not directly saved in db
        em.detach(updatedPayment);
        updatedPayment
            .name(UPDATED_NAME)
            .priceNet(UPDATED_PRICE_NET)
            .vat(UPDATED_VAT)
            .priceGross(null)
            .createTime(UPDATED_CREATE_TIME)
            .updateTime(UPDATED_UPDATE_TIME);
        PaymentDTO paymentDto = paymentMapper.toDto(updatedPayment);

        restPaymentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paymentDto.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paymentDto))
            )
            .andExpect(status().isOk());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeUpdate);
        Payment testPayment = paymentList.get(paymentList.size() - 1);
        assertThat(testPayment.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPayment.getPriceNet()).isEqualTo(UPDATED_PRICE_NET);
        assertThat(testPayment.getVat()).isEqualTo(UPDATED_VAT);
        assertThat(testPayment.getPriceGross()).isEqualTo(UPDATED_PROPER_PRICE_GROSS);
        assertNotNull(testPayment.getCreateTime());
        assertNotNull(testPayment.getUpdateTime());
    }

    @Test
    @Transactional
    @WithMockUser(username = "admin", password = "admin", authorities = AuthoritiesConstants.ADMIN)
    void putNonExistingPayment() throws Exception {
        int databaseSizeBeforeUpdate = paymentRepository.findAll().size();
        payment.setId(count.incrementAndGet());

        // Create the Payment
        PaymentDTO paymentDto = paymentMapper.toDto(payment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paymentDto.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paymentDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    @WithMockUser(username = "admin", password = "admin", authorities = AuthoritiesConstants.ADMIN)
    void putWithIdMismatchPayment() throws Exception {
        int databaseSizeBeforeUpdate = paymentRepository.findAll().size();
        payment.setId(count.incrementAndGet());

        // Create the Payment
        PaymentDTO paymentDto = paymentMapper.toDto(payment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paymentDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    @WithMockUser(username = "admin", password = "admin", authorities = AuthoritiesConstants.ADMIN)
    void putWithMissingIdPathParamPayment() throws Exception {
        int databaseSizeBeforeUpdate = paymentRepository.findAll().size();
        payment.setId(count.incrementAndGet());

        // Create the Payment
        PaymentDTO paymentDto = paymentMapper.toDto(payment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paymentDto)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    @WithMockUser(username = "admin", password = "admin", authorities = AuthoritiesConstants.ADMIN)
    void deletePayment() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        int databaseSizeBeforeDelete = paymentRepository.findAll().size();

        // Delete the payment
        restPaymentMockMvc
            .perform(delete(ENTITY_API_URL_ID, payment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
