package infinityshopping.online.app.service.mapper;

import infinityshopping.online.app.domain.Cart;
import infinityshopping.online.app.service.dto.CartDTO;
import infinityshopping.online.app.service.dto.CartDtoAmountOfCartGross;
import infinityshopping.online.app.service.dto.CartDtoAmountsGross;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface CartMapper extends EntityMapper<CartDTO, Cart> {
    @Mapping(target = "user", source = "user", qualifiedByName = "login")
    CartDTO toDto(Cart s);

    @Mapping(target = "user", source = "user", qualifiedByName = "login")
    CartDtoAmountsGross toDtoAmountsGross(Cart s);

    @Mapping(target = "user", source = "user", qualifiedByName = "login")
    CartDtoAmountOfCartGross toDtoAmountOfCartGross(Cart s);
}
