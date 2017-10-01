package urchin.controller.api;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableMessageDto.class)
@JsonDeserialize(as = ImmutableMessageDto.class)
public interface MessageDto {

    @Value.Parameter
    String getMessage();
}
