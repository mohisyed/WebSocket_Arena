package org.example.dto.incoming;

public record InputMessage(String type, boolean up, boolean down, boolean left, boolean right) {
}
