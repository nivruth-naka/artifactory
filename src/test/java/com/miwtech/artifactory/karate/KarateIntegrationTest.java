package com.miwtech.artifactory.karate;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.intuit.karate.junit4.Karate;
import cucumber.api.CucumberOptions;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

@RunWith(Karate.class)
@CucumberOptions(features = "classpath:karate")
public class KarateIntegrationTest {

    private static final int PORT_NUMBER = 8097;

    private static final WireMockServer wireMockServer =
            new WireMockServer(WireMockConfiguration.options().port(PORT_NUMBER));


    @BeforeClass
    public static void setUp() {
        wireMockServer.start();
        configureFor("localhost", PORT_NUMBER);
        stubFor(get(urlEqualTo("/items"))
                .withBasicAuth("Authorization", "Basic bWl3LXVzZXI6Y2hhbmdlbWUxMjM=")
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("[{\"name\":\"wallet\",\"description\":\"trifold\",\"price\":44}]")));
        stubFor(post(urlEqualTo("/items"))
                .withHeader("content-type", equalTo("application/json"))
                .withBasicAuth("Authorization", "Basic bWl3LXVzZXI6Y2hhbmdlbWUxMjM=")
                .withRequestBody(containing("{\"name\":\"wallet\",\"description\":\"trifold\",\"price\":44}"))
                .willReturn(aResponse()
                        .withStatus(200)));
    }

    @AfterClass
    public static void tearDown() {
        wireMockServer.stop();
    }

}
