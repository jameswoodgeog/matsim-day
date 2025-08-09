package com.casa.cml;

import org.matsim.api.core.v01.population.PlanElement;
import org.matsim.core.router.AnalysisMainModeIdentifier;
import org.matsim.api.core.v01.population.Leg;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CustomMainModeIdentifier implements AnalysisMainModeIdentifier {
    private static final Set<String> MODES = new HashSet<>(Arrays.asList(
            "car", "bus", "subway", "walk", "bike", "rail", "ferry"));

    @Override
    public String identifyMainMode(List<? extends PlanElement> tripElements) {
        for (PlanElement pe : tripElements) {
            if (pe instanceof Leg) {
                String mode = ((Leg) pe).getMode();
                if (MODES.contains(mode)) {
                    return mode;
                }
            }
        }
        // fallback to first leg's mode or unknown
        for (PlanElement pe : tripElements) {
            if (pe instanceof Leg) {
                return ((Leg) pe).getMode();
            }
        }
        return "unknown";
    }
}
