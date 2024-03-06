package com.acorn.finals.controller;

import com.acorn.finals.model.Role;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class DummyControllerTest {
    @Test
    public void test() {
//        var a = Role.valueOf("guest");
        var b = Role.valueOf("ROLE_GUEST");
        var c = Role.ROLE_OWNER.ordinal();

//        Assertions.assertThat(a).isEqualTo(Role.ROLE_GUEST);
        assertThat(b).isEqualTo(Role.ROLE_GUEST);
        assertThat(c).isEqualTo(0);


    }

}