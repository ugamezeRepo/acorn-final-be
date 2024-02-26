package com.acorn.finals.service;

import com.acorn.finals.mapper.MemberMapper;
import com.acorn.finals.model.entity.MemberEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailService implements UserDetailsService {
    private final MemberMapper mapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("email is {}", username);
        MemberEntity entity = mapper.findOneByEmail(username);
        //만일 저장된 userName이 없다면
        if (entity == null) {
            //예외를 발생시킨다
            throw new UsernameNotFoundException("존재하지 않는 사용자 입니다");
        }
        var dto = entity.toDto();
        UserDetails ud = new User(dto.getEmail(), "", List.of());
        return ud;
    }

}
