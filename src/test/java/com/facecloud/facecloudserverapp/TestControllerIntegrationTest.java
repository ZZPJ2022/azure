package com.facecloud.facecloudserverapp;

import com.facecloud.facecloudserverapp.controller.LocationController;
import com.facecloud.facecloudserverapp.controller.TestMainController;
import com.facecloud.facecloudserverapp.service.LocationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LocationController.class)
public class TestControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LocationService service;

    @Test
    public void testReturnDefault() throws Exception {
        String name = "jan";
        when(service.getAllLocationsXmlForUser(name)).thenReturn("Hello");
        this.mockMvc.perform(MockMvcRequestBuilders.get("/homePage",name)).andDo(print())
                .andExpect(status().isOk());
    }

}
