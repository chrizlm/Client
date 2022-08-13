package com.chrislm.client;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ClientApplicationTests {

    Calc calc = new Calc();

    @Test
    void itShouldAddNumbers() {
        //given
        int num1 = 20;
        int num2 = 30;

        //when
        int result = calc.add(num1, num2);

        int expected = 50;
        //then
        assertThat(result).isEqualTo(expected);
    }

    class Calc{
        int add(int a, int b){
            return a + b;
        }
    }
}
