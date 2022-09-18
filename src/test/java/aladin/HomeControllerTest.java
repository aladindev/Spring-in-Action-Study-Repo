package aladin;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import tacos.HomeController;


@WebMvcTest(HomeController.class)
public class HomeControllerTest {
	@Autowired
	private MockMvc mockMvc; //MockMvc 주입
	
	@Test
	public void testMomePage() throws Exception {
//		mockMvc.prerform(get"/")
//				.andExpect(status().isOk())
//				.andExpect(view().name("home"))
//				.andExpect(content().string(containsString("Welcome to...")));
	}
}
