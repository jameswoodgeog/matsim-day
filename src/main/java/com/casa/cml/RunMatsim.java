package com.casa.cml;

import ch.sbb.matsim.mobsim.qsim.SBBTransitModule;
import ch.sbb.matsim.mobsim.qsim.pt.SBBTransitEngineQSimModule;
import ch.sbb.matsim.routing.pt.raptor.SwissRailRaptorModule;
import org.matsim.api.core.v01.Scenario;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.controler.Controler;
import org.matsim.core.router.AnalysisMainModeIdentifier;
import org.matsim.core.router.MainModeIdentifier;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.simwrapper.SimWrapperModule;

public class RunMatsim {
    public static void main(String[] args) {
        
        // comment this out if required---------------------------------------
        System.out.println("CLASSPATH: " + System.getProperty("java.class.path"));
        //-------------------------------------------------------------------- 

        // load config
        Config config = ConfigUtils.loadConfig(args[0]);

        // load config into scenario
        final Scenario scenario = ScenarioUtils.loadScenario(config);

        Controler controler = new Controler(scenario);

        // Bind custom main mode identifier
        controler.addOverridingModule(new org.matsim.core.controler.AbstractModule() {
            @Override
            public void install() {
                bind(MainModeIdentifier.class).to(CustomMainModeIdentifier.class);
                bind(AnalysisMainModeIdentifier.class).to(CustomMainModeIdentifier.class);
            }
        });
        System.out.println("CustomMainModeIdentifier binding added!");

        controler.addOverridingModule(new SimWrapperModule());

        // To use the fast pt router (always needed for both QSim and Hermes):
        controler.addOverridingModule(new SwissRailRaptorModule());

        // --- HERMES (SBB deterministic PT simulation) ENABLED ---
        // To use Hermes, ensure the following two lines are Uncommented:
        controler.addOverridingModule(new SBBTransitModule());
        controler.configureQSimComponents(components -> {
            new SBBTransitEngineQSimModule().configure(components);
        });
        // --- END HERMES ---

        // --- QSIM (default MATSim mobsim) DISABLED ---
        // To use QSim instead of Hermes, COMMENT OUT the two Hermes lines above
        // and UNCOMMENT the following (if any QSim-specific modules are needed):
        // (No QSim-specific modules required for standard QSim operation)
        // --- END QSIM ---

        // // adding the multimodal module as a test
        // controler.addOverridingModule(new MultiModalModule());

        controler.run();
    }
}