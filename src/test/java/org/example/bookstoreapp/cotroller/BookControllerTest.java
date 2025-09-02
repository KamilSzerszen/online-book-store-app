package org.example.bookstoreapp.cotroller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.bookstoreapp.dto.book.BookDto;
import org.example.bookstoreapp.dto.book.CreateBookRequestDto;
import org.example.bookstoreapp.service.BookService;
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
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookControllerTest {

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
                    new ClassPathResource("database/add-three-default-books.sql"));
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
                    new ClassPathResource("database/remove-all-books.sql")
            );
        }
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("""
            Should find all existing book
            """)
    public void getAll_threeBooksInRepository_returnsAllThreeBooksDto() throws Exception {
        List<BookDto> expected = new ArrayList<>();

        BookDto bookOne = new BookDto();
        bookOne.setId(1L);
        bookOne.setTitle("Title1");
        bookOne.setAuthor("Author1");
        bookOne.setIsbn("Isbn1");
        bookOne.setPrice(BigDecimal.valueOf(12.99));
        expected.add(bookOne);

        BookDto bookTwo = new BookDto();
        bookTwo.setId(2L);
        bookTwo.setTitle("Title2");
        bookTwo.setAuthor("Author2");
        bookTwo.setIsbn("Isbn2");
        bookTwo.setPrice(BigDecimal.valueOf(22.99));
        expected.add(bookTwo);

        BookDto bookThree = new BookDto();
        bookThree.setId(3L);
        bookThree.setTitle("Title3");
        bookThree.setAuthor("Author3");
        bookThree.setIsbn("Isbn3");
        bookThree.setPrice(BigDecimal.valueOf(32.99));
        expected.add(bookThree);

        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders.get("/books")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String json = result.getResponse().getContentAsString();
        JsonNode root = objectMapper.readTree(json);

        BookDto[] actual = objectMapper.treeToValue(root.get("content"), BookDto[].class);
        Assertions.assertEquals(3, actual.length);
        Assertions.assertEquals(expected, Arrays.stream(actual).toList());
    }

    @Test
    @DisplayName("""
            Should return book with valid id
            """)
    @WithMockUser(username = "user", roles = {"USER"})
    public void getBookById_bookWithValidId_returnsBookDto() throws Exception {
        Long validId = 1L;

        BookDto expected = new BookDto();
        expected.setId(validId);
        expected.setTitle("Title1");
        expected.setAuthor("Author1");
        expected.setIsbn("Isbn1");
        expected.setPrice(BigDecimal.valueOf(12.99));

        MvcResult mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.get("/books/{id}", validId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        BookDto result = objectMapper
                .readValue(mvcResult.getResponse().getContentAsString(), BookDto.class);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(expected.getId(), result.getId());
        Assertions.assertEquals(expected.getTitle(), result.getTitle());
        Assertions.assertEquals(expected.getAuthor(), result.getAuthor());
        Assertions.assertEquals(expected.getIsbn(), result.getIsbn());
        Assertions.assertEquals(expected.getPrice(), result.getPrice());
    }

    @Test
    @DisplayName("""
            Should added new book
            """)
    @Sql(scripts = "classpath:database/remove-all-books.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/remove-all-books.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(scripts = "classpath:database/add-three-default-books.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void createBook_validRequest_returnsBookDto() throws Exception {
        CreateBookRequestDto request = new CreateBookRequestDto();
        request.setTitle("Create Book");
        request.setAuthor("Create Author");
        request.setIsbn("Create Isbn");
        request.setPrice(BigDecimal.valueOf(12.99));

        BookDto expected = new BookDto();
        expected.setTitle(request.getTitle());
        expected.setAuthor(request.getAuthor());
        expected.setIsbn(request.getIsbn());
        expected.setPrice(request.getPrice());

        MvcResult mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.post("/books")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        BookDto result = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), BookDto.class);

        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getId());
        Assertions.assertEquals(expected.getTitle(), result.getTitle());
        Assertions.assertEquals(expected.getAuthor(), result.getAuthor());
        Assertions.assertEquals(expected.getIsbn(), result.getIsbn());
        Assertions.assertEquals(expected.getPrice(), result.getPrice());
    }

    @Test
    @DisplayName("""
            Should deleted book with valid id
            """)
    @Sql(scripts = "classpath:database/remove-all-books.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(scripts = "classpath:database/add-three-default-books.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void delete_validId_verifyMock() throws Exception {
        Long validId = 1L;

        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/books/{id}", validId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("""
            Should update book with valid parameters
            """)
    @Sql(scripts = "classpath:database/remove-all-books.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(scripts = "classpath:database/add-three-default-books.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void update_validIdBookExisting_returnsBookDto() throws Exception {
        Long validId = 1L;

        BookDto expected = new BookDto();
        expected.setId(validId);
        expected.setTitle("TitleUpdate");
        expected.setAuthor("AuthorUpdate");
        expected.setIsbn("IsbnUpdate");
        expected.setPrice(BigDecimal.valueOf(12.99));

        CreateBookRequestDto request = new CreateBookRequestDto();
        request.setTitle(expected.getTitle());
        request.setAuthor(expected.getAuthor());
        request.setIsbn(expected.getIsbn());
        request.setPrice(expected.getPrice());

        MvcResult mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.put("/books/{id}", validId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        BookDto result = objectMapper
                .readValue(mvcResult.getResponse().getContentAsString(), BookDto.class);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(expected.getId(), result.getId());
        Assertions.assertEquals(expected.getTitle(), result.getTitle());
        Assertions.assertEquals(expected.getAuthor(), result.getAuthor());
        Assertions.assertEquals(expected.getIsbn(), result.getIsbn());
        Assertions.assertEquals(expected.getPrice(), result.getPrice());
    }

}
