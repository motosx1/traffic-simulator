package pl.zablocki.scenarioxml;

import java.util.List;

public class Scenarios {
    int simulationDuration;
    List<ScenarioXml> scenarios;
    List<RoadXml> roads;

    /* Desired look of the xml:
     *
      * <scenarios>
	<simulationDuration>600</simulationDuration>
	<scenario>
		<maxCarsPerHour>200</maxCarsPerHour>
		<typicalVehicle>
			<position>
				<roadId>0</roadId>
			</position>
			<params>
				<maxAcceleration>1.2</maxAcceleration> <!-- == acceleration -->
				<desiredSpeed>30</desiredSpeed> <!-- == actualSpeed -->
				<bParam>50</bParam>
			</params>
		</typicalVehicle>
		<stopLights>
			<notifyRadius>100</notifyRadius>
			<greenLightTimeSec>100</greenLightTimeSec>
			<redLightTimeSec>10</redLightTimeSec>
		</stopLights>
	</scenario>
	<roads>
		<road>
			<id>0</id>
		</road>
		<road>
			<id>10</id>
		</road>
	</roads>
</scenarios>
*/
}

