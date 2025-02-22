package com.example.U4_W7_Gestione_Eventi.config;
import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary uploader(){
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "dbl9dnzgm");
        config.put("api_key", "622788718379498");
        config.put("api_secret", "Ydb_Io8liFr9VyAmAqUVPpbGbfM");
        return new Cloudinary(config);
    }

}
