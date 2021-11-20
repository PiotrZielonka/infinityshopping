package infinityshopping.online.app.repository;

import infinityshopping.online.app.domain.OrderMain;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@SuppressWarnings("unused")
@Repository
public interface OrderMainRepository extends JpaRepository<OrderMain, Long> {

  @Query(value = "SELECT * FROM ORDER_MAIN WHERE BUYER_LOGIN ="
      + " ?#{principal.username}", nativeQuery = true)
  List<OrderMain> getAllOrderMainByCurrentUserLogin();
}
