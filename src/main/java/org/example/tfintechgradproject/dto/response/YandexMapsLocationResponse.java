package org.example.tfintechgradproject.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class YandexMapsLocationResponse {
    private String address;
    private Point coordinates;
}
