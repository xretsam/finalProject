package Client.Model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class JsonParser {
    public static Board parse(String jsonString) {
        ObjectMapper objectMapper = new ObjectMapper();
        Board board = new Board();
        try {
            JsonNode jsonNode = objectMapper.readTree(jsonString);
            ArrayNode pieces = (ArrayNode) jsonNode.get("pieces");
            JsonNode currentTurn = jsonNode.get("currentTurn");
            board.setCurrentTurn(currentTurn.asText());
            for(JsonNode jsonPiece : pieces){
                Piece piece = objectMapper.readValue(jsonPiece.toString(), Piece.class);
                board.setPiece(piece);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return board;
    }
}
