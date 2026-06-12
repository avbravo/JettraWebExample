package com.jettra.example.entity.library;

import io.jettra.wui.core.annotations.PropertiesLabel;
import io.jettra.wui.validations.NotNull;
import io.jettra.wui.validations.Size;

public record Author(@NotNull
        @Size(min = 2, max = 5)
        String id,
        @Size(min = 2, max = 5)
        String name,
        String country) {

}
