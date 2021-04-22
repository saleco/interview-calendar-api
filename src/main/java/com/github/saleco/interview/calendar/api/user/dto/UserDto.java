package com.github.saleco.interview.calendar.api.user.dto;

import com.github.saleco.interview.calendar.api.enums.UserType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "User")
public class UserDto {

        @Schema(description = "User's Identification", required = true, example = "1")
        private Long id;

        @Schema(description = "User's Name", required = true, example = "Marie Clark")
        private String name;

        @Schema(description = "User's Type", required = true, implementation = UserType.class)
        private UserType userType;

}

