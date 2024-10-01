
package org.consoleconnect.spring_react_user_registration.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.consoleconnect.spring_react_user_registration.TestConfig;
import org.consoleconnect.spring_react_user_registration.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest(classes = {TestConfig.class})
@ActiveProfiles("test")
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserControllerIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    void beforeAllInitializeData() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/initData")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testCreate() throws Exception {

        String newUserJson = "{\"firstName\":\"Jane\",\"lastName\":\"Smith\",\"email\":\"janesmith@gmail.com\"}";

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newUserJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists()); // Check if ID is returned
    }

    @Test
    void shouldGetAllUsers() throws Exception {

        mockMvc.perform(get("/api/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void testUpdate() throws Exception {

        String updatedUserJson = "{\"firstName\":\"John\",\"lastName\":\"Smith\",\"email\":\"john.smith@gmail.com\"}";

        mockMvc.perform(put("/api/users/1") // Update user with ID 1
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedUserJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("lastName").value("Smith"));
    }

    @Test
    void testUpdateMultiple() throws Exception {

        String updatedUsersJson = "[{\"id\":1,\"firstName\":\"John\",\"lastName\":\"Smith\",\"email\":\"john.smith@gmail.com\"}," +
                "{\"id\":2,\"firstName\":\"Jane\",\"lastName\":\"Smith\",\"email\":\"jane.smith@gmail.com\"}]";

        mockMvc.perform(put("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedUsersJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("[0].lastName").value("Smith"))
                .andExpect(jsonPath("[1].lastName").value("Smith"));
    }

    @Test
    void testDeactivate() throws Exception {

        mockMvc.perform(put("/api/users/1/deactivate") // Deactivate user with ID 1
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("isDeactivated").value(true));
    }

    @Test
    void testDeactivateMultiple() throws Exception {

        mockMvc.perform(put("/api/users/deactivate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[1,2]")) // Assuming IDs are 1 and 2
                .andExpect(status().isOk())
                .andExpect(jsonPath("[0].isDeactivated").value(true))
                .andExpect(jsonPath("[1].isDeactivated").value(true));
    }

    @Test
    void testActivateUser() throws Exception {

        mockMvc.perform(put("/api/users/1/activate") // Activate user with ID 1
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent()); // No Content
    }

    @Test
    void testFindById_UserNotFound() throws Exception {

        mockMvc.perform(get("/api/users/999") // Assuming this ID does not exist
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreate_UniqueConstraintViolation() throws Exception {

        String newUserJson = "{\"firstName\":\"John\",\"lastName\":\"Doe\",\"email\":\"johndoe@gmail.com\"}";

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newUserJson))
                .andExpect(status().isConflict()); // Conflict
    }

    @Test
    void testCreate_ValidationError() throws Exception {

        String invalidUserJson = "{\"firstName\":\"\",\"lastName\":\"\",\"email\":\"\"}";

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidUserJson))
                .andExpect(status().isBadRequest()); // Bad Request
    }

    @Test
    void testUpdate_UserNotFound() throws Exception {

        String updatedUserJson = "{\"firstName\":\"John\",\"lastName\":\"Smith\",\"email\":\"john.smith@gmail.com\"}";

        mockMvc.perform(put("/api/users/999") // Assuming this ID does not exist
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedUserJson))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeactivate_UserNotFound() throws Exception {

        mockMvc.perform(put("/api/users/999/deactivate") // Assuming this ID does not exist
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testActivate_UserNotFound() throws Exception {

        mockMvc.perform(put("/api/users/999/activate") // Assuming this ID does not exist
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}