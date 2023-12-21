package Server;

import Game.Chess;
import Game.Pieces.Piece;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class JsonAdapter implements JsonGame{
    private final Chess game;

    public JsonAdapter(Chess game) {
        this.game = game;
    }
    public String getJsonAsString(){
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode json = objectMapper.createObjectNode();
        json.put("currentTurn", game.getTurn());
        json.put("end", game.end);
        json.put("cause", game.cause);
        ArrayNode pieces = json.putArray("pieces");
        for (Piece[] pieceList : game.board){
            for (Piece piece : pieceList) {
                if (piece == null) continue;
                ObjectNode pieceObject = objectMapper.createObjectNode();
                pieceObject.put("type", piece.getType());
                pieceObject.put("color", piece.getColor());
                pieceObject.put("x", piece.x);
                pieceObject.put("y", piece.y);
                ArrayNode availableMoves = objectMapper.createArrayNode();
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        availableMoves.add(piece.availableMoves[i][j]);
                    }
                }
                pieceObject.set("availableMoves", availableMoves);
                pieces.add(pieceObject);
            }
        }
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}