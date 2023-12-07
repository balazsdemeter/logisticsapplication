package hu.cubix.balage.logisticsapplication.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

@ConfigurationProperties(prefix = "logistics")
@Component
public class LogisticsConfigurationProperties {

    private Delay delay;
    private Percent percent;
    private JwtData jwtData = new JwtData();

    public Delay getDelay() {
        return delay;
    }

    public void setDelay(Delay delay) {
        this.delay = delay;
    }

    public Percent getPercent() {
        return percent;
    }

    public void setPercent(Percent percent) {
        this.percent = percent;
    }

    public JwtData getJwtData() {
        return jwtData;
    }

    public void setJwtData(JwtData jwtData) {
        this.jwtData = jwtData;
    }

    public static class Delay {
        private Integer delay1;
        private Integer delay2;
        private Integer delay3;

        public Integer getDelay1() {
            return delay1;
        }

        public void setDelay1(Integer delay1) {
            this.delay1 = delay1;
        }

        public Integer getDelay2() {
            return delay2;
        }

        public void setDelay2(Integer delay2) {
            this.delay2 = delay2;
        }

        public Integer getDelay3() {
            return delay3;
        }

        public void setDelay3(Integer delay3) {
            this.delay3 = delay3;
        }
    }

    public static class Percent {
        private Double percent1;
        private Double percent2;
        private Double percent3;

        public Double getPercent1() {
            return percent1;
        }

        public void setPercent1(Double percent1) {
            this.percent1 = percent1;
        }

        public Double getPercent2() {
            return percent2;
        }

        public void setPercent2(Double percent2) {
            this.percent2 = percent2;
        }

        public Double getPercent3() {
            return percent3;
        }

        public void setPercent3(Double percent3) {
            this.percent3 = percent3;
        }
    }

    public static class JwtData {
        private String issuer;
        private String secret;
        private Duration duration;

        public String getIssuer() {
            return issuer;
        }

        public void setIssuer(String issuer) {
            this.issuer = issuer;
        }

        public String getSecret() {
            return secret;
        }

        public void setSecret(String secret) {
            this.secret = secret;
        }

        public Duration getDuration() {
            return duration;
        }

        public void setDuration(Duration duration) {
            this.duration = duration;
        }
    }
}