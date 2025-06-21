package io.praegus.bda.locationservice.adapter.apipostcode;

public record AddressResponse(String street, String city, String house_number,
                                      String zip_code,
                                      float longitude,
                                      float latitude,
                                      String province) {
}
