package com.scm.Forms;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserForm {

    @NotBlank(message = "Username can not be empty.")
    @Size(min = 3, message = "Minimium 3 characters is required.")
    private String name;

    @NotBlank(message = "Email can not be empty.")
    @Email(message = "Invalid Email Address.")
    // regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$"
    private String email;

    @NotBlank(message = "Password can not be empty.")
    @Size(min = 6, message = "Minimum 6 characters is required.")
    private String password;

    @NotBlank(message = "About section is required.")
    private String about;

    @Size(min = 8, max = 12, message = "Invalid Number.")
    private String phoneNumber;
}
