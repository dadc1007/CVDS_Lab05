//package edu.eci.UniReserva.UniReserva_Backend.controller;
//
//import org.junit.jupiter.api.AutoClose;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//import org.springframework.test.web.servlet.MockMvc;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import edu.eci.UniReserva.UniReserva_Backend.model.User;
//import edu.eci.UniReserva.UniReserva_Backend.service.impl.UserServiceImpl;
//
//@WebMvcTest(UserController.class)
//class UserControllerTest {
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @MockitoBean
//    private UserServiceImpl userServiceImpl;
//
//    private User validUser;
//
//
//    @BeforeEach
//    void setUp() {
//        validUser = new User("1037126548", "Daniel", "email@gmail.com", "Password#123");
//    }
//
//
//    @Test
//    public void shouldUpdateName() throws Exception {
//        User updatedUser = new User("1", "Alejandro", "john@example.com", "Password#123");
//        String updateNameJson = objectMapper.writeValueAsString(new User(null, "Alejandro", null, null));
//
//        when(userServiceImpl.updateUser(any(String.class), any(User.class))).thenReturn(updatedUser);
//
//        mockMvc.perform(patch("/user/update/{id}", validUser.getId())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(updateNameJson))
//                .andExpect(status().isOk())
//                .andExpect(content().json(objectMapper.writeValueAsString(updatedUser)));
//    }
//
//    @Test
//    public void shouldUpdatePassword() throws Exception {
//        User updatedUser = new User("1", "John Doe", "john@example.com", "NewPassword#123");
//        String updatePasswordJson = objectMapper.writeValueAsString(new User(null, null, null, "NewPassword#123"));
//
//        when(userServiceImpl.updateUser(any(String.class), any(User.class))).thenReturn(updatedUser);
//
//        mockMvc.perform(patch("/user/update/{id}", validUser.getId())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(updatePasswordJson))
//                .andExpect(status().isOk())
//                .andExpect(content().json(objectMapper.writeValueAsString(updatedUser)));
//    }
//
//    @Test
//    public void shouldNotUpdateInvalidPassword() throws Exception {
//        String invalidPasswordJson = objectMapper.writeValueAsString(new User(null, null, null, "newpassword#123"));
//
//        when(userServiceImpl.updateUser(any(String.class), any(User.class))).thenThrow(new IllegalArgumentException("Invalid password"));
//
//        mockMvc.perform(patch("/user/update/{id}", validUser.getId()).contentType(MediaType.APPLICATION_JSON)
//                .content(invalidPasswordJson))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().string("{\"error\":\"Invalid password\"}"));
//    }
//
//    @Test
//    public void shouldNotUpdateEmail() throws Exception {
//        String invalidEmailJson = objectMapper.writeValueAsString(new User(null, null, "newEmail@gmail.com", null));
//
//        when(userServiceImpl.updateUser(any(String.class), any(User.class))).thenThrow(new IllegalArgumentException("The email cannot be updated"));
//
//        mockMvc.perform(patch("/user/update/{id}", validUser.getId()).contentType(MediaType.APPLICATION_JSON)
//                .content(invalidEmailJson))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().string("{\"error\":\"The email cannot be updated\"}"));
//    }
//
//    @Test
//    public void shouldNotUpdateNonExistentUser() throws Exception {
//        String updateNameJson = objectMapper.writeValueAsString(new User(null, "Alejandro", null, null));
//
//        when(userServiceImpl.updateUser(any(String.class), any(User.class))).thenThrow(new IllegalArgumentException("User not found"));
//
//        mockMvc.perform(patch("/user/update/{id}", "1111111111").contentType(MediaType.APPLICATION_JSON)
//                .content(updateNameJson))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().string("{\"error\":\"User not found\"}"));
//    }
//
//    @Test
//    public void shouldDeleteUserUserDeletedReturnsOk() throws Exception {
//        when(userServiceImpl.deleteUser("123")).thenReturn("User deleted successfully");
//
//        mockMvc.perform(delete("/user/delete/123"))
//                .andExpect(status().isNoContent());
//    }
//
//    @Test
//    public void shouldNotDeleteUserUserWhenNotFound() throws Exception {
//        when(userServiceImpl.deleteUser("999")).thenThrow(new IllegalArgumentException("User not found"));
//
//        mockMvc.perform(delete("/user/delete/999"))
//                .andExpect(status().isNotFound())
//                .andExpect(content().string("{\"error\":\"User not found\"}"));
//    }
//
//    @Test
//    public void shouldNotDeleteUserWhenHasRepository() throws Exception {
//        when(userServiceImpl.deleteUser(validUser.getId())).thenThrow(new IllegalArgumentException("User has Repository, can't be deleted"));
//        mockMvc.perform(delete("/user/delete/1037126548"))
//                .andExpect(status().isNotFound())
//                .andExpect(content().string("{\"error\":\"User has Repository, can't be deleted\"}"));
//    }
//
//
//
//    @Test
//    void shouldGetUserByIdUserFound() throws Exception {
//        User user = new User("1234567", "Chente", "chentechaurio@example.com", null);
//
//        when(userServiceImpl.getUser("1234567")).thenReturn(user);
//
//        mockMvc.perform(get("/user/getUser/1234567"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value("1234567"))
//                .andExpect(jsonPath("$.name").value("Chente"))
//                .andExpect(jsonPath("$.email").value("chentechaurio@example.com"));
//    }
//
//
//    @Test
//    void shouldNotGetUserWhenIdUserNotFound() throws Exception {
//        when(userServiceImpl.getUser("999")).thenThrow(new IllegalArgumentException("User not found"));
//
//        mockMvc.perform(get("/user/getUser/999"))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().string("{\"error\":\"User not found\"}"));
//    }
//
//}
