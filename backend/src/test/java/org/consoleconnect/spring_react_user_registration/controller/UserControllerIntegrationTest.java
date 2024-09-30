package org.consoleconnect.spring_react_user_registration.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.consoleconnect.spring_react_user_registration.TestConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(classes = {TestConfig.class})
@ActiveProfiles("test")
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserControllerIntegrationTest {

    // Assuming MyCommandLineRunner creates initial user data

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testFindAll() throws Exception {

        mockMvc.perform(get("/api/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Nikos"))
                .andExpect(jsonPath("$[1].firstName").value("John"));
    }

    @Test
    void testFindById() throws Exception {

        mockMvc.perform(get("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Nikos"));
    }

    @Test
    void testCreate() throws Exception {

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"Jane\",\"lastName\":\"Smith\",\"email\":\"janesmith@gmail.com\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void testUpdate() throws Exception {

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"John\",\"lastName\":\"Smith\",\"email\":\"john.smith@gmail.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastName").value("Smith"));
    }

    @Test
    void testUpdateMultiple() throws Exception {

        mockMvc.perform(put("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[{\"id\":1,\"firstName\":\"John\",\"lastName\":\"Smith\",\"email\":\"john.smith@gmail.com\"}," +
                                "{\"id\":2,\"firstName\":\"Jane\",\"lastName\":\"Smith\",\"email\":\"jane.smith@gmail.com\"}]"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].lastName").value("Smith"))
                .andExpect(jsonPath("$[1].lastName").value("Smith"));
    }

    @Test
    void testDeactivate() throws Exception {

        mockMvc.perform(put("/api/users/1/deactivate")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isDeactivated").value(true));
    }

    @Test
    void testDeactivateMultiple() throws Exception {

        mockMvc.perform(put("/api/users/deactivate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[1,2]"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].isDeactivated").value(true))
                .andExpect(jsonPath("$[1].isDeactivated").value(true));
    }

    @Test
    void testActivateUser() throws Exception {

        mockMvc.perform(put("/api/users/1/activate")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void testFindById_UserNotFound() throws Exception {

        // initial users size is 2
        mockMvc.perform(get("/api/users/3")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreate_UniqueConstraintViolation() throws Exception {

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"John\",\"lastName\":\"Doe\",\"email\":\"johndoe@gmail.com\"}"))
                .andExpect(status().isConflict());
    }

    @Test
    void testCreate_ValidationError() throws Exception {

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"\",\"lastName\":\"\",\"email\":\"\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdate_UserNotFound() throws Exception {

        // initial users size is 2
        mockMvc.perform(put("/api/users/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"John\",\"lastName\":\"Smith\",\"email\":\"john.smith@gmail.com\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeactivate_UserNotFound() throws Exception {

        // initial users size is 2
        mockMvc.perform(put("/api/users/3/deactivate")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testActivate_UserNotFound() throws Exception {

        // initial users size is 2
        mockMvc.perform(put("/api/users/3/activate")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}