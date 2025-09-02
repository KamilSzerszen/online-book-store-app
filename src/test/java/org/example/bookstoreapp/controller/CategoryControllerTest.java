package org.example.bookstoreapp.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.bookstoreapp.dto.category.CategoryDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CategoryControllerTest {

    protected static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(
            @Autowired WebApplicationContext applicationContext,
            @Autowired DataSource dataSource
    ) throws SQLException {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();

        teardown(dataSource);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource("database/add-three-default-categories.sql"));
        }
    }

    @AfterAll
    static void afterAll(
            @Autowired DataSource dataSource
    ) throws SQLException {
        teardown(dataSource);
    }

    private static void teardown(DataSource dataSource) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/remove-all-categories.sql")
            );
        }
    }

    @Test
    @DisplayName("""
            Should Create new category and return dtos
            """)
    @Sql(
            scripts = "classpath:database/remove-all-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/remove-all-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/add-three-default-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void createCategory_validRequest_returnDto() throws Exception {
        CategoryDto expected = new CategoryDto();
        expected.setName("Name");
        expected.setDescription("Description");

        MvcResult mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.post("/categories")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(expected))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        CategoryDto result = objectMapper
                .readValue(mvcResult.getResponse().getContentAsString(), CategoryDto.class);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(expected.getName(), result.getName());
        Assertions.assertEquals(expected.getDescription(), result.getDescription());
    }

    @Test
    @DisplayName("""
            Should get all three category
            """)
    @WithMockUser(username = "user", roles = {"USER"})
    public void getAllCategories_threeCategory_returnAll() throws Exception {
        List<CategoryDto> expected = new ArrayList<>();

        CategoryDto categoryDtoOne = new CategoryDto();
        categoryDtoOne.setName("One");
        expected.add(categoryDtoOne);

        CategoryDto categoryDtoTwo = new CategoryDto();
        categoryDtoTwo.setName("Two");
        expected.add(categoryDtoTwo);

        CategoryDto categoryDtoThree = new CategoryDto();
        categoryDtoThree.setName("Three");
        expected.add(categoryDtoThree);

        MvcResult mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.get("/categories")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        JsonNode jsonNode = objectMapper.readTree(mvcResult.getResponse().getContentAsString());

        CategoryDto[] result = objectMapper.treeToValue(
                jsonNode.get("content"), CategoryDto[].class
        );

        Assertions.assertNotNull(result);
        Assertions.assertEquals(expected, Arrays.stream(result).toList());
    }

    @Test
    @DisplayName("""
            Should get category with valid id
            """)
    @WithMockUser(username = "user", roles = {"USER"})
    public void getCategoryById_validId_returnDto() throws Exception {

        CategoryDto expected = new CategoryDto();
        expected.setName("One");
        Long validId = 1L;

        MvcResult mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.get("/categories/" + validId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        CategoryDto result = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), CategoryDto.class
        );

        Assertions.assertNotNull(result);
        Assertions.assertEquals(expected.getName(), result.getName());
    }

    @Test
    @DisplayName("""
            Should update existing category
            """)
    @Sql(
            scripts = "classpath:database/remove-all-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/add-three-default-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void updateCategory_validRequest_returnDto() throws Exception {
        CategoryDto expected = new CategoryDto();
        expected.setName("One Updated");
        Long validId = 1L;

        MvcResult mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.put("/categories/{id}", validId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(expected))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        CategoryDto result = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), CategoryDto.class);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(expected.getName(), result.getName());

    }

    @Test
    @DisplayName("""
            Should delete existing category
            """)
    @Sql(
            scripts = "classpath:database/remove-all-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/add-three-default-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void deleteCategory_validId_verifyMock() throws Exception {
        Long validId = 1L;

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/categories/{id}", validId)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
