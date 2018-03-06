package pl.maciejpajak.api;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import pl.maciejpajak.domain.game.Sport;
import pl.maciejpajak.repository.SportRepository;

@RunWith(SpringRunner.class)
public class SportApiTest {

    private MockMvc mockMvc;

    private SportApi sportApi;
    
    @Mock
    private SportRepository sportRepository;

    @Before
    public void setUp() {
        sportApi = new SportApi(sportRepository);
        mockMvc = MockMvcBuilders.standaloneSetup(sportApi).build();
    }

    @Test
    public void getAllSports_GivenSportsFound_ShouldReturnValidList() throws Exception {
        // given
        Sport first = new Sport();
        first.setId(1L);
        first.setName("Football");
        first.setVisible(true);
        
        Sport second = new Sport();
        second.setId(2L);
        second.setName("Basketball");
        second.setVisible(true);
        
        // when
        when(sportRepository.findAllByVisible(true)).thenReturn(Arrays.asList(first, second));
        
        // then
        mockMvc.perform(get("/sports/all"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].id", is(1)))
            .andExpect(jsonPath("$[0].name", is(first.getName())))
            .andExpect(jsonPath("$[1].id", is(2)))
            .andExpect(jsonPath("$[1].name", is(second.getName())))
            .andDo(print());
    
    }

}
