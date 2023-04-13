package org.rangiffler.data;

import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class FriendsId implements Serializable {

    private UUID user;
    private UUID friend;
}
