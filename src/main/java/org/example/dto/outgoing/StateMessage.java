package org.example.dto.outgoing;

import java.util.List;

public record StateMessage(String type, List<PlayerSnapshot> players) { }
