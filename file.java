import org.springframework.boot.SpringApplication; import org.springframework.boot.autoconfigure.SpringBootApplication; import org.springframework.web.bind.annotation.*; import java.util.HashMap; import java.util.Map;

@SpringBootApplication @RestController public class CropRecommendationApp {

private static final Map<String, CropInfo> CROP_DATA = new HashMap<>();
private static final Map<String, String> CROP_ROTATION = new HashMap<>();

static {
    CROP_DATA.put("loamy", new CropInfo(new String[]{"Wheat", "Corn", "Soybeans"}, new String[]{"Compost", "Manure"}));
    CROP_DATA.put("sandy", new CropInfo(new String[]{"Carrots", "Peanuts", "Watermelon"}, new String[]{"Seaweed", "Bone Meal"}));
    CROP_DATA.put("clay", new CropInfo(new String[]{"Rice", "Broccoli", "Cabbage"}, new String[]{"Gypsum", "Compost Tea"}));
    
    CROP_ROTATION.put("Wheat", "Legumes (Soybeans, Peas)");
    CROP_ROTATION.put("Corn", "Root Crops (Carrots, Potatoes)");
    CROP_ROTATION.put("Rice", "Leafy Greens (Spinach, Lettuce)");
}

public static void main(String[] args) {
    SpringApplication.run(CropRecommendationApp.class, args);
}

@PostMapping("/recommend")
public Map<String, Object> recommend(@RequestBody Map<String, String> request) {
    String soilType = request.getOrDefault("soil_type", "loamy");
    String location = request.getOrDefault("location", "");
    
    Map<String, Object> response = new HashMap<>();
    if (CROP_DATA.containsKey(soilType)) {
        CropInfo cropInfo = CROP_DATA.get(soilType);
        Map<String, String> cropRotation = new HashMap<>();
        for (String crop : cropInfo.crops) {
            cropRotation.put(crop, CROP_ROTATION.getOrDefault(crop, "General Rotation"));
        }
        response.put("recommended_crops", cropInfo.crops);
        response.put("organic_fertilizers", cropInfo.fertilizers);
        response.put("crop_rotation", cropRotation);
        response.put("location", location);
    } else {
        response.put("error", "Unknown soil type");
    }
    return response;
}

}

class CropInfo { String[] crops; String[] fertilizers;

CropInfo(String[] crops, String[] fertilizers) {
    this.crops = crops;
    this.fertilizers = fertilizers;
}

}
