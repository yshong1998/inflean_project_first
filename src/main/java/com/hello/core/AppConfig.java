package com.hello.core;

import com.hello.core.discount.DiscountPolicy;
import com.hello.core.discount.RateDiscountPolicy;
import com.hello.core.member.MemberService;
import com.hello.core.member.MemberServiceImpl;
import com.hello.core.member.MemoryMemberRepository;
import com.hello.core.order.OrderService;
import com.hello.core.order.OrderServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    //@Bean memberService -> new MemoryMemberRepository();
    //@Bean orderService -> new MemoryMemberRepository();
    // 이러면 싱글톤이 더 이상 아니지 않나??
    //깨지는지 테스트 코드를 통해 알아보자

    //예상
    //call AppConfig.memberService
    //call AppConfig.memberRepository
    //call AppConfig.memberRepository
    //call AppConfig.orderService
    //call AppConfig.memberRepository

    //실제
    //call AppConfig.memberService
    //call AppConfig.memberRepository
    //call AppConfig.orderService

    //어떻게 스프링은 memberRepository를 3번 호출하지 않고 한 번만 호출하는 걸까?
    // @Configuration을 통해, @Bean이 붙은 메서드마다 이미 스프링 컨테이너에 객체가 등록되어 있으면 해당 객체를 반환하고,
    // 없다면 새로운 객체를 생성해서 반환하기 때문이다.
    // 이 로직은 CGLIB라는 Appconfig를 상속받는 새로운 클래스에 구현되어 있고, 이를 스프링이 자동으로 생성한다
    @Bean
    public MemberService memberService() {
        System.out.println("call AppConfig.memberService");
        return new MemberServiceImpl(memberRepository());
    }

    @Bean
    public MemoryMemberRepository memberRepository() {
        System.out.println("call AppConfig.memberRepository");
        return new MemoryMemberRepository();
    }

    @Bean
    public OrderService orderService() {
        System.out.println("call AppConfig.orderService");
//        return new OrderServiceImpl(memberRepository(), discountPolicy());
        return null;
    }

    @Bean
    public DiscountPolicy discountPolicy() {
//        return new FixDiscountPolicy();
        return new RateDiscountPolicy();
    }
}
