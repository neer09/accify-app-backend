package org.accify.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Update {

    public Long update_id;
    public Message message;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Message {
        public Long message_id;
        public User from;
        public Chat chat;
        public Long date;
        public String text;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class User {
        public Long id;
        public Boolean is_bot;
        public String first_name;
        public String username;
        public String language_code;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Chat {
        public Long id;
        public String first_name;
        public String username;
        public String type;
    }
}
