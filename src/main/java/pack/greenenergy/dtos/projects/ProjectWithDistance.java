package pack.greenenergy.dtos.projects;

import pack.greenenergy.entities.projects.Project;

public record ProjectWithDistance(Project project, Double distanceKm) {}

