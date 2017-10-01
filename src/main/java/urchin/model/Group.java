package urchin.model;

import org.immutables.value.Value;

import java.time.LocalDateTime;

@Value.Immutable
public interface Group {

    GroupId getGroupId();

    GroupName getName();

    LocalDateTime getCreated();

}
