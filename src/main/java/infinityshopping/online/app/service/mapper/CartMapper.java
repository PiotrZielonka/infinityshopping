package infinityshopping.online.app.service.mapper;

import infinityshopping.online.app.domain.Cart;
import infinityshopping.online.app.service.dto.CartDTO;
import infinityshopping.online.app.service.dto.CartDtoAmountOfCartGross;
import infinityshopping.online.app.service.dto.CartDtoAmountsGross;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface CartMapper extends EntityMapper<CartDTO, Cart> {

  CartDTO toDto(Cart s);

  CartDtoAmountsGross toDtoAmountsGross(Cart s);

  CartDtoAmountOfCartGross toDtoAmountOfCartGross(Cart s);

  @Named("id")
  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "id", source = "id")
  CartDTO toDtoId(Cart cart);
}
