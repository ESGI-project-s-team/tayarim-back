package fr.esgi.al5.tayarim;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
  "stripe.secret-key=test-stripe-secret-key"
})
class TayarimApplicationTests {

  @Test
  void contextLoads() {
  }

}
