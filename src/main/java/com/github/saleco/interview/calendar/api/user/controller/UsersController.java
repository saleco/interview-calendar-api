package com.github.saleco.interview.calendar.api.user.controller;

import com.github.saleco.interview.calendar.api.enums.UserType;
import com.github.saleco.interview.calendar.api.user.dto.CreateUserDto;
import com.github.saleco.interview.calendar.api.user.dto.UserDto;
import com.github.saleco.interview.calendar.api.user.service.UserService;
import com.github.saleco.interview.calendar.api.utils.InterviewCalendarAPIResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/ap1/v1/users")
@Tag(name = "Users's API", description = "Resources related to User's information")
@RequiredArgsConstructor
@Slf4j
public class UsersController {

    private final UserService userService;

    @Operation(summary = "Retrieve the list of users")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "The list of users has been returned",
        content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserDto.class))))})
    public Page<UserDto> getUsers(@Parameter(description = "Page number. Starting from 0")
                                  @RequestParam(defaultValue = "CANDIDATE") UserType userType,
                                  @RequestParam(defaultValue = "0") int page,
                                  @Parameter(description = "Page size. By default 20")
                                  @RequestParam(defaultValue = "20") int size) {
        return userService.getUsersByType(page, size, userType);
    }

    @Operation(summary = "Create User",
      description = "As an Interviewer / Candidate, I would like to create an user")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "The User were successfully created.",
        content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserDto.class)))),
      @ApiResponse(responseCode = "400", description = "Invalid parameter",
        content = @Content(schema = @Schema(implementation = InterviewCalendarAPIResponse.class)))})
    public UserDto createUser(
      @RequestBody @Valid CreateUserDto createUserDto) {
        return userService.createUser(createUserDto);
    }
}

