package top.jingbh.seatoccupation.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import top.jingbh.seatoccupation.util.StringToEnumConverterFactory;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Bean
    ObjectMapper jacksonObjectMapper(Jackson2ObjectMapperBuilder builder) {
        return builder.featuresToEnable(
            MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS,
            DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL
        ).build();
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverterFactory(new StringToEnumConverterFactory());
    }
}
