package pl.zablocki.filewriter;

import lombok.Data;

import java.text.DecimalFormat;
import java.util.Map;
import java.util.TreeMap;

@Data
class FileLine{
    private double timeElapsed;
    private int roadId;
    private double averageSpeed;
    private int vehiclesStopped;
    private double vehiclesPassed;
    private TreeMap<Integer, VehicleFileParams> vehicleFileParams = new TreeMap<>();

   String getCsvString(int maxId){
       int lastVehId = vehicleFileParams.lastKey();

       DecimalFormat df = new DecimalFormat("#.##");
       String timeString = df.format(timeElapsed);
       return  timeString + ";" +
               roadId + ";" +
               averageSpeed + ";" +
               vehiclesStopped + ";" +
               vehiclesPassed + ";" +
               createCsvParamsString(vehicleFileParams)+
               createLastSemicolons(maxId, lastVehId);

   }

    private String createLastSemicolons(int maxId, int lastId) {
       String result = "";
        for (int i = 0; i < maxId-lastId; i++) {
            result+=";;;";
        }
        return result;
    }

    private String createCsvParamsString(TreeMap<Integer, VehicleFileParams> vehicleFileParams) {
        String result = "";

        int firstVehId = vehicleFileParams.firstKey();

        for (int i = 0; i < firstVehId; i++) {
            result += ";;;";
        }

        for (Map.Entry<Integer, VehicleFileParams> entry : vehicleFileParams.entrySet()) {
            VehicleFileParams param = entry.getValue();
            //entry.getKey() + "->" +
            result +=  param.getVehPos() + ";" + param.getVehSpeed() + ";" + param.getVehAcc() + ";";
        }

        return result;

    }
}