package guru.qa.niffler.model.auth;

import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class AuthorityJson {
    private UUID id;
    private Authority authority;
    private UUID userId;


    public static AuthorityJson fromEntity(AuthorityEntity entity) {
        AuthorityJson authority = new AuthorityJson();
        authority.setId(entity.getId());
        authority.setAuthority(entity.getAuthority());
        authority.setUserId(entity.getUserId());
        return authority;
    }
}