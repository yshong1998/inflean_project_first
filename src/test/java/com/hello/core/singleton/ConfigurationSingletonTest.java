package com.hello.core.singleton;

import com.hello.core.AppConfig;
import com.hello.core.member.MemberRepository;
import com.hello.core.member.MemberServiceImpl;
import com.hello.core.order.OrderServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.*;

public class ConfigurationSingletonTest {

    @Test
    void configurationTest(){
        ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);

        MemberServiceImpl memberService = ac.getBean("memberService", MemberServiceImpl.class);
        OrderServiceImpl orderService = ac.getBean("orderService", OrderServiceImpl.class);
        MemberRepository memberRepository = ac.getBean("memberRepository", MemberRepository.class);

        MemberRepository memberRepository1 = memberService.getMemberRepository();
        MemberRepository memberRepository2 = orderService.getMemberRepository();

        System.out.println("memberService -> memberRepository = " + memberRepository1);
        System.out.println("orderService -> memberRepository = " + memberRepository2);
        System.out.println("memberRepository = " + memberRepository);

        assertThat(memberService.getMemberRepository()).isSameAs(memberRepository);
        assertThat(orderService.getMemberRepository()).isSameAs(memberRepository);
    }

    @Test
    void configurationDeep(){
        ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
        AppConfig bean = ac.getBean(AppConfig.class);
        System.out.println("bean = " + bean.getClass());
        // bean = class com.hello.core.AppConfig$$EnhancerBySpringCGLIB$$6a66b293 ??
        // $$뒷부분은 왜 나오는 걸까?
        // 스프링이 CGLIB라는 바이트 코드 조작 라이브러리를 이용해
        // AppConfig클래스를 상속받은 임의의 클래스를 만들고 해당 클래스를 스프링 빈으로 등록하기 때문이다.
        // 이걸 하라는 명령을 내리는 게 @Configuration이다.
        // @Configuration을 하지 않고 @Bean만 해도 되기는 하지만, 이 경우 CGLIB라는 AppConfig를 상속받는 클래스가 생성되지 않는다.
        // 따라서 그냥 객체 new를 할 때 빈에 등록되었는지 여부를 확인하는 로직이 없어지기 때문에 싱글톤이 깨져버린다.
        // 그래서 생성되는 memberRepository가 여러 개가 되므로 더 이상 같은 객체가 아니게 된다.
        // 그래서 이렇게 @Bean만 해 놓은 객체들은 스프링의 관리를 받지 않게 된다.
    }
}
