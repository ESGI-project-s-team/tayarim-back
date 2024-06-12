package fr.esgi.al5.tayarim.dto.reservation;

import java.time.LocalTime;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data // Generates getters, setters, toString, equals, and hashCode methods
@RequiredArgsConstructor
@AllArgsConstructor // Generates an all-args constructor
@Builder
public class ReservationUpdateDto {

    @Valid

    /*@NotBlank(message = "error_reservation_invalid_status")
    @Pattern(regexp = "wainting|validate|refused|payed|done", message = "error_reservation_invalid_status")
    private String statut;*/

    @Pattern(regexp = "[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}", message = "error_reservation_invalid_mail")
    private String email;

    @Size(min = 1, max = 20, message = "error_reservation_invalid_name")
    @Pattern(regexp = "[a-zA-Z]*", message = "error_reservation_invalid_name")
    private String nom;

    @Pattern(regexp = "[a-zA-Z]*", message = "error_reservation_invalid_firstName")
    @Size(min = 1, max = 20, message = "error_reservation_invalid_firstName")
    private String prenom;

    @Min(value = 1, message = "error_reservation_invalid_nbPersonnes")
    private Integer nbPersonnes;

    @Min(value = 1, message = "error_reservation_invalid_amount")
    private Float montant;

    @Pattern(regexp = "([01][0-9]|2[0-3]):[0-5][0-9]", message = "error_reservation_invalid_checkin")
    private String checkIn;

    @Pattern(regexp = "([01][0-9]|2[0-3]):[0-5][0-9]", message = "error_reservation_invalid_checkout")
    private String checkOut;

    @Min(value = 1, message = "error_reservation_invalid_home")
    private Long idLogement;

}
