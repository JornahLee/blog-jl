package com.jornah.model.newP;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@Data
@SuperBuilder
@NoArgsConstructor
public class BaseEntity {
    private Long id;
    private Instant created;
    private Instant updated;

}
