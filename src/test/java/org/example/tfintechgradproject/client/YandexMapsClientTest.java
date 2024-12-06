package org.example.tfintechgradproject.client;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.example.tfintechgradproject.dto.response.YandexMapsLocationResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.any;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WireMockTest(extensionScanningEnabled = true)
public class YandexMapsClientTest {

    @Autowired
    private YandexMapsClient apiClient;

    @Autowired
    private WKTReader wktReader;



    @BeforeAll
    public static void setUp() {
        configureFor(wireMockExtension.getPort());
    }

    @RegisterExtension
    static WireMockExtension wireMockExtension = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort().dynamicPort())
            .build();

    @DynamicPropertySource
    public static void setUpMockBaseUrl(DynamicPropertyRegistry registry) {
        registry.add("yandexMapsApi.url", () -> wireMockExtension.baseUrl() + "/1.x");
    }


    @Test
    public void test_getLocationInfo() throws ParseException {
        var response = new YandexMapsLocationResponse("Дубай, бульвар Мухаммед Бин Рашид, 1", (Point) wktReader.read("POINT(25.197300 55.274243)"));
        var address = "Address";
        var json = """
                {
                  "response": {
                    "GeoObjectCollection": {
                      "metaDataProperty": {
                        "GeocoderResponseMetaData": {
                          "request": "Дубай, бульвар Мухаммед Бин Рашид, дом 1",
                          "found": "1",
                          "results": "10"
                        }
                      },
                      "featureMember": [
                        {
                          "GeoObject": {
                            "metaDataProperty": {
                              "GeocoderMetaData": {
                                "kind": "house",
                                "text": "ОАЭ, Дубай, бульвар Мухаммед Бин Рашид, дом 1",
                                "precision": "exact",
                                "Address": {
                                  "country_code": "UAE",
                                  "postal_code": "00000",
                                  "formatted": "Дубай, бульвар Мухаммед Бин Рашид, 1"
                                }
                              }
                            },
                            "boundedBy": {
                              "Envelope": {
                                "lowerCorner": "25.196563 55.274149",
                                "upperCorner": "25.197612 55.274183"
                              }
                            },
                            "Point": {
                              "pos": "25.197300 55.274243"
                            }
                          }
                        }
                      ]
                    }
                  }
                }
                """;
        stubFor(any(urlPathEqualTo("/1.x"))
                        .willReturn(
                                aResponse()
                                        .withHeader("Content-Type", "application/json")
                                        .withBody(json)
                        )
        );

        var result = apiClient.getLocationInfo(address);
        assertEquals(response, result);
    }
}
