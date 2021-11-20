package infinityshopping.online.app.service;

import infinityshopping.online.app.service.dto.OrderMainDTO;
import java.util.List;
import java.util.Optional;

public interface OrderMainService {

  OrderMainDTO save(OrderMainDTO orderMainDto);

  Optional<OrderMainDTO> partialUpdate(OrderMainDTO orderMainDto);

  List<OrderMainDTO> findAll();

  List<OrderMainDTO> getAllOrderMainByCurrentUserLogin();

  Optional<OrderMainDTO> findOne(Long id);

  void deleteOrderMainAndAllProductInOrdersByIdOrderMain(Long id);  
}
