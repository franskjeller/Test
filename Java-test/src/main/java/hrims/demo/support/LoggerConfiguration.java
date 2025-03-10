package hrims.demo.support;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.FileAppender;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
import org.slf4j.LoggerFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.file.Path;
import java.nio.file.Paths;

import hrims.demo.support.Settings;

@Configuration
public class LoggerConfiguration  {

    @Autowired
    private Settings _settings;

    @Bean
    public LoggerContext loggerContext() {
        var logDirectory = _settings.LogDirectory;
        var logFilePath = Paths.get(logDirectory, "application.log").toString();

        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();

        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(context);
        encoder.setPattern("%d{yyyy-MM-dd HH:mm:ss} %-5level %logger{36} - %msg%n");
        encoder.start();

        RollingFileAppender<ILoggingEvent> rollingFileAppender = new RollingFileAppender<>();
        rollingFileAppender.setContext(context);
        rollingFileAppender.setAppend(true);
        rollingFileAppender.setEncoder(encoder);
        
        // Set up file path and name pattern for monthly rollover
        rollingFileAppender.setFile(logFilePath);

        TimeBasedRollingPolicy<ILoggingEvent> rollingPolicy = new TimeBasedRollingPolicy<>();
        rollingPolicy.setContext(context);
        
        // Use monthly rollover strategy 
        rollingPolicy.setFileNamePattern(logFilePath.replace(".log", "-%d{yyyy-MM}.log"));
        
         // Configure max history if needed (e.g., keep 12 months)
         rollingPolicy.setMaxHistory(12);

         // Attach the policy to appender and start it.
         rollingPolicy.setParent(rollingFileAppender); 
         rollingPolicy.start();
        
         // Start appender after adding policy.
         rollingFileAppender.setRollingPolicy(rollingPolicy); 
         rollingFileAppender.start();

         context.getLogger("hrims").addAppender(rollingFileAppender);

         return context;   
     }
}
