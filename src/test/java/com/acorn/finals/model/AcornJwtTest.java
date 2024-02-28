package com.acorn.finals.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import org.junit.jupiter.api.Test;

class AcornJwtTest {

    @Test
    void fromJws_Case_Valid_JWS() {
        var sampleJws = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwiaWF0IjoxNTE2MjM5MDIyfQ.GNfw-L2XciVuvRiWwtvZ7N37_JRmYXqcp89QVHkhi48";
        var sampleSecret = "your-256-bit-secretyour-256-bit-secretyour-256-bit-secretyour-256-bit-secretyour-256-bit-secret";
        AcornJwt sampleJwt = AcornJwt.fromJws(sampleJws, sampleSecret);
        assertThat(sampleJwt).isNotNull();
        assertThat(sampleJwt.getSubject()).isEqualTo("1234567890");
        assertThat(sampleJwt.getIssuedAt()).isEqualTo(new Date(1516239022 * 1000L));
    }


    @Test
    void fromJws_Case_Invalid_JWS() {
        var sampleJws = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwiaWF0IjoxNTE2MjM5MDIyfQ.GNfw-L2XciVuvRiWwtvZ7N37_JRmYXqcp89QVHkhiqp";
        var sampleSecret = "your-256-bit-secretyour-256-bit-secretyour-256-bit-secretyour-256-bit-secretyour-256-bit-secret";
        AcornJwt sampleJwt = AcornJwt.fromJws(sampleJws, sampleSecret);
        assertThat(sampleJwt).isNull();
    }
}