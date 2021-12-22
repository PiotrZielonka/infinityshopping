package infinityshopping.online.app.service.impl;

import infinityshopping.online.app.domain.OrderMain;
import infinityshopping.online.app.domain.ProductInOrderMain;
import infinityshopping.online.app.repository.OrderMainRepository;
import infinityshopping.online.app.repository.ProductInOrderMainRepository;
import infinityshopping.online.app.service.ProductInOrderMainService;
import infinityshopping.online.app.service.dto.ProductInOrderMainDTO;
import infinityshopping.online.app.service.errors.OrderMainNotFoundException;
import infinityshopping.online.app.service.errors.ProductInOrderMainNotFoundException;
import infinityshopping.online.app.service.mapper.ProductInOrderMainMapper;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductInOrderMainServiceImpl implements ProductInOrderMainService {

  private final Logger log = LoggerFactory.getLogger(ProductInOrderMainServiceImpl.class);

  private final ProductInOrderMainRepository productInOrderMainRepository;

  private final OrderMainRepository orderMainRepository;

  private final ProductInOrderMainMapper productInOrderMainMapper;

  private BigDecimal amountOfCartNet;

  private BigDecimal amountOfCartGross;

  private OrderMain orderMain;


  public ProductInOrderMainServiceImpl(
      ProductInOrderMainRepository productInOrderMainRepository,
      OrderMainRepository orderMainRepository,
      ProductInOrderMainMapper productInOrderMainMapper
  ) {
    this.productInOrderMainRepository = productInOrderMainRepository;
    this.orderMainRepository = orderMainRepository;
    this.productInOrderMainMapper = productInOrderMainMapper;
  }

  @Override
  @Transactional
  public ProductInOrderMainDTO save(ProductInOrderMainDTO productInOrderMainDto) {
    log.debug("Request to save ProductInOrderMain : {}", productInOrderMainDto);
    ProductInOrderMain productInOrderMain = productInOrderMainMapper.toEntity(
        productInOrderMainDto);

    setTotalPriceNet(productInOrderMain);
    setTotalPriceGross(productInOrderMain);
    setAmountOfCartNetToProperOrderMain(productInOrderMain);
    setAmountOfCartGrossToProperOrderMain(productInOrderMain);
    setAmountOfOrderNetToProperOrderMain(productInOrderMain);
    setAmountOfOrderGrossToProperOrderMain(productInOrderMain);

    productInOrderMain = productInOrderMainRepository.save(productInOrderMain);
    return productInOrderMainMapper.toDto(productInOrderMain);
  }

  private void setTotalPriceNet(ProductInOrderMain productInOrderMain) {
    productInOrderMain.setTotalPriceNet(
        productInOrderMain.getQuantity().multiply(productInOrderMain.getPriceNet()));
    productInOrderMainRepository.save(productInOrderMain);
  }

  private void setTotalPriceGross(ProductInOrderMain productInOrderMain) {
    productInOrderMain.setTotalPriceGross(
        productInOrderMain.getQuantity().multiply(productInOrderMain.getPriceGross()));
    productInOrderMainRepository.save(productInOrderMain);
  }

  private void setAmountOfCartNetToProperOrderMain(ProductInOrderMain productInOrderMain) {
    orderMain = checkIfProperOrderMainExist(productInOrderMain);

    amountOfCartNet = BigDecimal.ZERO;

    orderMain.getProductInOrderMains().forEach(productInOrderMainForEach ->
        amountOfCartNet = amountOfCartNet.add(productInOrderMainForEach.getTotalPriceNet())
    );

    orderMain.setAmountOfCartNet(amountOfCartNet);
    orderMainRepository.save(orderMain);
  }

  private void setAmountOfCartGrossToProperOrderMain(ProductInOrderMain productInOrderMain) {
    orderMain = checkIfProperOrderMainExist(productInOrderMain);

    amountOfCartGross = BigDecimal.ZERO;

    orderMain.getProductInOrderMains().forEach(productInOrderMainForEach ->
        amountOfCartGross = amountOfCartGross.add(productInOrderMainForEach.getTotalPriceGross())
    );

    orderMain.setAmountOfCartGross(amountOfCartGross);
    orderMainRepository.save(orderMain);
  }

  private void setAmountOfOrderNetToProperOrderMain(ProductInOrderMain productInOrderMain) {
    orderMain = checkIfProperOrderMainExist(productInOrderMain);

    orderMain.setAmountOfOrderNet(BigDecimal.ZERO);

    orderMain.setAmountOfOrderNet(orderMain.getAmountOfCartNet().add(
        orderMain.getAmountOfShipmentNet()));
    orderMainRepository.save(orderMain);
  }

  private void setAmountOfOrderGrossToProperOrderMain(ProductInOrderMain productInOrderMain) {
    orderMain = checkIfProperOrderMainExist(productInOrderMain);

    orderMain.setAmountOfOrderGross(BigDecimal.ZERO);

    orderMain.setAmountOfOrderGross(orderMain.getAmountOfCartGross().add(
        orderMain.getAmountOfShipmentGross()));
    orderMainRepository.save(orderMain);
  }

  @Override
  @Transactional(readOnly = true)
  public List<ProductInOrderMainDTO> findByOrderMainId(Long id, Pageable pageable) {
    log.debug("REST request to get all ProductInOrder by id OrderMain");
    return productInOrderMainRepository.findByOrderMainId(id, pageable).stream()
        .map(productInOrderMainMapper::toDto)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<ProductInOrderMainDTO> findOne(Long id) {
    log.debug("Request to get ProductInOrderMain : {}", id);
    return productInOrderMainRepository.findById(id).map(productInOrderMainMapper::toDto);
  }

  @Override
  @Transactional
  public void delete(Long id) {
    log.debug("Request to delete ProductInOrderMain : {}", id);

    ProductInOrderMain productInOrderMain = checkIfProductInOrderMainExist(id);

    minusSelectedProductTotalPriceNetToProperOrderMain(productInOrderMain);
    minusSelectedProductTotalPriceGrossToProperOrderMain(productInOrderMain);
    minusSelectedProductTotalPriceNetToProperOrderMainAmountOfOrderNet(productInOrderMain);
    minusSelectedProductTotalPriceGrossToProperOrderMainAmountOfOrderGross(productInOrderMain);

    productInOrderMainRepository.deleteById(id);
  }

  private void minusSelectedProductTotalPriceNetToProperOrderMain(
      ProductInOrderMain productInOrderMain) {
    orderMain = checkIfProperOrderMainExist(productInOrderMain);

    orderMain.setAmountOfCartNet(orderMain.getAmountOfCartNet().subtract(
        productInOrderMain.getTotalPriceNet()));
    orderMainRepository.save(orderMain);
  }

  private void minusSelectedProductTotalPriceGrossToProperOrderMain(
      ProductInOrderMain productInOrderMain) {
    orderMain = checkIfProperOrderMainExist(productInOrderMain);

    orderMain.setAmountOfCartGross(orderMain.getAmountOfCartGross().subtract(
        productInOrderMain.getTotalPriceGross()));
    orderMainRepository.save(orderMain);
  }

  private void minusSelectedProductTotalPriceNetToProperOrderMainAmountOfOrderNet(
      ProductInOrderMain productInOrderMain) {
    orderMain = checkIfProperOrderMainExist(productInOrderMain);

    orderMain.setAmountOfOrderNet(orderMain.getAmountOfOrderNet().subtract(
        productInOrderMain.getTotalPriceNet()));
    orderMainRepository.save(orderMain);
  }

  private void minusSelectedProductTotalPriceGrossToProperOrderMainAmountOfOrderGross(
      ProductInOrderMain productInOrderMain) {
    orderMain = checkIfProperOrderMainExist(productInOrderMain);

    orderMain.setAmountOfOrderGross(orderMain.getAmountOfOrderGross().subtract(
        productInOrderMain.getTotalPriceGross()));
    orderMainRepository.save(orderMain);
  }

  private OrderMain checkIfProperOrderMainExist(ProductInOrderMain productInOrderMain) {
    return orderMainRepository.findById(productInOrderMain.getOrderMain().getId())
        .orElseThrow(OrderMainNotFoundException::new);
  }

  private ProductInOrderMain checkIfProductInOrderMainExist(Long id) {
    return productInOrderMainRepository.findById(id)
        .orElseThrow(ProductInOrderMainNotFoundException::new);
  }
}
