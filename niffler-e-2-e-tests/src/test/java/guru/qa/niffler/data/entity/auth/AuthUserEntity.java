package guru.qa.niffler.data.entity.auth;

import guru.qa.niffler.data.entity.userdata.UdUserEntity;
import guru.qa.niffler.model.UdUserJson;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class AuthUserEntity implements Serializable {
  private UUID id;
  private String username;
  private String password;
  private Boolean enabled;
  private Boolean accountNonExpired;
  private Boolean accountNonLocked;
  private Boolean credentialsNonExpired;
  private List<AuthorityEntity> authorities = new ArrayList<>();

  public static UdUserEntity fromJson(UdUserJson json) {
    UdUserEntity ue = new UdUserEntity();
    ue.setId(json.id());
    ue.setUsername(json.username());
    ue.setCurrency(json.currency());
    ue.setFirstname(json.firstname());
    ue.setSurname(json.surname());
    ue.setFullname(json.fullname());
    ue.setPhoto(json.photo().getBytes());
    ue.setPhotoSmall(json.photoSmall().getBytes());
    return ue;
  }
}
